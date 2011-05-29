package printable.enemy;

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
class Enemy extends Plane{
	private int hp;
	private Route route;
	private int enemyId;

	private Bullet[] bullet;

	private Point center = new Point();

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
			center = route.move();
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
