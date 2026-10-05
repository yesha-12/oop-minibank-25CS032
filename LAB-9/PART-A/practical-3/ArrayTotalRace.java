public class ArrayTotalRace {
    static class UnsafeArrayTotal {
        private long total = 0;

        public void addValues(int[] numbers, int start, int end) {
            for (int i = start; i < end; i++) {
                total += numbers[i];
            }
        }

        public long getTotal() {
            return total;
        }
    }

    static class SynchronizedArrayTotal {
        private long total = 0;

        public synchronized void addValues(int[] numbers, int start, int end) {
            for (int i = start; i < end; i++) {
                total += numbers[i];
            }
        }

        public long getTotal() {
            return total;
        }
    }

    static class PartitionedTotal {
        private long total = 0;

        public synchronized void addValue(long value) {
            total += value;
        }

        public long getTotal() {
            return total;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] numbers = new int[100000];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;
        }

        UnsafeArrayTotal unsafe = new UnsafeArrayTotal();
        Thread[] threads = new Thread[4];

        for (int i = 0; i < threads.length; i++) {
            final int start = i * (numbers.length / threads.length);
            final int end = (i == threads.length - 1)
                    ? numbers.length
                    : (i + 1) * (numbers.length / threads.length);

            threads[i] = new Thread(() -> unsafe.addValues(numbers, start, end), "UnsafeThread-" + (i + 1));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long expected = 0;
        for (int num : numbers) {
            expected += num;
        }

        System.out.println("Unsafe shared total: " + unsafe.getTotal());
        System.out.println("Expected total: " + expected);

        SynchronizedArrayTotal safe = new SynchronizedArrayTotal();
        Thread[] safeThreads = new Thread[4];

        for (int i = 0; i < safeThreads.length; i++) {
            final int start = i * (numbers.length / safeThreads.length);
            final int end = (i == safeThreads.length - 1)
                    ? numbers.length
                    : (i + 1) * (numbers.length / safeThreads.length);

            safeThreads[i] = new Thread(() -> safe.addValues(numbers, start, end), "SafeThread-" + (i + 1));
            safeThreads[i].start();
        }

        for (Thread t : safeThreads) {
            t.join();
        }

        System.out.println("Synchronized shared total: " + safe.getTotal());

        PartitionedTotal partitioned = new PartitionedTotal();
        long[] partial = new long[4];
        Thread[] partitionThreads = new Thread[4];

        for (int i = 0; i < partitionThreads.length; i++) {
            final int index = i;
            final int start = i * (numbers.length / partitionThreads.length);
            final int end = (i == partitionThreads.length - 1)
                    ? numbers.length
                    : (i + 1) * (numbers.length / partitionThreads.length);

            partitionThreads[i] = new Thread(() -> {
                long localTotal = 0;
                for (int j = start; j < end; j++) {
                    localTotal += numbers[j];
                }
                partial[index] = localTotal;
                partitioned.addValue(localTotal);
            }, "PartitionedThread-" + (i + 1));
            partitionThreads[i].start();
        }

        for (Thread t : partitionThreads) {
            t.join();
        }

        System.out.println("Partitioned + synchronized total: " + partitioned.getTotal());
        System.out.println("Difference between expected and unsafe total: " + (expected - unsafe.getTotal()));
    }
}
