import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;

import java.awt.*;
import javax.swing.JFrame;



public class GWindow implements GameEventListener{
	/**
	 * @param args
	 */
	
	private static int width = 800;
	private static int height = 600; 
	
	private JFrame window;
	public Container content;
	
	private String state;
	
	public GWindow(){
		
		window = new JFrame();
		window.setTitle("Nightmare");
		window.setLayout(null);
		window.setSize(width, height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setVisible(true);
		
		//cp = window.getContentPane();
		menu();
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
	
	public void addKeyListener(KeyListener key) {
		window.addKeyListener(key);
	}

	public void addKeyListener(KeyAdapter keyAdapter) {
		window.addKeyListener(keyAdapter);
	}
		
	public void removeKeyListener(KeyListener key) {
		window.removeKeyListener(key);
	}
	public void removeKeyListener(GMenu menu) {
		window.removeKeyListener(menu);
	}

	public void removeKeyListener(KeyAdapter keyAdapter) {
		window.removeKeyListener(keyAdapter);
	}



	public void repaint() {
		// TODO Auto-generated method stub
		window.repaint();
	}
	
	private void add(Container content){
		if(this.content != null){
			window.remove(this.content);
		}
		this.content = content;
		window.add(content);
	}
	
	private void menu(){
		GMenu menu = new GMenu();
		add(menu.getContent());
		menu.display();
	}
	
	private void gameStart(Stage stage,String hero){
		Game game = new Game(stage,hero);
		add(game.getContent());
		game.display();
	}
	
	private void gameEnd(){
	}
	
	private void close(){
	}

	public void trigger(GameEvent event){
		String signal = event.getSignal();
		if(signal.equals("menu")){
			menu();
		}
		if(signal.equals("start")){
			Object[] data = event.getData();
			Stage stage = (Stage)data[0];
			String hero = (String)data[1];
			gameStart(stage,hero);
		}
		else if(signal.equals("end")){
			gameEnd();
		}
		else if(signal.equals("close")){
			close();
		}
	}



	
}

