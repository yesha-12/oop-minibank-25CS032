public class CounterRace {
    static class Counter {
        private int count = 0;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    static class SynchronizedCounter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Without synchronization ===");
        Counter counter = new Counter();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    counter.increment();
                }
            }, "CounterThread-" + (i + 1));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Final count without synchronization: " + counter.getCount());

        System.out.println("\n=== With synchronization ===");
        SynchronizedCounter syncCounter = new SynchronizedCounter();
        Thread[] syncThreads = new Thread[10];

        for (int i = 0; i < syncThreads.length; i++) {
            syncThreads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    syncCounter.increment();
                }
            }, "SyncCounterThread-" + (i + 1));
            syncThreads[i].start();
        }

        for (Thread t : syncThreads) {
            t.join();
        }

        System.out.println("Final count with synchronization: " + syncCounter.getCount());
    }
}
