import java.io.*;
import java.util.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;

public class Stage{
	Image[] images;
	Instruction[][] instructions;
	Map<String,Integer> anchors;
	
	int PC = 0;
	
	public Stage(String stageName){
		String path = "map/" + stageName + "/";
		ImageMapper mapper = new ImageMapper(path);
		images = mapper.get();

		InstructionParser parser = new InstructionParser(path);
		instructions = (Instruction[][])parser.get();
	}
	public Instruction[] get(){
		if(PC < instructions.length){
			return instructions[PC++];
		}
		else{
			return null;
		}
	}
	public void jump(String anchor){
		Integer target = anchors.get(anchor);
		if(target != null){
			PC = target;
		}
	}
	public Image getImage(int imageId){
		return images[imageId];
	}
}


class InstructionCompiler{
	private ArrayList<ArrayList<Instruction>> instructionTable;
	private BufferedReader reader;
	private Root root;
	
	private VariableTable variables;
	private enemyID = 1;
	private bulletID = 1;
	private wave = 1;
	
	
	public InstructionCompiler(File map){
		analyze();
		compile();
	}
	private analyze(BufferedReader reader){
		this.reader = reader;
		root = new Root();
	}
	private compile(){
		instructionTable = new ArrayList<ArrayList<Instruction>>();
		variables = new VariableTable();
		root.calc();
	}
	private addInstruction(int time,Instruction instruction){
		int size = instructionTable.size();
		for(int i=size;i<time;i++)
			instructionTable.add(null);
		ArrayList<Instruction> instructions;
		if((instructions = instructionTable.get(time)) == null){
			instructions = new ArrayList<Instruction>();
			instructionTable.set(time,instructions);
		}
		instructions.add(instruction);
	}
	
	private interface Element{
		void calc();
	}
	


////////////////////////////////////////////////////////////////////////////////


	private class Root{
		private ArrayList<Element> elements = new ArrayList<Element>();
		public Root(){
			String line = null;
			try{
				while((line = readerLine()) != null && !("".equals(line))){
					String statement = line.split(" ",2)[0];
					if(statement.equals("PATH"))      {elements.add(new Path(line));}
					//else if(statement.equals("ROLE")) {elements.add(new Role(line));}
					//else if(statement.equals("IMAGE")){elements.add(new Image(line));}
					//else if(statement.equals("BOSS")) {elements.add(new Boss(line));}
				}
			}
			catch(IOException){
				System.err.println("IOException");
			}
		}
		void calc(){
			for(Element e:elements){
				e.calc(null);
			}
		}
	}
	


////////////////////////////////////////////////////////////////////////////////
	
	
	private class Path{
		private ArrayList<Element> elements = new ArrayList<Element>();
		private String attribute;
		public Path(String attribute){
			this.attribute = attribute;
			parse();
		}
		public parse(){
			String line = null;
			try{
				while((line = readerLine()) != null && !("".equals(line))){
					String statement = line.split(" ",2)[0];
					if(statement.equals("ENEMY"))   {elements.add(new Enemy(line));}
					if(statement.equals("GROUP"))   {elements.add(new Group(line));}
					else if(statement.equals("END")){return;}
				}
			}
			catch(IOException){
				System.err.println("IOException");
			}
		}
		void calc(){
			String time = attr(attribute,1);
			variables.set("time",time);
			for(Element e:elements){
				e.calc();
			}
		}
	}
	


////////////////////////////////////////////////////////////////////////////////

	
	private class Group{
		private ArrayList<Element> elements = new ArrayList<Element>();
		private String attribute;
		public Group(String attribute){
			this.attribute = attribute;
			parse();
		}
		public parse(){
			String line = null;
			try{
				while((line = readerLine()) != null && !("".equals(line))){
					String statement = line.split(" ",2)[0];
					if(statement.equals("ENEMY"))   {elements.add(new Enemy(line));}
					if(statement.equals("GROUP"))   {elements.add(new Group(line));}
					else if(statement.equals("END")){return;}
				}
			}
			catch(IOException){
				System.err.println("IOException");
			}
		}
		
		
		
