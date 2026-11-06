package app.persistence;

import app.model.Note;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class NoteStore {
    private NoteStore() {
    }

    public static void save(List<Note> notes, Path file) throws IOException {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(new ArrayList<>(notes));
        }
    }

    public static List<Note> load(Path file)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream input =
                     new ObjectInputStream(Files.newInputStream(file))) {
            Object savedData = input.readObject();
            if (!(savedData instanceof List<?> savedNotes)) {
                throw new IOException("Saved file does not contain a note list");
            }

            ArrayList<Note> notes = new ArrayList<>();
            for (Object savedNote : savedNotes) {
                if (!(savedNote instanceof Note note)) {
                    throw new IOException("Saved list contains an invalid item");
                }
                notes.add(note);
            }
            return List.copyOf(notes);
        }
    }
}
