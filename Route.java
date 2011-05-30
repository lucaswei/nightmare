class RouteFactory{
	private RouteFactory(){};
	public static Route getRoute(Point self, Point target, int speed, String routeType){
		if(routeType == "linear")
			return new StraightRoute(self, target, speed);
		else if(routeType == "stop")
			return new StopRoute(self, target, speed);
	}
}
abstract class Route{
	private float x;
	private float y;
	private float vectorX;
	private float vectorY;
	private int speed;
	private Point self;
	private Point destiny;
	public Route(Point self, Point destiny, int speed){
		this.self = self;
		this.destiny = destiny;
		this.speed = speed;
		x = self.getX();
		y = self.getY();
	}
}
class StopRoute extends Route{
	public StopRoute(Point self, Point destiny, int speed){
		super(self, destiny, speed);
	}
	public Point move(){
		Point point = new Point(x, y);
		return point;
	}
}
class StraightRoute extends Route{
	public Route(Point self, Point destiny, int speed){
		super(self, destiny, speed);
		calcVector();
	}
	public Point move(){
		x = x+vectorX;
		y = y+vectorY;
		Point point = new Point(x, y);
		return point;
	}
	private void calcVector(){
		int deltaX;
		int deltaY;
		float fraction;
		deltaX = self.getX()-destiny.getX();
		deltaY = self.getY()-destiny.getY();
		fraction = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
		vectorX = (destiny.getX()-self.getX())*fraction;
		vectorY = (destiny.getY()-self.getY())*fraction;
	}
}
/*
class CurveRoute extends Route{
	private float beziert;
	private Point thirdPoint;
	public Route(Point self, Point destiny, int speed, Point thirdPoint){
		super(self, destiny, speed);
		this.thirdPoint = thirdPoint;
	}
	public Point move(){
	}
}
*/
