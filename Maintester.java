import java.util.*;
public class Maintester {

	public static void main(String[] args) {
		CityGenerator c = new CityGenerator();
		City[] listOfCities = c.generate();
		EventQueue eQueue = new EventQueue();
		Population pop = new Population(0.001);

		for(int i = 0; i < 150; i++){
			double x = (Math.random()*((250-1)+1))+1;
			Individual indi = new Individual(listOfCities);
			pop.add(indi);
			Event Death = new Event('D', x, indi);
			eQueue.add(Death);
			double x1 = (Math.random()*((250-1)+1))+1;
			double x2 = (Math.random()*((250-1)+1))+1;
			Event reproduce = new Event('R', x2, indi);
			eQueue.add(reproduce);
			Event mutate = new Event('M', x1, indi);
			eQueue.add(mutate);
		}
		City[] vgh = new City[25];
		vgh = pop.bestPath();

		for(int i = 0; i<pop.size(); i++){

			System.out.println(pop.fitness(pop.getIndividual(i)));
		}

		/*for(Event ups: eQueue){
			System.out.println(ups);
		}*/
	}

}
