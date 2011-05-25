public abstract class Printable{
	private int imagedID;
	public int getID(){
		return imageID;
	}
	/*	left-top coordinate	*/
	abstract public Point getCoord();
}

abstract class Plane extends Printable{
	public abstract void move();
	public abstract Bullet[] shoot();
	/* -1:gameover ; -2:nothing	*/
	public abstract int crash();
	public abstract Point getCoord();
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

	/*	center coordinate	*/
	private Point coord = new Point();

	private int radius;

	public Player(ActionSource source, int radius, int life){
		this.HEIGHT=600;
		this.WIDTH=450;
		power = 1;
		this.source = source;
		this.life = life;
		this.radius = radius;
	}
	
	public void move(){
		int x = coord.getX();
		int y = coord.getY();
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
		coord.setLocation(x,y)
	}

	public Point getCoord(){
		int x = coord.getX()-radius;
		int y = coord.getY()-radius;
		Point point = new Point(x, y);
		return point;
	}

	public Point getPosition(){
		int x = coord.getX();
		int y = coord.getY();
		Point point = new Point(x, y);
		return point;
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

class Enemy extends Plane{
	private int hp;
	private Route route;
	private int enemyId;

	private Bullet[] bullet;

	private Coordinate coord = new Coordinate();

	public Enemy(int hp){
		this.hp = hp;
	}
	public boolean attacted(int harm){
		hp = hp-harm;
		if(hp < 0)
			return true;
		else
			return false;
	}
	public void setBullet(Bullet[] bullet){
		this.bullet = bullet;
	}

	public void setRoute(Route route){
		this.route = route;
	}
	public void move(){
		if(route != null)
			coord = route.move();
	}
	public Bullet[] shoot(){
		Bullet[] returnValue = bullet;
		bullet = null;
		return returnValue;
	}
	public int crash(){
		return -2;
	}
}
/*TODO*/
class Boss extends Enemy{
}

/* bullet */
class Bullet extends Printable{
	private int power;
	private int bulletId;

	private Route route;

	private Coordinate coord;
	private int radius;

	public Bullet(Coordinate coord, int radius){
		this.coord = coord;
		this.radius = radius;
	}
	public setRoute(Route route){
		this.route = route;
	}
	public void move(){
		coord = route.move();
	}
	public Point getCoord(){
		int x = coord.getX()-radius;
		int y = coord.getY()-radius;
		Point point = new Point(x, y);
		return point;
	}
	public Point getPosition(){
		int x = coord.getX();
		int y = coord.getY();
		Point point = new Point(x, y);
		return point;
	}

}
