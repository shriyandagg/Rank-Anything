import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RankingSystem {
    private ArrayList<Item> rankings;

    public RankingSystem() {
        rankings = new ArrayList<>();
        loadRankings();
    }

    public void displayRankings() {
        System.out.println("\n=== Current Rankings ===");

        if (rankings.isEmpty()) {
            System.out.println("No rankings yet.");
            return;
        }

        for (int i = 0; i < rankings.size(); i++) {
            System.out.println((i + 1) + ". " + rankings.get(i).getName());
        }
    }

    public void addItem(String itemName) {
    Scanner input = new Scanner(System.in);

    Item newItem = new Item(itemName);

    int low = 0;
    int high = rankings.size();

    while (low < high) {
        int mid = (low + high) / 2;

        System.out.println("\nWhich do you prefer?");
        System.out.println("1. " + newItem.getName());
        System.out.println("2. " + rankings.get(mid).getName());
        System.out.print("Enter 1 or 2: ");

        int choice = input.nextInt();

        if (choice == 1) {
            high = mid;
        } else {
            low = mid + 1;
        }
    }

    rankings.add(low, newItem);
    saveRankings();
}

    public void deleteItem(int rankNumber) {
    int index = rankNumber - 1;

    if (index < 0 || index >= rankings.size()) {
        System.out.println("Invalid ranking number.");
        return;
    }

    Item removedItem = rankings.remove(index);
    saveRankings();

    System.out.println(removedItem.getName() + " was removed.");
}

    public void loadRankings() {
        try {
            File file = new File("data/rankings.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                if (!line.trim().isEmpty()) {
                    rankings.add(new Item(line));
                }
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Rankings file not found.");
        }
    }

    public void saveRankings() {
        try {
            PrintWriter writer = new PrintWriter("data/rankings.txt");

            for (Item item : rankings) {
                writer.println(item.getName());
            }

            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error saving rankings.");
        }
    }
}