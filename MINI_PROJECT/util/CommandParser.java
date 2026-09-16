package util;

import model.Command;
import model.TransactionType;

public class CommandParser {

    public static Command parse(String line) {

        String[] parts = line.trim().split("\\s+");

        TransactionType type =
                TransactionType.valueOf(parts[0]);

        String account = parts[1];

        long amount =
                Long.parseLong(parts[2]);

        return new Command(type, account, amount);

    }

}