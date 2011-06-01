import java.awt.Point;

public abstract class Printable{
	protected int imageId;
	protected Point position;
	protected float angle;
	public int getImageId(){
		return imageId;
	}
	public Point getPosition(){
		return position;
	}
	public float getAngle(){
		return angle;
	}
	public void setPosition(Point position){
		this.position.setLocation(position);
	}
}

abstract class Plane extends Printable{
	protected int radius;
	public int getRadius(){
		return radius;
	}
	public abstract void move();
	/* -1:gameover ; -2:nothing	*/
	public abstract int crash();
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
		this.BULLETIMAGEID = 17;
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
			CircleBullet bullet = new CircleBullet(bulletPoint, BULLETRADIUS, BULLETIMAGEID, 0);
			int x = (int)position.getX();
			int y = (int)position.getY()-10;
			Point destiny = new Point(x, y);
			Route bulletRoute = RouteFactory.getRoute(position, destiny, BULLETSPEED, "linear");
			bullet.setRoute(bulletRoute);
			return bullet;
		}
		return null;
	}

	public int crash(){
		life--;
		position.setLocation(225, 580);
		return life;
	}
	public int getPower(){
		return power;
	}
	public int getLife(){
		return life;
	}
}