		void calc(){
			int baseTime = parseInt(variables.get("time"));
			int plusTime = parseInt(attr(attribute,1));
			int times    = parseInt(attr(attribute,2));
			int interval = parseInt(attr(attribute,3));
			for(Element e:elements){
				for(int i=0;i<times;i++){
					variables.push();
					variables.set("time",(String)(baseTime + plusTime + i * interval));
					e.calc();
					variables.pop();
				}
			}
		}
	}
	
	
////////////////////////////////////////////////////////////////////////////////	


	private class Enemy{
		private Type type;
		private Position position;
		private Move move;
		private Shoot shoot;
		
		private String attribute;
		private String[] arguments = new String[7];
		public Enemy(String attribute){
			this.attribute = attribute;
			parse();
		}
		public parse(){
			String line = null;
			try{
				while((line = readerLine()) != null && !("".equals(line))){
					String statement = line.split(" ",2)[0];
					if(statement.equals("TYPE"))    {type     = new Type(line);}
					if(statement.equals("POSITION")){position = new Position(line);}
					if(statement.equals("MOVE"))    {move     = new Move(line);}
					if(statement.equals("SHOOT"))   {shoot    = new Shoot(line);}
					else if(statement.equals("END")){return;}
				}
			}
			catch(IOException){
				System.err.println("IOException");
			}
		}
		
		
		
		void calc(){
			int baseTime = parseInt(variables.get("time"));
			int plusTime = parseInt(attr(attribute,1));
			int time = baseTime + plusTime;
			
			/* inst_type */
			arguments[0] = "enemy";
			/* enemy_ID */
			arguments[1] = (String)enemyID;
			type.calc();
			position.calc();
			Instruction instruction = new Instruction(arguments);
			addInstruction(time,instruction);
			
			variables.push();
			variables.set("time",(String)time);
			variables.set("target_ID",(String)enemyID);
			variables.set("target_type","enemy");
			move.calc();
			shoot.calc();
			variables.pop();
			
			enemyID += 1;
		}
		
		
		
		private class Type{
			private String attribute;
			public EnemyType(String attribute){
				this.attribute = attribute;
			}
			void calc(String line){
				/* enemy_type*/
				String type = attr(attribute,1);
				arguments[2] = type;
				if(type.equals("custom")){
					/* image_ID */
					arguments[4] = attr(attribute,2);
					/* radius */
					arguments[5] = attr(attribute,3);
					/* HP */
					arguments[6] = attr(attribute,4);
				}
			}
		}
		private class Position{
			private String attribute;
			public Position(String attribute){
				this.attribute = attribute;
			}
			void calc(String line){
				arguments[3] = attr(attribute,1);
			}
		}
	}
	
	

////////////////////////////////////////////////////////////////////////////////

	
	private class Move{
		private ArrayList<Element> elements = new ArrayList<Element>();
		private String attribute;
		public Move(String attribute){
			this.attribute = attribute;
			parse();
		}
		public parse(){
			String line = null;
			try{
				while((line = readerLine()) != null && !("".equals(line))){
					String statement = line.split(" ",2)[0];
					if(statement.equals("ROUTE"))   {elements.add(new Route(line));}
					if(statement.equals("SHAPE"))   {elements.add(new Shape(line));}
					else if(statement.equals("END")){return;}
				}
			}
			catch(IOException){
				System.err.println("IOException");
			}
		}
		void calc(){
			for(Element e:elements){
				calc();
			}
		}
	}
	
	

////////////////////////////////////////////////////////////////////////////////

	
	private class Shoot{
		private ArrayList<Element> elements = new ArrayList<Element>();
		private String attribute;
		public Shoot(String attribute){
			this.attribute = attribute;
			parse();
		}
		public parse(){
			String line = null;
			try{
				while((line = readerLine()) != null && !("".equals(line))){
					String statement = line.split(" ",2)[0];
					if(statement.equals("BULLET"))  {elements.add(new Bullet(line));}
					if(statement.equals("CLIP"))    {elements.add(new Clip(line));}
					else if(statement.equals("END")){return;}
				}
			}
			catch(IOException){
				System.err.println("IOException");
			}
		}
		void calc(){
			for(Element e:elements){
				calc();
			}
		}
	}
	
	

////////////////////////////////////////////////////////////////////////////////

	
	private class Clip{
		private ArrayList<Element> elements = new ArrayList<Element>();
		private String attribute;
		public Clip(String attribute){
			this.attribute = attribute;
			parse();
		}
		public parse(){
			String line = null;
			try{
				while((line = readerLine()) != null && !("".equals(line))){
					String statement = line.split(" ",2)[0];
					if(statement.equals("BULLET"))  {elements.add(new Bullet(line));}
					if(statement.equals("CLIP"))    {elements.add(new Clip(line));}
					else if(statement.equals("END")){return;}
				}
			}
			catch(IOException){
				System.err.println("IOException");
			}
		}
		
		
		
