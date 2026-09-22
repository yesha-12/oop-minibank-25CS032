import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warehouse {
    static class InvalidQuantityException extends Exception {
        public InvalidQuantityException(String message) {
            super(message);
        }
    }

    static class OutOfStockException extends Exception {
        private final int shortfall;

        public OutOfStockException(String item, int shortfall) {
            super("Not enough stock for " + item + ". Shortfall: " + shortfall);
            this.shortfall = shortfall;
        }

        public int getShortfall() {
            return shortfall;
        }
    }

    private final Map<String, Integer> stock;

    public Warehouse() {
        stock = new HashMap<>();
        stock.put("pen", 10);
        stock.put("notebook", 5);
        stock.put("eraser", 8);
    }

    public void issue(String item, int qty) throws InvalidQuantityException, OutOfStockException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }

        int available = stock.getOrDefault(item, 0);
        if (qty > available) {
            int shortfall = qty - available;
            throw new OutOfStockException(item, shortfall);
        }

        stock.put(item, available - qty);
        System.out.println("Issued " + qty + " units of " + item + ".");
    }

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        List<String[]> requests = List.of(
                new String[] { "pen", "3" },
                new String[] { "notebook", "8" },
                new String[] { "eraser", "0" },
                new String[] { "marker", "2" },
                new String[] { "pen", "7" }
        );

        for (String[] request : requests) {
            String item = request[0];
            int qty;

            try {
                qty = Integer.parseInt(request[1]);
                warehouse.issue(item, qty);
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity format for " + item + ".");
            } catch (InvalidQuantityException e) {
                System.out.println("Failed: " + e.getMessage());
            } catch (OutOfStockException e) {
                System.out.println("Failed: " + e.getMessage());
            }
        }
    }
}
