import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MiniRunner {

    public static void main(String[] args) {

        TestClass obj = new TestClass();

        int count = 0;

        Method[] methods = obj.getClass().getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(Run.class)) {

                try {
                    method.invoke(obj);
                    count++;
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        System.out.println("Methods ran: " + count);
    }
}