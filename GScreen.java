import java.util.concurrent.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;

public class GScreen implements Runnable,GameEventListener{

	private Container game;

	private static int PLAY = 0;
	private static int PAUSE = 1;
	private static int END = 2;
	
	private int state = PLAY;
	private int life = 0;
	private String imagePath = "image/";
	
	private Image image;
	private Image playBg,scoreBg,pauseBg;
	private Image a,b,c,d,e,f;
	private Image lifeImg;
	private Component play,scoreBar;
	private BlockingQueue<Printable[]> queue;
	private Stage stage;
	
	private KeyListener key;
	
	private Hero player;
	private Graphics playGraphics;
	private Image    playBuffer;
	private Graphics playBufferGraphics;
	
	private Graphics scoreGraphics;
	private Image    scoreBuffer;
	private Graphics scoreBufferGraphics;
	
	private Image    pauseBuffer;
	private Graphics pauseBufferGraphics;
	
	private Container pauseMenu;
	
	private ItemList pauseList;
	private ItemList deadList;
	private ListMenu menu;
	
	public GScreen(BlockingQueue<Printable[]> queue, KeyListener key,final Stage stage,Hero player){
		
		this.key   = key;
		this.stage = stage;
		this.player = player;
		this.queue = queue;
		
		game = new Container();
		game.setBounds(0,0,800,600);
				
		play = new Component(){};
		play.setBounds(0,0,450,600);
		playBuffer = new BufferedImage(450,600,BufferedImage.TYPE_4BYTE_ABGR );
		playBufferGraphics = playBuffer.getGraphics();
		
		pauseBuffer = new BufferedImage(450,600,BufferedImage.TYPE_4BYTE_ABGR );
		pauseBufferGraphics = pauseBuffer.getGraphics();
		
		scoreBar = new Component(){};
		scoreBar.setBounds(450,0,800,600);
		scoreBar.setFocusable(false);
		scoreBuffer = new BufferedImage(350,600,BufferedImage.TYPE_4BYTE_ABGR);
		scoreBufferGraphics = scoreBuffer.getGraphics();
		
		pauseMenu = new Container();
		pauseMenu.setBounds(0,0,450,600);
		
		try{
			playBg = ImageIO.read(new File(imagePath + "testbg.gif"));
			pauseBg = ImageIO.read(new File(imagePath + "pause_menu_bg.png"));
			scoreBg = ImageIO.read(new File(imagePath + "scroeBg.png"));
			lifeImg = ImageIO.read(new File(imagePath + "life.png"));
			a = ImageIO.read(new File(imagePath + "continue1.png"));
			b = ImageIO.read(new File(imagePath + "continue2.png"));
			c = ImageIO.read(new File(imagePath + "exitgame1.png"));
			d = ImageIO.read(new File(imagePath + "exitgame2.png"));
			e = ImageIO.read(new File(imagePath + "restart1.png"));
			f = ImageIO.read(new File(imagePath + "restart2.png"));
		}
		catch(IOException e){
			System.err.println("Background can't load");
		}
		
		pauseList = new ItemList();
		pauseList.add(a,b,new Runnable(){public void run(){gameContinue();}});
		pauseList.add(c,d,new Runnable(){public void run(){EventConnect.dispatch("menu");}});
		pauseList.add(e,f,new Runnable(){public void run(){restart();}});
		
		deadList = new ItemList();
		deadList.add(c,d,new Runnable(){public void run(){EventConnect.dispatch("menu");}});
		//deadList.add(e,f,new Runnable(){public void run(){restart();}});
													
		OverlayLayout layout = new OverlayLayout(game);
		game.setLayout(layout);
		
		game.add(pauseMenu);
		game.add(play);
		game.add(scoreBar);
		
		game.addKeyListener(key);
		
		life = player.getLife();
	}

	public void run(){
		playGraphics = play.getGraphics();
		scoreGraphics = scoreBar.getGraphics();
		game.requestFocus();
		
		while(true){
		
			if(state == END){
				gameOver();
				break;
			}
			
			if(state == PAUSE){
				paused();
			}
			
			drawScore();
			
			Printable[] list = null;
			try {
				list = queue.take();
			} catch (InterruptedException e) {
				System.err.println("Thread is interupted");
			}
			
			/* Background */
			playBufferGraphics.drawImage(playBg,0,0,null);
			
			/* Printables */
			int length = list.length;
			for(int i=0;i<length;i++){
				
				Printable fly = list[i];
				int id = fly.getImageId();
				image = stage.getImage(id);
				if(image != null){
					Point targetCoord = fly.getPosition();
					
					int imageHeight = image.getHeight(null);
					int imageWidth = image.getWidth(null);
					
					int dx = praseInt(targetCoord.getX()) - imageWidth/2;
					int dy = praseInt(targetCoord.getY()) - imageHeight/2;
					
					playBufferGraphics.drawImage(image, dx, dy,null);
					
				}			
			}
			
			/* Paste playBuffer */
			playGraphics.drawImage(playBuffer,0,0,null);
			
		}
		
	}
	public Container getContent(){
		return game;
	}
	
	public void drawScore(){
		scoreBufferGraphics.drawImage(scoreBg,0,0,null);
		for(int i=0;i<life;i++){
			scoreBufferGraphics.drawImage(lifeImg, 95 + 25*i, 80,25,25, null);
		}
		scoreGraphics.drawImage(scoreBuffer,0,0,null);
	}
	
	
	public int praseInt(double value){
		return (new Double(value)).intValue();
	}

	public void trigger(GameEvent event){
		String signal = event.getSignal();
		if(signal.equals("pause")){
			state = PAUSE;
		}
		if(signal.equals("game_dead")){
			state = END;
		}
		if(signal.equals("game_clear")){
			state = END;
		}
		if(signal.equals("crash")){
			life = player.getLife();
		}
	}

	private void paused(){
		pauseMenu.getGraphics().drawImage(pauseBg,0,0,null);
		menu = new ListMenu(pauseList,80,300,370,40,new Runnable(){public void run(){gameContinue();}});
		pauseMenu.add(menu);
		menu.display();
	}
	
	
	private void gameOver(){
		pauseMenu.getGraphics().drawImage(pauseBg,0,0,null);
		menu = new ListMenu(deadList,80,300,370,40);
		pauseMenu.add(menu);
		menu.display();
	}
	
	private void gameContinue(){
		EventConnect.dispatch("continue");
		state = PLAY;
		pauseMenu.remove(menu);
		game.requestFocus();
	}
	
	private void restart(){
		stage.restart();
		EventConnect.dispatch(new GameEvent("restart",new Object[]{stage,"Rio"}));
	}
	
}


