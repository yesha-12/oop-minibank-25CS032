package app.service;

import app.model.Note;
import java.util.ArrayList;
import java.util.List;

public class NoteService {
    private final ArrayList<Note> notes = new ArrayList<>();

    public void add(Note note) {
        if (note == null || note.getText() == null || note.getText().isBlank()) {
            throw new IllegalArgumentException("Note text cannot be blank");
        }
        notes.add(note);
    }

    public List<Note> list() {
        return List.copyOf(notes);
    }

    public void replaceWith(List<Note> savedNotes) {
        notes.clear();
        notes.addAll(savedNotes);
    }
}
