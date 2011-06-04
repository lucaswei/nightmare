import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;

public class GMenu implements KeyListener{
	
	//private GWindow window;
	private Stage stage;
	private Image bg,play1,play2,exit1,exit2,sword;
	
	private BufferedImage buffer;
	private Graphics graphics;
	private int keyFlag;
	
	private String imagePath = "image/";
	
	private Container menu = new Container();
	
	public GMenu(){
		
		//Choose map
		String mapName = "";
		this.stage = new Stage("default");

		keyFlag = 1;
				
		menu.setBounds(0, 0, 800, 600);
		menu.setVisible(true);
		menu.addKeyListener(this);
	}

	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				keyFlag--;
				
				if(keyFlag < 1)
					keyFlag = 1;
				else{
					graphics.drawImage(play2, 400, 200, null);
					graphics.drawImage(exit1, 400, 250, null);
				}
				break;
			case KeyEvent.VK_DOWN:
				keyFlag++;
				
				if(keyFlag > 2)
					keyFlag = 2;
				else{
					graphics.drawImage(play1, 400, 200, null);
					graphics.drawImage(exit2, 400, 250, null);
				}
				break;
			case KeyEvent.VK_ENTER://Enter RoleMenu
				switch(keyFlag){
					case 1:
						menu.removeKeyListener(this);
						drawRoleMenu();
						
						menu.addKeyListener(new KeyAdapter(){
							public void keyPressed(KeyEvent e){
								switch(e.getKeyCode()){
									case KeyEvent.VK_UP:
										break;
									case KeyEvent.VK_DOWN:
										break;
									case KeyEvent.VK_ENTER://Enter menu
										menu.removeKeyListener(this);
										menu.remove(menu);//delete menu;
										EventConnect.dispatch(new GameEvent("start",new Object[]{stage,"Rio"}));
										break;
									case KeyEvent.VK_ESCAPE://Back to Menu
										menu.removeKeyListener(this);
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
	
	public Container getContent(){
		return menu;
	}
	public void display(){
		graphics = menu.getGraphics();
		drawMenu();
		menu.requestFocus();
	}

	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	public void drawMenu(){
		try{
			play1 = ImageIO.read(new File(imagePath + "play1.png"));
			play2 = ImageIO.read(new File(imagePath + "play2.png"));
			exit1 = ImageIO.read(new File(imagePath + "exit1.png"));
			exit2 = ImageIO.read(new File(imagePath + "exit2.png"));
			bg = ImageIO.read(new File(imagePath + "menubg.png"));
		}
		catch(IOException e){}
		
		graphics.drawImage(bg, 0, 0, null);
		graphics.drawImage(play2, 400, 200, null);
		graphics.drawImage(exit1, 400, 250, null);
	}
	
	public void drawRoleMenu(){
		try{
			sword = ImageIO.read(new File(imagePath + "sword2.png"));
			bg = ImageIO.read(new File(imagePath + "rolebg.png"));
		}
		catch(IOException e){}
		
		graphics.drawImage(bg, 0, 0, null);
		graphics.drawImage(sword, 400, 300, null);
	}
	
}

