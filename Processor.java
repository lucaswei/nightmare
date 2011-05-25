import java.lang.Thread;
public enum state{
	end,
	pause,
	playing
}

public class Processor implements Runnable{
	private Stage stage;
	private Clock clock;
	private EnemyFactory enemyFactory;
	private ArrayList<Enemy> enemyList;
	private ArrayList<Bullet> bulletList;
	private Player player;

	private void runInstructions(){
		Instruction[] insts = stage.getIstructions(clock.getTime);
		for(int i=0;i<insts.length;i++){
			switch( inst[i].getInstType() ){
				case ENEMY:
					EnemyInstruction inst = (EnemyInstruction) inst[i];
					enemyList.add(inst.getEnemyId, enemyFactory.getEnemy(inst) );
					break;
				case BULLET:
					BulletInstruction inst = (BulletInstruction) inst[i];
					if(enemyList.indexOf(inst.getEnemyId) != null){
						bulletList.add(inst.getBulletId, player.getPosition() );
					}
					break;
				case ROUTE:
					break;
			}
			
		}
	}
	private void calcBullet(){
		
	}
	private void calcRoles(){
	}
	private void collision(){
	}
	public void run(){
		while(state!=end){
			wait();
			runInstruction();
			calcBullet();
			calcRoles();
			collision();
		}
	}
	public Processor(Stage stage,
						Clock clock,
	 					EnemyFactory enemyFactory,
						BulletFactory bulletFactory,
						RouteFactory routeFactory,
						Player player){
		enemyList  = new ArrayList<Enemy>();
		bulletList = new ArrayList<Bullet>();
		this.enemyFactory  = enemyFactory;
		this.bulletFactory = bulletFactory;
		this.routeFactory  = routeFactory;
		this.player= player; 
		this.stage = stage;
		this.clock = clock;
	}
}

