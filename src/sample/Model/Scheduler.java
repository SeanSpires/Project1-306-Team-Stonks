package  sample;

public class Scheduler {

    private List<Schedule> schedules = new ArrayList<Schedule>();

    public void CreateSchedule(Schedule s, List<Task> reachableTasks, int numberOfProcessors) {

        List<Task> copyOfReachableTasks = ArrayList<>();


        for (Task t : reachableTasks) {
            copyOfReachableTasks = reachableTasks.copy();
            copyOfReachableTasks.remove(t);
            copyOfReachableTasks.add(t.getSubTasks());
            for (int i = 0; i < numberOfProcessors; i++){

            }
        }
    }


}