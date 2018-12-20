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

		private double fit(double omega, double costMin, Individual i){
			double fitness;
			fitness = (omega + (Math.pow((costMin/i.cost()),2)))/(1+(2*omega));
			return fitness;
		}


	// adds i to this population
	public  void add(Individual i){
		if(population.size() == 0){
			population.add(i);
			costMin = i.cost();
			bestPath = i.path();
		}
		else if(fit(omega, costMin, i) < fit(omega, costMin, population.get(0))){
			population.add(0,i);
			if(i.cost() < costMin){
				costMin = i.cost();
			}
		}

		else{
			boolean isAdded = false;
			int j = 0;
			while(j < population.size() && isAdded == false){
				if(fit(omega, costMin, i) > fit(omega, costMin, population.get(j-1)) && fit(omega, costMin, i) < fit(omega, costMin, population.get(j))){
					population.add(j,i);
					isAdded = true;
				}
			}
			j++;
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
			Math.pow(fit(population.get(i)),2) //somthing somthing
		}
	}

	//returns the fitness of individual i in the population;
	public double fitness(Individual i){
		return fit(omega, costMin, i);
	}

	//returns a copy of the best path ever present in an Individual in this population
	public City[] bestPath(){
		return bestPath;
	}
}
