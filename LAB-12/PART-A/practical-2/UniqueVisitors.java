import java.util.Arrays;
import java.util.LinkedHashSet;

public class UniqueVisitors {
    public static void main(String[] args) {
        String[] logins = args.length == 0
                ? new String[] {"asha", "ravi", "asha", "meera", "ravi", "dev"}
                : args;
        LinkedHashSet<String> visitors = new LinkedHashSet<>(Arrays.asList(logins));
        System.out.println("Unique visitors: " + visitors.size());
        visitors.stream().limit(3).forEach(System.out::println);
    }
}
