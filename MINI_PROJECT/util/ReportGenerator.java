package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public final class ReportGenerator {
    private ReportGenerator() {
    }

    public static String generate(Path logsFolder, Path reportFile) throws IOException {
        Files.createDirectories(logsFolder);
        long deposits = 0;
        long withdrawals = 0;
        StringBuilder details = new StringBuilder();

        try (var files = Files.newDirectoryStream(logsFolder, "*.log")) {
            for (Path file : files) {
                BasicFileAttributes attributes =
                        Files.readAttributes(file, BasicFileAttributes.class);
                if (!attributes.isRegularFile() || attributes.size() == 0) {
                    continue;
                }
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length < 3) {
                            throw new IOException("Malformed transaction in " + file
                                    + ": " + line);
                        }
                        long amount;
                        try {
                            amount = Long.parseLong(parts[parts.length - 1]);
                        } catch (NumberFormatException exception) {
                            throw new IOException("Invalid transaction amount in "
                                    + file + ": " + line, exception);
                        }
                        if (amount < 0) {
                            throw new IOException("Negative transaction amount in "
                                    + file + ": " + line);
                        }
                        switch (parts[0]) {
                            case "DEPOSIT" -> deposits = Math.addExact(deposits, amount);
                            case "WITHDRAW" -> withdrawals =
                                    Math.addExact(withdrawals, amount);
                            default -> throw new IOException(
                                    "Unknown transaction type in " + file + ": " + line);
                        }
                    }
                }
                details.append(file.getFileName())
                        .append(" | ").append(attributes.size()).append(" bytes | ")
                        .append(attributes.lastModifiedTime()).append(System.lineSeparator());
            }
        } catch (ArithmeticException exception) {
            throw new IOException("Transaction totals exceed the supported range", exception);
        }

        long netChange;
        try {
            netChange = Math.subtractExact(deposits, withdrawals);
        } catch (ArithmeticException exception) {
            throw new IOException("Net transaction total exceeds the supported range",
                    exception);
        }
        String report = "Deposits: " + deposits + System.lineSeparator()
                + "Withdrawals: " + withdrawals + System.lineSeparator()
                + "Net change: " + netChange + System.lineSeparator()
                + "Log files:" + System.lineSeparator() + details;
        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(reportFile, report);
        return report;
    }
}
