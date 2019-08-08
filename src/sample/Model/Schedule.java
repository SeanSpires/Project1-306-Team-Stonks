package sample;


public class Schedule {
    private HashMap<Task, Integer> tasks = new HashMap<Task, Integer>;

    public HashMap<Task, Integer> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<Task, Integer> tasks) {
        this.tasks = tasks;
    }

    public boolean isComplete(List<Task> allTasks) {
        for (Task t : allTasks) {
            if (!(tasks.containsKey(t))) {
                return false;
            }
        }
        return true;
    }



}