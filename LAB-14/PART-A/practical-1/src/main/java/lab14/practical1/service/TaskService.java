package lab14.practical1.service;

import java.util.ArrayList;
import java.util.List;
import lab14.practical1.model.Task;

public class TaskService {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public List<Task> performAction(String action, String taskTitle) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        return switch (action) {
            case "add" -> {
                tasks.add(new Task(taskTitle));
                yield listTasks();
            }
            case "list" -> listTasks();
            default -> throw new IllegalArgumentException(
                    "Invalid action: " + action);
        };
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public List<Task> listTasks() {
        return List.copyOf(tasks);
    }
}
