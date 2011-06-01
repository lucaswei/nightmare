import java.awt.event.*;
import java.util.*;

interface ActionSource{
	public boolean check(String s);
	public void clear();
}

class KeyboardListener implements KeyListener,ActionSource{
	private Map<String,Boolean> actions;
	private Map<Integer,String> EventToString;
	public KeyboardListener(){
		actions = new HashMap<String , Boolean>();
		actions.put("UP"   ,false);
		actions.put("DOWN" ,false);
		actions.put("LEFT" ,false);
		actions.put("RIGHT",false);
		actions.put("SHOOT",false);
		actions.put("BOMB" ,false);
		actions.put("PAUSE",false);
		actions.put("SLOW" ,false);
		
		EventToString = new HashMap<Integer , String>();
		EventToString.put(KeyEvent.VK_UP,"UP");
		EventToString.put(KeyEvent.VK_DOWN,"DOWN");
		EventToString.put(KeyEvent.VK_LEFT,"LEFT");
		EventToString.put(KeyEvent.VK_RIGHT,"RIGHT");
		EventToString.put(KeyEvent.VK_ESCAPE,"PAUSE");
		EventToString.put(KeyEvent.VK_Z,"SHOOT");
		EventToString.put(KeyEvent.VK_X,"BOMB");
		EventToString.put(KeyEvent.VK_C,"SLOW");
	}
	public void clear(){
		actions.put("UP"   ,false);
		actions.put("DOWN" ,false);
		actions.put("LEFT" ,false);
		actions.put("RIGHT",false);
		actions.put("SHOOT",false);
		actions.put("BOMB" ,false);
		actions.put("PAUSE",false);
		actions.put("SLOW" ,false);
	}
	public boolean check(String s){
		
		Boolean bool = actions.get(s);
		if(bool != null)
			return bool;
		return false;
	}
	public void keyPressed(KeyEvent e){
		keyToggle(e,true);
	}
	public void keyReleased(KeyEvent e){
		keyToggle(e,false);
	}
	private void keyToggle(KeyEvent e,boolean turn){
		String output = EventToString.get(e.getKeyCode());
		actions.put(output, turn);
	}
	public void keyTyped(KeyEvent e){}
}
