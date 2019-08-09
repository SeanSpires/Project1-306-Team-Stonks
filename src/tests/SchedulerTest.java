package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import sample.Model.Schedule;
import sample.Model.Scheduler;
import sample.Model.Task;

public class SchedulerTest {

	@Test
	public void testScheduler() {
		
		List<sample.Model.Task> tasks = new ArrayList<>();
		Scheduler scheduler = new Scheduler();
		List<Integer> expectedTasks = new ArrayList<>(); 
		List<Integer> expectedTimes = new ArrayList<>();
		
		expectedTasks.add(2);
		expectedTasks.add(1);
		expectedTasks.add(3);
		
		expectedTimes.add(3);
		expectedTimes.add(5);
		expectedTimes.add(11);
		
		sample.Model.Task task1 = new Task(1);
		sample.Model.Task task2 = new Task(2);
		sample.Model.Task task3 = new Task(3);
		sample.Model.Task task4 = new Task(4);

		task1.setWeight(2);
		task2.setWeight(3);
		task3.setWeight(3);
		task4.setWeight(2);
		
		
		task1.setSubTasks(task2, 10);
		task1.setSubTasks(task3, 10);
		
		task2.setSubTasks(task4, 12);
		
		task3.setSubTasks(task4, 12);
		
		
		task2.setParentTasks(task1);
		task3.setParentTasks(task1);
		
		task4.setParentTasks(task2);
		task4.setParentTasks(task3);
		
		tasks.add(task3);
		tasks.add(task2);
		tasks.add(task1);
		tasks.add(task4);
		
		Schedule s = scheduler.createBasicSchedule(tasks, 1);
		
		List<Integer> scheduledTasks = new ArrayList<>();
		
		for (Task t : s.getTasks().keySet()) {
			scheduledTasks.add(t.getNodeNumber());
			System.out.println(t.getNodeNumber());
		}
		
		List<Integer> scheduledTimes =  new ArrayList<>();
		
		for (Integer i : s.getTasks().values()) {
			scheduledTimes.add(i);
		}
	
		assertEquals(expectedTasks, scheduledTasks);
		assertEquals(expectedTimes, scheduledTimes);
		
	}

}


