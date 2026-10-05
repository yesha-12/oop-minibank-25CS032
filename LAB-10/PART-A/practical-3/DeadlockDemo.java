import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class DeadlockDemo {
    private static final class LockResource {
        private final int id;

        private LockResource(int id) {
            this.id = id;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockResource first = new LockResource(1);
        LockResource second = new LockResource(2);
        CountDownLatch bothHoldOneLock = new CountDownLatch(2);

        Thread firstThenSecond = deadlockingThread(
                "FirstThenSecond", first, second, bothHoldOneLock);
        Thread secondThenFirst = deadlockingThread(
                "SecondThenFirst", second, first, bothHoldOneLock);
        firstThenSecond.setDaemon(true);
        secondThenFirst.setDaemon(true);
        firstThenSecond.start();
        secondThenFirst.start();
        bothHoldOneLock.await();

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
        while ((firstThenSecond.getState() != Thread.State.BLOCKED
                || secondThenFirst.getState() != Thread.State.BLOCKED)
                && System.nanoTime() < deadline) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(1));
        }
        System.out.println("Opposite lock order: " + firstThenSecond.getState()
                + " / " + secondThenFirst.getState());

        LockResource safeFirst = new LockResource(1);
        LockResource safeSecond = new LockResource(2);
        Thread safeForward = orderedLockingThread("SafeForward", safeFirst, safeSecond);
        Thread safeReverse = orderedLockingThread("SafeReverse", safeSecond, safeFirst);
        safeForward.start();
        safeReverse.start();
        safeForward.join();
        safeReverse.join();
        System.out.println("Consistent lock order: both threads completed");
    }

    private static Thread deadlockingThread(String name, LockResource first,
            LockResource second, CountDownLatch bothHoldOneLock) {
        return new Thread(() -> {
            synchronized (first) {
                bothHoldOneLock.countDown();
                try {
                    bothHoldOneLock.await();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (second) {
                    System.out.println(name + " acquired both locks");
                }
            }
        }, name);
    }

    private static Thread orderedLockingThread(String name, LockResource first,
            LockResource second) {
        return new Thread(() -> {
            LockResource lowerId = first.id < second.id ? first : second;
            LockResource higherId = first.id < second.id ? second : first;
            synchronized (lowerId) {
                synchronized (higherId) {
                    System.out.println(name + " acquired both locks");
                }
            }
        }, name);
    }
}