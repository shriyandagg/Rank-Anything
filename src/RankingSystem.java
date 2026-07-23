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
            Item item = rankings.get(i);

            System.out.printf(
                "%d. %s — %.1f/10%n",
                i + 1,
                item.getName(),
                item.getRating()
            );
        }
    }

    public boolean addItem(
    String itemName,
    Scanner input
) {
    String cleanedName = itemName.trim();

    if (cleanedName.isEmpty()) {
        System.out.println("Item name cannot be empty.");
        return false;
    }

    int existingRank = findItemRank(cleanedName);

    if (existingRank != -1) {
        Item existingItem = rankings.get(existingRank - 1);

        System.out.println(
            "\"" + existingItem.getName()
            + "\" already exists at rank #" + existingRank + "."
        );

        return false;
    }

    Item newItem = new Item(cleanedName, 0.0);

    insertItem(newItem, input);
    saveRankings();

    System.out.println(
        "\n" + newItem.getName()
        + " was added successfully."
    );

    return true;
}

private void insertItem(Item item, Scanner input) {
    int low = 0;
    int high = rankings.size();

    while (low < high) {
        int middle = low + (high - low) / 2;
        Item existingItem = rankings.get(middle);

        System.out.println("\nWhich do you prefer?");
        System.out.println("1. " + item.getName());
        System.out.println("2. " + existingItem.getName());

        int choice = readComparisonChoice(input);

        if (choice == 1) {
            high = middle;
        } else {
            low = middle + 1;
        }
    }

    rankings.add(low, item);
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

        System.out.printf(
            "Rating: %.1f/10%n",
            foundItem.getRating()
        );
    }

    public boolean rerankItem(int rankNumber, Scanner input) {
    int index = rankNumber - 1;

    if (index < 0 || index >= rankings.size()) {
        System.out.println("That ranking number does not exist.");
        return false;
    }

    Item item = rankings.remove(index);

    insertItem(item, input);
    saveRankings();

    int newRank = findItemRank(item.getName());

    System.out.println(
        item.getName()
        + " was re-ranked to position #"
        + newRank + "."
    );

    return true;
}

    public int getItemCount() {
        return rankings.size();
    }

    public double getAverageRating() {
        if (rankings.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;

        for (Item item : rankings) {
            total += item.getRating();
        }

        return total / rankings.size();
    }

    public Item getTopRankedItem() {
        if (rankings.isEmpty()) {
            return null;
        }

        return rankings.get(0);
    }

    public Item getBottomRankedItem() {
        if (rankings.isEmpty()) {
            return null;
        }

        return rankings.get(rankings.size() - 1);
    }

    private int findItemRank(String itemName) {
        for (int i = 0; i < rankings.size(); i++) {
            if (
                rankings.get(i)
                    .getName()
                    .equalsIgnoreCase(itemName)
            ) {
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
                System.out.println(
                    "Invalid input. Please enter 1 or 2."
                );
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

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    String[] parts = line.split("\\|");

                    if (parts.length != 2) {
                        System.out.println(
                            "Skipped an invalid ranking entry: " + line
                        );
                        continue;
                    }

                    String name = parts[0].trim();
                    double rating = Double.parseDouble(
                        parts[1].trim()
                    );

                    if (
                        name.isEmpty()
                        || rating < 1.0
                        || rating > 10.0
                    ) {
                        System.out.println(
                            "Skipped an invalid ranking entry: " + line
                        );
                        continue;
                    }

                    rankings.add(new Item(name, rating));
                } catch (NumberFormatException e) {
                    System.out.println(
                        "Skipped an invalid ranking entry: " + line
                    );
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
                writer.println(
                    item.getName()
                    + "|"
                    + item.getRating()
                );
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
    
    public boolean editItemName(int rankNumber, String newName) {
    int index = rankNumber - 1;
    String cleanedName = newName.trim();

    if (index < 0 || index >= rankings.size()) {
        System.out.println("That ranking number does not exist.");
        return false;
    }

    if (cleanedName.isEmpty()) {
        System.out.println("Item name cannot be empty.");
        return false;
    }

    int existingRank = findItemRank(cleanedName);

    if (existingRank != -1 && existingRank != rankNumber) {
        System.out.println(
            "\"" + cleanedName + "\" already exists at rank #"
            + existingRank + "."
        );
        return false;
    }

    Item item = rankings.get(index);
    String oldName = item.getName();

    item.setName(cleanedName);
    saveRankings();

    System.out.println(
        "\"" + oldName + "\" was renamed to \""
        + cleanedName + "\"."
    );

    return true;
}
    public boolean editRating(int rankNumber, double newRating) {
    int index = rankNumber - 1;

    if (index < 0 || index >= rankings.size()) {
        System.out.println("That ranking number does not exist.");
        return false;
    }

    if (newRating < 1.0 || newRating > 10.0) {
        System.out.println(
            "The rating must be between 1.0 and 10.0."
        );
        return false;
    }

    Item item = rankings.get(index);
    double oldRating = item.getRating();

    item.setRating(newRating);
    saveRankings();

    System.out.printf(
        "%s's manual rating changed from %.1f to %.1f.%n",
        item.getName(),
        oldRating,
        newRating
    );

    return true;
    }
}
