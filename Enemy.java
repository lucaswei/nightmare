import java.awt.Point;
class EnemyFactory{
	private EnemyFactory(){}
	public static Enemy getEnemy(EnemyInstruction inst){
		String enemyType = inst.getEnemyType();
		Point point = inst.getPoint();
		int enemyId = inst.getEnemyId();
		if(enemyType.equals("small")){
			return new SmallEnemy(enemyId, point);
		}
		else if(enemyType.equals("normal")){
			return new NormalEnemy(enemyId, point);
		}
		else if(enemyType.equals("large")){
			return new LargeEnemy(enemyId, point); 
		}
		return null;
	}
}



abstract class Enemy extends Plane{
	protected int hp;
	protected Route route;
	protected int enemyId;

	public boolean attacted(int harm){
		hp = hp - harm;
		if(hp < 0)
			return true;
		else
			return false;
	}
	public void setRoute(Route route){
		this.route = route;
	}
	public void move(){
		if(route != null)
			position = route.move();
		try{
			angle = route.rotate();
		}
		catch(Exception e){
		}
	}
	public int crash(){
		return -2;
	}
}
/*TODO*/
class Boss extends Enemy{
	public Boss(int id,Point point){
		position = point;
		enemyId = id;
	}
}


class SmallEnemy extends Enemy{
	public SmallEnemy(int id, Point point){
		radius  = 25;
		position  = point;
		enemyId = id;
		hp=10;
		imageId = 14;
	}
}
class NormalEnemy extends Enemy{
	public NormalEnemy(int id, Point point){
		radius  = 30;
		position  = point;
		enemyId = id;
		hp=20;
		imageId = 16;
	}
}
class LargeEnemy extends Enemy{
	public LargeEnemy(int id, Point point){
		radius  = 35;
		position  = point;
		enemyId = id;
		hp=30;
		imageId = 15;
	}
}
