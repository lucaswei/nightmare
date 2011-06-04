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
	
	private PauseMenu pauseMenu;
	
	public GScreen(BlockingQueue<Printable[]> queue, KeyListener key, Stage stage,Hero player){
		
		this.key   = key;
		this.stage = stage;
		this.player = player;
		this.queue = queue;
		
		game = new Container();
		game.setBounds(0,0,800,600);
		game.setVisible(true);
				
		play = new Canvas();
		play.setBounds(0,0,450,600);
		playBuffer = new BufferedImage(450,600,BufferedImage.TYPE_4BYTE_ABGR );
		playBufferGraphics = playBuffer.getGraphics();
		
		
		scoreBar = new Canvas();
		scoreBar.setBounds(450,0,800,600);
		scoreBuffer = new BufferedImage(350,600,BufferedImage.TYPE_4BYTE_ABGR);
		scoreBufferGraphics = scoreBuffer.getGraphics();
		
		pauseMenu = new PauseMenu();
		pauseMenu.setVisible(false);
		
		try{
			playBg = ImageIO.read(new File(imagePath + "testbg.gif"));
			pauseBg = ImageIO.read(new File(imagePath + "pauseBg.png"));
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
		/*
		OverlayLayout layout = new OverlayLayout(game);
		game.setLayout(layout);
		*/
		
		game.add(pauseMenu);
		game.add(play);
		game.add(scoreBar);
		
		game.addKeyListener(key);
		
		life = player.getLife();
	}
	
	public Container getContent(){
		return game;
	}

	public void run(){
		playGraphics = play.getGraphics();
		scoreGraphics = scoreBar.getGraphics();
		game.requestFocus();
		
		while(state != END){
			
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
		if(signal.equals("end")){
			state = END;
		}
		if(signal.equals("crash")){
			life = player.getLife();
		}
	}

	private void paused(){
		pauseMenu.display();
	}
	private void gameContinue(){
		state = PLAY;
		pauseMenu.setVisible(false);
		game.requestFocus();
	}
	
	private class PauseMenu extends Panel{
		private PauseMenu menu;
		private Graphics graphics;
		public PauseMenu(){
			setBounds(0,0,450,600);
			//setBackground(new Color(0,0,0,0));
			addKeyListener(new PauseKeyListener());
		}
		public void display(){
			setVisible(true);
			graphics = this.getGraphics();
			drawPauseMenu(1);
			requestFocus();
		}
		class PauseKeyListener implements KeyListener{
			private int keyFlag = 1;
			public void keyPressed(KeyEvent e){
			switch(e.getKeyCode()){
				case KeyEvent.VK_UP:
					keyFlag--;
					if(keyFlag < 1)	keyFlag += 3;
					drawPauseMenu(keyFlag);		
					break;
				case KeyEvent.VK_DOWN:
					keyFlag++;
					if(keyFlag > 3)	keyFlag -= 3;
					drawPauseMenu(keyFlag);
					break;
				case KeyEvent.VK_ENTER:
					switch(keyFlag){
						case 1:
							EventConnect.dispatch("continue");
							gameContinue();
							break;
						case 2:
							EventConnect.dispatch("menu");
							break;
						case 3:
							stage.restart();
							EventConnect.dispatch(new GameEvent("restart",new Object[]{stage,"Rio"}));
							break;
						default:break;
					}
					break;
				case KeyEvent.VK_ESCAPE:
					EventConnect.dispatch("continue");
					gameContinue();
					break;
				default:
					break;
				}
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}
		}
		public void drawPauseMenu(int keyFlag){
			//graphics.drawImage(playBuffer,0,0,null);
			switch(keyFlag){
				case 1:
					graphics.drawImage(b, 80, 300, null);
					graphics.drawImage(c, 80, 350, null);
					graphics.drawImage(e, 80, 400, null);
					break;
				case 2:
					graphics.drawImage(a, 80, 300, null);
					graphics.drawImage(d, 80, 350, null);
					graphics.drawImage(e, 80, 400, null);
					break;
				case 3:
					graphics.drawImage(a, 80, 300, null);
					graphics.drawImage(c, 80, 350, null);
					graphics.drawImage(f, 80, 400, null);
					break;
				default:break;
			}
		}
	}
	
}


