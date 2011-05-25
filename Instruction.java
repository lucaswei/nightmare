public enum InstType
{enemy,bullet}
public class Instruction{
	private Clock clock = new Clock();
	private InstType insttype;
}

public class EnemyInstruction extends Instruction{
	private int enemyId;
	private int enemyType;
	private Point point;

	public int getEnemyId(){
	return enemyId;
	}
	public int getEnemyType(){
	return enemyType;
	}
	public Point getPoint(){
	return point;
	}
}

public class EnemyInstruction extends Instruction{
	private 
}
