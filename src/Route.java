import java.awt.Point;
import java.awt.geom.Point2D;
import static java.lang.Math.*;

class RouteFactory{
	private static final Point ORIGIN = new Point(0,0);
	private RouteFactory(){}
	
	public static Route getRoute(String routeType,Point self, Point target, int speed,int angle){
		if(routeType.equals("linear")){
			Point2D.Double vector = getVector(self,target,angle,speed);
			return new StraightRoute(self,vector);
		}
		else if(routeType.equals("stop")){
			return new StopRoute(self);
		}
		return null;
	}
	
	public static Route getRoute(RouteInstruction instruction,Printable self, Point player){
		String routeType   = instruction.getRouteType();
		String pointRefer  = instruction.getPointRefer();
		Point  pointOffset = instruction.getPointOffset();
		double pointAngle  = instruction.getPointAngle();
		
		/* route_type */
		if(routeType.equals("linear")){
			int speed = instruction.getSpeed();
			/* target vector*/
			Point2D.Double vector = null;
			if(pointRefer.equals("player")){
				vector = getVector(self.getPosition(),player,pointAngle,speed);
			}
			else if(pointRefer.equals("self")){
				vector = getVector(ORIGIN,pointOffset,pointAngle,speed);
			}
			else if(pointRefer.equals("global")){
				vector = getVector(self.getPosition(),pointOffset,pointAngle,speed);
			}
			if(vector != null){
				if(vector.getX() == 0  && vector.getY() == 0){
					double angle = pointAngle + self.getAngle();
					double radian = angle / 180 * PI;
					vector = new Point2D.Double(cos(radian)*speed , sin(radian)*speed);
				}
				return new StraightRoute(self.getPosition(), vector);
			}
		}
		else if(routeType.equals("stop")){
			return new StopRoute(self.getPosition());
		}
		return null;
	}
	private static Point2D.Double getVector(Point start,Point end,double angle,int speed){
		double deltaX   = end.getX() - start.getX();
		double deltaY   = end.getY() - start.getY();
		double distance = sqrt(deltaX * deltaX + deltaY * deltaY);
		if(distance != 0){
			double radian   = angle / 180 * PI;
			double vectorX  = (deltaX * cos(radian) + deltaY * sin(radian)) / distance * speed;
			double vectorY  = (deltaY * cos(radian) - deltaX * sin(radian)) / distance * speed;
			return new Point2D.Double(vectorX,vectorY);
		}
		else{
			return new Point2D.Double(0,0);
		}
	}
	
	private Point add(Point p,Point q){return new Point((int)(p.getX()+q.getX()) , (int)(p.getY()+q.getY()));}
}


abstract class Route{
	abstract public Point move();
	abstract public Integer rotate();
}


class StopRoute extends Route{
	Point target;
	public StopRoute(Point target){
		this.target = target;
	}
	public Point move(){
		return target;
	}
	public Integer rotate(){
		return null;
	}
}


class StraightRoute extends Route{
	Point2D.Double position;
	Point2D.Double vector;
	int angle;
	public StraightRoute(Point start, Point2D.Double vector){
		position = new Point2D.Double(start.getX(),start.getY());
		this.vector = vector;
		double x = vector.getX();
		double y = vector.getY();
		angle = (new Double(atan2(y,x) / PI * 180)).intValue();
		
	}
	public Point move(){
		double x = position.getX() + vector.getX();
		double y = position.getY() + vector.getY();
		position = new Point2D.Double(x,y);
		return new Point((int)x,(int)y);
	}
	public Integer rotate(){
		return angle;
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
