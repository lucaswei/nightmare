package stage;

import java.awt.Point;
import java.lang.Math.*;

public class Stage{
	Instruction[][] instructions;
	Map<String,Integer> anchors;
	
	int PC = 0;
	
	public Stage(File map){
		StageParser parser = new StageParser(map);
		instructions = parser.get();
	}
	public Instruction[] get(){
		return instructions.get(time);
	}
	public void jump(String anchor){
		target = anchors.get(anchor);
		if(target != null){
			PC = target;
		}
	}
}

class StageParser{
	ArrayList<ArrayList<Instruction>> instructions;
	Map<String,Integer> anchors;
	BufferedReader reader;
	
	int enemyId = 0;
	int bulletId = 0;
	
	public StageParser(File map){
		reader = new BufferedReader(map);
		parseInstruction(reader);
	}
	public Instruction[][] get(){
		ArrayList<Instruction>[] inter = instructions.toArray();
		Instruction[][] out = new Instruction[inter.length][];
		for(int i=0;i<inter.length;i++){
			out[i] = inter[i].toArray();
		}
		return out;
	}
	private parseInstruction(BufferedReader reader){
		ArrayList<ArrayList<Instruction>> instructions;
		String line;
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			String type = tokens.next();
			int time = tokens.nextInt();
			switch(type){
				case "PARTA":
					anchors.add("PARTA",time);
					statePart(time);
					break;
				case "PARTB":
					anchors.add("PARTB",time);
					statePart(time);
					break;
				case "BOSSA":
					anchors.add("BOSSA",time);
					stateBoss(time);
					break;
				case "BOSSB":
					anchors.add("BOSSB",time);
					stateBoss(time);
					break;
			}
		}
	}
	
	private statePart(int baseTime){
		String line;
		while((line = reader.readLine()) != null){
			Scanner tokens = new Scanner(line);
			switch(tokens.next()){
				case "GROUP":
					int time  = tokens.nextInt();
					int times = tokens.nextInt();
					int inter = tokens.nextInt();
					for(int i=0;i<times;i++){
						statsEnemy(baseTime + time);
						time += inter;
					}
					break;
				case "ENEMY":
					int time  = tokens.nextInt();
					statsEnemy(baseTime + time);
					break;
				case "END":
					return;
				default:
					
			}
		}
	}
	private stateEnemy(int baseTime){
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
					addInst(insts,baseTime,enemyInstruction);
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
	private stateShoot(int baseTime,int enemyId){
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
						statsBullet(time,enemyId,amount);
						time += inter;
					}
					break;
				case "END":
					return;
				default:
					
			}
		}
	}
	private stateBullet(int baseTime,int enemyId,int amount){
		String line;
		int id = bulletId;
		bullet += amount;
		
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
						addInst(insts,baseTime,bulletInstruction);
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
	private stateMove(int baseTime,String targetType,int targetId,int amount){
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
	private stateRoute(int baseTime,String targetType,int targetId,int amount,Scanner tokens){
		String arguments = new String[9];
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
					arguments[2] = Integer.toString(id);
					arguments[7] = "0";
					Instruction routeInsturction = new Instruction(arguments);
					addInst(insts,baseTime,routetInstruction);
				}
				else{
					float theta = angle/amount;
					float startAngle = -1 * theta * ((amount - 1)/2);
					for(int i=0;i<amount;i++){
						arguments[2] = id + i;
						arguments[6] = startAngle + i*theta;
						Instruction routeInsturction = new Instruction(arguments);
						addInst(insts,baseTime,routeInstruction);
					}
				}
				break;
			default:
		}
	}
	
	private stateBoss(int baseTime){
		int time = 0;
		int id = enemyId;
		enemtId++;
		
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
					anchors.add("WAVE"+wave,baseTime + time);
					wave++;
					arguments[6] = tokens.next();
					
					Instruction enemyInstruction = new Instruction(arguments);
					addInst(insts,baseTime + time,enemyInstruction);
					Instruction bossInstruction = new Instruction(new String[]{"boss",period});
					addInst(insts,baseTime + time,bossInstruction);
					stateWave(baseTime,id);
					break;
				case "END":
					return;
				default:
					
			}
		}
	}
	private stateWave(int baseTime,int id){
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
						statsBullet(baseTime,id,amount);
						time += inter;
					}
					break;
				case "END":
					return;
				default:
			}
		}
	}
	
	private addInst(int time,Instruction inst){
		ArrayList<Instruction> list = insts.get(time);
		if(list == null){
			list = new ArrayList<Instruction>();
			insts.add(time,list);
		}
		list.add(inst);
	}
}

