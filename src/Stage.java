import java.io.*;
import java.util.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;
import java.lang.reflect.Constructor;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Stage{
	private String stageName;
	private Image[] images;
	private Instruction[][] instructions;
	private Map<String,Integer> anchors;
	private int enemyTotal;
	private int bulletTotal;
	
	int PC = 0;
	
	public class StageLoadException extends Exception{}
	public Stage(String stageName) throws StageLoadException{
		this.stageName = stageName;
		String path = "map/" + stageName + "/";
		try{
			InstructionCompiler compiler = new InstructionCompiler(path);
			instructions = compiler.getInstructionTable();
			images       = compiler.getImageTable();
			enemyTotal   = compiler.getEnemyTotal();
			bulletTotal  = compiler.getBulletTotal();
		}
		catch(MapCompileException e){
			throw new StageLoadException();
		}
	}
	public Instruction[] get(){
		if(PC < instructions.length){
			return instructions[PC++];
		}
		else{
			return null;
		}
	}
	public Instruction[] get(long time){
		if(time < instructions.length){
			return instructions[(new Long(time)).intValue()];
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
	public void restart(){
		PC = 0;
	}
	public Image getImage(int imageId){
		if(imageId < images.length)
			return images[imageId];
		else
			return null;
	}
	public int getEnemyTotal(){
		return enemyTotal;
	}
	public int getBulletTotal(){
		return bulletTotal;
	}
}
	


////////////////////////////////////////////////////////////////////////////////


class MapCompileException extends Exception{}
class InstructionCompiler{
	private ArrayList<ArrayList<Instruction>> instructionTable;
	private ArrayList<Image> images;
	private String path;
	private BufferedReader reader;
	private Root root;
		
	private VariableTable variables;
	private int enemyID = 1;
	private int bulletID = 1;
	private int wave = 1;
	
	public InstructionCompiler(String path) throws MapCompileException{
		this.path = path;
		File map;
		try{
			map = new File(path + "map");
			this.reader = new BufferedReader(new FileReader(map));
			loadDefaultImage();
			analyze();
			compile();
		}
		catch(FileNotFoundException e){
			System.err.println("File is not found");
			throw new MapCompileException();
		}
		catch(ParseException e){
			throw new MapCompileException();
		}
		
	}
	
	public Instruction[][] getInstructionTable(){
		Instruction[][] out;
		Object[] array = instructionTable.toArray();
		int length = array.length;
		out = new Instruction[length][];
		for(int i=0;i<length;i++){
			ArrayList<Instruction> arr = (ArrayList<Instruction>)array[i];
			if(array[i] != null)
				out[i] = arr.toArray(new Instruction[arr.size()]);
		}
		
		
		/* Dump */
		if(Nightmare.debug){
			for(int i=0;i<length;i++){
				Instruction[] arr = out[i];
				if(arr != null){
					for(Instruction inst:arr){
						System.out.println(i+"\t:\t"+inst.toString());
					}
				}
			}
		}
		
		
		return out;
	}
	
	public Image[] getImageTable(){
		Image[] out =  images.toArray(new Image[images.size()]);
		return out;
	}
	public int getEnemyTotal(){
		return enemyID;
	}
	public int getBulletTotal(){
		return bulletID;
	}
	
	
	private void loadDefaultImage(){
		images = new ArrayList<Image>();
		try{
			File imageIndex;
			imageIndex = new File("image/image");
			BufferedReader reader = new BufferedReader(new FileReader(imageIndex));
			
			String line;
			while((line = reader.readLine()) != null && !(line.equals(""))){
				Scanner tokens = new Scanner(line);
				int imageID = tokens.nextInt();
				String imageName = tokens.next();
				Image image;
				try{
					image = ImageIO.read(new File("image/"+imageName));
					int size = images.size();
					for(int i=size;i<imageID;i++){
						images.add(null);
					}
				images.add(imageID,image);
				}
				catch(IOException e){
					System.err.println("Image '"+imageName+"' can't open");
				}
			}
		}
		catch(IOException e){
			System.err.println(e.getMessage());
		}
	}
	private void analyze() throws ParseException{
		root = new Root();
	}
	private void compile(){
		instructionTable = new ArrayList<ArrayList<Instruction>>();
		variables = new VariableTable();
		root.calc();
	}
	private void addInstruction(int time,Instruction instruction){
		int size = instructionTable.size();
		for(int i=size;i<=time;i++)
			instructionTable.add(null);
		ArrayList<Instruction> instructions;
		if((instructions = instructionTable.get(time)) == null){
			instructions = new ArrayList<Instruction>();
			instructionTable.set(time,instructions);
		}
		instructions.add(instruction);
	}
	
	
	private interface Element{
		public void calc();
	}
	


////////////////////////////////////////////////////////////////////////////////


	private class Root implements Element{
		private Element[] elements;
		public Root() throws ParseException{
			String[] allowed = {"PATH"};
			elements = parse(allowed);
		}
		public void calc(){
			for(Element e:elements){
				e.calc();
			}
		}
	}
	


////////////////////////////////////////////////////////////////////////////////
	
	
	private class Path implements Element{
		private Element[] elements;
		private Value[] attributes;
		public Path(Value[] attributes) throws ParseException{
			this.attributes = attributes;
			String[] allowed = {"LOOP","ENEMY"};
			elements = parse(allowed);
		}
		public void calc(){
			String time = attributes[1].value();
			variables.set("time",time);
			for(Element e:elements){
				e.calc();
			}
		}
	}
	


////////////////////////////////////////////////////////////////////////////////
	
	
	private class Loop implements Element{
		private Element[] elements;
		private Value[] attributes;
		public Loop(Value[] attributes) throws ParseException{
			this.attributes = attributes;
			String[] allowed = {"LOOP","ENEMY","BULLET","ROUTE"};
			elements = parse(allowed);
		}
		public void calc(){
			String var  = attributes[1].value();
			float start = attributes[2].floatValue();
			float end   = attributes[3].floatValue();
			float inter = attributes[4].floatValue();
			variables.push();
			for(float val=start;val<=end;val+=inter){
				variables.set(var,parseString(val));
				for(Element e:elements){
					e.calc();
				}
			}
			variables.pop();
		}
	}
	


////////////////////////////////////////////////////////////////////////////////	


	private class Enemy implements Element{
		private Element[] elements;
		private Value[] attributes;
		public Enemy(Value[] attributes) throws ParseException{
			this.attributes = attributes;
			String[] allowed = {"MOVE","SHOOT"};
			elements = parse(allowed);
		}		
		
		public void calc(){
			String[] arguments = new String[7];
			//inst_type
			arguments[0] = "enemy";
			//enemy_ID
			arguments[1] = parseString(enemyID);
			//enemy_type
			String type = attributes[2].value();
			arguments[2] = type;
			//position
			arguments[3] = attributes[3].value();
			
			if(type.equals("custom")){
				//image_ID
				arguments[4] = attributes[4].value();
				//radius
				arguments[5] = attributes[5].value();
				//HP
				arguments[6] = attributes[6].value();
			}
			
			int baseTime = parseInt(variables.get("time"));
			int plusTime = attributes[1].intValue();
			int time = baseTime + plusTime;
			
			Instruction instruction = new Instruction(arguments);
			addInstruction(time,instruction);
			
			variables.push();
			variables.set("time",parseString(time));
			variables.set("target_ID",parseString(enemyID));
			variables.set("target_type","enemy");
			
			for(Element e:elements){
				e.calc();
			}
			variables.pop();
			
			enemyID += 1;
		}
	}
		

////////////////////////////////////////////////////////////////////////////////

	
	private class Move implements Element{
		private Element[] elements;
		private Value[] attributes;
		public Move(Value[] attributes) throws ParseException{
			this.attributes = attributes;
			String[] allowed = {"LOOP","ROUTE"};
			elements = parse(allowed);
		}
		public void calc(){
			for(Element e:elements){
				e.calc();
			}
		}
	}
	
	

////////////////////////////////////////////////////////////////////////////////

	
	private class Shoot implements Element{
		private Element[] elements;
		private Value[] attributes;
		public Shoot(Value[] attributes) throws ParseException{
			this.attributes = attributes;
			String[] allowed = {"LOOP","BULLET"};
			elements = parse(allowed);
		}
		public void calc(){
			for(Element e:elements){
				e.calc();
			}
		}
	}
	
	

////////////////////////////////////////////////////////////////////////////////

	
	private class Bullet implements Element{
		private Element[] elements;
		private Value[] attributes;
		public Bullet(Value[] attributes) throws ParseException{
			this.attributes = attributes;
			String[] allowed = {"MOVE"};
			elements = parse(allowed);
		}	
		
		public void calc(){
		
			String[] arguments = new String[7];
			//inst_type
			arguments[0] = "bullet";
			//enemy_ID
			arguments[1] = parseString(enemyID);
			//bullet_ID
			arguments[2] = parseString(bulletID);
			//bullet_type
			String type = attributes[2].value();
			arguments[3] = type;
			
			if(type.equals("custom")){
				//image_ID
				arguments[4] = attributes[3].value();
				//radius
				arguments[5] = attributes[4].value();
				//power
				arguments[6] = attributes[5].value();
			}
			
			int baseTime = parseInt(variables.get("time"));
			int plusTime = attributes[1].intValue();
			int time = baseTime + plusTime;
			
					//System.out.println(bulletID);
			Instruction instruction = new Instruction(arguments);
			addInstruction(time,instruction);
			
			variables.push();
			variables.set("time",parseString(time));
			variables.set("target_ID",parseString(bulletID));
			variables.set("target_type","bullet");
			for(Element e:elements){
				e.calc();
			}
			variables.pop();
			
			bulletID += 1;
		}
	}


////////////////////////////////////////////////////////////////////////////////

	
	private class Route implements Element{
		private Value[] attributes;
		public Route(Value[] attributes){
			this.attributes = attributes;
		}
		
		public void calc(){
			
			String[] arguments = new String[10];
			/* inst_type */
			arguments[0] = "route";
			/* target_type */
			arguments[1] = variables.get("target_type");
			/* target_ID */
			arguments[2] = variables.get("target_ID");
			/* route_type */
			boolean isTypeValid = false;
			String routeType = attributes[2].value();
			if(routeType.equals("stop")){
				isTypeValid = true;
				arguments[3] = "stop";
			}
			else if(routeType.equals("line")){
				isTypeValid = true;
				arguments[3] = "linear";
				/* speed */
				arguments[4] = attributes[3].value();
				/* point_refer */
				arguments[5] = attributes[4].value();
				/* point_offset */
				arguments[6] = attributes[5].value();
				/* point_angle */
				arguments[7] = attributes[6].value();
			}
			
			if(isTypeValid){
				int baseTime = parseInt(variables.get("time"));
				int plusTime = attributes[1].intValue();
				int time = baseTime + plusTime;
				Instruction instruction = new Instruction(arguments);
				addInstruction(time,instruction);
			}
		}
	}
	


////////////////////////////////////////////////////////////////////////////////	

	
	private String parseString(int i)  {return Integer.toString(i);}
	private String parseString(float f){return Float.toString(f);}
	private int    parseInt(String i){return Float.valueOf(i).intValue();}


////////////////////////////////////////////////////////////////////////////////	

	
	private class ParseException extends Exception{};
	private Element[] parse(String[] allowed) throws ParseException{
		ArrayList<Element> elements = new ArrayList<Element>();
		String line = null;
		try{
			int length = allowed.length;
			while((line = reader.readLine()) != null && !("".equals(line))){
				String statement = (new Scanner(line)).next();
				if(statement.equals("END")){break;}
				for(int i=0;i<length;i++){
					if(allowed[i].equals(statement)){
						elements.add(createElement(statement,line));
						break;
					}
				}
			}
		}
		catch(IOException e){
			System.err.println("Can't not read next line");
		}
		catch(NoSuchElementException e){
			throw new ParseException();
		}
		
		return elements.toArray(new Element[elements.size()]);
	}
	
	private class NoSuchElementException extends Exception{};
	private Element createElement(String elementName,String attributes) throws NoSuchElementException{
		
		String className = capitalize(elementName);
		
		String[] tokens = attributes.trim().split(" +");
		int length = tokens.length;
		Value[] values = new Value[length];
		for(int i=0;i<length;i++){
			values[i] = new Value(tokens[i]);
		}
		
		Element element;
		
		try{
			Class[] parameter = {this.getClass(),values.getClass()};
			Class classType = Class.forName("InstructionCompiler$"+className);
			Constructor constructor = classType.getConstructor(parameter);
			element = (Element)constructor.newInstance(this,values);
		}
		catch(NoSuchMethodException e){
			System.err.println("Syntax error:"+elementName);
			throw new NoSuchElementException();
		}
		catch(SecurityException e){
			System.err.println("Privilege error:"+className);
			throw new NoSuchElementException();
		}
		catch(ClassNotFoundException e){
			System.err.println("Class '"+className+"' is not found");
			throw new NoSuchElementException();
		}
		catch(InstantiationException e){
			System.err.println("Class '"+className+"' can't get instance");
			throw new NoSuchElementException();
		}
		catch(IllegalAccessException e){
			System.err.println("IllegalAccessException : "+className);
			throw new NoSuchElementException();
		}
		catch(java.lang.reflect.InvocationTargetException e){
			System.err.println("InvocationTargetException : "+className);
			throw new NoSuchElementException();
		}
		return element;
	}
	
	private String capitalize(String s){
		char[] str = s.toLowerCase().toCharArray();
		str[0] = Character.toUpperCase(str[0]);
		return new String(str);
	}
	
	private class Value{
		private String _value;
		public Value(String _value){this._value = _value;}
		
		public int    intValue()  {return Float.valueOf(calcValue()).intValue();}
		public float  floatValue(){return Float.valueOf(calcValue());}
		public String value()     {return calcValue();}
		
		private String calcValue(){
			String value = _value;
			try{
				if(value.matches(".*\\d.*")){
					//Number or Point
					if(value.matches(".+,.+")){
						//Point
						String[] pair = value.split(",");
						pair[0] = Calculator.calculate(replaceVariable(pair[0]));
						pair[1] = Calculator.calculate(replaceVariable(pair[1]));
						value = pair[0] + ',' + pair[1];
					}
					else{
						//Number
						value = Calculator.calculate(replaceVariable(value));
					}
				}
				else{
					//Pure string
				}
			}
			catch(NoSuchVariableException e){
			}
			return value;
		}
		
		private class NoSuchVariableException extends Exception{};
		private String replaceVariable(String s) throws NoSuchVariableException{
			String result = s;
			String var    = "";
			String value  = "";
			Pattern pattern = Pattern.compile("[_A-Z]+");
			Matcher matcher = pattern.matcher(s);
			while(matcher.find()){
				var = matcher.group();
				value = variables.get(var);
				if(value != null){
					result = result.replace(var,value);
				}
				else{
					System.err.println("No such variable : "+var);
					throw new NoSuchVariableException();
				}
			}
			return result;
		}
	}
}
	


////////////////////////////////////////////////////////////////////////////////



class VariableTable{
	private Stack<HashMap<String,String>> stack = new Stack<HashMap<String,String>>();

	public VariableTable(){
		push();
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
		String out = stack.peek().get(key);
		if( out != null){
			return out;
		}
		else{
			int size = stack.size() - 2;
			for(int i=size;i>=0;i--){
				out = stack.get(i).get(key);
				if( out != null){
					return out;
				}
			}
		}
		return null;
	}
}
	
