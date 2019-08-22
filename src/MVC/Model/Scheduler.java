package  MVC.Model;

import java.util.*;

public class Scheduler {

	public Schedule createBasicSchedule(List<Task> tasks, int processor) {

		Schedule schedule = new Schedule();
		Queue<Task> taskQueue = new LinkedList<>();
		List<Task> rootTasks = getRootTasks(tasks);

		Task currentTask;
		Set<Task> subTasks;

		taskQueue.addAll(rootTasks);
		int startTime = 0;

		while (!(taskQueue.isEmpty())) {
			currentTask = taskQueue.element();
			subTasks = currentTask.getSubTasks().keySet();
			taskQueue.removeAll(subTasks);   //remove duplicate subtasks
			taskQueue.addAll(subTasks);
			taskQueue.remove(currentTask);

			currentTask.setProcessor(processor);
			schedule.addTask(currentTask, startTime);

			startTime = startTime + currentTask.getWeight();
		}

		return schedule;

	}

	private List<Task> getRootTasks(List<Task> tasks) {
		List<Task> rootTasks = new ArrayList<>();

		// if a task has no parent it must be a root 
		for (Task t : tasks) {
			if (t.getParentTasks().isEmpty()) {
				rootTasks.add(t);
			}
		}

		return rootTasks;
	}

	public Schedule createOptimalSchedule (List<Task> tasks, int numberOfProcessors) {

		boolean notFinished = true;

		double lowerBound = 0;

		List<Node> openNodes = new ArrayList<>();

		// Set up a root node
		Node node = new Node();
		node.setUnscheduledTasks(tasks);
		node.addOpenNode(node);

		Node bestNode = branchFromNode(node, numberOfProcessors);




		// Need to convert bestNode to a schedule

		return null;
	}


	public Node branchFromNode (Node node, int numProc){
        double bestUpperBound = Double.POSITIVE_INFINITY;
	    int startTime = 0;
	    double upperBound = Double.POSITIVE_INFINITY;
	    double lowerBound = 0;
        for (Task t : node.getUnscheduledTasks()) {
            for (int i = 1; i <= numProc; i++) {
                Node childNode = new Node(node);
                if (node.getAllScheduledTasks().containsAll(t.getParentTasks())|| t.getParentTasks().isEmpty()) {
                    startTime = getStartTime(i, t, childNode);
                    childNode.addScheduledTask(i, t);
                    t.setProcessor(startTime);


                    childNode.removeUnscheduledTask(t);
                    childNode.addComputationTime(t, startTime + t.getWeight());



                    upperBound = calcUpperBound(childNode, numProc);
                    childNode.setUpperBound(upperBound);
                    lowerBound = calcLowerBound(childNode, numProc);
                    childNode.setLowerBound(lowerBound);

                    if (upperBound < bestUpperBound) {
                        bestUpperBound = upperBound;
                        for (Node n : node.getOpenNodes()) {
                            if (n.getUpperBound() > bestUpperBound) {
                                node.removeOpenNode(n);
                            }
                        }
                    }


                    if (childNode.getLowerBound() > childNode.getUpperBound()) {
                        node.removeOpenNode(childNode);
                    } else {
                        node.addOpenNode(childNode);
                    }

                }
            }
        }
        Node minNode = new Node(node);
        for (Node n : node.getOpenNodes()) {
            if (n.getLowerBound() < minNode.getLowerBound()) {
                minNode = n;
            }
        }

        node = minNode;

        if (node.getLowerBound() == node.getUpperBound() && node.getUnscheduledTasks().isEmpty()) {
            return node;
        } else {
            return branchFromNode(node, numProc);
        }

    }



	private int calcMakeSpan(Node node) {
		int makeSpan = 0;
        int temptCompTime = 0;
		Set<Task> tasks = node.getComputationTimes().keySet();

		for (Task t : tasks) {
		    temptCompTime = node.getComputationTimes().get(t);
		    if (temptCompTime > makeSpan) {
		        makeSpan = temptCompTime;
            }
        }

		return makeSpan;
	}



