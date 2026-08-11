import java.util.Random;
import java.util.Scanner;

enum Move {
    ROCK, PAPER, SCISSORS, LIZARD, SPOCK
}

public class RPSLS {

    public static int winner(Move a, Move b) {
        if (a == b) {
            return 0;
        }

        return switch (a) {
            case ROCK ->
                (b == Move.SCISSORS || b == Move.LIZARD) ? 1 : -1;

            case PAPER ->
                (b == Move.ROCK || b == Move.SPOCK) ? 1 : -1;

            case SCISSORS ->
                (b == Move.PAPER || b == Move.LIZARD) ? 1 : -1;

            case LIZARD ->
                (b == Move.SPOCK || b == Move.PAPER) ? 1 : -1;

            case SPOCK ->
                (b == Move.SCISSORS || b == Move.ROCK) ? 1 : -1;
        };
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int playerScore = 0;
        int computerScore = 0;

        System.out.println("Moves: ROCK, PAPER, SCISSORS, LIZARD, SPOCK");

        for (int round = 1; round <= 5; round++) {
            System.out.println("\nRound " + round);
            System.out.print("Enter your move: ");

            Move playerMove;
            try {
                playerMove = Move.valueOf(sc.next().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid move! Round skipped.");
                continue;
            }

            Move computerMove = Move.values()[random.nextInt(Move.values().length)];

            System.out.println("You: " + playerMove);
            System.out.println("Computer: " + computerMove);

            int result = winner(playerMove, computerMove);

            if (result == 1) {
                System.out.println("You win this round!");
                playerScore++;
            } else if (result == -1) {
                System.out.println("Computer wins this round!");
                computerScore++;
            } else {
                System.out.println("This round is a tie!");
            }
        }

        
        System.out.println("Your Score: " + playerScore);
        System.out.println("Computer Score: " + computerScore);

        if (playerScore > computerScore) {
            System.out.println("Overall Winner: You win " + playerScore + "–" + computerScore + "!");
        } else if (computerScore > playerScore) {
            System.out.println("Overall Winner: Computer wins " + computerScore + "–" + playerScore + "!");
        } else {
            System.out.println("Overall Result: It's a tie " + playerScore + "–" + computerScore + "!");
        }

        sc.close();
    }
}