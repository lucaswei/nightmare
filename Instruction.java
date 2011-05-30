/*
 *0      1           2          3           4          5           6            7
 *enemy  enemy_id    enemy_type position    image_id   radius      Hp
 *bullet enemy_id    bullet_id  bullet_type image_id   radius      power
 *route  target_type target_id  route_type  speed      point_refer point_offset point_angle curve_radio curve_angle
 *boss   period
*/

import java.awt.Point;
import java.lang.Integer;


private interface IInstruction{
	String getInstType();
}



public interface EnemyInstruction extends IInstruction{
	int    getEnemyId();
	String getEnemyType();
	Point  getPoint();
	int    getImageId();
	int    getRadius();
	int    getHp();
}



public interface BulletInstruction extends IInstruction{
	int    getEnemyId();
	int    getBulletId();
	String getBulletType();
	int    getImageId();
	int    getRadius();
	int    getPower();
}



public interface RouteInstruction extends IInstruction{
	int    getTargetId();
	String getTargetType();
	String getRouteType();
	int    getSpeed();
	String getCoordType();
	String getPointRefer();
	int    getPointAngle();
	Point  getPointOffset();
	float  getCurveRadius();
	int    getCurveAngle();
}



public class Instruction implements
             EnemyInstruction,
             BulletInstruction,
             RouteInstruction{
	private String[] arguments;
	
	public  Instruction(String[] arguments){
		this.arguments = arguments.copy();
	}
		
	/* IIstruction */
	public  String getInstType(){
		return arguments[0];
	}
	
	
	
	/* Printable */
	public  int    getImageId(){
		return Integer.valueOf(arguments[4]);
	}
	public  int    getRadius(){
		return Integer.valueOf(arguments[5]);
	}
	
	
	
	/* EnemyInstruction */
	public  int    getEnemyId(){
		return Integer.valueOf(arguments[1]);
	}
	public  String getEnemyType(){
		return arguments[2];
	}
	public  Point  getPoint(){
		String[] pair = arguments[3].split(',');
		x = Integer.valueOf(pair[0]);
		y = Integer.valueOf(pair[1]);
		return new Point(x,y);
	}
	public  int    getHp(){
		return arguments[6];
	}
	
	
	
	/* BulletInstruction */
	public  int    getBulletId(){
		return Integer.valueOf(arguments[2]);
	}
	public  String getBulletType(){
		return arguments[3];
	}
	public  int    getPower(){
		return Integer.valueOf(arguments[6]);
	}
	
	
	
	/* RouteInstruction */
	public  int    getTargetId(){
		return Integer.valueOf(arguments[2]);
	}
	public  String getTargetType(){
		return arguments[1];
	}
	public  String getRouteType(){
		return arguments[3];
	}
	public  int    getSpeed(){
		return Integer.valueOf(arguments[4]);
	}
	public  String getCoordType(){
		return arguments[5];
	}
	public  String getPointRefer(){
		return argument[6];
	}
	public  int    getPointAngle(){
		return Integer.valueOf(arguments[7]);
	}
	public  Point  getPointOffset(){
		String[] pair = arguments[7].split(',');
		x = Integer.valueOf(pair[0]);
		y = Integer.valueOf(pair[1]);
		return new Point(x,y);
	}
	public  float  getCurveRefer(){
		return Floot.valueOf(arguments[7]);
	}
	public  int    getCurveAngle(){
		return Integer.valueOf(arguments[7]);
	}
}
