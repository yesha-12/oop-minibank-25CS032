public class SeatBookingRace {
    static class BookingSystem {
        private int seatsLeft = 5;

        public boolean bookSeat() {
            if (seatsLeft > 0) {
                seatsLeft--;
                return true;
            }
            return false;
        }

        public int getSeatsLeft() {
            return seatsLeft;
        }
    }

    static class SynchronizedBookingSystem {
        private int seatsLeft = 5;

        public synchronized boolean bookSeat() {
            if (seatsLeft > 0) {
                seatsLeft--;
                return true;
            }
            return false;
        }

        public int getSeatsLeft() {
            return seatsLeft;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Without synchronization ===");
        BookingSystem system = new BookingSystem();
        Thread[] threads = new Thread[10];
        int successfulBookings = 0;

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                boolean booked = system.bookSeat();
                if (booked) {
                    synchronized (System.out) {
                        System.out.println(Thread.currentThread().getName() + " booked a seat.");
                    }
                } else {
                    synchronized (System.out) {
                        System.out.println(Thread.currentThread().getName() + " failed to book.");
                    }
                }
            }, "BookingThread-" + (i + 1));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Seats left without synchronization: " + system.getSeatsLeft());

        System.out.println("\n=== With synchronization ===");
        SynchronizedBookingSystem safeSystem = new SynchronizedBookingSystem();
        Thread[] safeThreads = new Thread[10];

        for (int i = 0; i < safeThreads.length; i++) {
            safeThreads[i] = new Thread(() -> {
                boolean booked = safeSystem.bookSeat();
                if (booked) {
                    synchronized (System.out) {
                        System.out.println(Thread.currentThread().getName() + " booked a seat.");
                    }
                } else {
                    synchronized (System.out) {
                        System.out.println(Thread.currentThread().getName() + " failed to book.");
                    }
                }
            }, "SafeBookingThread-" + (i + 1));
            safeThreads[i].start();
        }

        for (Thread t : safeThreads) {
            t.join();
        }

        System.out.println("Seats left with synchronization: " + safeSystem.getSeatsLeft());
    }
}
