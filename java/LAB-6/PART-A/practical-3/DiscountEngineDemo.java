package discount;

import java.util.ArrayList;
import java.util.Scanner;

public class DiscountEngineDemo {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Double> prices = new ArrayList<>();

        prices.add(500.0);
        prices.add(1000.0);
        prices.add(1500.0);
        prices.add(2000.0);

        System.out.println("Discount Rules:");
        System.out.println("1. 10% Discount");
        System.out.println("2. 20% Discount");
        System.out.println("3. Flat Rs.100 Discount");

        System.out.print("Choose discount rule: ");
        int choice = sc.nextInt();

        DiscountRule rule;

        if (choice == 1) {

            rule = price -> price - (price * 0.10);

        } else if (choice == 2) {

            rule = price -> price - (price * 0.20);

        } else if (choice == 3) {

            rule = price -> price - 100;

        } else {

            System.out.println("Invalid choice.");
            sc.close();
            return;
        }

        System.out.println();
        System.out.println("Original Price -> Final Price");

        for (double price : prices) {

            double finalPrice = rule.apply(price);

            System.out.println(
                price + " -> " + finalPrice
            );
        }

        sc.close();
    }
}