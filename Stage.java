import java.io.*;
import java.util.*;
import java.awt.*;
import java.lang.Math.*;

public class Stage{
	Image[] images;
	Instruction[][] instructions;
	Map<String,Integer> anchors;
	
	int PC = 0;
	
	public Stage(String map){
		String path = "map/" + map + "/";
		ImageMapper mapper = new ImageMapper(new FileReader(path+"image"));
		images = mapper.get();
		InstructionParser parser = new InstructionParser(new FileReader(path+"map"));
		instructions = parser.get();
	}
	public Instruction[] get(){
		return instructions[PC];
	}
	public void jump(String anchor){
		int target = anchors.get(anchor);
		if(target != null){
			PC = target;
		}
	}
	public Image getImage(int imageId){
		return images[imageId];
	}
}

class ImageMapper{
	public ImageMapper(FileReader map){
		BufferedReader reader = new BufferedReader(map);
		mapImages(reader);
	}
	public Image[] get(){
		return (Image[])images.toArray();
	}
	private ArrayList<Image> images;
	private void mapImages(BufferedReader reader){
		
	}
}

class InstructionParser{
	ArrayList<ArrayList<Instruction>> instructions;
	Map<String,Integer> anchors;
	BufferedReader reader;
	
	int enemyId = 0;
	int bulletId = 0;
	int wave = 0;
	
