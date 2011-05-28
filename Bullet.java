/*TODO Transform inst into bullet*/
public class BulletFactory{
	public Bullet getBullet(BulletInstruction inst, Point point){
		String bulletType = inst.getBulletType();
		Point bulletPoint = inst.getPosition();
	}
}
abstract class Bullet extends Printable{
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
	/*
	public Point getposition(){
		int x = position.getX();
		int y = position.getY();
		Point point = new Point(x, y);
		return point;
	}
	*/
}
class CircleBullet extends Bullet{
	private int radius;
	public CircleBullet(Point position, int radius){
		this.position = position;
		this.radius = radius;
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
