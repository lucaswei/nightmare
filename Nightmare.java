public class Nightmare{
	public static void main(String[] args){
		//Main menu
		Menu menu = new Menu();
		//
	}
	public static void newGame(Window window,Stage stage,String hero){
		Clock clock = new Clock();
		ArrayBlockqueue<Printable[]> channel = new ArrayBlockingQueue<Printable[]>;
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
