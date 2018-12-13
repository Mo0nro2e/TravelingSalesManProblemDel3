import java.util.ArrayList;
public class EventQueue{
	//Attributes
	private ArrayList<Event> eventQueue;


	//Constructor
	public EventQueue(){
		eventQueue = new ArrayList<Event>();
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




}
