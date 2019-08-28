package mvc.model;


import mvc.controller.MenuController;

import java.util.*;

/**
 * The scheduler class is responsable for creating an optimal scheudle (Node).
 */
public class Scheduler {
	/**
     	* Create an optimal schedule using branch & bound A* algorithm.
	* Takes in a list of unschedulded tasks and the number of processors to create an optimal
	* schedule based off these two inputs. A menucontroller is passed in to update the frontend
	* during runtime.
	* Further details on how the algorithm works can be found in git wiki
    	*/
	public Node createOptimalSchedule(List<Task> tasks, int numProc, MenuController controller) {

		// queue of nodes to be explored
		PriorityQueue<Node> openNodes = new PriorityQueue<>();
		Node node = new Node(); //create root node
		node.setUnscheduledTasks(tasks);

		boolean algoNotFinished = true;
		double bestUpperBound = Double.POSITIVE_INFINITY;
		
		int startTime = 0;
		double upperBound;
		
		// create a hashset of nodes that have already been visted
		HashSet<Integer> closed = new HashSet<Integer>();
		
		while (algoNotFinished) {
			for (Task t : new ArrayList<>(node.getUnscheduledTasks())) {
				for (int i = 1; i < numProc + 1; i++) {
					Node childNode = new Node(node);

					if (containsParents(node, t) || t.getParentTasks().isEmpty()) {
						childNode.removeUnscheduledTask(t);
						t = new Task(t);
						t.setProcessor(i);
						startTime = getStartTime(i, t, childNode);
						t.setStatus(startTime + t.getWeight());
						t.setStartTime(startTime);
						childNode.addScheduledTask(t);
						childNode.addTasksToProcessor(t, i);

						upperBound = Math.max(childNode.getLastBL(), t.getStartTime() + getComputationalBottomLevel(new Node(childNode), t));

						childNode.setLastBL(upperBound);

						childNode.setUpperBound(upperBound);

						if (upperBound >= bestUpperBound) {
							continue;
						}
						if(closed.contains(childNode.hashCode())){
							continue;
						}

						if (upperBound < bestUpperBound && childNode.getUnscheduledTasks().isEmpty()) {
							bestUpperBound = upperBound;
						}

						openNodes.add(childNode);

					}
				}
			}
			
			Node minNode = openNodes.poll(); // get head of queue
			
			if(controller == null){
				//Do nothing
			} else {
				controller.updateGraph(node.getScheduledTasks());
			}
			
			if (minNode.getUnscheduledTasks().isEmpty()){
				return minNode;
			}

			node = new Node(minNode);

			closed.add(node.hashCode()); // node has been visited 
		}
		return null;
	}
	
	
	/**
     	*  Computes the computational bottom level of the current schedule and the task to be added.
	*  The computational bottom level is the maximum potentional path through the schedule.
    	*/
	public int getComputationalBottomLevel(Node input, Task added) {
		if (added.getSubTasks().size() > 0) {
			int max = 0;
			for (Integer i : added.getSubTasks().keySet()) {
				Task t = added.getSubTasks().get(i);
				int current = (int) (getComputationalBottomLevel(input, t));
				if (max < current) {
					max = current;
				}
			}
			return max + (int) added.getWeight();
		} else {
			return (int) added.getWeight();
		}
	}

	/**
	* Helper function which checks if the parents of a specific task is scheduled in a schedule
	* If a task has no parents (i.e. is a root node), the function also returns true.
	*/
	private boolean containsParents(Node node, Task t) {

		List<Task> scheduled = node.getScheduledTasks();

		for (Task parent : t.getParentTasks()) {
			boolean found = false;
			for (Task tScheduled : scheduled) {
				if (tScheduled.getNodeNumber() == parent.getNodeNumber()) {
					found = true;
					break;
				}
			}

			if (!found) {
				return false;
			}
		}
		return true;
	}


	/**
	* getStartTime takes in a task that wants to be scheduled on a specfic processor and the schedule that
	* it wants to be scheduled on. This function will calculate the start time of this for the schedule
	*/
	private int getStartTime(int proc, Task task, Node node) {
		int comCost = 0;
		int endTime = 0;
		
		List<Integer> allStartTimes = new ArrayList<> ();
		
		allStartTimes.add(0);
		for (Task t : task.getParentTasks()) {
			t = node.getTaskByNumber(t.getNodeNumber());
			
			if (t.getProcessor() != proc) {
				comCost = t.getSubTaskArcs().get(task.getNodeNumber());
				endTime = t.getStatus();	
			}
			
			allStartTimes.add(comCost + endTime);
		}
		
		for (Task t : node.getTasksForProcessor(proc)) {
			endTime = t.getStatus();
			allStartTimes.add(endTime);
		}
		
		return Collections.max(allStartTimes);
	}
}
