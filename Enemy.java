class EnemyFactory{
	public Enemy getEnemy(EnemyInstruction inst){
		String enemyType = inst.getEnemyType();
		Point point = inst.getPoint();
		int enemyId = inst.getEnemyId();
		switch(enemyType){
			case "small":
				return new SmallEnemy(enemyId, point); 
			case "normal":
				return new NormalEnemy(enemyId, point); 
			case "large":
				return new LargeEnemy(enemyId, point); 
		}
	}
}



abstract class Enemy extends Plane{
	private int hp;
	private Route route;
	private int enemyId;

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
	}
	public int crash(){
		return -2;
	}
}
/*TODO*/
class Boss extends Enemy{
	public Boss(int id,Point point){
		center = point;
		enemyId = id;
	}
	public setHp(){
	}
}


class SmallEnemy extends Enemy{
	public SmallEnemy(int id, Point point){
		center=point;
		enemyId=id;
		hp=10;
	}
}
class NormalEnemy extends Enemy{
	public NormalEnemy(int id, Point point){
		center=point;
		enemyId=id;
		hp=20;
	}
}
class LargeEnemy extends Enemy{
	public LargeEnemy(int id, Point point){
		center=point;
		enemyId=id;
		hp=30;
	}
}
class Boss extends Enemy{
}
