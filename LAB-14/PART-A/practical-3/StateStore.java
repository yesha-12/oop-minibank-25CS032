import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class StateStore {
    public static void save(Object value, Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream output =
                     new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input =
                     new ObjectInputStream(Files.newInputStream(file))) {
            return (T) input.readObject();
        }
    }
}
