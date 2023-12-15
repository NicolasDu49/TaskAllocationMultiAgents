import java.util.ArrayList;
import java.util.List;

public class Agent {
	
	public static int incrementalID = 0;

	private AgentType type;
	private int agentID;
	private ArrayList<Coalition> possibleCoalitions;
	private ArrayList<Coalition> coalitionCalculations;
	private ArrayList<Integer> hasApproched;
	private ArrayList<Integer> wasApproched;
	
	private int capacity;
	private int loadingCapacity;
	private boolean crossesLand;
	private boolean crossesWater;
	private int operatingCost;
	private int fuelCost;

	public Agent(AgentType type, 
			int capacity, 
			int loadingCapacity, 
			boolean crossesLand,
			boolean crossesWater,
			int operatingCost, 
			int fuelCost) 
	{
		Agent.incrementalID++;
		this.agentID = incrementalID;
		
		this.type = type;
		this.possibleCoalitions = new ArrayList<>();
		this.coalitionCalculations = new ArrayList<>();
		
		this.hasApproched = new ArrayList<>();
		this.wasApproched = new ArrayList<>();
		
		this.capacity = capacity;
		this.loadingCapacity = loadingCapacity;
		this.crossesLand = crossesLand;
		this.crossesWater = crossesWater;
		this.operatingCost = operatingCost;
		this.fuelCost = fuelCost;
	}

	public int getAgentID() {
		return agentID;
	}
	
	public AgentType getType() {
		return type;
	}
	
	public void hasApprochedAgent(int agentID) {
		this.hasApproched.add(agentID);
	}
	
	public void hasBeenApproched(int agentID) {
		this.wasApproched.add(agentID);
	}
	
	public boolean hasNegotiatedWith(int agentID) {
		return this.hasApproched.contains(agentID) || this.wasApproched.contains(agentID);
	}

	public int getCapacity() {
		return capacity;
	}

	public int getLoadingCapacity() {
		return loadingCapacity;
	}

	public int getOperatingCost() {
		return operatingCost;
	}
	
	public int getFuelCost() {
		return fuelCost;
	}
	
	public int assessCost(Task task) {
		return this.operatingCost + task.getTotalDistance() * this.fuelCost;
	}
	
	public boolean crossesLand() {
		return this.crossesLand;
	}
	
	public boolean crossesWater() {
		return this.crossesWater;
	}
	
	
	
	public ArrayList<Coalition> getCoalitionCalculations() {
		return coalitionCalculations;
	}
	
	public void addCoalitionCalculation(Coalition coalition) {
		this.coalitionCalculations.add(coalition);
	}
	
	public void addCoalition(Coalition coalition) {
        this.possibleCoalitions.add(coalition);
    }
	
	
	
	public List<Coalition> getPossibleCoalitions() {
		return possibleCoalitions;
	}
	
	public void generatePossibleCoalitions(List<Agent> agents, int size, int start, Coalition currentCoalition) {
		if (!currentCoalition.getAgentList().contains(this)) {
	        currentCoalition.addAgent(this);
	    }

	    for (int i = start; i <= agents.size(); i++) {
	        if (agents.get(i-1) != this) {
	            Coalition newCoalition = new Coalition();
	        	newCoalition.addAgents(currentCoalition.getAgentList());
	        	currentCoalition.addAgent(agents.get(i-1));
	            this.generatePossibleCoalitions(agents, size, i+1, newCoalition);
	        }
	        
	        if (currentCoalition.getAgentList().size() == size) {
		    	Coalition newCoalition = new Coalition();
	        	newCoalition.addAgents(currentCoalition.getAgentList());
		        this.addCoalition(newCoalition);
		        return;
		    }
	    }
    }
	
	public void removePossibleCoalitions(Agent agent) {
		ArrayList<Coalition> commonCoalitions = this.getCommonCoalitions(agent.getAgentID());
		
		for (Coalition commonCoalition : commonCoalitions) {
			int index = -1;
			
			for (Coalition possibleCoalition : this.getPossibleCoalitions()) {
				if (possibleCoalition.getCoalitionID() == commonCoalition.getCoalitionID()) {
					index = this.getPossibleCoalitions().indexOf(possibleCoalition);
				}
			}
			if (index > -1) {
				this.getPossibleCoalitions().remove(index);
			}
		}
	}
	
	public ArrayList<Coalition> getCommonCoalitions(int agentcID) {
		ArrayList<Coalition> commonCoalitions = new ArrayList<>();
		
		for (Coalition coalition : this.getPossibleCoalitions()) {
			for (Agent agentc : coalition.getAgentList()) {
				if (agentc.getAgentID() == agentcID) {
					commonCoalitions.add(coalition);
				}
			}
		}
		
		return commonCoalitions;
	}
	
	public void commitToCalculus(ArrayList<Coalition> commonCoalitions) {
		for (Coalition commonCoalition : commonCoalitions) {
			boolean contains = false;
			
			for (Coalition committedCoalition : this.getCoalitionCalculations()) {
				if (committedCoalition.getCoalitionID() == commonCoalition.getCoalitionID()) {
					contains = true;
				}
			}
			if (!contains) {
				this.addCoalitionCalculation(commonCoalition);
			}
		}
	}
	
	public void resetCalculations() {
		for (Coalition coalition : this.coalitionCalculations) {
			coalition.emptyDoableTasks();
			coalition.deassignTask();
			coalition.setValue(0);
			coalition.resetCapacities();
		}
	}
	
	public String typeToString() {
		String string = "";
		
		switch(this.type) {
		case TRUCK:
			string = "Camion";
			break;
			
		case CARGO:
			string = "Cargo";
			break;
			
		case PLANE:
			string = "Avion";
			break;
			
		case LIFT_TRUCK:
			string = "Monte-Charge";
			break;
			
		case CRANE:
			string = "Grue";
			break;
			
		default:
		}
		
		return string + "(s)";
	}
	
	public String toString() {
		String string = "----- Agent n°" + this.getAgentID() + "-----\n";
		
		string += "Possible Coalitions left: " + this.getPossibleCoalitions().size() + "\n";
		for (Coalition coalition : this.getPossibleCoalitions()) {
			string += coalition.toString();
		}
		
		string += "Commited Coalition calculations: \n";
		for (Coalition coalition : this.getCoalitionCalculations()) {
			string += coalition.toString();
		}
		
		string += "---------------------\n";
		return string;
	}
}
