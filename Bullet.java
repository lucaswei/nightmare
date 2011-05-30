/*TODO Transform inst into bullet*/
public class BulletFactory{
	private BulletFactory(){}
	public static Bullet getBullet(int imageId, int bulletId, int radius, int power, Point enemy, String bulletType){
		switch(bulletType){
			case "large":
				return bullet(enemy, 32, imageId, bulletId);
				break;
			case "normal":
				return bullet(enemy, 12, imageId, bulletId);
				break;
			case "small":
				return bullet(enemy, 8, imageId, bulletId);
				break;
		}
	}
}
abstract class Bullet extends Printable{
	private int test;
	private int power;
	private int bulletId;

	private Route route;

	public setRoute(Route route){
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
		int x = plain.getPosition().getX() - position.getX();
		int y = plain.getPosition().getY() - position.getY();
		int r = plain.getRadius() + radius;
		if( x*x+y*y <  r*r )
			return true;
		else
			return false;
	}
}
