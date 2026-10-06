import java.util.Scanner;

public class CounterApp {
    public static void main(String[] args) {
        CounterService service = new CounterService();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("How many times should the counter increment? ");
            int amount = scanner.nextInt();
            service.add(amount);
            System.out.println("Count: " + service.getCount());
        }
    }
}
