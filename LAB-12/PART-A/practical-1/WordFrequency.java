import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordFrequency {
    public static void main(String[] args) {
        String paragraph = args.length == 0
                ? "Maps count words and maps can sort words by frequency"
                : String.join(" ", args);
        Map<String, Long> counts = Arrays.stream(
                        paragraph.toLowerCase().split("[^\\p{L}\\p{N}]+"))
                .filter(word -> !word.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}

