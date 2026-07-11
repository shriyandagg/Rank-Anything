import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RankingSystem {
    private static final String FILE_PATH = "data/rankings.txt";

    private final ArrayList<Item> rankings;

    public RankingSystem() {
        rankings = new ArrayList<>();
        loadRankings();
    }

    public void displayRankings() {
        System.out.println("\n==================================");
        System.out.println("          Current Rankings");
        System.out.println("==================================");

        if (rankings.isEmpty()) {
            System.out.println("No items have been ranked yet.");
            return;
        }

        for (int i = 0; i < rankings.size(); i++) {
            System.out.println((i + 1) + ". " + rankings.get(i).getName());
        }
    }

    public boolean addItem(String itemName, Scanner input) {
        String cleanedName = itemName.trim();

        if (cleanedName.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return false;
        }

        int existingRank = findItemRank(cleanedName);

        if (existingRank != -1) {
            System.out.println(
                "\"" + rankings.get(existingRank - 1).getName()
                + "\" already exists at rank #" + existingRank + "."
            );
            return false;
        }

        Item newItem = new Item(cleanedName);

        int low = 0;
        int high = rankings.size();

        while (low < high) {
            int middle = low + (high - low) / 2;
            Item existingItem = rankings.get(middle);

            System.out.println("\nWhich do you prefer?");
            System.out.println("1. " + newItem.getName());
            System.out.println("2. " + existingItem.getName());

            int choice = readComparisonChoice(input);

            if (choice == 1) {
                high = middle;
            } else {
                low = middle + 1;
            }
        }

        rankings.add(low, newItem);
        saveRankings();

        System.out.println(
            "\n" + newItem.getName()
            + " was added at rank #" + (low + 1) + "."
        );

        return true;
    }

    public boolean deleteItem(int rankNumber) {
        int index = rankNumber - 1;

        if (index < 0 || index >= rankings.size()) {
            System.out.println("That ranking number does not exist.");
            return false;
        }

        Item removedItem = rankings.remove(index);
        saveRankings();

        System.out.println(
            removedItem.getName() + " was removed from your rankings."
        );

        return true;
    }

    public void searchItem(String itemName) {
        String cleanedName = itemName.trim();

        if (cleanedName.isEmpty()) {
            System.out.println("Search name cannot be empty.");
            return;
        }

        int rank = findItemRank(cleanedName);

        if (rank == -1) {
            System.out.println(
                "\"" + cleanedName + "\" was not found in your rankings."
            );
            return;
        }

        Item foundItem = rankings.get(rank - 1);

        System.out.println("\n==================================");
        System.out.println("             Search Result");
        System.out.println("==================================");
        System.out.println("Item: " + foundItem.getName());
        System.out.println("Current rank: #" + rank);
    }

    public int getItemCount() {
        return rankings.size();
    }

    private int findItemRank(String itemName) {
        for (int i = 0; i < rankings.size(); i++) {
            if (rankings.get(i).getName().equalsIgnoreCase(itemName)) {
                return i + 1;
            }
        }

        return -1;
    }

    private int readComparisonChoice(Scanner input) {
        while (true) {
            System.out.print("Enter 1 or 2: ");
            String line = input.nextLine().trim();

            try {
                int choice = Integer.parseInt(line);

                if (choice == 1 || choice == 2) {
                    return choice;
                }

                System.out.println("Please enter either 1 or 2.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }
    }

    private void loadRankings() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createEmptyRankingsFile();
            return;
        }

        try {
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (!line.isEmpty()) {
                    rankings.add(new Item(line));
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not load the rankings file.");
        }
    }

    private void saveRankings() {
        File file = new File(FILE_PATH);
        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try {
            PrintWriter writer = new PrintWriter(file);

            for (Item item : rankings) {
                writer.println(item.getName());
            }

            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not save your rankings.");
        }
    }

    private void createEmptyRankingsFile() {
        File file = new File(FILE_PATH);
        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.close();

            System.out.println(
                "No rankings file was found. A new one was created."
            );
        } catch (FileNotFoundException e) {
            System.out.println("Could not create the rankings file.");
        }
    }
}