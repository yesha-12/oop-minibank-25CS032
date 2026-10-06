import java.util.Arrays;

public class GenericStack<T> {
    private Object[] values = new Object[4];
    private int size;

    public void push(T value) {
        if (size == values.length) {
            values = Arrays.copyOf(values, values.length * 2);
        }
        values[size++] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (size == 0) {
            throw new IllegalStateException("Stack is empty");
        }
        T value = (T) values[--size];
        values[size] = null;
        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        GenericStack<String> names = new GenericStack<>();
        names.push("Asha");
        names.push("Ravi");
        System.out.println(names.pop());

        GenericStack<Integer> numbers = new GenericStack<>();
        numbers.push(10);
        numbers.push(20);
        System.out.println(numbers.pop());
    }
}
