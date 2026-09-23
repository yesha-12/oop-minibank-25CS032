public class CardDriver {
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

        for (Card current : input) {
            for (int j = 0; j < count; j++) {
                if (current.equals(cards[j])) {
                    System.out.println("Duplicate found: " + current);
                    break;
                }
            }

            cards[count] = current;
            count++;
        }
    }
}
