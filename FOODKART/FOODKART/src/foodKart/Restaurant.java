// Restaurant.java
package foodKart;

import java.util.*;

public class Restaurant {
    private String name;
    private String dish;
    private double price;
    private int quantity;
    private List<String> pincodes;
    private List<Review> reviews;
    private String ownerId; // Unique identifier for the restaurant owner

    public Restaurant(String name, List<String> pincodes, String dish, double price, int quantity, String ownerId) {
        this.name = name;
        this.pincodes = pincodes;
        this.dish = dish;
        this.price = price;
        this.quantity = quantity;
        this.reviews = new ArrayList<>();
        this.ownerId = ownerId; // Store the owner ID
    }

    public void addReview(int rating, String comment) {
        reviews.add(new Review(rating, comment));
    }

    public double getAverageRating() {
        if (reviews.isEmpty()) {
            return 0;
        }
        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return totalRating / (double) reviews.size();
    }

    public boolean deliversTo(String pincode) {
        return pincodes.contains(pincode);
    }

    public boolean hasEnoughQuantity(int requestedQuantity) {
        return quantity >= requestedQuantity;
    }

    public void decreaseQuantity(int quantity, String requesterId) {
        this.quantity -= quantity; // Allow decreasing quantity for orders
    }

    public void updateQuantity(int quantity, String requesterId) {
        if (isOwner(requesterId)) {
            this.quantity = quantity; // Allow owner to set new quantity
        }
    }

    private boolean isOwner(String requesterId) {
        return this.ownerId.equals(requesterId);
    }

    public String getName() {
        return name;
    }

    public String getDish() {
        return dish;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getOwnerId() {
        return ownerId; // Get the owner's phone number
    }
}
