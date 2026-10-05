public class ProducerConsumer {
    private static final int ITEM_COUNT = 10;

    private static class SharedBuffer {
        private final int[] items = new int[3];
        private int count;
        private int nextWrite;
        private int nextRead;

        public synchronized void put(int item) throws InterruptedException {
            while (count == items.length) {
                wait();
            }

            items[nextWrite] = item;
            nextWrite = (nextWrite + 1) % items.length;
            count++;
            notifyAll();
        }

        public synchronized int take() throws InterruptedException {
            while (count == 0) {
                wait();
            }

            int item = items[nextRead];
            nextRead = (nextRead + 1) % items.length;
            count--;
            notifyAll();
            return item;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SharedBuffer buffer = new SharedBuffer();

        Thread producer = new Thread(() -> {
            try {
                for (int item = 1; item <= ITEM_COUNT; item++) {
                    buffer.put(item);
                    System.out.println("Produced " + item);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int item = 1; item <= ITEM_COUNT; item++) {
                    System.out.println("Consumed " + buffer.take());
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}