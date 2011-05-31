import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;

import javax.swing.JFrame;



public class GWindow {
	/**
	 * @param args
	 */
	private JFrame game;
	public Container cp;
	
	public GWindow(){
		
		game = new JFrame();
		game.setTitle("Nightmare");
		game.setLayout(null);
		game.setSize(800, 600);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setLocationRelativeTo(null);
		game.setResizable(false);
		game.setVisible(true);
		
		cp = game.getContentPane();
	}

	public int getWidth() {
		return game.getWidth();
	}

	public int getHeight() {
		return game.getHeight();
	}
	
	public void addKeyListener(GMenu menu) {
		game.addKeyListener(menu);
	}

	public void addKeyListener(KeyAdapter keyAdapter) {
		game.addKeyListener(keyAdapter);
	}
	
	public void removeKeyListener(GMenu menu) {
		game.removeKeyListener(menu);
	}

	public void removeKeyListener(KeyAdapter keyAdapter) {
		game.removeKeyListener(keyAdapter);
	}

	public void removeKeyListener(GScreen gScreen) {
		game.removeKeyListener((KeyListener) gScreen);
		
	}
	
}

