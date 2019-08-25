package mvc.model;


import mvc.controller.MenuController;

import java.util.*;

public class Scheduler {

	
	
	public Node createOptimalSchedule(List<Task> tasks, int numProc, MenuController controller) {

		PriorityQueue<Node> openNodes = new PriorityQueue<>();
		Node node = new Node();
		node.setUnscheduledTasks(tasks);

		boolean algoNotFinished = true;
		double bestUpperBound = Double.POSITIVE_INFINITY;
		
		int startTime = 0;
		double upperBound;
		
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
			
			Node minNode = openNodes.poll();
			if(controller == null){
				//Do nothing
			} else {
				controller.updateGraph(node.getScheduledTasks());
			}
			
			if (minNode.getUnscheduledTasks().isEmpty()){
				return minNode;
			}

			node = new Node(minNode);

			closed.add(node.hashCode());
		}
		return null;
	}
	
	
	
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
