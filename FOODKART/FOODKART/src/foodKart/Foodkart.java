// Foodkart.java
package foodKart;

import java.util.*;

public class Foodkart {
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Restaurant> restaurants = new HashMap<>();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMenu();
            choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    registerRestaurant(scanner);
                    break;
                case 4:
                    placeOrder(scanner);
                    break;
                case 5:
                    updateFoodQuantity(scanner);
                    break;
                case 6:
                    createReview(scanner);
                    break;
                case 7:
                    showRestaurants(scanner);
                    break;
                case 8:
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 8);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("Welcome to FoodKart!");
        System.out.println("1. Register User");
        System.out.println("2. Login User");
        System.out.println("3. Register Restaurant");
        System.out.println("4. Order Food");
        System.out.println("5. Change Food Quantity (Owner Only)");
        System.out.println("6. Rate a Restaurant");
        System.out.println("7. Show Restaurants");
        System.out.println("8. Exit");
        System.out.print("Please select an option: ");
    }

    private static int getUserChoice(Scanner scanner) {
        return scanner.nextInt();
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.next();
        System.out.print("Enter your gender (M/F): ");
        String gender = scanner.next();
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.next();
        System.out.print("Enter your pincode: ");
        String pincode = scanner.next();

        if (users.containsKey(phoneNumber)) {
            System.out.println("User with phone number " + phoneNumber + " already exists!");
            return;
        }

        // Ask if the user wants to register as an owner
        System.out.print("Do you want to register as a restaurant owner? (yes/no): ");
        String ownerResponse = scanner.next();
        boolean isOwner = ownerResponse.equalsIgnoreCase("yes");

        User user = new User(name, gender, phoneNumber, pincode, isOwner);
        users.put(phoneNumber, user);
        System.out.println("User " + name + " registered successfully.");
    }

    private static void loginUser(Scanner scanner) {
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.next();

        if (!users.containsKey(phoneNumber)) {
            System.out.println("Invalid phone number! Please register.");
            return;
        }

        loggedInUser = users.get(phoneNumber);
        System.out.println("User " + loggedInUser.getName() + " logged in successfully.");
    }

    private static void registerRestaurant(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        // Check if the logged-in user is a restaurant owner
        if (!loggedInUser.isOwner()) {
            System.out.println("Only restaurant owners can register a restaurant.");
            return;
        }

        System.out.print("Enter restaurant name: ");
        String name = scanner.next();
        System.out.print("Enter available pincodes (comma-separated): ");
        String pincodeInput = scanner.next();
        List<String> pincodes = Arrays.asList(pincodeInput.split(","));
        System.out.print("Enter dish name: ");
        String dish = scanner.next();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter initial quantity: ");
        int quantity = scanner.nextInt();

        if (restaurants.containsKey(name)) {
            System.out.println("Restaurant " + name + " is already registered.");
            return;
        }

        Restaurant restaurant = new Restaurant(name, pincodes, dish, price, quantity, loggedInUser.getPhoneNumber());
        restaurants.put(name, restaurant);
        System.out.println("Restaurant " + name + " registered successfully.");
    }

    private static void placeOrder(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter restaurant name to order from: ");
        String restaurantName = scanner.next();
        System.out.print("Enter quantity to order: ");
        int quantity = scanner.nextInt();

        Restaurant restaurant = restaurants.get(restaurantName);
        if (restaurant == null) {
            System.out.println("Restaurant not found!");
            return;
        }
        if (!restaurant.deliversTo(loggedInUser.getPincode())) {
            System.out.println("Restaurant does not deliver to your area.");
            return;
        }
        if (!restaurant.hasEnoughQuantity(quantity)) {
            System.out.println("Not enough quantity available.");
            return;
        }
        restaurant.decreaseQuantity(quantity, loggedInUser.getPhoneNumber());
        System.out.println("Order placed successfully from " + restaurantName + ".");
    }

    private static void updateFoodQuantity(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter restaurant name to update quantity: ");
        String restaurantName = scanner.next();
        System.out.print("Enter new quantity: ");
        int newQuantity = scanner.nextInt();

        Restaurant restaurant = restaurants.get(restaurantName);
        if (restaurant == null || !restaurant.getOwnerId().equals(loggedInUser.getPhoneNumber())) {
            System.out.println("You are not authorized to update this restaurant's quantity.");
            return;
        }

        restaurant.updateQuantity(newQuantity, loggedInUser.getPhoneNumber());
        System.out.println("Quantity updated successfully for " + restaurantName + ".");
    }

    private static void createReview(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("Please log in to add a review.");
            return;
        }

        System.out.print("Enter restaurant name to review: ");
        String restaurantName = scanner.next();
        System.out.print("Enter your rating (1-5): ");
        int rating = scanner.nextInt();
        String comment = "";
        System.out.print("Enter your comment (optional): ");
        comment = scanner.next();

        Restaurant restaurant = restaurants.get(restaurantName);
        if (restaurant == null) {
            System.out.println("Restaurant not found!");
            return;
        }
        if (restaurant.getOwnerId().equals(loggedInUser.getPhoneNumber())) {
            System.out.println("You cannot rate your own restaurant.");
            return; // Prevent owner from rating their own restaurant
        }
        restaurant.addReview(rating, comment);
        System.out.println("Review added to " + restaurantName + ".");
    }

    private static void showRestaurants(Scanner scanner) {
        System.out.println("Sort by: 1. Rating 2. Price");
        int sortChoice = scanner.nextInt();

        List<Restaurant> sortedRestaurants = new ArrayList<>(restaurants.values());

        if (sortChoice == 1) {
            sortedRestaurants.sort((r1, r2) -> Double.compare(r2.getAverageRating(), r1.getAverageRating()));
        } else if (sortChoice == 2) {
            sortedRestaurants.sort(Comparator.comparingDouble(Restaurant::getPrice));
        } else {
            System.out.println("Invalid sort key!");
            return;
        }

        for (Restaurant restaurant : sortedRestaurants) {
            System.out.println("Restaurant: " + restaurant.getName() + ", Dish: " + restaurant.getDish() +
                ", Price: " + restaurant.getPrice() + ", Rating: " + restaurant.getAverageRating() +
                ", Quantity: " + restaurant.getQuantity());
        }
    }
}
