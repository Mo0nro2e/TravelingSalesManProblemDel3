import java.util.Scanner;
public class Simulator {
	private static EventQueue eventQueue;
	private static Population population;
	private static Scanner reader;
	private static int option,
		initialPopulation, 
		maxPopulation,
		eventInterval;	
	private static double omega, 
		simulationTime,
		mutationInterval,
		reproductionInterval,
		deathInterval,
		timeInterval,
		currentTime;
		

	/**
	Initialisation
	*/

	private static void init() {
		eventQueue = new EventQueue();
		population = new Population(omega);
	}

	private static void addAllEvents(Individual individual, boolean initialPopulation) {
		if(initialPopulation) {	
			double mutationTime = RandomUtils.getRandomTime((1.0-Math.log(population.fitness(individual)))*mutationInterval);
			double reproductionTime = RandomUtils.getRandomTime((1.0-Math.log(population.fitness(individual)))*((double)population.size()/maxPopulation)*reproductionInterval);
			double deathTime = RandomUtils.getRandomTime((1.0-Math.log(1-population.fitness(individual)))*deathInterval);
			eventQueue.add(new Event(Event.MUTATION, mutationTime, individual));
			eventQueue.add(new Event(Event.REPRODUCTION, reproductionTime, individual));
			eventQueue.add(new Event(Event.DEATH, deathTime, individual));
		}
		else {
			double mutationTime = currentTime+RandomUtils.getRandomTime((1.0-Math.log(population.fitness(individual)))*mutationInterval);
			double reproductionTime = currentTime+RandomUtils.getRandomTime((1.0-Math.log(population.fitness(individual)))*((double)population.size()/maxPopulation)*reproductionInterval);
			double deathTime = currentTime+RandomUtils.getRandomTime((1.0-Math.log(1.0-population.fitness(individual)))*deathInterval);
			eventQueue.add(new Event(Event.MUTATION, mutationTime, individual));
			eventQueue.add(new Event(Event.REPRODUCTION, reproductionTime, individual));
			eventQueue.add(new Event(Event.DEATH, deathTime, individual));
		}
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
			addAllEvents(individual, true);
		}
	}

	/**
	Simulates a mutation event.
	*/

	private static void mutationEvent(Individual individual) {
		population.remove(individual);
		if(RandomUtils.getRandomEvent((Math.pow((1.0-population.fitness(individual)),2)))) {
			individual.mutate();
			if(RandomUtils.getRandomEvent(0.3)) {
				individual.mutate();
				if(RandomUtils.getRandomEvent(0.15)) {
					individual.mutate();
				}
			}
			double timeMutated = currentTime+RandomUtils.getRandomTime((1.0-Math.log(population.fitness(individual)))*mutationInterval);
			eventQueue.add(new Event(Event.MUTATION, timeMutated, individual));
		}
		else {
		double timeNotMutated = currentTime+RandomUtils.getRandomTime((1.0-Math.log(population.fitness(individual)))*mutationInterval/10);
		eventQueue.add(new Event(Event.MUTATION, timeNotMutated, individual));
		}
		population.add(individual); 
	} 

	/**
	Simulates a reproduction event
	*/

	private static void reproductionEvent(Individual individual) {
		Individual child = individual.reproduce();
		population.add(child);
		addAllEvents(child, false);
		double reproductionTime = currentTime+RandomUtils.getRandomTime((1.0-Math.log(population.fitness(individual)))*((double)population.size()/maxPopulation)*reproductionInterval);
		eventQueue.add(new Event(Event.REPRODUCTION, reproductionTime, individual));
	}

	/**
	Simulates a death event
	*/

	private static void deathEvent(Individual individual) {
		if(RandomUtils.getRandomEvent(1-Math.pow(population.fitness(individual),2)))
			population.remove(individual);
		else {
			double deathEventTime = currentTime+RandomUtils.getRandomTime((1.0-Math.log(1.0-population.fitness(individual)))*deathInterval);
			eventQueue.add(new Event(Event.DEATH, deathEventTime, individual));
		}
	}


	

	/**
	Method that takes an event as argument, and simulates the
	event corresponding to the argument.
	*/

	private static void simulateEvent(Event e) {
		switch(e.type()) {
			case Event.MUTATION:
				mutationEvent(e.individual());
				if(option == 3)
					System.out.println("Simulating: Event type: Mutation; time: " + currentTime);
				break;
			case Event.REPRODUCTION:
				reproductionEvent(e.individual());
				if(option == 3)
					System.out.println("Simulating: Event type: Reproduction; time: " + currentTime);
				break;
			case Event.DEATH:
				deathEvent(e.individual());
				if(option == 3)
					System.out.println("Simulating: Event type: Death; time: " + currentTime);
				break;
		}
	}

	/**
	Prints the cost and textual representation of the path it takes as argument.
	*/

	private static void printPath(City[] city) {
		for(City c : city)
			System.out.print(c.name() + "; ");

		double cost = city[city.length-1].distanceTo(city[0]);
	    for(int i = 1; i <city.length; i++) {
	    	cost += city[i-1].distanceTo(city[i]);
	    }
	    System.out.println("(cost: " + cost + ")");
	}

	/**
	Prints information about the simulation at a given time/number of observations.
	Used when mode 1 or 2 is chosen by user.
	*/

	private static void printObservation(int eventCount) {
		System.out.println("Observation");
		System.out.println("-----------");
		System.out.println("Current time: " + currentTime);
		System.out.println("Events simulated: " + eventCount);
		System.out.println("Population size: " + population.size());
		printPath(population.bestPath());
		System.out.println("");
	}


	/**
	Obtains parameters from user
	*/

	private static void selectParameters() {
		System.out.print("Size of the initial population: ");
		do {
			while(!reader.hasNextInt()) {
				reader.next(); //discards the input.
				System.out.print("Initial size must be a whole number.");
				System.out.println(" Please try again.");
				System.out.print("Size of the initial population: ");
			}
			initialPopulation = reader.nextInt();
			if(initialPopulation <= 0) {
				System.out.println("Initial size must be larger than 0. Please try again.");
				System.out.print("Size of the initial population: ");
			}
		} while(initialPopulation <= 0);

		System.out.print("Maximum size of the population: ");
		do {
			while(!reader.hasNextInt()) {
				reader.next(); //discards the input.
				System.out.print("Maximum size has to be a whole number.");
				System.out.println(" Please try again.");
				System.out.print("Size of the initial population: ");
			}
			maxPopulation = reader.nextInt();
			if(maxPopulation < initialPopulation) {
				System.out.println("Maximum size cannot be smaller than"
									+ " initial size, please try again.");
				System.out.print("Maximum size of the population: ");
			}
		} while(maxPopulation < initialPopulation);
		
		System.out.print("Value of the parameter omega: ");
		do {
			while(!reader.hasNextDouble()) {
				reader.next(); //discards the input.
				System.out.print("Omega must be a number.");
				System.out.println(" Please try again.");
				System.out.print("Value of the parameter omega: ");
			}
			omega = reader.nextDouble();
			if(omega <= 0) {
				System.out.print("Omega must be larger than 0. ");
				System.out.println("Please try again.");
				System.out.print("Value of the parameter omega: ");
			}
		} while(omega <= 0);

		System.out.print("Value of the parameter D: ");
		do {
			while(!reader.hasNextDouble()) {
				reader.next(); //discards the input.
				System.out.print("Parameter D must be a number.");
				System.out.println(" Please try again.");
				System.out.print("Size of the parameter D: ");
			}
			deathInterval = reader.nextDouble();
			if(deathInterval < 0) {
				System.out.print("Parameter D cannot be smaller than 0.");
				System.out.println(" Please try again.");
				System.out.print("Size of the parameter D: ");
			}
		} while(deathInterval < 0);
		
		System.out.print("Value of the parameter M: ");
		do {
			while(!reader.hasNextDouble()) {
				reader.next(); //discards the input.
				System.out.print("Parameter M must be a number.");
				System.out.println(" Please try again.");
				System.out.print("Size of the parameter M: ");
			}
			mutationInterval = reader.nextDouble();
			if(mutationInterval < 0) {
				System.out.print("Parameter M cannot be smaller than 0.");
				System.out.println(" Please try again.");
				System.out.print("Size of the parameter M: ");
			}
		} while(mutationInterval < 0);

		System.out.print("Value of the parameter R: ");
        do {
			while(!reader.hasNextDouble()) {
				reader.next(); //discards the input.
				System.out.print("Parameter R must be a number.");
				System.out.println(" Please try again.");
				System.out.print("Size of the parameter R: ");
			}
			reproductionInterval = reader.nextDouble();
			if(reproductionInterval < 0) {
				System.out.print("Parameter R cannot be smaller than 0.");
				System.out.println(" Please try again.");
				System.out.print("Size of the parameter R: ");
			}
		} while(reproductionInterval < 0);
		
		System.out.print("Time to run simulation: ");
		do {
			while(!reader.hasNextDouble()) {
				reader.next(); //discards the input.
				System.out.print("Simulation time must be a number.");
				System.out.println(" Please try again.");
				System.out.print("Time to run simulation: ");
			}
			simulationTime = reader.nextDouble();
			if(simulationTime <= 0) {
				System.out.print("Simulation time must be larger than 0.");
				System.out.println(" Please try again.");
				System.out.print("Time to run simulation: ");
			}
		} while(simulationTime <= 0);	
	}

	/**
	Method for the options of modes to run the simulator in
	*/

	public static void selectMode() {
		System.out.print("Simulation mode (1: every t units; 2: every n events; 3: verbose; 4: silent): ");
		do {
			while(!reader.hasNextInt()) {
				reader.next(); //discards the input.
				System.out.println("Invalid input. Please type in a correct mode.");
				System.out.print("Simulation mode (1: every t units; 2: every n events; 3: verbose; 4: silent): ");
			}
			option = reader.nextInt();
			if((option < 1) || (option > 4)) {
				System.out.println("Invalid input. Please type in a correct mode.");
				System.out.print("Simulation mode (1: every t units; 2: every n events; 3: verbose; 4: silent): ");
			}
		} while((option < 1) || (option > 4));	
		if(option == 1) {
			System.out.println("Interval between observations: ");
			do {
				while(!reader.hasNextDouble()) {
					reader.next(); 
					System.out.println("Invalid input. Interval must be a number larger than 0.");
					System.out.print("Interval between observations: ");
				}
				timeInterval = reader.nextDouble();
				if(timeInterval <= 0) {
					System.out.println("Invalid input. Interval must be a number larger than 0.");
					System.out.print("Interval between observations: ");
				}
			} while(timeInterval <= 0);	
		}
		if(option == 2) {
			System.out.println("Interval between observations: ");		
			do {
				while(!reader.hasNextInt()) {
					reader.next(); 
					System.out.println("Invalid input. Interval must be a whole number larger than 0.");
					System.out.print("Interval between observations: ");
				}
				eventInterval = reader.nextInt();
				if(eventInterval <= 0) {
					System.out.println("Invalid input. Interval must be a whole number larger than 0.");
					System.out.print("Interval between observations: ");
				}
			} while(eventInterval <= 0);	
		}
	}
	
	public static void main(String[] args) {
		reader = new Scanner(System.in);
		int eventCount = 0;
		int intervalAdded = eventInterval;
		double timeAdded = timeInterval;
		init();
		selectParameters();
		selectMode();
		fillPopulation();
		Event nextEvent = eventQueue.next(); //the first event.
		currentTime = nextEvent.time();

		while(eventQueue.hasNext() && (currentTime < simulationTime)) {
			if(population.size() > maxPopulation)
				population.epidemic();
			
			if(population.contains(nextEvent.individual())) {
				simulateEvent(nextEvent);
				eventCount++;
			}
			
			if((option == 1) && (currentTime >= timeAdded)) {
				printObservation(eventCount);
				timeAdded = timeAdded + timeInterval;
			}

			if((option == 2) && (eventCount >= intervalAdded)) {
				printObservation(eventCount);
				intervalAdded = intervalAdded + eventInterval;
			}

			nextEvent = eventQueue.next();
			currentTime = nextEvent.time();
		}
		if((option == 3) || (option == 4)) {
			printPath(population.bestPath());
		}
		
		reader.close();		
	}
}