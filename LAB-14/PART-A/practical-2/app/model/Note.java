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
}
