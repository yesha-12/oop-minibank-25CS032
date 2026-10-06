import java.util.Arrays;

public class GenericContainers {
    private static class Box<T> {
        private T value;

        Box(T value) {
            this.value = value;
        }

        T get() {
            return value;
        }

        void set(T value) {
            this.value = value;
        }
    }

    private record Pair<A, B>(A first, B second) {
    }

    private static <T> T first(T[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
        return values[0];
    }

    public static void main(String[] args) {
        Box<String> label = new Box<>("MiniBank");
        label.set("OOP practical");
        Pair<String, Integer> student = new Pair<>("Asha", 101);
        System.out.println(label.get());
        System.out.println(student.first() + " - " + student.second());
        System.out.println("First value: " + first(Arrays.asList(4, 8, 12).toArray(Integer[]::new)));
    }
}
