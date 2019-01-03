import java.util.*;
public class Maintester {

	public static void main(String[] args) {
		CityGenerator c = new CityGenerator();
		City[] listOfCities = c.generate();
		EventQueue eQueue = new EventQueue();
		Population pop = new Population(0.001);

		for(int i = 0; i < 2; i++){
			double x = (Math.random()*((250-1)+1))+1;
			Individual indi = new Individual(listOfCities);
			pop.add(indi);
			Event Death = new Event('D', x, indi);
			eQueue.add(Death);
			double x1 = (Math.random()*((250-1)+1))+1;
			double x2 = (Math.random()*((250-1)+1))+1;
			Event reproduce = new Event('R', x1, indi);
			eQueue.add(reproduce);
			Event mutate = new Event('M', x2, indi);
			eQueue.add(mutate);

		}
		City[] vgh = new City[25];
		//vgh = pop.bestPath();

		//for(int i = 0; i<pop.size(); i++){

			//System.out.println(pop.fitness(pop.getIndividual(i)));
		//}
			/*Individual indi = new Individual(listOfCities);
			Event Death = new Event('D', 50, indi);
			eQueue.add(Death);
			Event reproduce = new Event('R', 200, indi);
			eQueue.add(reproduce);
			Event mutate = new Event('M', 30, indi);
			eQueue.add(mutate);*/

		for(Event ups: eQueue){
			System.out.println(ups);
		}

		System.out.println(eQueue.next());
		System.out.println(eQueue.hasNext());
		System.out.println(eQueue.next());
		System.out.println(eQueue.hasNext());
		System.out.println(eQueue.next());
		System.out.println(eQueue.next());
		System.out.println(eQueue.hasNext());
		System.out.println(eQueue.next());
		System.out.println(eQueue.next());
		System.out.println(eQueue.hasNext());

	}

}
