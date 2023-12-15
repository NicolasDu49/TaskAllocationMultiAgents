import java.util.ArrayList;

public class CoalitionMaker {

	private ArrayList<Agent> agents;
	private ArrayList<Task> tasks;
	private ArrayList<Coalition> coalitions;
	
	private int maxSize;
	
	public CoalitionMaker(ArrayList<Agent> agents, ArrayList<Task> tasks, int maxSize) {
		this.agents = agents;
		this.tasks = tasks;
		this.coalitions = new ArrayList<>();
		this.maxSize = maxSize;
	}
	
	public void makeCoalitions() {
		
		// Generate every possible coalitions for each agent, only coalitions which they belong to
		for (Agent agent : agents) {
	        for (int size = 2; size <= this.maxSize; size++) {
	        	Coalition coalition = new Coalition();
	            agent.generatePossibleCoalitions(this.agents, size, 1, coalition);
	        }
	    }
		
		// Log
		for (Agent agent : agents) {
			System.out.println(agent);
		}
		
		// Coalition value Calculus repartition
		for (Agent agent : agents) {
			while (agent.getPossibleCoalitions().size() > 0) {
				Coalition consideredCoalition = agent.getPossibleCoalitions().get(0);
				
				for (Agent agentc : consideredCoalition.getAgentList()) {
					if (agent.getAgentID() != agentc.getAgentID()
							&& !agent.hasNegotiatedWith(agentc.getAgentID())) 
					{
						agent.hasApprochedAgent(agentc.getAgentID());
						agentc.hasBeenApproched(agent.getAgentID());
						
						ArrayList<Coalition> commonCoalitions = agent.getCommonCoalitions(agentc.getAgentID());
						agent.commitToCalculus(commonCoalitions);
						agentc.removePossibleCoalitions(agent);
					}
				}
				agent.getPossibleCoalitions().remove(consideredCoalition);
			}
		}
		
		// Log
		for (Agent agent : agents) {
			System.out.println(agent);
		}
		int totalCoalitions = 0;
		for (Agent agent : agents) {
			System.out.println("A" + agent.getAgentID() + ": " + agent.getCoalitionCalculations().size() + " calculations to do;");
			totalCoalitions += agent.getCoalitionCalculations().size();
		}
		System.out.println("__________________________________________________");
		System.out.println("Total amount of Coalition Calculations: " + totalCoalitions + "\n");
		
		
		// Calculate Coalition Values and form final Coalitions, 
		// repeat whole process until no task left, or all Agents grouped, or no possible valuable coalition left
		ArrayList<Agent> activeAgents = new ArrayList<>();
		activeAgents.addAll(this.agents);
		int tasksLeft = tasks.size();
		boolean isValuableCoalition = true;
		
		//for (int i = 0; i < 2; i++) {
		while(activeAgents.size() > 0 && tasksLeft > 0 && isValuableCoalition) {
			// Calculate Coalition Values based on a given Task, keep the best Value for each Coalition
			for (Agent agent : activeAgents) {
				for (Coalition coalition : agent.getCoalitionCalculations()) {
					for (Task consideredTask : tasks) {
						if (!consideredTask.isAssigned()) {
							coalition.calculateCoalitionalCapacities(consideredTask);
							if (coalition.canDoTask(consideredTask) && !coalition.isUselessAgents(consideredTask)) {
								coalition.addDoableTask(consideredTask);
							}
						}
					}
					
					for (Task doableTask : coalition.getDoableTasks()) {
						int value = coalition.calculateCoalitionValue(doableTask);
						if (value > coalition.getValue()) {
							coalition.setValue(value);
							coalition.assignTask(doableTask);
						}
					}
				}
				
				// Log
				System.out.println("Best Coalition values for Agent n°" + agent.getAgentID() + ":");
				for (Coalition coalition : agent.getCoalitionCalculations()) {
					// coalition.printCapacity();
					if (coalition.getAssignedTask() != null) {
						System.out.println("Coalition n°" + coalition.getCoalitionID() + ": "
								+ "Task n°" + coalition.getAssignedTask().getTaskID() + " = " + coalition.getValue() + "\n");
					}
				}
			}
			
			// Decide which Coalition shall be formed (the one with the smaller Coalitional Weight)
			Coalition chosenCoalition = null;
			double minWeight = 0.0;
			
			for (Agent agent : activeAgents) {
				for (Coalition coalition : agent.getCoalitionCalculations()) {
					if (coalition.getAssignedTask() != null) {
						double weight = (double) ((1.0 / coalition.getValue()) / coalition.getAgentList().size());
						
						// Log
						System.out.println("Coalition n°" + coalition.getCoalitionID()
								+ " Task n°" + coalition.getAssignedTask().getTaskID() + " weight = " + weight);
						
						if (weight < minWeight || minWeight == 0) {
							minWeight = weight;
							chosenCoalition = coalition;
						}
					}
				}
			}
			if (minWeight <= 0.0) {
				isValuableCoalition = false;
			}			
			else {
				// Log
				System.out.println("=============== Chosen Coalition =================");
				System.out.println(chosenCoalition);
				System.out.println("For Task n°" + chosenCoalition.getAssignedTask().getTaskID());
				chosenCoalition.printCapacity();
				System.out.println("Weight = " + minWeight);
				System.out.println("==================================================\n");
				
				
				// Form Coalition, remove grouped Agents from active agents, and inform remaining active agents
				this.coalitions.add(chosenCoalition);
				chosenCoalition.getAssignedTask().assign(true);
				
				for (Task task : tasks) {
					if (task.isAssigned()) {
						tasksLeft--;
					}
				}
				
				for (Agent groupedAgent : chosenCoalition.getAgentList()) {			
					activeAgents.remove(groupedAgent);
				}
				
				for (Agent activeAgent : activeAgents) {
					for (Agent groupedAgent : chosenCoalition.getAgentList()) {
						ArrayList<Coalition> tmpList = new ArrayList<>();
						tmpList.addAll(activeAgent.getCoalitionCalculations());
						for (Coalition coalition : tmpList) {
							if (coalition.getAgentList().contains(groupedAgent)) {
								activeAgent.getCoalitionCalculations().remove(coalition);
							}
						}
					}
					activeAgent.resetCalculations();
				}
			}
		}
		
		printResult();
	}
	
	public void printResult() {
		System.out.println("============ Final Coalition Configuration: ==============\n");
		System.out.println("Tasks left unassigned:");
		for (Task task : tasks) {
			if (!task.isAssigned()) {
				task.printCapacity();
			}
		}
		System.out.println("//////////////////////////////////////////////////////////");
		for (Coalition coalition : coalitions) {
			coalition.printCapacity();
			System.out.println("                   ------------------                     ");
			
		}
		int score = 0;
		for (Coalition coalition : coalitions) {
			score += coalition.getValue();
		}
		System.out.println("FINAL SCORE: " + score);
		System.out.println("==========================================================");
	}
}
