public class MiniBankThreadDemo {
    static class Account {
        private long balance;

        public Account(long balance) {
            this.balance = balance;
        }

        public void deposit(long amount) {
            balance += amount;
        }

        public synchronized void depositSafe(long amount) {
            balance += amount;
        }

        public void withdraw(long amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
            }
        }

        public synchronized void withdrawSafe(long amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
            }
        }

        public long getBalance() {
            return balance;
        }
    }

    static class AccountWorker implements Runnable {
        private final Account account;
        private final int times;
        private final long amount;

        public AccountWorker(Account account, int times, long amount) {
            this.account = account;
            this.times = times;
            this.amount = amount;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " started");
            for (int i = 0; i < times; i++) {
                account.deposit(amount);
                if (i % 500 == 0) {
                    Thread.yield();
                }
            }
            System.out.println(Thread.currentThread().getName() + " finished; current balance: " + account.getBalance());
        }
    }

    static class SafeAccountWorker implements Runnable {
        private final Account account;
        private final int times;
        private final long amount;

        public SafeAccountWorker(Account account, int times, long amount) {
            this.account = account;
            this.times = times;
            this.amount = amount;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " started");
            for (int i = 0; i < times; i++) {
                account.depositSafe(amount);
                if (i % 500 == 0) {
                    Thread.yield();
                }
            }
            System.out.println(Thread.currentThread().getName() + " finished; current balance: " + account.getBalance());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Unsynchronized race demo ===");
        Account account = new Account(0);
        Thread[] workers = new Thread[10];

        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(new AccountWorker(account, 20000, 10), "Worker-" + (i + 1));
            workers[i].start();
        }

        for (Thread t : workers) {
            t.join();
        }

        System.out.println("Final balance without synchronization: " + account.getBalance());

        System.out.println("\n=== Synchronized fix ===");
        Account safeAccount = new Account(0);
        Thread[] safeWorkers = new Thread[10];

        for (int i = 0; i < safeWorkers.length; i++) {
            safeWorkers[i] = new Thread(new SafeAccountWorker(safeAccount, 20000, 10), "SafeWorker-" + (i + 1));
            safeWorkers[i].start();
        }

        for (Thread t : safeWorkers) {
            t.join();
        }

        System.out.println("Final balance with synchronization: " + safeAccount.getBalance());
    }
}
