package app;

import app.model.Note;
import app.persistence.NoteStore;
import app.service.NoteService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private static final Path STATE_FILE = Path.of("notes.dat");

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        NoteService service = new NoteService();
        if (Files.exists(STATE_FILE)) {
            service.replaceWith(NoteStore.load(STATE_FILE));
        }

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n1. Add note");
                System.out.println("2. List notes");
                System.out.println("3. Save notes");
                System.out.println("0. Save and exit");
                System.out.print("Choose an action: ");
                switch (scanner.nextLine().trim()) {
                    case "1" -> {
                        System.out.print("Note text: ");
                        service.add(new Note(scanner.nextLine()));
                        System.out.println("Note added.");
                    }
                    case "2" -> service.list().forEach(
                            note -> System.out.println(note.getText()));
                    case "3" -> {
                        NoteStore.save(service.list(), STATE_FILE);
                        System.out.println("Notes saved.");
                    }
                    case "0" -> {
                        NoteStore.save(service.list(), STATE_FILE);
                        running = false;
                    }
                    default -> System.out.println("Invalid action.");
                }
            }
        }
    }
}
