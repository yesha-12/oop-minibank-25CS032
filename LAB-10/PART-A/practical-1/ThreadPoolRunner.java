import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ThreadPoolRunner {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int taskId = 1; taskId <= 10; taskId++) {
            int id = taskId;
            executor.execute(() -> {
                System.out.println("Task " + id + " running on "
                        + Thread.currentThread().getName());
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200));
            });
        }

        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
    }
}