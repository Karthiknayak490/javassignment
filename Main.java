package foodsyste;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.sql.Statement;
class Food {
    String food_name;
    String beverages;
    String dessert;
    boolean isOrdered;


    public Food(String food_name, String beverages, String dessert) {
        this.food_name = food_name;
        this.beverages = beverages;
        this.dessert = dessert;
    }
    public void insertFoodData(double totalAmount) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String query = "INSERT INTO foods (food_name, beverages, dessert, price) " +
                    "VALUES ('" + food_name + "', '" + beverages + "', '" + dessert + "', " + totalAmount + ")";
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Food data inserted successfully!");
            } else {
                System.out.println("Failed to insert food data.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting food data: " + e.getMessage());
        }
    }



    public void order_status() {
        System.out.println("Food order status - " + food_name + ":");
        System.out.println("Ordered: " + isOrdered);
    }
}

class Customer {
    String customer_name;
    String customer_address;
    String id;
    String password;
    String customer_phoneno;

    public Customer(String customer_name, String customer_address, String id, String password, String customer_phoneno) {
        this.customer_name = customer_name;
        this.customer_address = customer_address;
        this.id = id;
        this.password = password;
        this.customer_phoneno = customer_phoneno;
    }

    void login() {
        System.out.println("Logged in as " + this.customer_name);
    }
}

class Restaurant {
    String restaurant_name;
    String restaurant_address;
    String restaurant_phone_no;
    double total;

    public Restaurant(String restaurant_name, String restaurant_address, String restaurant_phoneno) {
        this.restaurant_name = restaurant_name;
        this.restaurant_address = restaurant_address;
    }
    public void insertRestaurantData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String query = "INSERT INTO restaurants (name,address,phone_no) VALUES ('" + restaurant_name + "', '" + restaurant_address + "','"+ restaurant_phone_no+ "')";
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Restaurant data inserted successfully!");
            } else {
                System.out.println("Failed to insert restaurant data.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting restaurant data: " + e.getMessage());
        }
    }
    void deliver() {
        System.out.println("Food delivered from: " + restaurant_address);
    }

    void calculate_total(double food_price) {
        if (food_price > 0) {
            total = food_price + 50.00; // Assuming delivery charge
        } else {
            total = 0.0; // No food ordered, no delivery charge
        }
        System.out.println(" Minimum Delivery charge is 50.00");
        System.out.println("Total amount to be paid: " + total);
    }

}

class Payment {
    String card_number;
    String card_holder_name;
    String payment_type;

    public void makeOnlinePayment(String card_number, String card_holder_name, String payment_type, double totalAmount) {
        System.out.println("Online payment successful!");
        System.out.println("Card Number: " + card_number);
        System.out.println("Card Holder Name: " + card_holder_name);
        System.out.println("Payment Type: " + payment_type);
        System.out.println("Total Amount Paid: $" + totalAmount);

    }

    public void makeOfflinePayment(double totalAmount) {
        System.out.println("Offline payment successful!");

        System.out.println("Total Amount Paid: " + totalAmount);
    }
}



public class Main {

