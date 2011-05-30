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

	private BlockingQueue<Printable> queue;
	private ArrayList<Enemy> enemyList;
	private ArrayList<Bullet> bulletList;
	private ArrayList<Bullet> playerBulletList;

	private Player player;

	private void runInstructions(){
		Instruction[] insts = stage.getIstructions(clock.getTime);
		for(int i=0;i<insts.length;i++){
			switch( inst[i].getInstType() ){
				case ENEMY:
					EnemyInstruction inst = inst[i];
					enemyList.add(inst.getEnemyId(), EnemyFactory.getEnemy(inst) );
					break;
				case BULLET:
					BulletInstruction inst = inst[i];
					if(enemyList.indexOf(inst.getEnemyId) != null){
						int imageId = inst.getImageId();
						int bulletId= inst.getBulletId();
						int radius  = inst.getRadius();
						int power   = inst.getPower();
						int enemyId = inst.getEnemyId();
						String bulletType = inst.getBulletType();
						Enemy enemy = enemyList.indexOf(enemyId);
						bulletList.add(bulletId, BulletFactory.getBullet(imageId, bulletId, radius, power, enemy, bulletType) );
					}
					break;
				case ROUTE:
					RouteInstruction inst = inst[i];
					String targetType = inst.getTargetType();
					int    targetId   = inst.getTargetId();
					int    speed      = inst.getSpeed();
					String pointRefer = inst.getPointRefer();
					String routeType  = inst.getRouteType();
					int    pointAngle = inst.getPointAngle();
					Point  offset     = inst.getPointOffset();
					Point target;
					if(targetTyoe == "enemy"){
						if(enemyList.indexOf(targetId) != null){
							Enemy enemy;
							enemy = enemyList.get(targetId);
							target = calcTarget(enemy, pointRefer, pointAngle, offset);
							Route route = RouteFactory.getRoute(enemy, target, speed, routeType);
							enemy.setRoute(route);
						}
					}
					else if(targetType == "bullet"){
						if(bulletList.indexOf(targetId) != null){
							Bullet bullet;
							bullet = bulletList.get(targetId);
							target = calctarget(bullet, pointRefer, pointAngle, offset);
							Route route = RouteFactory(enemy, target, speed, routeType);
							bullet.setRoute(route);
						}
					}
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
	private void outputToScreem(){
		ArrayList<Printable> output = new ArrayList<Printable>;
		output.addAll(enemyList);
		output.addAll(bulletList);
		output.addAll(playerBulletList);
		queue.offer(output.toArray());
		/*
		Printable[] enemy = enemyList.toArray();
		Printable[] bullet = bulletList.toArray();
		Printable[] playerBullet = playerBulletList.toArray();
		int elength = enemy.length;
		int blength = bullet.length;
		int plength = playerBullet.length;
		Printable[] output = new Printable[];
		Systme.
		*/
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
	private Point calcTarget(Point self, Point pointRefer, int pointAngle, Point offset){
		int x,y;
		Point selfOffset;
		if(pointRefer == "global"){
			if(pointAngle == 0){
				Point point = new Point(offset);
				return point;
			}else{
				x = offset.getX()-self.getX();
				y = offset.getY()-self.getY();
				Point point = new point(x,y);
				selfOffset = round(point, angle);
			}
		}
		else if(pointRefer == "self"){
			if(pointAngle == 0){
				Point point = new Point(self.getX()+offset.getX(), self.getY()+offset.getY());
				return point;
			}else{
				x = offset.getX();
				y = offset.getY();
				Point point = new point(x,y);
				selfOffset = round(point, angle);
			}
		}
		else if(pointRefer == "player"){
			if(pointAngle == 0)
				Point point = new Point(player.getX()+offset.getX(), player.getY()+offset.getY());
				return point;
			else{
				x = ( offset.getX()+player.getX() )-self.getX();
				y = ( offset.getY()+player.getY() )-self.getY();
				Point point = new point(x,y);
				selfOffset = round(point, angle);
			}
		}
		x = selfOffset.getX()+self.getX();
		y = selfOffset.getY()+self.getY();
		Point target = new Point(x, y);
		return target;
	}
	private Point round(Point point,int angle){
		float x = point.getX();
		float y = point.getY();
		float length = Math.sqrt(x*x+y*y);
		float th;
		th = Math.tan(y/x);
		th = th+(float)angle;
		Point afterRound = new Point((int)length*Math.cos(th), (int)length*Math.sin(th));
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
			outputToScreem();
		}
	}
	public Processor(Stage stage, Clock clock, Player player, BlockingQueue<Printable[]> queue){
		enemyList  = new ArrayList<Enemy>();
		bulletList = new ArrayList<Bullet>();
		playerBulletList = new ArrayList<Bullet>();
		this.queue = queue;
		this.player= player; 
		this.stage = stage;
		this.clock = clock;
		this.time = 0;
	}
}

