import java.awt.Point;
/*TODO Transform inst into bullet*/
class BulletFactory{
	private BulletFactory(){}
	public static Bullet getBullet(int imageId, int bulletId, int radius, int power, Point enemy, String bulletType){
		switch(bulletType){
			case "large":
				return new CircleBullet(enemy, 32, imageId, bulletId);
				break;
			case "normal":
				return new CircleBullet(enemy, 12, imageId, bulletId);
				break;
			case "small":
				return new CircleBullet(enemy, 8, imageId, bulletId);
				break;
		}
	}
}
abstract class Bullet extends Printable{
	protected int test;
	protected int power;
	protected int bulletId;

	protected Route route;

	public void setRoute(Route route){
		this.route = route;
	}
	public void move(){
		if(route != null)
			position = route.move();
	}
}
class CircleBullet extends Bullet{
	private int radius;
	public CircleBullet(Point position, int radius, int imageId, int bulletId){
		this.imageId  = imageId;
		this.position = position;
		this.radius   = radius;
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
