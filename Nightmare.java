import java.util.concurrent.*;

public class Nightmare{
	public static void main(String[] args){
		//Main menu
		GMenu menu = new GMenu();
		//
	}
	public static void newGame(GWindow window,Stage stage,String hero){
		Clock clock = new Clock(100);
		KeyboardListener keyboard = new KeyboardListener();
		
		Hero player = new Hero(keyboard);
		ArrayBlockingQueue<Printable[]> channel = new ArrayBlockingQueue<Printable[]>(1);
		Processor processor = new Processor(stage,clock,player,channel);
		
		GScreen output = new GScreen(window,keyboard,channel,stage);
		
		Thread processorThread = new Thread(processor);
		Thread outputThread = new Thread(output);
		
		processorThread.start();
		outputThread.start();
		clock.start();
	}
}
