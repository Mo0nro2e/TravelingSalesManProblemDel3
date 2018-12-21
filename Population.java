import java.util.ArrayList;

public class Population {
	
	/*
	 * Attrubutes.
	 */

	private ArrayList<Individual> population;
	private City[] bestPath;
	private double omega, costMin;

	/*
	 * Constructor.
	 */

	public Population(double omega) {
		costMin = Double.MAX_VALUE;
		this.omega = omega;
		population = new ArrayList<>(0);
	}

	/*
	 * Adds this individual to the population in ascending order of path cost,
	 * and checks if its cost is the lowest yet seen.
	 */

	public void add(Individual i) {
		if(i.cost() < costMin) {
			costMin = i.cost();
			bestPath = i.path();
		}
		for(Individual p : population) {
			if(i.cost() < p.cost()) {
				population.add(population.indexOf(p), i);
				return; // Exits method as soon as the first element in the array is found that satisfies the if-statement.
			}
		}
		population.add(i); //If the cost is the highest in the population (or population is empty), the individual is added at the end.
	}

	/*
	 * Returns the size of this population.
	 */

	public int size() {
		return population.size();
	}

	/*
	 * Returns true if the population contains the individual.
	 */

	public boolean contains(Individual i) {
		return population.contains(i);
	}

	/*
	 * Removes the individual from the population.
	 */

	public void remove(Individual i) {
		population.remove(i);
	}

	/*
	 * Returns the fitness of this individual.
	 */

	public double fitness(Individual i) {
		return (omega + Math.pow((costMin / i.cost()), 2)) / (1 + 2 * omega);
	}

	/*
	 * Returns a copy of the best path ever in the population.
	 */

	public City[] bestPath() {
		return bestPath;
	}

	/*
	 * Models an epidemic.
	 */

	public void epidemic() {
		int sizeAtEpidemic = size(); //husk arrayList er dynamisk.. size() ændrer sig hver gang du tilføjer/fjerner et element.
		//removes the 5 worst individuals in terms of cost/fitness.
		for(int i = size()-1; i > sizeAtEpidemic-6; i--)
			population.remove(i);
		//The 5 fittest survive, rest are randomly removed based on probability.
		for(int i = 5; i < size(); i++) {
			if(!RandomUtils.getRandomEvent(Math.pow(fitness(population.get(i)),2)))
				population.remove(i);
		}
	}

	/*
	 * Returns a textual representation of the cost for each individual. 
	 */
  //bare for at tjekke at individerne er sorteret korrekt.
	public String toString() {
		String output = "";
		for(Individual i : population)
			output = output + i.cost() + "\n";
		return output;
	}
}
