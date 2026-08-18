import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        String[] logs = {
                "10:05 alice Hello there",
                "10:10 bob Good Morning",
                "WrongLine",
                "10:20 charlie hello everyone"
        };

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter keyword : ");

        String keyword = sc.nextLine();

        System.out.println(ChatFilter.filter(logs, keyword));

        sc.close();

    }

}