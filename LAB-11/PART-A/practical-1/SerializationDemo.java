import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SerializationDemo {
    private static class Student implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int rollNumber;
        private transient String sessionNote;

        private Student(String name, int rollNumber, String sessionNote) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.sessionNote = sessionNote;
        }

        @Override
        public String toString() {
            return name + " (" + rollNumber + "), session note: " + sessionNote;
        }
    }

    public static void main(String[] args) throws Exception {
        Path file = Path.of("students.dat");
        Student[] students = {
            new Student("Asha", 101, "present"),
            new Student("Ravi", 102, "late")
        };

        try (ObjectOutputStream output =
                     new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(students);
        }

        Student[] restored;
        try (ObjectInputStream input =
                     new ObjectInputStream(Files.newInputStream(file))) {
            restored = (Student[]) input.readObject();
        }

        for (Student student : restored) {
            System.out.println(student);
        }
    }
}
