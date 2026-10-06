package app;

import app.model.Note;
import app.service.NoteService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Path STATE_FILE = Path.of("notes.dat");

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        NoteService service = new NoteService();
        if (Files.exists(STATE_FILE)) {
            try (ObjectInputStream input =
                         new ObjectInputStream(Files.newInputStream(STATE_FILE))) {
                Object restored = input.readObject();
                if (!(restored instanceof List<?> savedNotes)) {
                    throw new IOException("Saved file does not contain a note list");
                }
                ArrayList<Note> notes = new ArrayList<>();
                for (Object savedNote : savedNotes) {
                    if (!(savedNote instanceof Note note)) {
                        throw new IOException("Saved list contains an invalid item");
                    }
                    notes.add(note);
                }
                service.replaceWith(notes);
            }
        }
        service.add(new Note("Layered app with packaged model and service"));
        try (ObjectOutputStream output =
                     new ObjectOutputStream(Files.newOutputStream(STATE_FILE))) {
            output.writeObject(new ArrayList<>(service.list()));
        }
        service.list().forEach(note -> System.out.println(note.getText()));
    }
}
