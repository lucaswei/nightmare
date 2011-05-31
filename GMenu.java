import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;

public class GMenu implements KeyListener{
	
	private GWindow game;
	private Stage stage;
	private Panel canvas;
	private Graphics g;
	private Image bg,play1,play2,exit1,exit2,sword;
	private BufferedImage img;
	private Nightmare nightmare;
	private int keyFlag;
	
	
	public GMenu(){		
		
		game = new GWindow();
		
		//Choose map
		String mapName = "";
		Stage stage = new Stage("default");

		keyFlag = 1;
	
		canvas = new Panel();
		canvas.setBounds(0, 0, 800, 600);
		game.cp.add(canvas);
		
		g = canvas.getGraphics();
		
		drawMenu();
		
		/*ctest = new Panel(){
			public void paint(Graphics g){		
				g.drawImage(img, 0, 0, null);
			}
		};
		
		ctest.setBounds(0, 0, 800, 600);
		img = new BufferedImage(game.getWidth(),game.getHeight(),BufferedImage.TYPE_INT_ARGB);
		*/
	}

	public void keyPressed(KeyEvent e){
		
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				keyFlag--;
				
				if(keyFlag < 1)
					keyFlag = 1;
				else{
					g.drawImage(play2, 400, 100, null);
					g.drawImage(exit1, 400, 150, null);
				}
				break;
			case KeyEvent.VK_DOWN:
				keyFlag++;
				
				if(keyFlag > 2)
					keyFlag = 2;
				else{
					g.drawImage(play1, 400, 100, null);
					g.drawImage(exit2, 400, 150, null);
				}
				break;
			case KeyEvent.VK_ENTER:
				switch(keyFlag){
					case 1:
						game.removeKeyListener(this);
						drawRoleMenu();
						
						game.addKeyListener(new KeyAdapter(){
							public void keyPressed(KeyEvent e){
								switch(e.getKeyCode()){
									case KeyEvent.VK_UP:
										break;
									case KeyEvent.VK_DOWN:
										break;
									case KeyEvent.VK_ENTER:
										game.removeKeyListener(this);
										game.cp.remove(canvas);
										Nightmare.newGame(game,stage,"Rio");
										break;
									case KeyEvent.VK_ESCAPE:
										game.removeKeyListener(this);		
										drawMenu();
										break;
								}
							}
							
							public void keyReleased(KeyEvent e){}
							public void keyTyped(KeyEvent e){}
						});
						break;
					case 2:
						System.exit(0);
						break;
					default:break;
				}
				break;
			case KeyEvent.VK_ESCAPE:
				
					
			
				break;
			default:break;
		}
		
	}

	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	public void drawMenu(){
		game.addKeyListener(this);
		
		try{
			play1 = ImageIO.read(new File("image/play1.png"));
			play2 = ImageIO.read(new File("image/play2.png"));
			exit1 = ImageIO.read(new File("image/exit1.png"));
			exit2 = ImageIO.read(new File("image/exit2.png"));
			bg = ImageIO.read(new File("image/bg.png"));
		}
		catch(IOException e){}
		
		g.drawImage(bg, 0, 0, null);
		g.drawImage(play2, 400, 100, null);
		g.drawImage(exit1, 400, 150, null);
		
		
	}
	
	public void drawRoleMenu(){
		
		try{
			sword = ImageIO.read(new File("image/sword2.png"));
			bg = ImageIO.read(new File("image/rolebg.png"));
		}
		catch(IOException e){}
		
		g.drawImage(bg, 0, 0, null);
		g.drawImage(sword, 400, 100, null);
	}
	
	
	/*	
	public void drawbg(){ 
		Graphics g = ctest.getGraphics();
		Graphics gg = img.getGraphics();

		g.drawImage(bg,0,0,800,600,null);
		gg.drawImage(bg,0,0,800,600,null);

		g.dispose();
		gg.dispose();
	}
	public void drawLine(int x,int y){ 
		Graphics g = ctest.getGraphics();
		Graphics gg = img.getGraphics();

		g.drawLine(x,y,800,600);
		gg.drawLine(x,y,800,600);

		g.dispose();
		gg.dispose();
	}
	*/
}

