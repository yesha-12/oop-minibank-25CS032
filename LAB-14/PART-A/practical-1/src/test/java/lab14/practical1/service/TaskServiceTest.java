package lab14.practical1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TaskServiceTest {
    @Test
    void addingTaskIncreasesTheCount() {
        TaskService service = new TaskService();

        service.performAction("add", "Finish lab report");

        assertEquals(1, service.getTaskCount());
    }

    @Test
    void invalidActionThrows() {
        TaskService service = new TaskService();

        assertThrows(IllegalArgumentException.class,
                () -> service.performAction("delete", null));
    }

    @Test
    void addingBlankTaskThrows() {
        TaskService service = new TaskService();

        assertThrows(IllegalArgumentException.class,
                () -> service.performAction("add", "  "));
        assertEquals(0, service.getTaskCount());
    }
}
