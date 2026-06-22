import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RankingSystem {
    private ArrayList<String> rankings;

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
            System.out.println((i + 1) + ". " + rankings.get(i));
        }
    }

    public void addItem(String itemName) {
        Scanner input = new Scanner(System.in);

        int position = rankings.size();

        for (int i = 0; i < rankings.size(); i++) {
            System.out.println("\nWhich do you prefer?");
            System.out.println("1. " + itemName);
            System.out.println("2. " + rankings.get(i));
            System.out.print("Enter 1 or 2: ");

            int choice = input.nextInt();

            if (choice == 1) {
                position = i;
                break;
            }
        }

        rankings.add(position, itemName);
        saveRankings();
    }

    public void loadRankings() {
        try {
            File file = new File("data/rankings.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                if (!line.trim().isEmpty()) {
                rankings.add(line);
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

            for (String anime : rankings) {
                writer.println(anime);
            }

            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error saving rankings.");
        }
    }
}