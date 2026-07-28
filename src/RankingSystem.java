import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RankingSystem {
    private static final int TRAINING_ITEMS = 10;
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
        System.out.println(
            "\"" + cleanedName + "\" already exists at rank #"
            + existingRank + "."
        );
        return false;
    }

    Item closestMatch = findClosestMatch(cleanedName);

if (closestMatch != null) {
    System.out.println(
        "\nDid you mean \"" + closestMatch.getName() + "\"?"
    );

    System.out.println(
        "It already exists at rank #"
        + findItemRank(closestMatch.getName()) + "."
    );

    System.out.println("1. Yes, view the existing item");
    System.out.println("2. No, add my entry anyway");
    System.out.println("3. Cancel");

    int choice = readChoiceInRange(
        input,
        "Choose an option: ",
        1,
        3
    );

    if (choice == 1) {
        searchItem(closestMatch.getName(), input);
        return false;
    }

    if (choice == 3) {
        System.out.println("Add cancelled.");
        return false;
    }
}
    // Temporary rating while comparisons determine its position
    Item newItem = new Item(cleanedName, 0.0);

    insertItem(newItem, input);

    int newRank = findItemRank(newItem.getName());

    System.out.println(
        "\n" + newItem.getName()
        + " was placed at rank #" + newRank + "."
    );

    displayNearbyItems(newRank);

   if (rankings.size() <= TRAINING_ITEMS) {
    double rating = readRating(input);
    newItem.setRating(rating);
} else {
    double rating = calculateRating(newItem);
    newItem.setRating(rating);

    System.out.printf(
        "\nBased on its ranking position, "
        + "%s received a rating of %.1f/10.%n",
        newItem.getName(),
        rating
    );
}

saveRankings();

    System.out.printf(
        "%s was saved with a rating of %.1f/10.%n",
        newItem.getName(),
        newItem.getRating()
    );

    return true;
}

    private double calculateRating(Item item) {
    int index = rankings.indexOf(item);

    if (index == -1) {
        throw new IllegalArgumentException(
            "The item must be ranked before calculating its rating."
        );
    }

    if (rankings.size() == 1) {
        return 5.0;
    }

    double rating;

    // New highest-ranked item
    if (index == 0) {
        double oldTopRating =
            rankings.get(1).getRating();

        rating = Math.min(
            10.0,
            oldTopRating + 0.1
        );
    }

    // New lowest-ranked item
    else if (index == rankings.size() - 1) {
        double oldBottomRating =
            rankings.get(index - 1).getRating();

        rating = Math.max(
            1.0,
            oldBottomRating - 0.1
        );
    }

    // New item is between two items:
    // inherit the rating directly below it
    else {
        rating = rankings
            .get(index + 1)
            .getRating();
    }

    return Math.round(rating * 10.0) / 10.0;
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

    public void searchItem(String itemName, Scanner input) {
    String cleanedName = itemName.trim();

    if (cleanedName.isEmpty()) {
        System.out.println("Search name cannot be empty.");
        return;
    }

    int rank = findItemRank(cleanedName);

    // Exact match
    if (rank != -1) {
        displaySearchResult(rank);
        return;
    }

    // No exact match, so check for a likely misspelling
    Item closestMatch = findClosestMatch(cleanedName);

    if (closestMatch != null) {
        System.out.println(
            "\nDid you mean \"" + closestMatch.getName() + "\"?"
        );
        System.out.println("1. Yes");
        System.out.println("2. No");

        int choice = readChoiceInRange(
            input,
            "Choose an option: ",
            1,
            2
        );

        if (choice == 1) {
            int closestRank = findItemRank(
                closestMatch.getName()
            );

            displaySearchResult(closestRank);
            return;
        }
    }

    System.out.println(
        "\"" + cleanedName + "\" was not found in your rankings."
    );
}

    private void displaySearchResult(int rank) {
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

    private double readRating(Scanner input) {
    while (true) {
        System.out.print(
            "\nEnter your rating from 1.0 to 10.0: "
        );

        String line = input.nextLine().trim();

        try {
            double rating = Double.parseDouble(line);

            if (rating >= 1.0 && rating <= 10.0) {
                return rating;
            }

            System.out.println(
                "Please enter a rating from 1.0 to 10.0."
            );

        } catch (NumberFormatException e) {
            System.out.println(
                "Invalid input. Please enter a valid number."
            );
        }
    }
}

    private void displayNearbyItems(int rankNumber) {
    int index = rankNumber - 1;

    System.out.println("\nNearby rankings:");

    if (index > 0) {
        Item above = rankings.get(index - 1);

        System.out.printf(
            "#%d %s — %.1f/10%n",
            index,
            above.getName(),
            above.getRating()
        );
    }

    System.out.println(
        "#" + rankNumber + " "
        + rankings.get(index).getName()
        + " — Not rated yet"
    );

    if (index < rankings.size() - 1) {
        Item below = rankings.get(index + 1);

        System.out.printf(
            "#%d %s — %.1f/10%n",
            rankNumber + 1,
            below.getName(),
            below.getRating()
        );
    }
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

    private int levenshteinDistance(String first, String second) {
    String a = first.toLowerCase();
    String b = second.toLowerCase();

    int[][] distance = new int[a.length() + 1][b.length() + 1];

    for (int i = 0; i <= a.length(); i++) {
        distance[i][0] = i;
    }

    for (int j = 0; j <= b.length(); j++) {
        distance[0][j] = j;
    }

    for (int i = 1; i <= a.length(); i++) {
        for (int j = 1; j <= b.length(); j++) {
            int substitutionCost =
                a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

            int deletion = distance[i - 1][j] + 1;
            int insertion = distance[i][j - 1] + 1;
            int substitution =
                distance[i - 1][j - 1] + substitutionCost;

            distance[i][j] = Math.min(
                Math.min(deletion, insertion),
                substitution
            );
        }
    }

    return distance[a.length()][b.length()];
}

    private Item findClosestMatch(String itemName) {
    if (rankings.isEmpty()) {
        return null;
    }

    Item closestItem = null;
    int smallestDistance = Integer.MAX_VALUE;

    for (Item item : rankings) {
        int distance = levenshteinDistance(
            itemName.trim(),
            item.getName()
        );

        if (distance < smallestDistance) {
            smallestDistance = distance;
            closestItem = item;
        }
    }

    int allowedDistance = Math.max(
        2,
        itemName.trim().length() / 4
    );

    if (smallestDistance <= allowedDistance) {
        return closestItem;
    }

    return null;
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

    private int readChoiceInRange(
    Scanner input,
    String prompt,
    int minimum,
    int maximum
) {
    while (true) {
        System.out.print(prompt);
        String line = input.nextLine().trim();

        try {
            int choice = Integer.parseInt(line);

            if (choice >= minimum && choice <= maximum) {
                return choice;
            }

            System.out.println(
                "Please enter a number from "
                + minimum + " to " + maximum + "."
            );
        } catch (NumberFormatException e) {
            System.out.println(
                "Invalid input. Please enter a whole number."
            );
        }
    }
}
}
