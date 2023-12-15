
public class Task {

	public static int incrementalID = 0;
	
	private String name;
	private int taskID;
	private boolean isAssigned;
	private int orderQuantity;
	
	private int totalCapacity;
	private int totalLoadingCapacity;
	private int[] totalDistance; // [Terre, Mer]
	private int income;
	
	private int[] totalRouteStages; // [Terre, Mer]
	
	public Task(String name, 
			int orderQuantity, 
			int[] totalDistance, 
			int[] totalRouteStages,
			int income) 
	{
		Task.incrementalID++;
		this.taskID = incrementalID;
		this.isAssigned = false;
		
		this.name = name;
		this.orderQuantity = orderQuantity;
		
		this.totalRouteStages = totalRouteStages;
		
		this.totalCapacity = orderQuantity * (this.totalRouteStages[0] + this.totalRouteStages[1]);
		this.totalLoadingCapacity = (this.totalRouteStages[0] + this.totalRouteStages[1] + 1) * orderQuantity;
		this.totalDistance = totalDistance;
		this.income = income;
	}
	
	public int getTaskID() {
		return taskID;
	}

	public String getName() {
		return name;
	}
	
	public boolean isAssigned() {
		return this.isAssigned;
	}
	
	public void assign(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}
	
	public int getOrderQuantity() {
		return orderQuantity;
	}

	public int getTotalCapacity() {
		return totalCapacity;
	}
	
	public int getTotalLoadingCapacity() {
		return totalLoadingCapacity;
	}

	public int getTotalDistance() {
		return totalDistance[0] + totalDistance[1];
	}
	public int getDistance(int movementType) {
		return totalDistance[movementType];
	}
	
	public int getTotalRouteStages(int movementType) {
		return totalRouteStages[movementType];
	}

	public int getIncome() {
		return income;
	}
	
	public void printCapacity() {
		System.out.println("Capacities of Task n°" + this.taskID + ":");
		System.out.println("- Name:              " + this.name);
		System.out.println("- Total Capacity:    " + this.totalCapacity);
		System.out.println("- Total Loading:     " + this.totalLoadingCapacity);
		System.out.println("- Distance to cover: " + this.getTotalDistance());
		System.out.println("- Income:            " + this.income + "\n");
	}
}
