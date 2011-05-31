import java.lang.Iterable;
import java.util.ArrayList;
import java.util.Iterator;

class ExtensibleArray<T> implements Iterable<T>{
	public ExtensibleArray(){
		array = new ArrayList<T>();
	}
	
	
	public void add(int index,T element){
		int size = array.size();
		int sub = index - size;
		for(int i=0;i<=sub;i++){
			array.add(null);
		}
		array.set(index,element);
			System.out.println(index + "," + array.size());
	}
	
	
	public T get(int index){
		return array.get(index);
	}
	
	
	public void remove(int index){
		array.remove(index);
	}
	
	
	public Object[]  toArray(){
		return array.toArray();
	}
	
	
	public Object[] toCondensedArray(){
		ArrayList<T> out = new ArrayList<T>();
		for(T element: array){
			if(element != null)
			out.add(element);
		}
		return out.toArray();
	}
	
	
	public Iterator<T> iterator(){
		return new ExtensibleArrayIterator();
	}
	
	
	
	private ArrayList<T> array;
	
	private class ExtensibleArrayIterator implements Iterator<T>{
		private Iterator<T> iterator;
		private T nextElement;
		public ExtensibleArrayIterator(){
			iterator = array.iterator();
		}
		public boolean hasNext(){
			while(iterator.hasNext()){
				nextElement = iterator.next();
				if(nextElement != null){
					return true;
				}
			}
			return false;
		}
		public T next(){
			return (T)nextElement;
		}
		public void remove(){
		}
	}
}
