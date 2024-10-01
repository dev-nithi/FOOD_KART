// User.java
package foodKart;

public class User {
    private String name;
    private String gender;
    private String phoneNumber;
    private String pincode;
    private boolean isOwner; // New field to distinguish user type

    public User(String name, String gender, String phoneNumber, String pincode, boolean isOwner) {
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.pincode = pincode;
        this.isOwner = isOwner; // Set user type during registration
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public boolean isOwner() {
        return isOwner; // Check if the user is a restaurant owner
    }

    public void setOwner(boolean owner) {
        isOwner = owner; // Method to set user as an owner (can be used during login)
    }
}