    public static void main(String[] args) {
        //InsertData();
        // deleteData();
        //  updateData();
        // getData();
        getConnection();
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("------Welcome to Food Delivery System------");
            System.out.println("\nCustomer Information: ");
            System.out.println("Please enter your name:");
            String customer_name = scanner.nextLine();

            System.out.println("\nPlease enter your address:");
            String customer_address = scanner.nextLine();

            System.out.println("\nPlease enter your ID:-------- ");
            String id = scanner.nextLine();

            System.out.println("\nPlease enter your password:");
            String password = scanner.nextLine();

            System.out.println("\nPlease enter your phone number: ");
            String customer_phoneno = scanner.nextLine();

            insertData(id, customer_name, customer_address, password, customer_phoneno);

            Customer customer = new Customer(customer_name, customer_address, id, password, customer_phoneno);
            customer.login();
            System.out.println("\n\n\nRestaurant Information:");
            System.out.println("Please enter the restaurant name: ");
            String restaurant_name = scanner.nextLine();

            System.out.println("\nPlease enter the restaurant address: ");
            String restaurant_address = scanner.nextLine();

            Map<String, String> restaurantPhones = new HashMap<>();
            restaurantPhones.put("swadesh", "123-456-7890");
            restaurantPhones.put("top in tiwn", "987-654-3210");
            restaurantPhones.put("fast food", "456-789-0123");

            String phoneNumber = restaurantPhones.get(restaurant_name);
            if (phoneNumber != null) {
                System.out.println("\nRestaurant Phone Number: " + phoneNumber);

                Restaurant restaurant = new Restaurant(restaurant_name, restaurant_address, phoneNumber);

            } else {
                System.out.println("\nRestaurant phone no not found in the list.");
            }
            Restaurant restaurant = new Restaurant(restaurant_name, restaurant_address, phoneNumber);
            restaurant.insertRestaurantData();
            Map<String, List<String>> menu = new HashMap<>();
            Map<String, Double> prices = new HashMap<>();
            FoodOrder.loadMenuFromFile();
            FoodOrder.orderFood();
            System.out.println("Please enter your food_name choice: ");
            String food_name = scanner.nextLine().toLowerCase();

            System.out.println("Please enter your beverage choice: ");
            String beverages = scanner.nextLine().toLowerCase();

            System.out.println("Please enter your dessert choice: ");
            String dessert = scanner.nextLine().toLowerCase();

            double food_price = prices.getOrDefault(food_name, 0.0) +
                    prices.getOrDefault(beverages, 0.0) +
                    prices.getOrDefault(dessert, 0.0);


            Food food = new Food(food_name, beverages, dessert);
            food.insertFoodData(food_price);

            Payment payment = new Payment();
            boolean foodAvailable = food_price > 0;
            boolean paymentSuccessful = false;

            System.out.println("You have successfully entered all details.");
            if (foodAvailable) {

                System.out.println("Your order total is: " + food_price);
                restaurant.calculate_total(food_price);

                double totalAmount = food_price + 50.00; // Assuming delivery charge
                System.out.println("Please select payment method (1 for online, 2 for offline): ");
                int paymentMethod = scanner.nextInt();

                if (paymentMethod == 1) {
                    // Online payment
                    System.out.println("Please enter your card details:");
                    System.out.print("Card Number: ");
                    String card_number = scanner.next();
                    System.out.print("Card Holder Name: ");
                    String card_holder_name = scanner.next();
                    System.out.print("Payment Type: ");
                    String payment_type = scanner.next();
                    payment.makeOnlinePayment(card_number, card_holder_name, payment_type, totalAmount);

                } else if (paymentMethod == 2) {
                    // Offline payment
                    System.out.println("Please pay cash on delivery.");

                } else {
                    System.out.println("Invalid payment method.");
                }
                paymentSuccessful = true;
            } else {
                System.out.println("No food ordered. No payment required.");
            }
            if (paymentSuccessful) {
                System.out.println("Thank you for your order! Your food will be delivered to " + customer.customer_address);
                restaurant.deliver();
            } else {
                System.out.println("Thank you for being with us.");
            }
            // getData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public static Connection getConnection() {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String databaseUrl = "jdbc:mysql://localhost:3306/fooddelivery";
            String userName = "root";
            String password = "Karthik420(5)";
            Class.forName(driver);
            Connection con = DriverManager.getConnection(databaseUrl, userName, password);
            System.out.println("connection done");
            return con;
        } catch (Exception e) {
            System.out.println("some error" + e);
        }
        return null;
    }

    public static void getData() {
        try {
            Statement statement = getConnection().createStatement();
            System.out.println("-----------------------Data stored in database are --------------------------------------");
            ResultSet result = statement.executeQuery("SELECT * FROM customers ORDER BY id DESC LIMIT 1");
            if (result.next()) {
                System.out.println(result.getString("id"));
                System.out.println(result.getString("name"));
                System.out.println(result.getString("address"));
                System.out.println(result.getString("password"));
                System.out.println(result.getString("phone_no"));
            } else {
                System.out.println("No recent data found.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }


    }

    public static void InsertData() {
        try {
            Statement statement = getConnection().createStatement();
            int result = statement.executeUpdate("insert into customers(id,name,address,password,phone_no) values(3,'karthik','parkala',123,9902770418)");
            System.out.println(result);
            if (result == 1) {
                System.out.println("data inserted");
            } else {
                System.out.println("some error");
            }
        } catch (Exception e) {
            System.out.println("error" + e);

        }
    }

    public static void deleteData() {
        try {
            Statement statement = getConnection().createStatement();
            int result = statement.executeUpdate("delete from customers where id=3");
            if (result == 1) {
                System.out.println("data deleted");
            } else {
                System.out.println("error while deleting recoord");
            }
        } catch (Exception e) {
            System.out.println("error" + e);

        }
    }

    public static void updateData() {
        try {
            Statement statement = getConnection().createStatement();
            int result = statement.executeUpdate("update customers set name ='karthik nayak' where id=3");
            System.out.println(result);
            if (result == 1) {
                System.out.println("data updatedd");
            } else {
                System.out.println("some error while updateing");
            }
        } catch (Exception e) {
            System.out.println("error" + e);

        }
    }

    public static void insertData(String id, String name, String address, String password, String phoneNo) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String query = "INSERT INTO customers (id, name, address, password, phone_no) VALUES ('" + id + "', '" + name + "', '" + address + "', '" + password + "', '" + phoneNo + "')";
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully! in database");
            } else {
                System.out.println("Failed to insert data.");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
}


class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/fooddelivery";
    private static final String USER = "root";
    private static final String PASSWORD = "Karthik420(5)";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}