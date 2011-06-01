import java.util.concurrent.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;

public class GScreen implements Runnable{

	private static final long serialVersionUID = -2440508607507254216L;
	
	
	
	private Image image;
	private Image playBg,scoreBg,pauseBg;
	private Image a,b,c,d,e,f;
	private BufferedImage setPlayImg,setImg;
	private Panel play,scoreBar;
	private BlockingQueue<Printable[]> queue;
	private Stage stage;
	protected GWindow game;
	private EventConnect checkKeyEvent;
	private KeyListener key;
	
	public GScreen(GWindow game, BlockingQueue<Printable[]> queue, KeyListener key, Stage stage,EventConnect checkKeyEvent){
		
		this.game = game;
		this.key = key;
		this.stage = stage;
		this.checkKeyEvent = checkKeyEvent;
		/*
		 * set playArea
		 */
		play = new Panel(){
			public void paint(Graphics g){
				g.drawImage(setPlayImg, 0, 0, null);
			}
			public void update(Graphics g) { 
		        this.paint(g);  // call paint() 
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
		setImg = new BufferedImage(350,600,BufferedImage.TYPE_INT_ARGB);
			 
		game.addKeyListener(key);
		
		this.queue = queue;
		
		game.cp.add(play);
		game.cp.add(scoreBar);

		drawPlayBg();
		drawscoreBg();
		
	}

	public void run(){
		while(true){
			
			drawPlayBg();
			
			Printable[] list = null;
			try {
				list = queue.take();
			} catch (InterruptedException e) {}
			
			
			int length = list.length;
			if(checkKeyEvent.isPause()){
				System.out.println("123");

				game.removeKeyListener(key);
				//game.cp.remove(play);
				//game.cp.add(pause);
				paused();
				//game.addKeyListener(key);
			}
			
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
					
					drawPlayArea(image, dx, dy);
				}
				else{
					System.out.println(image == null);
				}
				
			}

		}

	}

	public void paused(){

		drawPauseMenu(1);
			
		game.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
			
			int keyFlag = 1;
			
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
							
							break;
						case 2:
							break;
						case 3:
							break;
						default:break;
					}
					break;
				case KeyEvent.VK_ESCAPE:
					game.removeKeyListener(this);
					game.addKeyListener(key);
					//game.cp.remove(pause);
					//game.cp.add(play);
					break;
				default:
					System.out.println("No this operator");
					break;
				}
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}
		});		
		//}
	}
	
	public void drawPlayBg(){ 
		try{
			playBg = ImageIO.read(new File("map/default/image/testbg.gif"));
		}
		catch(IOException e){}
		
		Graphics g = play.getGraphics();
		Graphics gg = setPlayImg.getGraphics();

		g.drawImage(playBg,0,0,450,600,null);
		gg.drawImage(playBg,0,0,450,600,null);

		g.dispose();
		gg.dispose();
	}
	
	public void drawscoreBg(){
		try{
			scoreBg = ImageIO.read(new File("map/default/image/scroeBg.png"));
		}
		catch(IOException e){}
		
		Graphics g = scoreBar.getGraphics();
		Graphics gg = setImg.getGraphics();

		g.drawImage(scoreBg,0,0,350,600,null);
		gg.drawImage(scoreBg,0,0,350,600,null);

		g.dispose();
		gg.dispose();
	}
	
	public void drawPlayArea(Image img, int dx, int dy){ 
		
		Graphics g = play.getGraphics();
		Graphics gg = setPlayImg.getGraphics();

		g.drawImage(img, dx, dy, null);
		gg.drawImage(img, dx, dy, null);

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
}


