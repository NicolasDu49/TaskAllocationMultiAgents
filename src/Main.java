import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<Agent> agents = new ArrayList<>();		
		ArrayList<Task> tasks = new ArrayList<>();
		
		// =============================== TEST 1 =================================== //
//		for (int i = 0; i < 3; i++) {
//			agents.add(new Agent(AgentType.TRUCK, 3000, 0, true, false, 500, 1));
//		}
//		for (int i = 0; i < 2; i++) {
//			agents.add(new Agent(AgentType.LIFT_TRUCK, 0, 10000, false, false, 2000, 0));
//		}
//		
//		tasks.add(new Task("Ottawa", 2000, new int[] {450, 0}, new int[] {1, 0}, 30000));
//		tasks.add(new Task("Rio De Janeiro", 10000, new int[] {0, 9000}, new int[] {0, 1}, 70000));
//		tasks.add(new Task("Vancouver", 4000, new int[] {4800, 0}, new int[] {1, 0}, 30000));
//		
//		CoalitionMaker maker = new CoalitionMaker(agents, tasks, 3);
//		maker.makeCoalitions();
		// ========================================================================== //
		
		
		
		// =============================== TEST 2 =================================== //
		for (int i = 0; i < 12; i++) {
			agents.add(new Agent(AgentType.TRUCK, 2000, 0, true, false, 500, 1));
		}
		for (int i = 0; i < 2; i++) {
			agents.add(new Agent(AgentType.CARGO, 20000, 0, false, true, 1000, 4));
		}
		for (int i = 0; i < 1; i++) {
			agents.add(new Agent(AgentType.PLANE, 12000, 0, true, true, 9000, 2));
		}
		for (int i = 0; i < 8; i++) {
			agents.add(new Agent(AgentType.LIFT_TRUCK, 0, 10000, false, false, 2000, 0));
		}
		for (int i = 0; i < 2; i++) {
			agents.add(new Agent(AgentType.CRANE, 0, 20000, false, false, 1000, 0));
		}
		
		tasks.add(new Task("Ottawa", 2000, new int[] {450, 0}, new int[] {1, 0}, 12000));
		tasks.add(new Task("Mexico", 6000, new int[] {400, 4800}, new int[] {1, 1}, 50000));
		tasks.add(new Task("Vancouver", 6500, new int[] {4800, 0}, new int[] {1, 0}, 30000));
		tasks.add(new Task("Washington D.C", 2000, new int[] {1200, 0}, new int[] {1, 0}, 20000));
		tasks.add(new Task("San Francisco", 6000, new int[] {5000, 0}, new int[] {1, 0}, 25000));
		tasks.add(new Task("Brasilia", 6000, new int[] {2000, 6800}, new int[] {1, 1}, 40000));
		tasks.add(new Task("Rio De Janeiro", 10000, new int[] {0, 9000}, new int[] {0, 1}, 40000));
		
		// ---------------------------------------------------------------- //
		CoalitionMaker maker = new CoalitionMaker(agents, tasks, 4);
//		// ---------------------------------------------------------------- //
//		//CoalitionMaker maker = new CoalitionMaker(agents, tasks, 5);
//		// ---------------------------------------------------------------- //
		maker.makeCoalitions();
		// ========================================================================== //
	}

}
