import java.lang.Thread;
public enum state{
	END,
	PAUSE,
	PLAYING
}

public class Processor implements Runnable{
	private Stage stage;
	private Clock clock;
	private long time;

	private EnemyFactory enemyFactory;
	private BulletFactory bulletFactory;
	private ArrayList<Enemy> enemyList;
	private ArrayList<Bullet> bulletList;
	private ArrayList<Bullet> playerBulletList;

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
						bulletList.add(inst.getBulletId, bulletFactory.getBullet() );
					}
					break;
				case ROUTE:

					break;
			}
		}
	}
	private void calcBullet(){
		Point position;
		for(ArrayList<Bullet> bullet : bulletList){
			bullet.move();
			position = bullet.getPosition();
			if(position.getX() < 0 || position.getY() < 0)
				bulletList.remove(bullet);
		}
	}
	private void calcEnemy(){
		Point position;
		for(ArrayList<Enemy> enemy : enemyList){
			enemy.move();
			position = enemy.getPosition();
			if(position.getX() < 0 || position.getY() < 0)
				bulletList.remove(bullet);
		}
	}
	private void calcPlayer(){
		player.move();
		playerBulletList.add( player.shoot() );
	}
	private void collision(){
		for(ArrayList<Bullet> bullet : bulletList){
			if(bullet.collision(player)){
				player.crash();
			}
		}
		for(ArrayList<bullet> bullet : playerBulletList){
			for(ArrayList<Enemy> enemy: enemyList){
				if(bullet.collision(enemy)){
					enemy.crash();
				}				
			}
		}
	}
	/*recycle trash over the screen*/
	private void recycle(){
		for(ArrayList<Bullet> bullet : bulletList){
		}
		for(ArrayList<Bullet> bullet : playerBulletList){
		}
		for(ArrayList<Enemy> enemy : enemyList){
		}
	}
	public void run(){
		while(status!=END){
			try{
				time = clock.next(time);
			}
			catch(DelayException e){
			}
			runInstruction();
			calcBullet();
			calcEnemy();
			calcPlayer();
			collision();
		}
	}
	public Processor(Stage stage,
						Clock clock,
						Player player){
		enemyList  = new ArrayList<Enemy>();
		bulletList = new ArrayList<Bullet>();
		playerBulletList = new ArrayList<Bullet>();
		this.player= player; 
		this.stage = stage;
		this.clock = clock;
		this.time = 0;
	}
}

