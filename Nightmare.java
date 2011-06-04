import java.util.concurrent.*;
import java.awt.Point;
import java.awt.Container;

public class Nightmare{
	public static void main(String[] args){
		GWindow  window = new GWindow();
		EventConnect.addEventListener(window);
	}
}

class Game implements GameEventListener{
	private GScreen output;
	private Clock clock;
	private Processor processor;

	public Game(Stage stage,String hero){
		clock = new Clock(32);
		KeyboardListener keyboard = new KeyboardListener();
		
		Hero player = new Hero(keyboard,new Point(225,500),8,2);
		ArrayBlockingQueue<Printable[]> channel = new ArrayBlockingQueue<Printable[]>(1);
		processor = new Processor(stage,clock,player,channel);
		
		output = new GScreen(channel,keyboard,stage,player);
		EventConnect.addEventListener(output);
	}
	
	public Container getContent(){
		return output.getContent();
	}
	
	public void display(){
		Thread processorThread = new Thread(processor);
		Thread outputThread = new Thread(output);
		
		processorThread.start();
		outputThread.start();
		clock.start();
	}
	
	public void trigger(GameEvent event){
		String signal = event.getSignal();
		if(signal.equals("pause")){
			clock.pause();
		}
		else if(signal.equals("continu")){
			clock.start();
		}
		else if(signal.equals("end")){
			clock.stop();
			finalize();
		}
	}
	
	protected void finalize(){
		EventConnect.removeEventListener(output);
	}
}
