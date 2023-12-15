import java.util.ArrayList;

public class Coalition {
	
	public static int incrementalID = 0;

	private int coalitionID;
	
	private int totalCapacity;
	private int totalLoadingCapacity;
	private int[] coveredDistance; // [Terre, Mer]
	private int totalCost;
	
	private ArrayList<Agent> agents;
	private ArrayList<Task> doableTasks;
	private Task assignedTask;
	private int coalitionalValue;
	
	public Coalition() {
		Coalition.incrementalID++;
		this.coalitionID = incrementalID;

		this.setTotalCapacity(0);
		this.setTotalLoadingCapacity(0);
		this.setTotalCost(0);
		
		this.coveredDistance = new int[2];
		this.coveredDistance[0] = 0;
		this.coveredDistance[1] = 0;
		
		this.agents = new ArrayList<>();
		this.doableTasks = new ArrayList<>();
		this.assignedTask = null;
		this.coalitionalValue = 0;
	}
	
	public int getCoalitionID() {
		return coalitionID;
	}
	
	public void addAgent(Agent agent) {
		this.agents.add(agent);
	}
	
	public void addAgents(ArrayList<Agent> agentList) {
		this.agents.addAll(agentList);
	}
	
	public ArrayList<Agent> getAgentList() {
		return this.agents;
	}
	
	public void addDoableTask(Task task) {
		this.doableTasks.add(task);
	}
	
	public void emptyDoableTasks() {
		this.doableTasks = new ArrayList<>();
	}
	
	public ArrayList<Task> getDoableTasks() {
		return this.doableTasks;
	}

	public int getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(int totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public int getTotalLoadingCapacity() {
		return totalLoadingCapacity;
	}

	public void setTotalLoadingCapacity(int totalLoadingCapacity) {
		this.totalLoadingCapacity = totalLoadingCapacity;
	}
	
	public void setCoveredDistance(int movementType, int distance) {
		this.coveredDistance[movementType] = distance;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
	
	public void assignTask(Task task) {
		this.assignedTask = task;
	}
	
	public void deassignTask() {
		this.assignedTask = null;
	}
	
	public Task getAssignedTask() {
		return this.assignedTask;
	}
	
	public int getValue() {
		return this.coalitionalValue;
	}
	
	public void setValue(int value) {
		this.coalitionalValue = value;
	}
	
	public void resetCapacities() {
		this.setTotalCapacity(0);
		this.setTotalLoadingCapacity(0);
		this.setTotalCost(0);
		
		this.coveredDistance = new int[2];
		this.coveredDistance[0] = 0;
		this.coveredDistance[1] = 0;
	}
	
	public void calculateCoalitionalCapacities(Task task) {
		int quantityOnLand = 0;
		int quantityOnWater = 0;
		
		this.resetCapacities();
		
		for (Agent agent : this.agents) {			
			this.totalCapacity += agent.getCapacity();
			this.totalLoadingCapacity += agent.getLoadingCapacity();
			this.totalCost += agent.assessCost(task);
			
			if (agent.crossesLand()) {
				quantityOnLand += agent.getCapacity();
			}
			if (agent.crossesWater()) {
				quantityOnWater += agent.getCapacity();
			}
		}
		
		if (quantityOnLand >= task.getOrderQuantity()) {
			this.setCoveredDistance(0, task.getDistance(0));
		}
		if (quantityOnWater >= task.getOrderQuantity()) {
			this.setCoveredDistance(1, task.getDistance(1));
		}
	}
	
	public boolean canDoTask(Task task) {
		return this.totalCapacity >= task.getTotalCapacity()
				&& this.totalLoadingCapacity >= task.getTotalLoadingCapacity()
				&& this.coveredDistance[0] + this.coveredDistance[1] >= task.getTotalDistance()
				&& this.totalCost < task.getIncome();
	}
	
	public int calculateCoalitionValue(Task task) {
		this.calculateCoalitionalCapacities(task);
		int value = 0;
		
		value += task.getIncome() - this.totalCost;
		value -= (this.totalCapacity - task.getTotalCapacity()) * (this.totalCapacity / this.totalCost);
		value -= (this.totalLoadingCapacity - task.getTotalLoadingCapacity()) * (this.totalLoadingCapacity / this.totalCost);
		
		return value;
	}
	
	public boolean isUselessAgents(Task task) {
		boolean isUseless = false;
		
		Coalition tmpCoal = new Coalition();
		tmpCoal.addAgents(agents);
		
		for (Agent agent : this.agents) {
			tmpCoal.getAgentList().remove(agent);
			tmpCoal.calculateCoalitionalCapacities(task);
			if (tmpCoal.canDoTask(task)) {
				isUseless = true;
			}
		}
		
		return isUseless;
	}
	
	public void printCapacity() {
		System.out.println("Capacities of Coalition n°" + this.coalitionID + ":");
		System.out.println("For Task \"" + this.assignedTask.getName() + "\":");
		//this.assignedTask.printCapacity();
		this.printComposition();
		
		System.out.println("- Total Capacity:   " + this.totalCapacity);
		System.out.println("- Total Loading:    " + this.totalLoadingCapacity);
		System.out.println("- Covered Distance: " + (this.coveredDistance[0] + this.coveredDistance[1]));
		System.out.println("- Total Cost:       " + this.totalCost);
		System.out.println("- Score: " + this.coalitionalValue + "\n");
	}
	
	public void printComposition() {
		int nbTrucks = 0;
		int nbCargos = 0;
		int nbPlanes = 0;
		int nbLifts = 0;
		int nbCranes = 0;
		
		for (Agent agent : this.agents) {
			switch(agent.getType()) {
			case TRUCK:
				nbTrucks++;
				break;
				
			case CARGO:
				nbCargos++;
				break;
				
			case PLANE:
				nbPlanes++;
				break;
				
			case LIFT_TRUCK:
				nbLifts++;
				break;
				
			case CRANE:
				nbCranes++;
				break;
				
			default:
			}
		}
		
		System.out.println("Composition:");
		System.out.println(nbTrucks + "x Camion(s), " + nbCargos + "x Cargo(s), " + nbPlanes + "x Avion(s),\n"
				+ nbLifts + "x Monte-Charge(s)" + nbCranes + "x Grue(s)");
	}
	
	public String toString() {
		String string = " - Coalition n°" + this.coalitionID + ": \n"
				+ "    -> ";
		
		for (Agent agent : this.getAgentList()) {
			string += agent.getAgentID() + ";";
		}
		
		string += "\n";
		return string;
	}
}
