import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Leaderboard {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Asha", 91);
        scores.put("Ravi", 87);
        scores.put("Meera", 91);
        scores.put("Dev", 76);

        int limit = args.length == 0 ? 3 : Integer.parseInt(args[0]);
        if (limit < 0) {
            throw new IllegalArgumentException("Top-N cannot be negative");
        }
        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(
                                Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .forEach(entry -> System.out.println(
                        entry.getKey() + " - " + entry.getValue()));
    }
}
