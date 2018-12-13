import java.util.ArrayList;
public class EventQueue{
	//Attributes
	private ArrayList<Event> EvenetQueue;


	//Constructor
	public EventQueue(){
		EvenetQueue = new ArrayList<Event>();
	}

	// adds a new event to the queue
	public void add(Event e){
		if(EvenetQueue.size() == 0){
			EvenetQueue.add(e);
		}
		if(EventQueue.get(0).time() < e.time()){
			EvenetQueue.add(0,e);
		}

		else{
			for (int i = 1; i < EventQueue.size(); i++){
				 if (EvenetQueue.get(i-1).time() <= e.time() && EvenetQueue.get(i+1).time() >= e.time()){
					 EvenetQueue.add(i, e);
				 }
			}
		}
	}
	//returns the next event in the queue and removes it
	public Event next(){
		Event nextEvent = EvenetQueue.get(0);
		EvenetQueue.remove(0);
		return nextEvent;
	}

	//Checks if the queue has more events
	public boolean hasNext(){
		if(EventQueue.size() != 0){
			retrun true;
		}
	}




}
