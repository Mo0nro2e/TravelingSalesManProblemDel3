import java.util.Scanner;
public class Simulator {
	private static EventQueue eventQueue;
	private static Population population;
	private static Scanner reader;
	private static int initialPopulation,
		maxPopulation,
		mutationInterval,
		reproductionInterval,
		deathInterval;
	private static double comfortNormalization,
		simulationTime;

	/**
	Initialisation
	*/

	private static void init() {
		eventQueue = new EventQueue();
		population = new Population(comfortNormalization);
	}

	/**
	Helper methods that return the event time
	for each type of event for an individual.
	*/

	private static double mutationTime(double fitness, boolean hasMutated) { 
		if(hasMutated)	
			return RandomUtils.getRandomTime((1-Math.log(fitness))*mutationInterval);
		else
			return RandomUtils.getRandomTime((1-Math.log(fitness))*(double)mutationInterval/10);
	}

	private static double reproductionTime(double fitness) {
		return RandomUtils.getRandomTime((1-Math.log(fitness))*((double)initialPopulation/maxPopulation)*reproductionInterval);
	}

	private static double deathTime(double fitness) {
		return RandomUtils.getRandomTime((1-Math.log(1-fitness))*deathInterval);
	}
	/**
	Helper methods that return a new event of each type
	*/

	private static Event createMutationEvent(Individual i, boolean hasMutated) {
		if(hasMutated) 
			return new Event(Event.MUTATION, mutationTime(population.fitness(i),true), i);
		else
			return new Event(Event.MUTATION, mutationTime(population.fitness(i),false), i);
	}

	private static Event createReproductionEvent(Individual i) {
		return new Event(Event.REPRODUCTION, reproductionTime(population.fitness(i)), i);
	}

	private static Event createDeathEvent(Individual i) {
		return new Event(Event.DEATH, deathTime(population.fitness(i)), i);
	}
	/**
	Helper method that adds each event to the queue for that individual.
	*/

	private static void addAllEvents(Individual i) {
		eventQueue.add(createMutationEvent(i,true));
		eventQueue.add(createReproductionEvent(i));
		eventQueue.add(createDeathEvent(i));
	}

	/**
	Obtains parameters from user
	*/

	private static void userInput() {
		System.out.print("Size of the initial population: ");
		initialPopulation = reader.nextInt();
		System.out.print("Maximum size of the population: ");
		do {
			maxPopulation = reader.nextInt();
			if(maxPopulation < initialPopulation)
				System.out.print("Maximum population cannot be smaller than"
									+ " initial population, please try again: ");
		} while(maxPopulation < initialPopulation);
		System.out.print("Value of the parameter omega: ");
		comfortNormalization = reader.nextDouble();
		System.out.print("Value of the parameter D: ");
		deathInterval = reader.nextInt();
		System.out.println("Value of the parameter M: ");
		mutationInterval = reader.nextInt();
		System.out.print("Value of the parameter R: ");
        reproductionInterval = reader.nextInt();
		System.out.print("Time to run simulation: ");
		simulationTime = reader.nextDouble();
	}

	/**
	Fills the population with individuals, each individual
	having the 3 different events assigned to it.
	*/

	private static void fillPopulation() {
		Individual individual;
		City[] listOfCities = CityGenerator.generate();
		for(int i = 0; i < initialPopulation; i++) {
			individual = new Individual(listOfCities);
			population.add(individual);
			addAllEvents(individual);
		}
	}

	/**
	Methods that simulate each of the 3 events as described in the contract
	*/

	private static void mutationEvent(Individual i) {
		population.remove(i);
		if(RandomUtils.getRandomEvent(Math.pow((1-population.fitness(i)),2))) {
			i.mutate();
			if(RandomUtils.getRandomEvent(0.3)) {
				i.mutate();
				if(RandomUtils.getRandomEvent(0.15))
					i.mutate();
			}
		eventQueue.add(createMutationEvent(i,true));
		} 
		else eventQueue.add(createMutationEvent(i,false));
		population.add(i);
	} 

	private static void reproductionEvent(Individual i) {
		Individual child = i.reproduce();
		eventQueue.add(createReproductionEvent(i));
		population.add(child);
		addAllEvents(child);
	}

	private static void deathEvent(Individual i) {
		if(RandomUtils.getRandomEvent(Math.pow((1-population.fitness(i)),2)))
			population.remove(i);
		else
			eventQueue.add(createDeathEvent(i));
	}

	private static void simulateNextEvent(Event e) {
		switch(e.type()) {
			case Event.MUTATION:
				System.out.println("Mutation event");
				mutationEvent(e.individual());
				break;
			case Event.REPRODUCTION:
				System.out.println("Reproduction event");
				reproductionEvent(e.individual());
				break;
			case Event.DEATH:
				System.out.println("Death event");
				deathEvent(e.individual());
				break;
		}
	}

	public static void main(String[] args) {
		reader = new Scanner(System.in);
		init();
		userInput();
		fillPopulation();
		Event nextEvent;
		double timeElapsed = 0;

		while(eventQueue.hasNext() && (timeElapsed < simulationTime)) {
			nextEvent = eventQueue.next();
				if(population.size() < maxPopulation) {
					if(population.contains(nextEvent.individual())) {
						simulateNextEvent(nextEvent);
						timeElapsed = timeElapsed + nextEvent.time();
						System.out.println(timeElapsed);
					}
				}
				else population.epidemic();
		}
		reader.close();		
	}

}