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

class SmallEnemy extends Enemy{
	public SmallEnemy(int id, Point point){
		coord=point;
		enemyId=id;
		hp=10;
	}
}
class NormalEnemy extends Enemy{
	public SmallEnemy(int id, Point point){
		coord=point;
		enemyId=id;
		hp=20;
	}
}
class LargeEnemy extends Enemy{
	public SmallEnemy(int id, Point point){
		coord=point;
		enemyId=id;
		hp=30;
	}
}
