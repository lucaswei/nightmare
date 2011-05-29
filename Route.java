public class Route{
	private float x;
	private float y;
	private int speed;
	private float beziert;
	/*straight, stop, curve*/
	private String routeType;
	/*global, enemy, player*/
	private String coordType;
	private Point player;
	private Point enemy;
	private Point offset;
	private Point destiny;
	private Point thirdPoint;
	public Route(IInstruction inst, Point player, Point enemy){
		routeType = getRouteType();
		speed = getSpeed();
		coordType = getCoordType();
		offset = getOffset();
		this.player = player;
		this.enemy = enemy;
	}
	public Point move(){
	}
	public calcPoint(){
		if(coordType == "global"){
		}
		if(coordType == "enemy"){
		}
		if(coordType == "player"){
		}
	}
}