	public InstructionParser(FileReader map){
		BufferedReader reader = new BufferedReader(map);
		parseInstruction(reader);
	}
	public Instruction[][] get(){
		ArrayList<Instruction>[] inter = (ArrayList<Instruction>[])instructions.toArray();
		Instruction[][] out = new Instruction[inter.length][];
		for(int i=0;i<inter.length;i++){
			out[i] = (Instruction[])inter[i].toArray();
		}
		return out;
	}
	private void parseInstruction(BufferedReader reader){
		ArrayList<ArrayList<Instruction>> instructions;
		String line;
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			String type = tokens.next();
			int time = tokens.nextInt();
			switch(type){
				case "PARTA":
					statePart(time);
					break;
				case "PARTB":
					statePart(time);
					break;
				case "BOSSA":
					stateBoss(time);
					break;
				case "BOSSB":
					stateBoss(time);
					break;
			}
		}
	}
	
	private void statePart(int baseTime){
		String line;
		int time;
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			switch(tokens.next()){
				case "GROUP":
					    time  = tokens.nextInt();
					int times = tokens.nextInt();
					int inter = tokens.nextInt();
					for(int i=0;i<times;i++){
						stateEnemy(baseTime + time);
						time += inter;
					}
					break;
				case "ENEMY":
					time  = tokens.nextInt();
					stateEnemy(baseTime + time);
					break;
				case "END":
					return;
				default:
					
			}
		}
	}
	private void stateEnemy(int baseTime){
		String line;
		int id = enemyId;
		enemyId++;
		String[] arguments = new String[7];
		arguments[0] = "enemy";
		arguments[1] = Integer.toString(id);
		
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			switch(tokens.next()){
				case "TYPE":
					String type  = tokens.next();
					arguments[2] = type;
					if(type == "custom"){
						arguments[4] = tokens.next();
						arguments[5] = tokens.next();
						arguments[6] = tokens.next();
					}
					break;
				case "POSITION":
					arguments[3] = tokens.next();
					Instruction enemyInstruction = new Instruction(arguments);
					addInst(baseTime,enemyInstruction);
					break;
				case "MOVE":
					stateMove(baseTime,"enemy",id,1);
					break;
				case "SHOOT":
					stateShoot(baseTime,id);
				case "END":
					return;
				default:
					
			}
		}
	}
	private void stateShoot(int baseTime,int enemyId){
		String line;
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			switch(tokens.next()){
				case "BULLET":
					int time   = tokens.nextInt();
					int times  = tokens.nextInt();
					int inter  = tokens.nextInt();
					int amount = tokens.nextInt();
					for(int i=0;i<times;i++){
						stateBullet(time,enemyId,amount);
						time += inter;
					}
					break;
				case "END":
					return;
				default:
					
			}
		}
	}
	private void stateBullet(int baseTime,int enemyId,int amount){
		String line;
		int id = bulletId;
		bulletId += amount;
		
		String[] arguments = new String[7];
		arguments[0] = "bullet";
		arguments[1] = Integer.toString(enemyId);
		
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			switch(tokens.next()){
				case "TYPE":
					String type  = tokens.next();
					arguments[2] = type;
					if(type == "custom"){
						arguments[4] = tokens.next();
						arguments[5] = tokens.next();
						arguments[6] = tokens.next();
					}
					for(int i=0;i<amount;i++){
						arguments[1] = Integer.toString(id + i);
						Instruction bulletInstruction = new Instruction(arguments);
						addInst(baseTime,bulletInstruction);
					}
					break;
				case "MOVE":
					stateMove(baseTime,"bullet",id,amount);
					break;
				case "END":
					return;
				default:
					
			}
		}
	}
	private void stateMove(int baseTime,String targetType,int targetId,int amount){
		String line;
		String[] arguments = new String[9];
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			switch(tokens.next()){
				case "Route":
					int time = tokens.nextInt();
					stateRoute(baseTime+time,targetType,targetId,amount,tokens);
					break;
				case "END":
					return;
				default:
					
			}
		}
	}
	private void stateRoute(int baseTime,String targetType,int targetId,int amount,Scanner tokens){
		String[] arguments = new String[9];
		arguments[0] = "route";
		arguments[1] = targetType;
		arguments[4] = tokens.next();
		
		String type = tokens.next();
		switch(type){
			case "FAN":
				arguments[3] = "linear";
				arguments[5] = tokens.next();
				
				int angle = tokens.nextInt();
		
				if(amount == 1){
					arguments[2] = Integer.toString(targetId);
					arguments[7] = "0";
					Instruction routeInstruction = new Instruction(arguments);
					addInst(baseTime,routeInstruction);
				}
				else{
					float theta = angle/amount;
					float startAngle = -1 * theta * ((amount - 1)/2);
					for(int i=0;i<amount;i++){
						arguments[2] = Integer.toString(targetId + i);
						arguments[6] = Integer.toString(startAngle + i*theta);
						Instruction routeInstruction = new Instruction(arguments);
						addInst(baseTime,routeInstruction);
					}
				}
				break;
			default:
		}
	}
	
	private void stateBoss(int baseTime){
		int time = 0;
		int id = enemyId;
		enemyId++;
		
		String[] arguments = new String[7];
		arguments[0] = "enemy";
		arguments[1] = Integer.toString(enemyId);
		arguments[2] = "custom";
		
		String line;
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			switch(tokens.next()){
				case "TYPE":
					arguments[4] = tokens.next();
					arguments[5] = tokens.next();
					break;
				case "WAVE":
					int period = tokens.nextInt();
					time += period;
					anchors.add("WAVE"+wave,new Integer(baseTime + time));
					wave++;
					arguments[6] = tokens.next();
					
					Instruction enemyInstruction = new Instruction(arguments);
					addInst(baseTime + time,enemyInstruction);
					Instruction bossInstruction = new Instruction(new String[]{"boss",Integer.toString(period)});
					addInst(baseTime + time,bossInstruction);
					stateWave(baseTime,id);
					break;
				case "END":
					return;
				default:
					
			}
		}
	}
	private void stateWave(int baseTime,int id){
		String[] arguments = new String[9];
		String line;
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			switch(tokens.next()){
				case "MOVE":
					stateMove(baseTime,"enemy",id,1);
					break;
				case "BULLET":
					int time   = tokens.nextInt();
					int times  = tokens.nextInt();
					int inter  = tokens.nextInt();
					int amount = tokens.nextInt();
					for(int i=0;i<times;i++){
						stateBullet(baseTime,id,amount);
						time += inter;
					}
					break;
				case "END":
					return;
				default:
			}
		}
	}
	
	private void addInst(int time,Instruction inst){
		ArrayList<Instruction> list = instructions.get(time);
		if(list == null){
			list = new ArrayList<Instruction>();
			instructions.add(time,list);
		}
		list.add(inst);
	}
}

