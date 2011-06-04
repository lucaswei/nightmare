import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;

public class GMenu{
	
	//private GWindow window;
	private Stage stage;
	private Image mainMenuBg;
	private Image roleMenuBg;
	private Image playItem,playItemActive;
	private Image exitItem,exitItemActive;
	private Image sword;
	
	private BufferedImage buffer;
	private Graphics graphics;
	private int keyFlag;
	
	private String imagePath = "image/";
	
	private Container menu = new Container();
	
	private KeyListener mainMenuKeyListener = new MainMenuKeyListener();
	private KeyListener roleMenuKeyListener = new RoleMenuKeyListener();
	
	
	public GMenu(){
		
		//Choose map
		String mapName = "";
		this.stage = new Stage("default");

		keyFlag = 1;
		
		try{
			playItem = ImageIO.read(new File(imagePath + "play1.png"));
			exitItem = ImageIO.read(new File(imagePath + "exit1.png"));
			playItemActive = ImageIO.read(new File(imagePath + "play2.png"));
			exitItemActive = ImageIO.read(new File(imagePath + "exit2.png"));
			sword = ImageIO.read(new File(imagePath + "sword2.png"));
			mainMenuBg = ImageIO.read(new File(imagePath + "menubg.png"));
			roleMenuBg = ImageIO.read(new File(imagePath + "rolebg.png"));
		}
		catch(IOException e){
			System.out.println("Can't load menu images");
		}
		menu.setBounds(0, 0, 800, 600);
		menu.setVisible(true);
		menu.addKeyListener(mainMenuKeyListener);
	}

	private class MainMenuKeyListener implements KeyListener{
		public void keyPressed(KeyEvent e){
			switch(e.getKeyCode()){
				case KeyEvent.VK_UP:
					keyFlag--;
				
					if(keyFlag < 1)
						keyFlag = 1;
					else{
						graphics.drawImage(playItemActive, 400, 200, null);
						graphics.drawImage(exitItem, 400, 250, null);
					}
					break;
				case KeyEvent.VK_DOWN:
					keyFlag++;
				
					if(keyFlag > 2)
						keyFlag = 2;
					else{
						graphics.drawImage(playItem, 400, 200, null);
						graphics.drawImage(exitItemActive, 400, 250, null);
					}
					break;
				case KeyEvent.VK_ENTER://Enter RoleMenu
					switch(keyFlag){
						case 1:
							drawRoleMenu();
							menu.removeKeyListener(mainMenuKeyListener);
							menu.addKeyListener(roleMenuKeyListener);
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
	}

	private class RoleMenuKeyListener implements KeyListener{
		public void keyPressed(KeyEvent e){
			switch(e.getKeyCode()){
				case KeyEvent.VK_UP:
					break;
				case KeyEvent.VK_DOWN:
					break;
				case KeyEvent.VK_ENTER://Enter menu
					EventConnect.dispatch(new GameEvent("start",new Object[]{stage,"Rio"}));
					break;
				case KeyEvent.VK_ESCAPE://Back to Menu
					menu.removeKeyListener(roleMenuKeyListener);
					menu.addKeyListener(mainMenuKeyListener);
					drawMenu();
					break;
			}
		}

		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}
	}
	
	public Container getContent(){
		return menu;
	}
	public synchronized void display(){
		graphics = menu.getGraphics();
		menu.requestFocus();
		drawMenu();
	}

	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	public void drawMenu(){
		graphics.drawImage(mainMenuBg, 0, 0, null);
		graphics.drawImage(playItemActive, 400, 200, null);
		graphics.drawImage(exitItem, 400, 250, null);
	}
	
	public void drawRoleMenu(){
		graphics.drawImage(roleMenuBg, 0, 0, null);
		graphics.drawImage(sword, 400, 300, null);
	}
	
}

