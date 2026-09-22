import java.util.Scanner;

public class GuardedCalculator {
    static class DivideByZeroException extends Exception {
        public DivideByZeroException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int attempt = 1;

        while (true) {
            try {
                System.out.print("Enter first number: ");
                double first = readNumber(sc);

                System.out.print("Enter second number: ");
                double second = readNumber(sc);

                System.out.print("Enter operator (+, -, *, /): ");
                char operator = sc.next().charAt(0);

                double result;
                switch (operator) {
                    case '+':
                        result = first + second;
                        break;
                    case '-':
                        result = first - second;
                        break;
                    case '*':
                        result = first * second;
                        break;
                    case '/':
                        if (second == 0) {
                            throw new DivideByZeroException("Cannot divide by zero.");
                        }
                        result = first / second;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator. Please use +, -, *, or /.");
                }

                System.out.println("Result: " + result);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number input. Please enter only numeric values.");
            } catch (DivideByZeroException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } finally {
                System.out.println("Attempt " + attempt + " logged.");
                attempt++;
            }
        }

        sc.close();
    }

    private static double readNumber(Scanner sc) {
        String value = sc.next();
        return Double.parseDouble(value);
    }
}
