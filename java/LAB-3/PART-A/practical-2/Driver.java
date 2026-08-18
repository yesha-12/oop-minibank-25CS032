public class Driver {
    public static void main(String[] args) {

        Card[] cards = new Card[5];
        int count = 0;

        Card[] input = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Queen", "Diamonds"),
            new Card("Ace", "Spades"),
            new Card("Jack", "Clubs")
        };

        for (int i = 0; i < input.length; i++) {
            boolean duplicate = false;

            for (int j = 0; j < count; j++) {
                if (input[i].equals(cards[j])) {
                    System.out.println("Duplicate found: " + input[i]);
                    duplicate = true;
                    break;
                }
            }

            cards[count] = input[i];
            count++;
        }
    }
}