		void calc(){
			int baseTime = parseInt(variables.get("time"));
			int plusTime = parseInt(attr(attribute,1));
			int times    = parseInt(attr(attribute,2));
			int interval = parseInt(attr(attribute,3));
			for(Element e:elements){
				for(int i=0;i<times;i++){
					variables.push();
					variables.set("time",(String)(baseTime + plusTime + i * interval));
					e.calc();
					variables.pop();
				}
			}
		}
	}
	
	
////////////////////////////////////////////////////////////////////////////////

	
	private class Bullet{
		private Type type;
		private Position position;
		private Move move;
		
		private String attribute;
		public Bullet(String attribute){
			this.attribute = attribute;
			parse();
		}
		public parse(){
			String line = null;
			try{
				while((line = readerLine()) != null && !("".equals(line))){
					String statement = line.split(" ",2)[0];
					if(statement.equals("TYPE"))    {type     = new Type(line);}
					if(statement.equals("POSITION")){position = new Position(line);}
					if(statement.equals("MOVE"))    {move     = new Move(line);}
					else if(statement.equals("END")){return;}
				}
			}
			catch(IOException){
				System.err.println("IOException");
			}
		}
		
		
		
		void calc(){
			int baseTime = parseInt(variables.get("time"));
			int plusTime = parseInt(attr(attribute,1));
			int time = baseTime + plusTime;
			
			int amount = parseInt(attr(attribute,2));
			/* inst_type */
			arguments[0] = "bullet";
			/* enemy_ID */
			arguments[1] = enemyID;
			type.calc();
			position.calc();
			for(int i=0;i<amount;i++){
				/* bullet_ID */
				arguments[2] = (String)bulletID + i;
				Instruction instruction = new Instruction(arguments);
				addInstruction(time,instruction);
			}
			variables.push();
			variables.set("time",(String)time);
			variables.set("target_ID",(String)bulletID);
			variables.set("amount",(String)amount);
			variables.set("target_type","bullet");
			move.calc();
			variables.pop();
			
			bulletID += amount;
		}
		
		
		
		private class Type{
			private String attribute;
			public EnemyType(String attribute){
				this.attribute = attribute;
			}
			void calc(String line){
				/* bullet_type*/
				String type = attr(attribute,1);
				arguments[3] = type;
				if(type.equals("custom")){
					/* image_ID */
					arguments[4] = attr(attribute,2);
					/* radius */
					arguments[5] = attr(attribute,3);
					/* Power */
					arguments[6] = attr(attribute,4);
				}
			}
		}
	}
	


////////////////////////////////////////////////////////////////////////////////

	
	private class Route{
		private Type type;
		private Position position;
		private Move move;
		
		private String attribute;
		public Bullet(String attribute){
			this.attribute = attribute;
		}
		
		
		
