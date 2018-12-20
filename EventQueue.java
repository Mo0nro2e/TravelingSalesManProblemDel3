import java.util.ArrayList;
import java.util.Iterator;
public class EventQueue implements Iterator<Event>  {
	private class EventQueueIterator implements Iterator<Event> {
		private int index = 0;


	  // The next three methods implement Iterator.
	  public boolean hasNext() {
	    if (index < EventQueue.this.eventQueue.size()){
	      return true;
	    }
	    return false;
	  }

	  public Event next() {
	    if (hasNext() == false)
	      System.out.println("No more elementes");
	    index++;
	    return EventQueue.this.eventQueue.get(index - 1);
	  }

	}
	//Attributes
	private ArrayList<Event> eventQueue;


	//Constructor
	public EventQueue(){
		this.eventQueue = new ArrayList<Event>();
	}

	// adds a new event to the queue
	public void add(Event e){
		if(eventQueue.size() == 0){
			eventQueue.add(e);
		}
		else if(eventQueue.get(0).time() > e.time()){
			eventQueue.add(0,e);
		}

		else{
			int i = 1;
			boolean isAdded = false;
			while (i < eventQueue.size() && isAdded == false){
				if (eventQueue.get(i-1).time() <= e.time() && eventQueue.get(i+1).time() >= e.time()){
				 eventQueue.add(i, e);
				 isAdded = true;
				}
				i ++;
			}
		}
	}
	//returns the next event in the queue and removes it
	public Event next(){
		return eventQueue.remove(0);
	}

	//Checks if the queue has more events
	public boolean hasNext(){
		return eventQueue.isEmpty();
	}

	// This method implements Iterable.
	public Iterator<Event> iterator() {
		return new EventQueueIterator();
	}








}
