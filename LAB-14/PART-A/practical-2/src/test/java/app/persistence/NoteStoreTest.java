package app.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import app.model.Note;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class NoteStoreTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void savedNotesReloadUnchanged() throws Exception {
        List<Note> original = List.of(
                new Note("Prepare the lab report"),
                new Note("Review ArrayList and JUnit"));
        Path file = temporaryDirectory.resolve("notes.dat");

        NoteStore.save(original, file);

        assertEquals(original, NoteStore.load(file));
    }
}
