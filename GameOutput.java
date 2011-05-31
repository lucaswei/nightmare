import java.util.concurrent.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GameOutput extends JFrame implements Runnable{

	private Image image;
	private Panel canvas,scrollBar,whenPause;
	private Graphics gc,gsb,gwp;
	private BlockingQueue<Printable[]> queue;
	private Stage stage;
	
	public GameOutput(GWindow game, BlockingQueue<Printable[]> queue, KeyListener key, Stage stage){
		
		canvas = new Panel();
		canvas.setBounds(0,0,450,600);
		canvas.setBackground(Color.yellow);
		this.add(canvas);
		gc = canvas.getGraphics();

		scrollBar = new Panel();
		scrollBar.setBounds(450,0,350,600);
		scrollBar.setBackground(Color.pink);
		this.add(canvas);
		gsb = canvas.getGraphics();
		
		whenPause = new Panel();
		whenPause.setBounds(0,0,450,600);
		gwp = whenPause.getGraphics();
		
		this.addKeyListener(key);
		this.queue = queue;
		run();
	}
	
	public void run(){
		while(true){
			Printable[] list = queue.take();
			int length = list.length;
			
			for(int i=0;i<length;i++){
				
				Printable fly = list[i];
				int id = fly.getImageID();
				image = stage.getImage();
				
				Coordinate targetCoord = fly.getCoord();
				Coordinate sourceCoord = stage.getCoord(id);
				Dimension  dimension   = stage.getDimension(id);
				
				int dx1 = targetCoord.getX();
				int dy1 = targetCoord.getY();
				int dx2 = dx1 + praseInt(dimension.getWidth());
				int dy2 = dx2 + praseInt(dimension.getHeight());
				int sx1 = sourceCoord.getX();
				int sy1 = sourceCoord.getY();
				int sx2 = sx1 + praseInt(dimension.getWidth());
				int sy2 = sx2 + praseInt(dimension.getHeight());
				
				gc.drawImage(image,
						     dx1, dy1, dx2, dy2,
						     sx1, sy1, sx2, sy2,
						     this);
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
	
	
	public int praseInt(double value){
		return (new Double(value)).intValue();
	}
}


