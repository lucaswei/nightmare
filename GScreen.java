import java.util.concurrent.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;

public class GScreen implements Runnable,GameEventListener{


	private int state = 0;
	private static int PAUSE = 1;
	private static int END = 2;
	private int life = 0;
	
	private Image image;
	private Image playBg,scoreBg,pauseBg;
	private Image a,b,c,d,e,f;
	private BufferedImage setPlayImg,setImg;
	private Component play,scoreBar;
	private BlockingQueue<Printable[]> queue;
	private Stage stage;
	protected GWindow game;
	private EventConnect checkKeyEvent;
	private KeyListener key;
	private Hero player;
	private Graphics playGraphics;
	
	private Image buffer;
	private Graphics bufferGraphics;
	
	public GScreen(GWindow game, BlockingQueue<Printable[]> queue, KeyListener key, Stage stage,EventConnect checkKeyEvent,Hero player){
		
		this.game  = game;
		this.key   = key;
		this.stage = stage;
		this.checkKeyEvent = checkKeyEvent;
		this.player = player;
		this.queue = queue;
		
		/*
		 * set playArea
		 */
		play = new Canvas(){
			public void update(Graphics g){
				System.out.println("OUT");
				super.paint(g);
			}
		};
		
		play.setBounds(0,0,450,600);
		
		/*
		 * set score bar
		 */
		scoreBar = new Panel(){
			public void paint(Graphics g){
				g.drawImage(setImg, 0, 0, null);
			}
		};
		
		scoreBar.setBounds(450,0,800,600);
		
		/*pause = new Panel(){
			public void paint(Graphics g){
				g.drawImage(setImg, 0, 0, null);
			}
		};
		pause.setBounds(0,0,450,600);*/

		setPlayImg = new BufferedImage(450,600,BufferedImage.TYPE_INT_ARGB);
		setImg     = new BufferedImage(350,600,BufferedImage.TYPE_INT_ARGB);
		
		try{
			playBg = ImageIO.read(new File("map/default/image/testbg.gif"));
		}
		catch(IOException e){
			System.err.println("Background can't load");
		}
		
		 
		
		game.cp.add(play);
		game.cp.add(scoreBar);

		playGraphics = play.getGraphics();
		buffer = new BufferedImage(450,600,BufferedImage.TYPE_4BYTE_ABGR );
		bufferGraphics = buffer.getGraphics();
		
		game.addKeyListener(key);
		
		life = player.getLife();
	}

	public void run(){
		while(true){
			drawscoreBg();
			
			Printable[] list = null;
			try {
				list = queue.take();
			} catch (InterruptedException e) {
				System.err.println("Thread is interupted");
			}
			
			/* Background */
			bufferGraphics.drawImage(playBg,0,0,null);
			
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
					
					bufferGraphics.drawImage(image, dx, dy,null);
					
				}			
			}
			
			/* Paste buffer */
			playGraphics.drawImage(buffer,0,0,null);
			
			if(state == PAUSE){
				game.removeKeyListener(key);
				paused();
			}
		}
	}

	public void paused(){

		drawPauseMenu(1);
		KeyAdapter pauseKeyAdapter = new KeyAdapter(){
			private int keyFlag = 1;
			public void keyPressed(KeyEvent e){
			
			
			switch(e.getKeyCode()){
				case KeyEvent.VK_UP:
					keyFlag--;
				
					if(keyFlag < 1)
						keyFlag = 1;
					
					drawPauseMenu(keyFlag);		
					break;
				case KeyEvent.VK_DOWN:
					keyFlag++;
					if(keyFlag > 3)
						keyFlag = 3;
					
					drawPauseMenu(keyFlag);
					break;
				case KeyEvent.VK_ENTER:
					switch(keyFlag){
						case 1:
							checkKeyEvent.dispatch("continue");
							game.removeKeyListener(this);
							game.addKeyListener(key);
							state = 0;
							break;
						case 2:
							System.exit(0);
							break;
						case 3:
							break;
						default:break;
					}
					break;
				case KeyEvent.VK_ESCAPE:
					checkKeyEvent.dispatch("continue");
					game.removeKeyListener(this);
					game.addKeyListener(key);
					state = 0;
					break;
				default:
					System.out.println("No this operator");
					break;
				}
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}
		};
		
		game.addKeyListener(pauseKeyAdapter);		
		
	}
	
	public void drawscoreBg(){
		
		Image lifeImg = null;
		
		try{
			scoreBg = ImageIO.read(new File("map/default/image/scroeBg.png"));
			lifeImg = ImageIO.read(new File("map/default/image/life.png"));
		}
		catch(IOException e){}
		
		Graphics g = scoreBar.getGraphics();
		Graphics gg = setImg.getGraphics();

		g.drawImage(scoreBg,0,0,350,600,null);
		gg.drawImage(scoreBg,0,0,350,600,null);
		for(int i=0;i<life;i++){
			g.drawImage(lifeImg, 95 + 25*i, 80, null);
			gg.drawImage(lifeImg, 95 + 25*i, 80,null);
		}

		g.dispose();
		gg.dispose();
	}
	
	public void drawPauseMenu(int keyFlag){
		
		Graphics g = play.getGraphics();
		Graphics gg = setPlayImg.getGraphics();
		
		try{
			pauseBg = ImageIO.read(new File("map/default/image/pauseBg.png"));
			a = ImageIO.read(new File("map/default/image/continue1.png"));
			b = ImageIO.read(new File("map/default/image/continue2.png"));
			c = ImageIO.read(new File("map/default/image/exitgame1.png"));
			d = ImageIO.read(new File("map/default/image/exitgame2.png"));
			e = ImageIO.read(new File("map/default/image/restart1.png"));
			f = ImageIO.read(new File("map/default/image/restart2.png"));
		}
		catch(IOException e){
			System.out.println("no image");
		}
		
		//g.drawImage(pauseBg, 0, 0, null);
		//gg.drawImage(pauseBg, 0, 0, null);
		
		switch(keyFlag){
			case 1:
				g.drawImage(b, 80, 300, null);
				g.drawImage(c, 80, 350, null);
				g.drawImage(e, 80, 400, null);
				gg.drawImage(b, 80, 300, null);
				gg.drawImage(c, 80, 350, null);
				gg.drawImage(e, 80, 400, null);
				break;
			case 2:
				g.drawImage(a, 80, 300, null);
				g.drawImage(d, 80, 350, null);
				g.drawImage(e, 80, 400, null);
				gg.drawImage(a, 80, 300, null);
				gg.drawImage(d, 80, 350, null);
				gg.drawImage(e, 80, 400, null);
				break;
			case 3:
				g.drawImage(a, 80, 300, null);
				g.drawImage(c, 80, 350, null);
				g.drawImage(f, 80, 400, null);
				gg.drawImage(a, 80, 300, null);
				gg.drawImage(c, 80, 350, null);
				gg.drawImage(f, 80, 400, null);
				break;
			default:break;
		}
		
		g.dispose();
		gg.dispose();
	}
	
	
	public int praseInt(double value){
		return (new Double(value)).intValue();
	}

	public void trigger(String event){
		if(event.equals("pause")){
			state = PAUSE;
		}
		if(event.equals("end")){
			state = END;
		}
		if(event.equals("crash")){
			life = player.getLife();
		}
	}
}


