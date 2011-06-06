import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;

public class GMenu{
	
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
	
	private Container container = new Container();
	
	private ListMenu mainMenu;
	private ListMenu roleMenu;
	
	private ItemList mainList;
	private ItemList roleList;
	
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
		container.setBounds(0, 0, 800, 600);
		container.setVisible(true);
		
		mainList = new ItemList();
		mainList.add(playItem,playItemActive,new Runnable(){public void run(){displayRoleMenu();}});
		mainList.add(exitItem,exitItemActive,new Runnable(){public void run(){System.exit(0);}});
	
		roleList = new ItemList();
		roleList.add(sword,sword,new Runnable(){public void run(){startGame();}});
	}
	private void startGame(){
		EventConnect.dispatch(new GameEvent("start",new Object[]{stage,"Rio"}));
	}
	
	private void displayMainMenu(){
		graphics = container.getGraphics();
		graphics.drawImage(mainMenuBg,0,0,null);
		if(roleMenu != null){
			container.remove(roleMenu);
			roleMenu = null;
		}
		mainMenu = new ListMenu(mainList,400,200,200,50);
		container.add(mainMenu);
		mainMenu.display();
	}
	private void displayRoleMenu(){
		graphics = container.getGraphics();
		graphics.drawImage(roleMenuBg,0,0,null);
		if(mainMenu != null){
			container.remove(mainMenu);
			roleMenu = null;
		}
		roleMenu = new ListMenu(roleList,400,200,200,50,new Runnable(){public void run(){displayMainMenu();}});
		container.add(roleMenu);
		roleMenu.display();
	}
	
	public Container getContent(){
		return container;
	}
	public synchronized void display(){

		displayMainMenu();
	}
}