		void calc(){
			int baseTime = parseInt(variables.get("time"));
			int plusTime = parseInt(attr(attribute,1));
			int time = baseTime + plusTime;
			
			/* inst_type */
			arguments[0] = "route";
			
			
			/* route_type */
			String routeType = attr(attribute,2);
			if(routeType.equals("stop")){
				arguments[3] = "stop";
				/* point_refer */
				arguments[5] = attr(attribute,3);
				/* point_offset */
				arguments[5] = attr(attribute,4);
				/* point_angle */
				arguments[6] = attr(attribute,5);
			}
			else if(routeType.equals("beeline")){
				arguments[3] = "linear";
				/* speed */
				arguments[4] = attr(attribute,3);
				/* point_refer */
				arguments[5] = attr(attribute,4);
				/* point_offset */
				arguments[5] = attr(attribute,5);
				/* point_angle */
				arguments[6] = attr(attribute,6);
			}
			
			
			/* target_type */
			String targetType = variables.gete("target_type");
			if(targetType.equals("enemy")){
				arguments[1] = "enemy";
				/* target_ID */
				arguments[2] = variables.get("target_ID");
				Instruction instruction = new Instruction(arguments);
				addInstruction(time,instruction);
			}
			else if(targetType.equals("bullet")){
				arguments[1] = "bullet";
				/* target_ID */
				int targetID = parseInt(variables.get("target_ID"));
				int amount   = parseInt(variables.get("amount"));
				for(int i=0;i<amount;i++){
					arguments[2] = targetID + i;
					Instruction instruction = new Instruction(arguments);
					addInstruction(time,instruction);
				}
			}
		}
	}
	


////////////////////////////////////////////////////////////////////////////////

	
	private class Shape{
		private Type type;
		private Position position;
		private Move move;
		
		private String attribute;
		public Bullet(String attribute){
			this.attribute = attribute;
		}
		
		
		
		void calc(){
			/* target_type */
			String targetType = variables.get("target_type");
			if(targetType.equals("enemy")){
				return;
			}
			arguments[1] = "bullet";
			
			int baseTime = parseInt(variables.get("time"));
			int plusTime = parseInt(attr(attribute,1));
			int time = baseTime + plusTime;
			
			/* inst_type */
			arguments[0] = "route";
			
			
			/* shape_type */
			String shapeType = attr(attribute,2);
			if(shapeType.equals("fan")){
				/* route_type */
				arguments[3] = "linear";
				/* speed */
				arguments[4] = attr(attribute,3);
				/* point_refer */
				arguments[5] = attr(attribute,4);
				/* point_offset */
				arguments[6] = attr(attribute,5);
				/* target_ID */
				int targetID = parseInt(variables.get("target_ID"));
				
				int offsetAngle = parseInt(attr(attribute,6));
				int amount   = parseInt(variables.get("amount"));
				if(amount == 1){
					/* target_ID */
					arguments[2] = (String)targetID;
					/* point_angle */
					arguments[7] = (String)offsetAngle;
					Instruction instruction = new Instruction(arguments);
					addInstruction(time,instruction);
				}
				else{
					int angle = parseInt(attr(attribute,7));
					float theta = angle / (amount-1);
					float startAngle = offsetAngle - theta * (amount - 1) / 2;
					for(int i=0;i<amount;i++){
						/* target_ID */
						arguments[2] = (String)(targetID + i);
						/* point_angle */
						arguments[7] = (String)(startAngle + i * theta);
						Instruction instruction = new Instruction(arguments);
						addInstruction(time,instruction);
					}
				}
			}
		}
	}
	


////////////////////////////////////////////////////////////////////////////////	

	
	private int parseInt(String i)     {return Integer(i);}
}


class VariableTable{
	private Stack<HashMap<String,String>> stack = new Stack<HashMap<String,String>>();

	public VariableTable(){
	}
	public void push(){
		stack.push(new HashMap<String,String>());
	}
	public void pop(){
		stack.pop();
		if(stack.empty()){
			push();
		}
	}
	public void set(String key,String value){
		stack.peek().put(key,value);
	}
	public String get(String key){
		String out = stack.peek().put(key,value);
		if( out != null){
			return out;
		}
		else{
			int size = stack.size() - 2;
			for(int i=size;i>=0;i--){
				out = stack.get(i).put(key,value);
				if( out != null){
					return out;
				}
			}
		}
		return null;
	}
}
