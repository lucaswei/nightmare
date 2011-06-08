import java.awt.Point;

public interface Printable{
	int getImageId();
	Point getPosition();
	int getAngle();
}

abstract class Plane implements Printable{
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
	
	//Plane
	protected int radius;
	public int getRadius(){
		return radius;
	}
	public abstract void move();
	public abstract boolean hurt(int harm);
	public abstract Effect crash();
	
}

class Hero extends Plane{
	private ActionSource source;
	private int slowSpeed = 5;
	private int normalSpeed = 15;

	private int life;
	private int power;

	private int HEIGHT;
	private int WIDTH;
	
	private Bullet bullet;
	private int BULLETRADIUS;
	private int BULLETIMAGEID;
	private int BULLETSPEED;
	
	private int shooting = 0;


	public Hero(ActionSource source, Point position, int radius, int life){
		this.position = position;
		this.HEIGHT=600;
		this.WIDTH=450;
		this.power = 1;
		this.source = source;
		this.life = life;
		this.radius = radius;
		this.imageId = 11;
		/*set bullet information*/
		this.BULLETRADIUS  = 8;
		this.BULLETIMAGEID = 23;
		this.BULLETSPEED   = 25;
		/*                      */
	}
	
	public void move(){
		int x = (int)position.getX();
		int y = (int)position.getY();
		int speed = normalSpeed;
		if(source.check("SLOW"))
			speed = slowSpeed;
		if(source.check("UP")){
			if(y-speed>=0)
				y = y-speed;
		}
		else if(source.check("DOWN")){
			if(y+speed<HEIGHT)
				y = y+speed;
		}

		if(source.check("LEFT")){
			if(x-speed>=0)
				x = x-speed;
		}else if(source.check("RIGHT")){
			if(x+speed<WIDTH)
				x = x+speed;
		}
		position.setLocation(x,y);
	}
	public boolean isPause(){
		if(source.check("PAUSE")){
			source.clear();
			return true;
		}
		else
			return false;
	}

	public Bullet shoot(){
		if(source.check("SHOOT")){
			Point bulletPoint = new Point( position);
			CircleBullet bullet = new CircleBullet(bulletPoint,0, BULLETRADIUS, BULLETIMAGEID, shooting++);
			int x = (int)position.getX();
			int y = (int)position.getY()-10;
			Point destination = new Point(x, y);
			Route bulletRoute = RouteFactory.getRoute("linear",position, destination, BULLETSPEED, 0);
			bullet.setRoute(bulletRoute);
			return bullet;
		}
		return null;
	}
	public boolean hurt(int harm){
		life--;
		position.setLocation(225, 580);
		invincibleTime = 32;
		return true;
	}

	public Effect crash(){
		return null;
	}
	public int getPower(){
		return power;
	}
	public int getLife(){
		return life;
	}
	
	private int invincibleTime = 0;
	public boolean isInvincible(){
		if(invincibleTime > 0){
			invincibleTime--;
			return true;
		}
		else{
			return false;
		}
	}
}
