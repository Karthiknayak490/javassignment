package foodsyste;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class FoodOrder {
    private static final String FILE_PATH = "D:\\java assignment\\foodsyste\\src\\foodsyste\\menu.txt";
    public static Map<String, List<String>> menu = new HashMap<>();
    public static Map<String, Double> prices = new HashMap<>();
    private static Map<String, Integer> quantities = new HashMap<>();

    public static void main(String[] args) {
        loadMenuFromFile();

    }

    public static void loadMenuFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String category = parts[0].trim();
                    String itemName = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int quantity = Integer.parseInt(parts[3].trim());

                    menu.putIfAbsent(category, new ArrayList<>());
                    menu.get(category).add(itemName);
                    prices.put(itemName, price);
                    quantities.put(itemName, quantity);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading menu from file: " + e.getMessage());
        }
    }
    public static void orderFood() {
        int maxLength = menu.keySet().stream().mapToInt(String::length).max().orElse(0);

        // Print menu in table-like format
        System.out.printf("%-" + (maxLength + 5) + "s%s%17s\n", "Category", "Items", "Price");
        for (String category : menu.keySet()) {
            System.out.printf("%-" + (maxLength + 5) + "s", category + ":");
            List<String> items = menu.get(category);
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);
                double price = prices.get(item);
                if (i > 0) {
                    System.out.printf("%" + (maxLength + 5) + "s", "");
                }
                System.out.printf("- %-15s %.2f\n", item, price);
            }
            System.out.println();
        }

        System.out.println("Menu:");
        for (String category : menu.keySet()) {
            System.out.println(category + ":");
            for (String item : menu.get(category)) {
                double price = prices.getOrDefault(item, 0.0);
                System.out.println("  - " + item + " : " + price);
            }
        }
    }

}