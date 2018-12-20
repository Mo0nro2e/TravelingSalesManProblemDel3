import java.util.*;
public class Maintester {

	public static void main(String[] args) {
		CityGenerator c = new CityGenerator();
		City[] listOfCities = c.generate();
		EventQueue eQueue = new EventQueue();
		//Iterator<Event> hov = new EventQueue.iterator();
		for(int i = 0; i < 50; i++){
			double x = (Math.random()*((250-1)+1))+1;
			Individual indi = new Individual(listOfCities);
			Event Death = new Event('D', x, indi);
			eQueue.add(Death);
			double x1 = (Math.random()*((250-1)+1))+1;
			double x2 = (Math.random()*((250-1)+1))+1;
			Event reproduce = new Event('R', x2, indi);
			eQueue.add(reproduce);
			Event mutate = new Event('M', x1, indi);
			eQueue.add(mutate);
		}

		for(Event ups: eQueue){
			System.out.println(ups.next());
		}
	}

}
