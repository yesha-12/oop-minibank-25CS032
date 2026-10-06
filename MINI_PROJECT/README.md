# MiniBank

MiniBank is a Java 17 console application demonstrating layered banking
operations, annotation-based generic repositories, serialization, transaction
logging, NIO reporting, and concurrent transaction processing.

## Build and test

From this directory, run:

```sh
mvn test
mvn package
```

The runnable JAR is written to `target/oop-minibank-1.0.0.jar`.

## Run

```sh
java -jar target/oop-minibank-1.0.0.jar
```

The menu supports opening accounts, deposits, withdrawals, transfers,
statements, listing and searching accounts, end-of-day reporting, and
concurrent batches of deposits or withdrawals. Accounts are saved to
`data/accounts.dat` when the program exits and loaded on the next start.
Successful transactions are appended to `data/logs/transactions.log`;
the report is written to `data/daily-report.txt`. Generated data is runtime
output and should not be committed.

The test suite covers balance changes, rejected withdrawals, concurrent
deposits, and persistence round trips.
