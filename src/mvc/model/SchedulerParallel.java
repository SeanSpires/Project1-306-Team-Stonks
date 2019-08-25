package mvc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerParallel {
	
	private PriorityBlockingQueue<Node> openNodes;
	private ForkJoinPool forkJoinPool;
	
	private AtomicLong bestLowerBound;
	private AtomicLong bestUpperBound;	
	
	
	public Node createOptimalSchedule(List<Task> tasks, int numProc, int numCores) {
		
		this.openNodes = new PriorityBlockingQueue<>();
		this.forkJoinPool = new ForkJoinPool(numCores);
		this.bestLowerBound = new AtomicLong();
		this.bestUpperBound = new AtomicLong();
		
		Node n = new Node();
		n.setUnscheduledTasks(tasks);
		openNodes.add(n);
		ArrayList<SchedulerParallelTask> schedulerTasks = new ArrayList<SchedulerParallelTask>();
		
		for(int i = 0 ; i < numCores; i++) {
			SchedulerParallelTask t = new SchedulerParallelTask(openNodes, numProc, bestLowerBound, bestUpperBound);
			schedulerTasks.add(t);
			forkJoinPool.invoke(t);
		}
		
		Node out = null;
		for (SchedulerParallelTask t : schedulerTasks) {
			Node joined = (Node) t.join();
			if(out == null || joined.getLowerBound() < out.getLowerBound()) {
				out = joined;		
			}
		}
				
		return out;
		
	}

}
