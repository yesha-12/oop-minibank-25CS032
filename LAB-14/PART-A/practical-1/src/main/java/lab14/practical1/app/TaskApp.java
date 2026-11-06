package lab14.practical1.app;

import java.util.Scanner;
import lab14.practical1.model.Task;
import lab14.practical1.service.TaskService;

public class TaskApp {
    public static void main(String[] args) {
        TaskService service = new TaskService();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n1. Add task");
                System.out.println("2. List tasks");
                System.out.println("0. Exit");
                System.out.print("Choose an action: ");
                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" -> {
                            System.out.print("Task title: ");
                            service.performAction("add", scanner.nextLine());
                            System.out.println("Task added.");
                        }
                        case "2" -> service.performAction("list", null)
                                .forEach(System.out::println);
                        case "0" -> running = false;
                        default -> service.performAction(choice, null);
                    }
                } catch (IllegalArgumentException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }
}
