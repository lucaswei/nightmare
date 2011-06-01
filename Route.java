import java.awt.Point;
class RouteFactory{
	private RouteFactory(){}
	public static Route getRoute(Point self, Point target, int speed, String routeType){
		if(routeType.equals("linear")){
			return new StraightRoute(self, target, speed);
		}
		else if(routeType.equals("stop")){
			return new StopRoute(self, target, speed);
		}
		return null;
	}
}
abstract class Route{
	protected double x;
	protected double y;
	protected double vectorX;
	protected double vectorY;
	protected int speed;
	protected Point self;
	protected Point destiny;
	public Route(Point self, Point destiny, int speed){
		this.self = self;
		this.destiny = destiny;
		this.speed = speed;
		x = (double)self.getX();
		y = (double)self.getY();
	}
	abstract public Point move();
}
class StopRoute extends Route{
	public StopRoute(Point self, Point destiny, int speed){
		super(self, destiny, speed);
	}
	public Point move(){
		Point point = new Point((int)x, (int)y);
		return point;
	}
}
class StraightRoute extends Route{
	public StraightRoute(Point self, Point destiny, int speed){
		super(self, destiny, speed);
		calcVector();
	}
	public Point move(){
		x = x+vectorX;
		y = y+vectorY;
		Point point = new Point((int)x, (int)y);
		return point;
	}
	private void calcVector(){
		double deltaX;
		double deltaY;
		double length;
		deltaX = self.getX()-(double)destiny.getX();
		deltaY = self.getY()-(double)destiny.getY();
		length = Math.sqrt( (double)(deltaX*deltaX+deltaY*deltaY) );
		vectorX = (destiny.getX()-self.getX())/length * speed;
		vectorY = (destiny.getY()-self.getY())/length * speed;
		
		
	}
}
/*
class CurveRoute extends Route{
	private double beziert;
	private Point thirdPoint;
	public Route(Point self, Point destiny, int speed, Point thirdPoint){
		super(self, destiny, speed);
		this.thirdPoint = thirdPoint;
	}
	public Point move(){
	}
}
*/
