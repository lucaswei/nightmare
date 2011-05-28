public abstract class Printable{
	private int imagedID;
	private Point position;
	private float angle;
	public int getID(){
		return imageID;
	}
	public Point getPosition(){
		return position;
	}
	public float getAngle(){
		return angle;
	}
}

abstract class Plane extends Printable{
	private radius;
	public int getRadius(){
		return radius;
	}
	public abstract void move();
	public abstract Bullet[] shoot();
	/* -1:gameover ; -2:nothing	*/
	public abstract int crash();
}

class Player extends Plane{
	private ActionSource source;
	private int slowSpeed;
	private int normalSpeed;

	private int life;
	private int power;

	private int HEIGHT;
	private int WIDTH;
	
	private Bullet[] bullet;


	public Player(ActionSource source, Point position, int radius, int life){
		this.position = position;
		this.HEIGHT=600;
		this.WIDTH=450;
	power = 1;
		this.source = source;
		this.life = life;
		this.radius = radius;
	}
	
	public void move(){
		int x = position.getX();
		int y = position.getY();
		int speed = normalSpeed;
		if(source.check("SLOW"))
			speed = slowSpeed;
		if(source.check("UP")){
			if(y-speed>=0)
				y = y-speed;
		}
		else if(source.check("DOWN")){
			if(y+speed>HEIGHT)
				y = y+speed;
		}

		if(source.check("LEFT"){
			if(x-speed>=0)
				x = x-speed;
		}else if(source.check("RIGHT")){
			if(x+speed<WIDTH)
				x = x+speed;
		}
		position.setLocation(x,y)
	}

	public Bullet[] shoot(){
		if(source.check("SHOOT")){
			return bullet;
		}
	}

	public int crash(){
		life--;
		return life;
	}

}
