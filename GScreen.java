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
	private Image palyBg,scrollBg;
	private Image a,b,c,d,e,f;
	private BufferedImage setPlayImg,setScrollImg;
	private Panel play,scrollBar,whenPause;
	private BlockingQueue<Printable[]> queue;
	private Stage stage;
	protected GWindow game;
	private EventConnect checkKeyEvent;
	
	public GScreen(GWindow game, BlockingQueue<Printable[]> queue, KeyListener key, Stage stage,EventConnect checkKeyEvent){
		
		this.game = game;
		this.checkKeyEvent = checkKeyEvent;
		/*
		 * set playArea
		 */
		play = new Panel(){
			public void paint(Graphics g){		
				g.drawImage(setPlayImg, 0, 0, null);
			}
		};
		play.setBounds(0,0,450,600);
		game.cp.add(play);
		
		/*
		 * set scroll bar
		 */
		scrollBar = new Panel(){
			public void paint(Graphics g){
				g.drawImage(setScrollImg, 0, 0, null);
			}
		};
		
		scrollBar.setBounds(450,0,350,600);
		game.cp.add(scrollBar);
		
		whenPause = new Panel();
		whenPause.setBounds(0,0,450,600);

		setPlayImg = new BufferedImage(game.getWidth(),game.getHeight(),BufferedImage.TYPE_INT_ARGB);
		setScrollImg = new BufferedImage(game.getWidth(),game.getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		try{
			palyBg = ImageIO.read(new File("map/default/image/testbg.png"));
			scrollBg = ImageIO.read(new File("map/default/image/scrollBg.png"));
			a = ImageIO.read(new File("map/default/image/continue1.png"));
			b = ImageIO.read(new File("map/default/image/continue2.png"));
			c = ImageIO.read(new File("map/default/image/exitgame1.png"));
			d = ImageIO.read(new File("map/default/image/exitgame2.png"));
			e = ImageIO.read(new File("map/default/image/restart1.png"));
			f = ImageIO.read(new File("map/default/image/restart2.png"));
		}
		catch(IOException e){}
			 
		game.addKeyListener(key);
		
		this.queue = queue;
		
		drawPlayBg();
		drawScrollBg();
		
		run();
	}

	public void run(){
		while(true){
			
			Printable[] list = null;
			try {
				list = queue.take();
			} catch (InterruptedException e) {}
			
			int length = list.length;
			
			if(checkKeyEvent.isPaused())
				paused();
			
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
					
				}
			}
		}

	}


	public void paused(){
		
		
		
		game.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				switch(e.getKeyCode()){
					case KeyEvent.VK_UP:
						break;
					case KeyEvent.VK_DOWN:
						break;
					case KeyEvent.VK_ENTER:
						break;
					case KeyEvent.VK_ESCAPE:
						game.removeKeyListener(this);
						break;
				}
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}
		});
		
	}
	
	public void drawPlayBg(){ 
		Graphics g = scrollBar.getGraphics();
		Graphics gg = setScrollImg.getGraphics();

		g.drawImage(scrollBg,0,0,450,600,null);
		gg.drawImage(scrollBg,0,0,450,600,null);

		g.dispose();
		gg.dispose();
	}
	
	public void drawScrollBg(){ 
		Graphics g = play.getGraphics();
		Graphics gg = setPlayImg.getGraphics();

		g.drawImage(palyBg,450,0,800,600,null);
		gg.drawImage(palyBg,450,0,800,600,null);

		g.dispose();
		gg.dispose();
	}
	
	
	private void drawPlayArea(Image img, int dx, int dy){ 
		Graphics g = play.getGraphics();
		Graphics gg = setPlayImg.getGraphics();

		g.drawImage(img, dx, dy, null);
		gg.drawImage(img, dx, dy, null);

		g.dispose();
		gg.dispose();
	}
	
	
	public int praseInt(double value){
		return (new Double(value)).intValue();
	}
}


