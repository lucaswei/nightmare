import java.util.*;
public class EventConnect{
	private static ArrayList<GameEventListener> listeners = new ArrayList<GameEventListener>();

	private EventConnect(){
	}
	
	public static void addEventListener(GameEventListener listener){
		listeners.add(listener);
	}
	public static void removeEventListener(GameEventListener listener){
		listeners.remove(listener);
	}
	
	public static void dispatch (String signal){
		GameEvent event = new GameEvent(signal);
		dispatch(event);
	}
	public static synchronized void dispatch (GameEvent event){
		GameEventListener[] list = listeners.toArray(new GameEventListener[listeners.size()]);
		for(GameEventListener listener: list){
			listener.trigger(event);
		}
	}
}

interface GameEventListener {
	void trigger(GameEvent event);
}

class GameEvent{
	private String signal;
	private Object[] data;
	
	public GameEvent(String signal){
		this.signal = signal;
		this.data   = null;
	}
	public GameEvent(String signal,Object[] data){
		this.signal = signal;
		this.data   = data;
	}
	public String getSignal(){
		return signal;
	}
	public Object[] getData(){
		return data;
	}
}
