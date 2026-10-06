import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class TreeReport {
    public static void main(String[] args) throws IOException {
        Path root = Path.of(args.length == 0 ? "." : args[0]);
        Path report = Path.of(args.length < 2 ? "tree-report.txt" : args[1]);
        Path reportAbsolute = report.toAbsolutePath().normalize();

        try (BufferedWriter writer = Files.newBufferedWriter(report)) {
            Files.walkFileTree(root, new java.nio.file.SimpleFileVisitor<>() {
                @Override
                public java.nio.file.FileVisitResult visitFile(
                        Path file, BasicFileAttributes attributes) throws IOException {
                    if (!file.toAbsolutePath().normalize().equals(reportAbsolute)) {
                        writer.write(String.format("%s | %d bytes | %s%n",
                                file, attributes.size(), attributes.lastModifiedTime()));
                    }
                    return java.nio.file.FileVisitResult.CONTINUE;
                }
            });
        }
        System.out.println("Wrote file inventory to " + report);
    }
}
