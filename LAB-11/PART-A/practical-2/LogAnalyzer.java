import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogAnalyzer {
    public static void main(String[] args) throws IOException {
        Path folder = Path.of(args.length == 0 ? "logs" : args[0]);
        String keyword = args.length > 1 ? args[1] : "ERROR";
        long totalLines = 0;
        long matchingLines = 0;

        try (var files = Files.newDirectoryStream(folder, "*.txt")) {
            for (Path file : files) {
                long fileMatches = 0;
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        totalLines++;
                        if (line.contains(keyword)) {
                            matchingLines++;
                            fileMatches++;
                        }
                    }
                }
                System.out.printf("%s: %d bytes, %d matching lines%n",
                        file.getFileName(), Files.size(file), fileMatches);
            }
        }
        System.out.printf("Total lines: %d; lines containing %s: %d%n",
                totalLines, keyword, matchingLines);
    }
}
