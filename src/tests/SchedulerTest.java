package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import MVC.Model.Schedule;
import MVC.Model.Scheduler;
import MVC.Model.Task;

public class SchedulerTest {

    private List<MVC.Model.Task> tasks;
    private Scheduler scheduler;



	@Before
    public void setup() {
        tasks = new ArrayList<>();
        scheduler = new Scheduler();

    }

	@Test
	public void testSimpleSchedule() {
    /*
    * simpleSchedulerTest tests the basic scheduling algorithm
    * with the simple example input from the project specification.
    *
    * Only one root task and one leaf task.
    *
    * */
        Task task1 = new Task(1);
        Task task2 = new Task(2);
        Task task3 = new Task(3);
        Task task4 = new Task(4);

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

//		s.getTasks().keySet().forEach(task -> {
//		    scheduledTasks.add(task.getNodeNumber());
//		});
		
		for (Task t : s.getTasks().keySet()) {
			scheduledTasks.add(t.getNodeNumber());
			System.out.println(t.getNodeNumber());
		}
		
		List<Integer> scheduledTimes =  new ArrayList<>();

		scheduledTimes.addAll(s.getTasks().values());

        List<Integer> expectedTasks = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
                add(4);
            }
        };

        List<Integer> expectedTimes = new ArrayList<Integer>() {
            {
                add(2);
                add(5);
                add(8);
                add(10);

            }
        };

		assertEquals(expectedTasks, scheduledTasks);
		assertEquals(expectedTimes, scheduledTimes);
		
	}


    @Test
    public void testMultiRootSchedule() {
        /*
         * Tests the basic scheduling algorithm
         *
         * More than one root tasks and one leaf task.
         *
         * */

        Task task1 = new Task(1);
        Task task2 = new Task(2);
        Task task3 = new Task(3);
        Task task4 = new Task(4);
        Task task5 = new Task(5);
        Task task6 = new Task(6);


        task1.setWeight(2);
        task2.setWeight(3);
        task3.setWeight(3);
        task4.setWeight(2);
        task5.setWeight(25);
        task6.setWeight(22);

        task1.setSubTasks(task2, 10);
        task1.setSubTasks(task3, 10);
        task1.setSubTasks(task5, 2);
        task1.setSubTasks(task6, 1);

        task2.setSubTasks(task4, 12);

        task3.setSubTasks(task4, 13);

        task4.setParentTasks(task2);
        task4.setParentTasks(task3);
        task5.setParentTasks(task1);
        task6.setParentTasks(task1);

        tasks.add(task3);
        tasks.add(task2);
        tasks.add(task1);
        tasks.add(task4);
        tasks.add(task6);
        tasks.add(task5);

        Schedule s = scheduler.createBasicSchedule(tasks, 1);

        List<Integer> scheduledTasks = new ArrayList<>();

//		s.getTasks().keySet().forEach(task -> {
//		    scheduledTasks.add(task.getNodeNumber());
//		});

        for (Task t : s.getTasks().keySet()) {
            scheduledTasks.add(t.getNodeNumber());
            System.out.println(t.getNodeNumber());
        }

        List<Integer> scheduledTimes =  new ArrayList<>();

        scheduledTimes.addAll(s.getTasks().values());

        List<Integer> expectedTasks = new ArrayList<Integer>() {
            {
                add(3);
                add(2);
                add(1);
                add(4);
                add(6);
                add(5);
            }
        };

        List<Integer> expectedTimes = new ArrayList<Integer>() {
            {
                add(16);
                add(13);
                add(8);
                add(65);
                add(38);
                add(63);

            }
        };

        assertEquals(expectedTasks, scheduledTasks);
        assertEquals(expectedTimes, scheduledTimes);

    }


    @Test
    public void testMultiLeafSchedule() {
        /*
         * Tests the basic scheduling algorithm
         *
         * More than one root tasks and more than one leaf tasks.
         *
         * */

        Task task1 = new Task(1);
        Task task2 = new Task(2);
        Task task3 = new Task(3);
        Task task4 = new Task(4);
        Task task5 = new Task(5);
        Task task6 = new Task(6);


        task1.setWeight(2);
        task2.setWeight(3);
        task3.setWeight(3);
        task4.setWeight(2);
        task5.setWeight(25);
        task6.setWeight(22);

        task1.setSubTasks(task2, 10);
        task1.setSubTasks(task3, 10);
        task1.setSubTasks(task5, 2);
        task1.setSubTasks(task6, 1);
        task1.setSubTasks(task4, 12);



        task2.setParentTasks(task1);
        task3.setParentTasks(task1);
        task4.setParentTasks(task1);
        task5.setParentTasks(task1);
        task6.setParentTasks(task1);

        tasks.add(task3);
        tasks.add(task2);
        tasks.add(task1);
        tasks.add(task4);
        tasks.add(task6);
        tasks.add(task5);

        Schedule s = scheduler.createBasicSchedule(tasks, 1);

        List<Integer> scheduledTasks = new ArrayList<>();

//		s.getTasks().keySet().forEach(task -> {
//		    scheduledTasks.add(task.getNodeNumber());
//		});

        for (Task t : s.getTasks().keySet()) {
            scheduledTasks.add(t.getNodeNumber());
            System.out.println(t.getNodeNumber());
        }

        List<Integer> scheduledTimes =  new ArrayList<>();

        scheduledTimes.addAll(s.getTasks().values());

        List<Integer> expectedTasks = new ArrayList<Integer>() {
            {
                add(1);
                add(5);
                add(2);
                add(6);
                add(4);
                add(3);
            }
        };

        List<Integer> expectedTimes = new ArrayList<Integer>() {
            {
                add(2);
                add(27);
                add(30);
                add(52);
                add(54);
                add(57);

            }
        };

        assertEquals(expectedTasks, scheduledTasks);
        assertEquals(expectedTimes, scheduledTimes);

    }

}


