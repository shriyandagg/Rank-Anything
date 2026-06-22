import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        RankingSystem rankingSystem = new RankingSystem();
        Scanner input = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n=== Rank Anything ===");
            System.out.println("1. View Rankings");
            System.out.println("2. Add Item");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int choice = input.nextInt();
            input.nextLine(); // clears leftover newline

            if (choice == 1) {
                rankingSystem.displayRankings();
            } 
            else if (choice == 2) {
                System.out.print("Enter a new anime: ");
                String animeName = input.nextLine();

                rankingSystem.addItem(animeName);

                System.out.println("\nItem added.");
                rankingSystem.displayRankings();
            } 
            else if (choice == 3) {
                running = false;
                System.out.println("Goodbye!");
            } 
            else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        input.close();
    }
}