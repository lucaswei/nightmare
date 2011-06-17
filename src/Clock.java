import java.util.*;

class DelayException  extends Exception{}
class ExceedException extends Exception{}

class Clock{
	public final static int START = 1;
	public final static int STOP  = 0;
	public final static int PAUSE = 2;
	private int status;
	
	private Timer timer;
	private long currentTime = 0;
	private long startTime;
	private int  period;
	
	public  Clock(int period){
		this.period = period;
		status = STOP;
	}
	
	public long next(long previousTime) throws DelayException{
		if(previousTime < currentTime){
			throw new DelayException();
		}
		try{
			while(previousTime >= currentTime){
				Thread.sleep(1);
			}
		}
		catch(InterruptedException e){
			System.out.println("Interrupted");
		}
		return currentTime;
	}
	public  long getTime(){
		return currentTime;
	}
	
	private class SimpleTask extends TimerTask{
		public synchronized void run(){
			currentTime++;
		}
	}
	
	public synchronized void start(){
		if(status == STOP){
			currentTime = 0;
		}
		status = START;
		timer = new Timer();
		timer.scheduleAtFixedRate(new SimpleTask(),period,period);
	}
	public synchronized void pause(){
		if(status == START){
			status = PAUSE;
			timer.cancel();
		}
	}
	public synchronized void stop(){
		if(status != STOP){
			status = STOP;
			timer.cancel();
		}
	}
}
