import java.util.concurrent.*;
import java.awt.Point;
import java.awt.Container;

public class Nightmare{
	public static boolean debug = false;
	public static void main(String[] args){
		if(args.length > 0 && args[0].equals("debug")){ debug = true;}
		GWindow  window = new GWindow();
		EventConnect.addEventListener(window);
	}
}

class Game implements GameEventListener{
	private GScreen output;
	private Clock clock;
	private Processor processor;
	
	Thread processorThread;
	Thread outputThread;

	public Game(Stage stage,String hero){
		clock = new Clock(32);
		KeyboardListener keyboard = new KeyboardListener();
		
		Hero player = new Hero(keyboard,new Point(225,500),8,2);
		ArrayBlockingQueue<Printable[]> channel = new ArrayBlockingQueue<Printable[]>(1);
		processor = new Processor(stage,clock,player,channel);
		
		output = new GScreen(channel,keyboard,stage,player);
	}
	
	public Container getContent(){
		return output.getContent();
	}
	
	public void display(){
		EventConnect.addEventListener(processor);
		EventConnect.addEventListener(output);
		EventConnect.addEventListener(this);
		
		processorThread = new Thread(processor);
		outputThread = new Thread(output);
		
		processorThread.start();
		outputThread.start();
		clock.start();
	}
	
	public void trigger(GameEvent event){
		String signal = event.getSignal();
		if(signal.equals("pause")){
			clock.pause();
		}
		else if(signal.equals("continue")){
			clock.start();
		}
		else if(signal.equals("restart")){
			gameEnd();
		}
		else if(signal.equals("end")){
			gameEnd();
		}
	}
	public void gameEnd(){
		clock.stop();
		EventConnect.removeEventListener(processor);
		EventConnect.removeEventListener(output);
		EventConnect.removeEventListener(this);
	}
}
