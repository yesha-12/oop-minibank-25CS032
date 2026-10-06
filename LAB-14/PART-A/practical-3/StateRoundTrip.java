import java.io.Serializable;
import java.nio.file.Path;
import java.util.Arrays;

public class StateRoundTrip {
    private static class Value implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String text;
        private final int number;

        private Value(String text, int number) {
            this.text = text;
            this.number = number;
        }

        @Override
        public String toString() {
            return text + ":" + number;
        }
    }

    public static void main(String[] args) throws Exception {
        Path file = Path.of("state.dat");
        Value[] original = {new Value("saved", 42)};
        StateStore.save(original, file);
        Value[] restored = StateStore.load(file);
        if (!original[0].toString().equals(restored[0].toString())) {
            throw new AssertionError("State changed during persistence");
        }
        System.out.println(Arrays.toString(restored));
    }
}
