import java.util.concurrent.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;

public class GScreen extends JFrame implements Runnable{

	private static final long serialVersionUID = -2440508607507254216L;
	
	private Image image;
	private Image palyBg,scrollBg;
	private BufferedImage setPlayImg,setScrollImg;
	private Panel play,scrollBar,whenPause;
	private Graphics graph;
	private BlockingQueue<Printable[]> queue;
	private Stage stage;
	protected GWindow game;
	
	public GScreen(GWindow game, BlockingQueue<Printable[]> queue, KeyListener key, Stage stage){
		
		this.game = game;
		
		/*
		 * set playArea
		 */
		play = new Panel(){
			public void paint(Graphics g){		
				g.drawImage(setPlayImg, 0, 0, null);
			}
		};
		play.setBounds(0,0,450,600);
		play.setBackground(Color.yellow);
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
		scrollBar.setBackground(Color.pink);
		game.cp.add(scrollBar);
		
		whenPause = new Panel();
		whenPause.setBounds(0,0,450,600);

		setPlayImg = new BufferedImage(game.getWidth(),game.getHeight(),BufferedImage.TYPE_INT_ARGB);
		setScrollImg = new BufferedImage(game.getWidth(),game.getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		try{
			palyBg = ImageIO.read(new File("image/testbg.png"));
			scrollBg = ImageIO.read(new File("image/scrollBg.png"));
		}
		catch(IOException e){}
		
		this.addKeyListener(key);
		
		this.queue = queue;
		
		drawPlayBg();
		drawScrollBg();
		
		run();
	}
	
	public void run(){
		while(true){
			
			Printable[] list = queue.take();
			int length = list.length;
			
			for(int i=0;i<length;i++){
				
				Printable fly = list[i];
				int id = fly.getImageId();
				image = stage.getImage(id);
				
				Point targetCoord = fly.getPosition();
				
				int imageHeight = image.getHeight(null);
				int imageWidth = image.getWidth(null);
				
				int dx = praseInt(targetCoord.getX()) - imageWidth/2;
				int dy = praseInt(targetCoord.getY()) - imageHeight/2;
				
				drawPlayArea(image, dx, dy);
			}
		}

	}


	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:break;
			case KeyEvent.VK_RIGHT:break;
			case KeyEvent.VK_DOWN:break;
			case KeyEvent.VK_LEFT:break;
			case KeyEvent.VK_Z:
				break;
			case KeyEvent.VK_X:
				break;
			case KeyEvent.VK_C:
					break;
			case KeyEvent.VK_ESCAPE:
				game.removeKeyListener(this);
				
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
				
				break;
			case KeyEvent.VK_F4:
				if(e.isAltDown())
					System.exit(0);
				break;
			case KeyEvent.VK_ENTER:break;
			
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	public void drawPlayBg(){ 
		Graphics g = scrollBar.getGraphics();
		Graphics gg = setScrollImg.getGraphics();

		g.drawImage(scrollBg,0,0,800,600,null);
		gg.drawImage(scrollBg,0,0,800,600,null);

		g.dispose();
		gg.dispose();
	}
	
	public void drawScrollBg(){ 
		Graphics g = play.getGraphics();
		Graphics gg = setPlayImg.getGraphics();

		g.drawImage(palyBg,0,0,800,600,null);
		gg.drawImage(palyBg,0,0,800,600,null);

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


