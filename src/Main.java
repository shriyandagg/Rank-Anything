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
        handleEditItem(rankingSystem, input);
        break;

    case 6:
        displayStatistics(rankingSystem);
        break;

    case 7:
        running = false;
        System.out.println("\nGoodbye!");
        break;

    default:
        System.out.println(
            "Invalid option. Please choose a number from 1 to 7."
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
    System.out.println("5. Edit Item");
    System.out.println("6. View Statistics");
    System.out.println("7. Exit");
    System.out.println("==================================");
}

    private static void handleAddItem(
        RankingSystem rankingSystem,
        Scanner input
    ) {
        System.out.print("Enter a new item: ");
        String itemName = input.nextLine();

        double manualRating = readDoubleInRange(
            input,
            "Enter your rating from 1.0 to 10.0: ",
            1.0,
            10.0
        );

        boolean added = rankingSystem.addItem(
            itemName,
            manualRating,
            input
        );

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

        int itemCount = rankingSystem.getItemCount();

        System.out.println("Total ranked items: " + itemCount);

        if (itemCount == 0) {
            System.out.println(
                "Add some items before viewing more statistics."
            );
            return;
        }

        System.out.printf(
            "Average manual rating: %.2f/10%n",
            rankingSystem.getAverageManualRating()
        );

        Item topItem = rankingSystem.getTopRankedItem();
        Item bottomItem = rankingSystem.getBottomRankedItem();

        System.out.println(
            "Top ranked item: " + topItem.getName()
        );

        System.out.println(
            "Lowest ranked item: " + bottomItem.getName()
        );
    }

    private static int readInt(
        Scanner input,
        String prompt
    ) {
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
    private static void handleEditItem(
    RankingSystem rankingSystem,
    Scanner input
) {
    if (rankingSystem.getItemCount() == 0) {
        System.out.println("There are no items to edit.");
        return;
    }

    rankingSystem.displayRankings();

    int rankNumber = readInt(
        input,
        "\nEnter the ranking number to edit: "
    );

    System.out.println("\nWhat would you like to edit?");
    System.out.println("1. Rename item");
    System.out.println("2. Change manual rating");
    System.out.println("3. Re-rank item");
    System.out.println("4. Cancel");

    int editChoice = readInt(input, "Choose an option: ");

    switch (editChoice) {
    case 1:
        System.out.print("Enter the new name: ");
        String newName = input.nextLine();

        rankingSystem.editItemName(rankNumber, newName);
        break;

    case 2:
        double newRating = readDoubleInRange(
            input,
            "Enter the new rating from 1.0 to 10.0: ",
            1.0,
            10.0
        );

        rankingSystem.editManualRating(
            rankNumber,
            newRating
        );
        break;

    case 3:
        rankingSystem.rerankItem(rankNumber, input);
        break;

    case 4:
        System.out.println("Edit cancelled.");
        break;

    default:
        System.out.println("Invalid edit option.");
        break;
    }
}
    private static double readDoubleInRange(
        Scanner input,
        String prompt,
        double minimum,
        double maximum
    ) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();

            try {
                double value = Double.parseDouble(line);

                if (value >= minimum && value <= maximum) {
                    return value;
                }

                System.out.println(
                    "Please enter a number from "
                    + minimum + " to " + maximum + "."
                );
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid input. Please enter a valid number."
                );
            }
        }
    }
}