import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import model.Account;
import model.SavingsAccount;
import service.TransactionProcessor;

public class MiniBankThreadDemo {
    private static final class Transaction implements Runnable {
        private final Account account;
        private final boolean deposit;
        private final long amount;

        private Transaction(Account account, boolean deposit, long amount) {
            this.account = account;
            this.deposit = deposit;
            this.amount = amount;
        }

        @Override
        public void run() {
            if (deposit) {
                account.deposit(amount);
                System.out.println("Deposited " + amount + " to " + account.getAccountNumber());
            } else {
                boolean completed = account.withdraw(amount);
                System.out.println("Withdrawal of " + amount + " from "
                        + account.getAccountNumber() + (completed ? " completed" : " declined"));
            }
        }
    }

    private static final class TransactionBuffer {
        private final Transaction[] transactions = new Transaction[3];
        private int count;
        private int nextWrite;
        private int nextRead;

        public synchronized void put(Transaction transaction) throws InterruptedException {
            while (count == transactions.length) {
                wait();
            }
            transactions[nextWrite] = transaction;
            nextWrite = (nextWrite + 1) % transactions.length;
            count++;
            notifyAll();
        }

        public synchronized Transaction take() throws InterruptedException {
            while (count == 0) {
                wait();
            }
            Transaction transaction = transactions[nextRead];
            transactions[nextRead] = null;
            nextRead = (nextRead + 1) % transactions.length;
            count--;
            notifyAll();
            return transaction;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account accountA = new SavingsAccount("Ava", 10000, 0);
        Account accountB = new SavingsAccount("Ben", 10000, 0);

        TransactionProcessor processor = new TransactionProcessor();
        for (int transactionId = 1; transactionId <= 12; transactionId++) {
            processor.submit(new Transaction(accountA, transactionId % 2 == 0,
                    transactionId % 2 == 0 ? 100 : 50));
        }
        processor.stop();
        System.out.println("Pool batch complete. Account A balance: " + accountA.getBalance());

        runProducerConsumer(accountB);
        demonstrateTransferDeadlock();
        runOppositeTransfers(accountA, accountB);
        System.out.println("Balances after transfers: " + accountA.getBalance()
                + " / " + accountB.getBalance());
        System.out.println("Combined balance: "
                + (accountA.getBalance() + accountB.getBalance()));
    }

    private static void runProducerConsumer(Account account) throws InterruptedException {
        TransactionBuffer buffer = new TransactionBuffer();
        Thread producer = new Thread(() -> {
            try {
                for (int id = 1; id <= 6; id++) {
                    buffer.put(new Transaction(account, id % 2 == 1, 25));
                    System.out.println("Queued transaction " + id);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "TransactionProducer");

        Thread consumer = new Thread(() -> {
            try {
                for (int id = 1; id <= 6; id++) {
                    buffer.take().run();
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "TransactionConsumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println("Producer-consumer batch complete. Account B balance: "
                + account.getBalance());
    }

    private static void runOppositeTransfers(Account accountA, Account accountB)
            throws InterruptedException {
        Thread forward = new Thread(() -> transferRepeatedly(accountA, accountB), "A-to-B");
        Thread reverse = new Thread(() -> transferRepeatedly(accountB, accountA), "B-to-A");
        forward.start();
        reverse.start();
        forward.join();
        reverse.join();
    }

    private static void demonstrateTransferDeadlock() throws InterruptedException {
        Account accountA = new SavingsAccount("Deadlock A", 1000, 0);
        Account accountB = new SavingsAccount("Deadlock B", 1000, 0);
        CountDownLatch bothHoldOneAccount = new CountDownLatch(2);
        Thread forward = deadlockingTransferThread("Deadlock-A-to-B", accountA,
                accountB, bothHoldOneAccount);
        Thread reverse = deadlockingTransferThread("Deadlock-B-to-A", accountB,
                accountA, bothHoldOneAccount);
        forward.setDaemon(true);
        reverse.setDaemon(true);
        forward.start();
        reverse.start();
        bothHoldOneAccount.await();

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
        while ((forward.getState() != Thread.State.BLOCKED
                || reverse.getState() != Thread.State.BLOCKED)
                && System.nanoTime() < deadline) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(1));
        }
        System.out.println("Opposite-order bank transfer locks: " + forward.getState()
                + " / " + reverse.getState());
    }

    private static Thread deadlockingTransferThread(String name, Account source,
            Account destination, CountDownLatch bothHoldOneAccount) {
        return new Thread(() -> {
            synchronized (source) {
                bothHoldOneAccount.countDown();
                try {
                    bothHoldOneAccount.await();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (destination) {
                    source.withdraw(1);
                    destination.deposit(1);
                }
            }
        }, name);
    }

    private static void transferRepeatedly(Account source, Account destination) {
        for (int count = 0; count < 100; count++) {
            if (!TransactionProcessor.transfer(source, destination, 10)) {
                throw new IllegalStateException("Transfer was unexpectedly declined");
            }
        }
    }
}
