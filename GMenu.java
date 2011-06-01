import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;

public class GMenu implements KeyListener{
	
	private GWindow game;
	private Stage stage;
	private Panel canvas;
	private Graphics g;
	private Image bg,play1,play2,exit1,exit2,sword;
	private BufferedImage img;
	private int keyFlag;
	
	
	public GMenu(){		
		
		game = new GWindow();
		
		//Choose map
		String mapName = "";
		this.stage = new Stage("default");

		keyFlag = 1;
	
		canvas = new Panel(){
			public void paint(Graphics g){
				g.drawImage(img, 0, 0, null);
			}
		};
		
		img = new BufferedImage(game.getWidth(),game.getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		canvas.setBounds(0, 0, 800, 600);
		game.cp.add(canvas);
		
		drawMenu();
		
	}

	public void keyPressed(KeyEvent e){
		
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				keyFlag--;
				
				if(keyFlag < 1)
					keyFlag = 1;
				else{
					g.drawImage(play2, 400, 200, null);
					g.drawImage(exit1, 400, 250, null);
				}
				break;
			case KeyEvent.VK_DOWN:
				keyFlag++;
				
				if(keyFlag > 2)
					keyFlag = 2;
				else{
					g.drawImage(play1, 400, 200, null);
					g.drawImage(exit2, 400, 250, null);
				}
				break;
			case KeyEvent.VK_ENTER://Enter RoleMenu
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
									case KeyEvent.VK_ENTER://Enter game
										game.removeKeyListener(this);
										game.cp.remove(canvas);//delete canvas;
										Nightmare.newGame(game,stage,"Rio");
										break;
									case KeyEvent.VK_ESCAPE://Back to Menu
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
			default:break;
		}
		
	}

	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	public void drawMenu(){
		game.addKeyListener(this);
		
		Graphics g = canvas.getGraphics();
		Graphics gg = img.getGraphics();
		
		try{
			play1 = ImageIO.read(new File("map/default/image/play1.png"));
			play2 = ImageIO.read(new File("map/default/image/play2.png"));
			exit1 = ImageIO.read(new File("map/default/image/exit1.png"));
			exit2 = ImageIO.read(new File("map/default/image/exit2.png"));
			bg = ImageIO.read(new File("map/default/image/menubg.png"));
		}
		catch(IOException e){}
		
		g.drawImage(bg, 0, 0, null);
		g.drawImage(play2, 400, 200, null);
		g.drawImage(exit1, 400, 250, null);
		gg.drawImage(bg, 0, 0, null);
		gg.drawImage(play2, 400, 200, null);
		gg.drawImage(exit1, 400, 250, null);
		
		g.dispose();
		gg.dispose();
	}
	
	public void drawRoleMenu(){
		
		Graphics g = canvas.getGraphics();
		Graphics gg = img.getGraphics();
		
		try{
			sword = ImageIO.read(new File("map/default/image/sword2.png"));
			bg = ImageIO.read(new File("map/default/image/rolebg.png"));
		}
		catch(IOException e){}
		
		g.drawImage(bg, 0, 0, null);
		g.drawImage(sword, 400, 200, null);
		gg.drawImage(bg, 0, 0, null);
		gg.drawImage(sword, 400, 200, null);
		
		g.dispose();
		gg.dispose();
	}
	
	/*
	ctest = new Panel(){
	public void paint(Graphics g){		
			g.drawImage(img, 0, 0, null);
		}
	};
	
	ctest.setBounds(0, 0, 800, 600);
	img = new BufferedImage(game.getWidth(),game.getHeight(),BufferedImage.TYPE_INT_ARGB);

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

