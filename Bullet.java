import java.awt.Point;
/*TODO Transform inst into bullet*/
class BulletFactory{
	private BulletFactory(){}
	public static Bullet getBullet(String bulletType,int imageId, int bulletId, int radius, int power, Point enemy,int angle){
		if(bulletType.equals("large")){
			return new CircleBullet(enemy, angle, 32, 15, bulletId);
		}
		else if (bulletType.equals("normal")){
			return new CircleBullet(enemy, angle, 12, 22, bulletId);
		}
		else if(bulletType.equals("small")){
			return new CircleBullet(enemy, angle, 8, 17, bulletId);
		}
		return null;
	}
	public static Bullet getBullet(BulletInstruction instruction,Printable enemy){
		int   bulletId = instruction.getBulletId();
		Point position = enemy.getPosition();
		int   angle    = enemy.getAngle();
		
		String bulletType = instruction.getBulletType();
		if(bulletType.equals("large")){
			return new CircleBullet(position, angle, 32, 15, bulletId);
		}
		else if (bulletType.equals("normal")){
			return new CircleBullet(position, angle, 12, 22, bulletId);
		}
		else if(bulletType.equals("small")){
			return new CircleBullet(position, angle, 8, 17, bulletId);
		}
		else if(bulletType.equals("costum")){
			int radius  = instruction.getRadius();
			int imageId = instruction.getImageId();
			return new CircleBullet(position, angle, radius, imageId, bulletId);
		}
		return null;
	}
}
abstract class Bullet implements Printable{
	//Printable
	protected int imageId;
	public int getImageId(){
		return imageId;
	}
	
	protected Point position;
	public Point getPosition(){
		return position;
	}
	
	protected int angle;
	public int getAngle(){
		return angle;
	}
	
	//Bullet
	protected int power;
	protected int bulletId;

	protected Route route;
	abstract public boolean collision(Plane plain);
	public void setRoute(Route route){
		this.route = route;
	}
	public void move(){
		if(route != null)
			position = route.move();
		try{
			angle = route.rotate();
		}
		catch(Exception e){
		}
	}
	public int getId(){
		return bulletId;
	}
	
	public Effect hit(){
		return new BulletHitEffect(position);
	}
}
class CircleBullet extends Bullet{
	private int radius;
	public CircleBullet(Point position,int angle, int radius, int imageId, int bulletId){
		this.imageId  = imageId;
		this.position = position;
		this.angle    = angle;
		this.radius   = radius;
		this.bulletId = bulletId;
	}
	public boolean collision(Plane plain){
		int x = (int)plain.getPosition().getX() - (int)position.getX();
		int y = (int)plain.getPosition().getY() - (int)position.getY();
		int r =      plain.getRadius() + radius;
		if( x*x+y*y <  r*r )
			return true;
		else
			return false;
	}
}


/*
class LineBullet extends Bullet{
	private int radius;
	public LineBullet(Point position,int angle, int length, int imageId, int bulletId){
		this.imageId  = imageId;
		this.position = position;
		this.angle    = angle;
		this.radius   = radius;
		this.bulletId = bulletId;
	}
	public boolean collision(Plane plain){
		int x = (int)plain.getPosition().getX() - (int)position.getX();
		int y = (int)plain.getPosition().getY() - (int)position.getY();
		int r = (int)plain.getRadius() + radius;
		if( x*x+y*y <  r*r )
			return true;
		else
			return false;
	}
}

*/
