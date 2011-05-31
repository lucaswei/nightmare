import java.util.concurrent.*;

public class Nightmare{
	public static void main(String[] args){
		//Main menu
		GMenu menu = new GMenu();
		//
	}
	public static void newGame(GWindow window,Stage stage,String hero){
		Clock clock = new Clock(100);
		ArrayBlockingQueue<Printable[]> channel = new ArrayBlockingQueue<Printable[]>(1);
		Processor processor = new Processor(clock,channel);
		processor.setStage(stage);
		
		KeyBoardListener keyboard = new KeyBoardListener(clock);
		GameOutput output = new GameOutput(window,keyboard,channel,stage);
		output.setStage(stage);
		
		Player player = new Player(hero,keyboard);
		processor.addPlayer(player);
		
		Thread processorThread = new Thread(processor);
		Thread outputThread = new Thread(output);
		
		processorThread.start();
		outputThread.start();
		clock.start();
	}
}
