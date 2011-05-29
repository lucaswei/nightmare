
public class Nightmare{
	public static void main(String[] args){
		//Main menu
		
		//
	}
	
	private void game(){
		//Choose map
		String mapName = "";
		Stage stage = new Stage(new File(mapName));
		//Choose role
		Clock clock = new Clock();
		ArrayBlockqueue<Printable[]> channel = new ArrayBlockqueue<Printable[]>;
		Processor processor = new Processor(clock,channel);
		processor.setStage(stage);
		
		KeyBoardListener keyboard = new KeyBoardListener(clock);
		GameOutput output = new GameOutput(clock,keyboard,channel);
		output.setStage(stage);
		
		Player player = new Player(keyboard);
		processor.addPlayer(player);
		
		Thread processorThread = new Thread(processor);
		Thread outputThread = new Thread(output);
		
		processorThread.start();
		outputThread.start();
		clock.start();
	}
}