	private double calcLowerBound(Node node, int numProc) {

		double makeSpan = 0;
		int sum = 0;
        List<Task> unscheduledTasks = node.getUnscheduledTasks();
        for (Task t : unscheduledTasks) {
            sum += t.getWeight();
        }

        makeSpan = calcMakeSpan(node);
        makeSpan += (sum / numProc);

		return makeSpan;

	}

//	private double calcUpperBound(Node node, int numProc) {
//		int makeSpan = 0;
//		List<Task> unscheduledTasks = node.getUnscheduledTasks();
//		Task task = null;
//		int startTime = 0;
//
//		while (!unscheduledTasks.isEmpty()) {
//		    for (int i = 1; i <= numProc; i++) {
//		        // Find the min cost task for which its parent tasks are all scheduled
//		        task = pickGreedyTask(node);
//
//		        startTime = getStartTime(i, task, node);
//
//		        // Set task to processor i
//		        task.setProcessor(i);
//		        node.addScheduledTask(i, task);
//		        node.addComputationTime(task, startTime + task.getWeight());
//
//
//            }
//            node.removeUnscheduledTask(task);
//		    unscheduledTasks = node.getUnscheduledTasks();
//        }
//
//        makeSpan = calcMakeSpan(node)
//
//		return makeSpan;
//	}

    private double calcUpperBound(Node node, int numProc) {
        int makeSpan = 0;
        List<Task> unscheduledTasks = node.getUnscheduledTasks();


        while (!unscheduledTasks.isEmpty()) {
            Task task = null;
            double minStartTime = Double.POSITIVE_INFINITY;

            // Find those tasks that all their predecessors are already scheduled
            for (Task t : node.getUnscheduledTasks()) {
                if (node.getScheduledTasks().keySet().contains(t.getParentTasks()) || t.getParentTasks().isEmpty()) {
                    task = t;
                    break;
                }
            }
            int tempStartTime = 0;
            int bestProc = 1;
            for (int i = 1; i <= numProc; i++) {
                tempStartTime = getStartTime(i, task, node);

                if (minStartTime > tempStartTime) {
                    minStartTime = tempStartTime;
                    bestProc = i;
                }
            }

            node.addScheduledTask(bestProc, task);
            task.setProcessor(bestProc);
            node.removeUnscheduledTask(task);
            node.addComputationTime(task, (int) minStartTime + task.getWeight());
            unscheduledTasks = node.getUnscheduledTasks();
        }

        makeSpan = calcMakeSpan(node);

        return makeSpan;
    }

    private int getStartTime(int proc, Task task, Node node) {
        int startTime = 0;

        for (Task t : task.getParentTasks()) {
            if (t.getProcessor() == proc) {
                if (startTime > node.getComputationTimes().get(t)) {
                    startTime = node.getComputationTimes().get(t);
                }
            } else {
                if (startTime > node.getComputationTimes().get(t) + t.getSubTasks().get(task)) {
                    startTime = node.getComputationTimes().get(t) + t.getSubTasks().get(task);
                }
            }
        }
        return startTime;
    }


    private Task pickGreedyTask(Node node) {
        double minWeight = Double.POSITIVE_INFINITY;
		List<Task> readyTaskList = new ArrayList<>();
		Task minTask = null;

		// Find those tasks that all their predecessors are already scheduled
		for (Task t : node.getUnscheduledTasks()) {
			if (node.getScheduledTasks().keySet().contains(t.getParentTasks())) {
				readyTaskList.add(t);
			}
		}
				
		for (Task t : readyTaskList) {
		    if (t.getWeight() < minWeight) {
					minTask = t;
					minWeight = t.getWeight();
		    }

		}
		
		return minTask;
		
		
	}



}
