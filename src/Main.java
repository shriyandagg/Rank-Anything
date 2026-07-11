import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RankingSystem rankingSystem = new RankingSystem();
        Scanner input = new Scanner(System.in);

        boolean running = true;

        while (running) {
            displayMenu();

            int choice = readInt(input, "Choose an option: ");

            switch (choice) {
                case 1:
                    rankingSystem.displayRankings();
                    break;

                case 2:
                    handleAddItem(rankingSystem, input);
                    break;

                case 3:
                    handleDeleteItem(rankingSystem, input);
                    break;

                case 4:
                    handleSearchItem(rankingSystem, input);
                    break;

                case 5:
                    displayStatistics(rankingSystem);
                    break;

                case 6:
                    running = false;
                    System.out.println("\nGoodbye!");
                    break;

                default:
                    System.out.println(
                        "Invalid option. Please choose a number from 1 to 6."
                    );
            }
        }

        input.close();
    }

    private static void displayMenu() {
        System.out.println("\n==================================");
        System.out.println("            RankAnything");
        System.out.println("==================================");
        System.out.println("1. View Rankings");
        System.out.println("2. Add Item");
        System.out.println("3. Delete Item");
        System.out.println("4. Search Item");
        System.out.println("5. View Statistics");
        System.out.println("6. Exit");
        System.out.println("==================================");
    }

    private static void handleAddItem(
        RankingSystem rankingSystem,
        Scanner input
    ) {
        System.out.print("Enter a new item: ");
        String itemName = input.nextLine();

        boolean added = rankingSystem.addItem(itemName, input);

        if (added) {
            rankingSystem.displayRankings();
        }
    }

    private static void handleDeleteItem(
        RankingSystem rankingSystem,
        Scanner input
    ) {
        if (rankingSystem.getItemCount() == 0) {
            System.out.println("There are no items to delete.");
            return;
        }

        rankingSystem.displayRankings();

        int rankNumber = readInt(
            input,
            "\nEnter the ranking number to delete: "
        );

        rankingSystem.deleteItem(rankNumber);
    }

    private static void handleSearchItem(
        RankingSystem rankingSystem,
        Scanner input
    ) {
        System.out.print("Enter the item name to search: ");
        String itemName = input.nextLine();

        rankingSystem.searchItem(itemName);
    }

    private static void displayStatistics(
        RankingSystem rankingSystem
    ) {
        System.out.println("\n==================================");
        System.out.println("             Statistics");
        System.out.println("==================================");
        System.out.println(
            "Total ranked items: " + rankingSystem.getItemCount()
        );
    }

    private static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();

            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid input. Please enter a whole number."
                );
            }
        }
    }
}
