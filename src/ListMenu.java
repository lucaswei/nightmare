import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ListMenu extends Component{
	private static final long serialVersionUID = 1;
	private Item[] list;
	private Graphics graphics;
	private int pointer = 0;
	private Runnable escape = null;
	
	private int x = 0;
	private int y = 0;
	private int width  = 0;
	private int thick  = 0;
	public ListMenu(ItemList list,int x,int y,int width,int thick){
		this.width = width;
		this.thick = thick;
		this.list  = list.get();
		addKeyListener(new ListMenuKeyListener());
		setBounds(x,y,width,thick*this.list.length);
	}
	
	public ListMenu(ItemList list,int x,int y,int width,int thick,Runnable escape){
		this.width = width;
		this.thick = thick;
		this.list  = list.get();
		addKeyListener(new ListMenuKeyListener());
		setBounds(x,y,width,thick*this.list.length);
		this.escape = escape;
	}
	
	public void display(){
		graphics = getGraphics();
		requestFocus();
		draw();
	}
	
	public void disappear(){
	}
	
	public void draw(){
		int length = list.length;		
		
		for(int i=0;i<length;i++){
			if(i == pointer)
				graphics.drawImage(list[i].active,0,thick*i,null);
			else
				graphics.drawImage(list[i].inactive,0,thick*i,null);
		}
		
	}
	
	class ListMenuKeyListener implements KeyListener{
		public void keyPressed(KeyEvent e){
			int size = list.length;
			switch(e.getKeyCode()){
				case KeyEvent.VK_UP:
					pointer--;
					if(pointer < 0)	pointer += size;
					draw();
					break;
				case KeyEvent.VK_DOWN:
					pointer++;
					if(pointer >= size)	pointer -= size;
					draw();
					break;
				case KeyEvent.VK_ENTER:
					disappear();
					list[pointer].run();
					break;
				case KeyEvent.VK_ESCAPE:
					if(escape != null)
						escape.run();
					break;
				default:
					break;
				}
			}
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}
	}
}

class ItemList{
	private ArrayList<Item> list = new ArrayList<Item>();
	public void add(Image inactive,Image active,Runnable runnable){
		list.add(new Item(inactive,active,runnable));
	}
	public Item[] get(){
		return list.toArray(new Item[list.size()]);
	}
	
}

class Item implements Runnable{
	public Image inactive;
	public Image active;
	public Runnable runnable;
	public Item(Image inactive,Image active,Runnable runnable){
		this.inactive = inactive;
		this.active   = active;
		this.runnable = runnable;
	}
	public void run(){
		if(runnable != null)
			runnable.run();
	}
}
	

