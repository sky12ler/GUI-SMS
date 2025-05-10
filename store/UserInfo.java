package store;

import java.util.Scanner;

//for password hashing
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class UserInfo {
    private String name;
    private String userID;
    private String passwordHash;
    private String userRole;

    // Default constructor
    public UserInfo() {
        this.name = "Guest";
        this.userID = "guest";
        this.userRole = "user";
    }

    // Constructor with parameters (used for retrieving from the database)
    public UserInfo(String name, String password, String userRole) {
        this.name = name;
        this.passwordHash = password;
        this.userRole = userRole;
        this.userID = generateUserID(name);
    }

    // Method to generate user ID based on name
    private String generateUserID(String name) {
        String[] parts = name.split(" ");
        if (parts.length > 1) {
            return parts[0].substring(0, 1).toUpperCase() + " " + parts[parts.length - 1];
        } else {
            return "guest";
        }
    }

    // For CLI purpose, Login/Register a user
    public void setName(Scanner scanner) {
        System.out.print("Enter full name: ");
        this.name = scanner.nextLine().trim();
        boolean userExists = false;
        
        // Check if user already exists in the ArrayList
        if (DatabaseHandler.isConnect()) { //the field DatabaseHandler.connect is not visible
            for (UserInfo user : DatabaseHandler.userInfos) {
                if (user.getUsername().equals(this.name)) {
                    userExists = true;
                    this.passwordHash=user.passwordHash;
                    this.userRole=user.userRole;
                    this.userID=user.userID;
                    break;
                }
            }
            
            if (userExists) {
                // Prompt user for password and verify it
                System.out.print("User exists. Please enter your password: ");
                boolean validPassword = false;
                while (validPassword != true) {
                    String password = scanner.nextLine();
                    
                    for (UserInfo user : DatabaseHandler.userInfos) {
                        if (user.getUsername().equals(this.name)) {
                            validPassword = user.verifyPassword(password);
                            break;
                        }
                    }
                    
                    if (!validPassword) {
                        System.out.println("Incorrect password! Please enter your password again: ");
                    } else { 	
                        System.out.println("Login successful.");
                        break;
                    }
                }

            }
            else {
                // User doesn't exist, prompt for password and register
                System.out.print("User does not exist. Please enter your password to register: ");
                String password = scanner.nextLine();
                System.out.print("Confirm password: ");
                String confirmPassword = scanner.nextLine();
                
                while(!password.equals(confirmPassword)) {
                	//please reenter
                	System.out.println("Passwords do not match, please enter again!");
                    System.out.print("Please enter your password to register: ");
                    password = scanner.nextLine();
                    System.out.print("Confirm password: ");
                    confirmPassword = scanner.nextLine();
                }
                
                if (password.equals(confirmPassword)) {
                    // Register the user
                	this.passwordHash = hashPassword(password);
                	this.userRole = "user";
                    DatabaseHandler.userInfos.add(this); // Add to the list
                    System.out.println("User registered successfully.");
                    System.out.println(this.passwordHash);
                } else {
                    
                }

            try {
                String query = String.format(
                    "INSERT INTO UserInfo VALUES ('%s', '%s','%s')",
                    this.name,
                    this.passwordHash,
                    this.userRole
                );
                DatabaseHandler.addDatabase(query);
            } catch (Exception e) {
                System.err.println("Error saving user to database: " + e.getMessage());
            }
        }
        
    }
        this.userID = generateUserID(this.name);
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "User Name: " + name + "\nUser ID: " + userID;
    }

    public String getName() {
        return name;
    }
    

    // Function to Hashing the password 
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            // convert to HexDec form
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
    
    // verify the password (login)
    public boolean verifyPassword(String inputPassword) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput.equals(this.passwordHash);
    }

    // Getters
    public String getUsername() {
    	return name; 
    	}
    
    public String getPasswordHash() {
    	return passwordHash; 
    	}
    
    public String getuserRole() {
    	return userRole; 
    	}
    
    //Only for GUI purpose
    public boolean authenticateUser(String username, String password) {
        this.name = username.trim();
        
        // Check if user exists in memory
        for (UserInfo user : DatabaseHandler.userInfos) {
            if (user.getUsername().equals(this.name)) {
                // User exists - verify password
                if (user.verifyPassword(password)) {
                    this.passwordHash = user.passwordHash;
                    this.userRole = user.userRole;
                    this.userID = user.userID;
                    return true; // Successful login
                }
                return false; // Wrong password
            }
        }
        
        // User doesn't exist - authentication failed
        return false;
    }
    
}
