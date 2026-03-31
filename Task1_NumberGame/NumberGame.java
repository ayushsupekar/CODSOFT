import java.util.Random;
import java.util.Scanner;

public class NumberGame {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int totalScore = 0;
        String playAgain;

        System.out.println("===== Welcome to Number Guessing Game =====");

        do {

            int number = random.nextInt(100) + 1;
            int guess = 0;
            int attempts = 0;
            int maxAttempts = 7;

            System.out.println("\nI have selected a number between 1 and 100.");
            System.out.println("You have " + maxAttempts + " attempts to guess it.");

            while (attempts < maxAttempts) {

                System.out.print("Enter your guess: ");
                guess = sc.nextInt();
                attempts++;

                if (guess > number) {
                    System.out.println("Too High!");
                } 
                else if (guess < number) {
                    System.out.println("Too Low!");
                } 
                else {
                    int score = (maxAttempts - attempts + 1) * 10;
                    totalScore += score;

                    System.out.println("Correct! You guessed the number in " + attempts + " attempts.");
                    System.out.println("You earned " + score + " points.");
                    break;
                }

                if (attempts == maxAttempts) {
                    System.out.println("You used all attempts!");
                    System.out.println("The correct number was: " + number);
                }
            }

            System.out.print("\nDo you want to play another round? (yes/no): ");
            playAgain = sc.next();

        } while (playAgain.equalsIgnoreCase("yes"));

        System.out.println("\nYour Total Score: " + totalScore);
        System.out.println("Thank you for playing!");

        sc.close();
    }
}