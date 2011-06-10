/*
 *0      1           2          3           4          5           6            7
 *enemy  enemy_id    enemy_type position    image_id   radius      Hp
 *bullet enemy_id    bullet_id  bullet_type image_id   radius      power
 *route  target_type target_id  route_type  speed      point_refer point_offset point_angle curve_radio curve_angle
 *boss   period
*/

import java.awt.Point;
import java.lang.Float;


interface IInstruction{
	String getInstType();
}



interface EnemyInstruction extends IInstruction{
	int    getEnemyId();
	String getEnemyType();
	Point  getPoint();
	int    getImageId();
	int    getRadius();
	int    getHp();
}



interface BulletInstruction extends IInstruction{
	int    getEnemyId();
	int    getBulletId();
	String getBulletType();
	int    getImageId();
	int    getRadius();
	int    getPower();
}



interface RouteInstruction extends IInstruction{
	int    getTargetId();
	String getTargetType();
	String getRouteType();
	int    getSpeed();
	String getPointRefer();
	int    getPointAngle();
	Point  getPointOffset();
	float  getCurveRadius();
	int    getCurveAngle();
}



class Instruction implements
             EnemyInstruction,
             BulletInstruction,
             RouteInstruction{
	private String[] arguments;
	
	public  Instruction(String[] arguments){
		this.arguments = arguments.clone();
	}
		
	/* IIstruction */
	public  String getInstType(){
		return arguments[0];
	}
	
	public  String toString(){
		String out = "";
		int length = arguments.length;
		for(int i=0;i<length;i++){
			out += arguments[i] + "\t";
		}
		return out;
	}
	
	/* Printable */
	public  int    getImageId(){
		return arguments[4]!=null ? Float.valueOf(arguments[4]).intValue() : 0;
	}
	public  int    getRadius(){
		return arguments[5]!=null ? Float.valueOf(arguments[5]).intValue() : 0;
	}
	
	
	
	/* EnemyInstruction */
	public  int    getEnemyId(){
		return arguments[1]!=null ? Float.valueOf(arguments[1]).intValue() : 0;
	}
	public  String getEnemyType(){
		return arguments[2];
	}
	public  Point  getPoint(){
		if(arguments[3] != null){
			String[] pair = arguments[3].split(",");
			int x = Float.valueOf(pair[0]).intValue();
			int y = Float.valueOf(pair[1]).intValue();
			return new Point(x,y);
		}
		else{
			return null;	
		}
	}
	public  int    getHp(){
		return arguments[6]!=null ? Float.valueOf(arguments[6]).intValue() : 0;
	}
	
	
	
	/* BulletInstruction */
	public  int    getBulletId(){
		return arguments[2]!=null ? Float.valueOf(arguments[2]).intValue() : 0;
	}
	public  String getBulletType(){
		return arguments[3];
	}
	public  int    getPower(){
		return arguments[6]!=null ? Float.valueOf(arguments[6]).intValue() : 0;
	}
	
	
	
	/* RouteInstruction */
	public  int    getTargetId(){
		return arguments[2]!=null ? Float.valueOf(arguments[2]).intValue() : 0;
	}
	public  String getTargetType(){
		return arguments[1];
	}
	public  String getRouteType(){
		return arguments[3];
	}
	public  int    getSpeed(){
		return arguments[4]!=null ? Float.valueOf(arguments[4]).intValue() : 0;
	}
	public  String getPointRefer(){
		return arguments[5];
	}
	public  Point  getPointOffset(){
		if(arguments[6] != null){
			String[] pair = arguments[6].split(",");
			int x = Float.valueOf(pair[0]).intValue();
			int y = Float.valueOf(pair[1]).intValue();
			return new Point(x,y);
		}
		else{
			return null;	
		}
	}
	public  int    getPointAngle(){
		return arguments[7]!=null ? Float.valueOf(arguments[7]).intValue() : 0;
	}
	public  float  getCurveRadius(){
		return arguments[8]!=null ? Float.valueOf(arguments[8]) : 0;
	}
	public  int    getCurveAngle(){
		return arguments[9]!=null ? Float.valueOf(arguments[9]).intValue() : 0;
	}
}
