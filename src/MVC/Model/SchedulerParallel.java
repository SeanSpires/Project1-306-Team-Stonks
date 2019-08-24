package MVC.Model;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerParallel {
	
	private PriorityBlockingQueue<Node> openNodes;
	private ForkJoinPool forkJoinPool;
	
	private AtomicLong bestLowerBound;
	private AtomicLong bestUpperBound;	
	
	public Node createOptimalSchedule(List<Task> tasks, int numberOfProcessors, int numCores) {
		this.openNodes = new PriorityBlockingQueue<>();
		this.forkJoinPool = new ForkJoinPool(numCores);
		this.bestLowerBound = new AtomicLong();
		this.bestUpperBound = new AtomicLong();	
		
		return null;
		
	}

}
