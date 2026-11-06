package app.model;

import java.io.Serializable;

public class Note implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String text;

    public Note(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Note note)) {
            return false;
        }
        return text.equals(note.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
