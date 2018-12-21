import java.util.ArrayList;
public class Population{
	private ArrayList<Individual> population;
	private double omega;
	private double costMin = 0;
	private City[] bestPath;

	//Constructor
	public Population(double omega){
		this.population = new ArrayList<Individual>();
		this.omega = omega;
	}
 
	public Individual getIndividual(int i){
		return population.get(i);
	}

	// adds i to this population
	public  void add(Individual i){
		if(population.size() == 0){
			population.add(i);
			costMin = i.cost();
			bestPath = i.path();
		}
		else if(fitness(i) < fitness(population.get(0))){
			population.add(0,i);
			if(i.cost() < costMin){
				costMin = i.cost();
				bestPath = i.path();
			}
		}

		else{
			boolean isAdded = false;
			int j = 1;
			while(j < population.size() && isAdded == false){
				if(fitness(i) > fitness(population.get(j-1)) && fitness(i) < fitness(population.get(j))){
					population.add(j,i);
					if(population.get(j).cost() < costMin){
						costMin = population.get(j).cost();
						bestPath = population.get(j).path();
					}
					isAdded = true;
				}
				j++;
			}

		}
	}

	// returns true if this population contains i;
	public boolean contains(Individual i){
		return population.contains(i);
		//if(population.get(t).path() == i.path()){
	}

	//removes i from this population, if it is there;
	public void remove(Individual i){
		if(contains(i) == true){
			population.remove(population.indexOf(i));
		}
	}

	//returns the number of individuals currently in this population;
	public int size(){
		return population.size();
	}
	// models an epidemic in this population;
	public void epidemic(){
		for(int i = population.size(); i > population.size()-5; i--){
			population.remove(i);
		}
		for(int i = population.size()+5; i < population.size(); i++){
			if(RandomUtils.getRandomEvent(Math.pow(fitness(population.get(i)),2)) == true){
				population.remove(i);
			}
		}
	}

	//returns the fitness of individual i in the population;
	public double fitness(Individual i){
		return (omega + (Math.pow((costMin/i.cost()),2)))/(1+(2*omega));
	}

	//returns a copy of the best path ever present in an Individual in this population
	public City[] bestPath(){
		return bestPath;
	}
}
