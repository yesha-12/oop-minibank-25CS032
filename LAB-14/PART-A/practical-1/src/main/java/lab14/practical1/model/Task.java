package lab14.practical1.model;

public class Task {
    private final String title;

    public Task(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title cannot be blank");
        }
        this.title = title.trim();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
