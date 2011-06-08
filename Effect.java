import java.awt.Point;

abstract class Effect implements Printable{
	abstract public boolean disappear();
}



class EnemyExplodEffect extends Effect{
	public EnemyExplodEffect(Point p){
		position = p;
	}
	
	private int imageId = 0;
	public  int getImageId(){
		return imageId;
	}
	
	private Point position;
	public  Point getPosition(){
		return position;
	}
	
	public  int getAngle(){
		return 0;
	}
	
	private int duration = 5;
	public boolean disappear(){
		if(duration > 0){
			duration--;
			return false;
		}
		else{
			return true;
		}
	}
}



class BulletHitEffect extends Effect{
	public BulletHitEffect(Point p){
		position = p;
	}
	
	private int imageId = 50;
	public  int getImageId(){
		return imageId;
	}
	
	private Point position;
	public  Point getPosition(){
		return position;
	}
	
	public  int getAngle(){
		return 0;
	}
	
	private int duration = 3;
	public boolean disappear(){
		if(duration > 0){
			duration--;
			return false;
		}
		else{
			return true;
		}
	}
}


