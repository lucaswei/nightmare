public class EventConnect{
	private ArrayList<GameEventListener> listeners;

	public EventConnect(){
		listeners = new ArrayList<GameEventListener>();
	}
	public void addEventListener(GameEventListener listener){
		this.listeners.add(listener);
	}
	public void dispatch (String event){
		for(GameEventListener listener: listeners){
			listener.trigger(event);
		}
}
interface GameEventListener {
	void trigger(String event);
}
