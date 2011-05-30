import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;

import javax.swing.JFrame;



public class Window {
	/**
	 * @param args
	 */
	private JFrame game;
	public Container cp;
	
	public Window(){
		
		game = new JFrame();
		game.setTitle("Nightmare");
		game.setLayout(null);
		game.setSize(800, 600);
		game.setDefaultCloseOperation(game.EXIT_ON_CLOSE);//Ãö³¬
		game.setLocationRelativeTo(null);
		game.setResizable(false);
		game.setVisible(true);
		
		cp = game.getContentPane();
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return game.getWidth();
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return game.getHeight();
	}
	
	public void addKeyListener(Menu menu) {
		// TODO Auto-generated method stub
		game.addKeyListener(menu);
	}

	public void addKeyListener(KeyAdapter keyAdapter) {
		// TODO Auto-generated method stub
		game.addKeyListener(keyAdapter);
	}
	
	public void removeKeyListener(Menu menu) {
		// TODO Auto-generated method stub
		game.removeKeyListener(menu);
	}

	public void removeKeyListener(KeyAdapter keyAdapter) {
		// TODO Auto-generated method stub
		game.removeKeyListener(keyAdapter);
	}
	
}

