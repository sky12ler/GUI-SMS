package store;

import java.util.ArrayList;
import java.util.Scanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class StockManagement {
    private static ArrayList<Product> productList = new ArrayList<>(); 
    private static Scanner scanner = new Scanner(System.in);
    private static UserInfo user;

    // Instance variables for stock management data
    private String itemNumber;
    private String productName;
    private String action;
    private int quantity;
    private Date date;

    // Constructor that matches the one you're trying to use
    public StockManagement(String itemNumber, String productName, String action, int quantity, Date date) {
        this.itemNumber = itemNumber;
        this.productName = productName;
        this.action = action;
        this.quantity = quantity;
        this.date = date;
    }

    // Getter methods
    public String getItemNumber() {
        return itemNumber;
    }

    public String getProductName() {
        return productName;
    }

    public String getAction() {
        return action;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getDate() {
        return date;
    }
    
	public static void main(String[] args) {
        try {
            // Initialize database connection
            if (DatabaseHandler.storePermission()) {
                DatabaseHandler.getConnection();
                DatabaseHandler.initialiseDatabase();
                productList = DatabaseHandler.products; // Load existing products from DB
            }
            
    		//Ask user to select - GUI / CLI
    		System.out.print("Press Enter to use GUI, 0 to keep in CLI: ");
    		String guiChoice = scanner.nextLine();
    		if (guiChoice == "") {
    			System.out.println("Redirecting to GUI!");
    			GUIWelcomePage.launchGUI(args);
    			return;
    		}    		

            // Display Welcome Message with Current Date & Time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("Welcome to the Stock Management System(SNS)!");
            System.out.println("Current Date & Time: " + dtf.format(now));
            System.out.println("Group Members: \n1. Chia Yue Sheng \n2. Hii Zi Wei \n3. Lee Hien Leong \n4. Teh Bee Ling");

            // Get User Name and Generate User ID
            user = new UserInfo();
            user.setName(scanner); 
            System.out.println("Your generated User ID: " + user.getUserID());

            // Ask if the user wants to add products
            System.out.print("Please enter a maximum number of products you want to add (or 0 to exit): ");
            int numProducts = scanner.nextInt();
            scanner.nextLine(); 
            
            if (numProducts > 0) {
                System.out.println("Your maximum number of products is "+numProducts);
                for (int i = 0; i < numProducts; i++) {
                    addProduct();
                }
            } else {
                exitProgram();
            }

            // Main menu loop
            while (true) {
                System.out.println("\n=== MENU ===");
                System.out.println("1. View Product");
                System.out.println("2. Add stock");
                System.out.println("3. Deduct Stock");
                System.out.println("4. Discontinue Product");
                System.out.println("0. Exit");
                System.out.print("Please enter a menu option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) {
                    exitUser();
                    break;
                }
               
                executeProductAction(choice, productList, scanner);
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void executeProductAction(int choice, ArrayList<Product> productList, Scanner scanner) {
        if (productList.isEmpty()) {
            System.out.println("No products available. Please add products first.");
            return;
        }

        System.out.println("\nSelect a product:");
        for (int i = 0; i < productList.size(); i++) {
            System.out.println((i + 1) + ". " + productList.get(i).getName());
        }
        System.out.print("Enter choice: ");
        int productIndex = scanner.nextInt() - 1;
        scanner.nextLine(); 

        if (productIndex < 0 || productIndex >= productList.size()) {
            System.out.println("Invalid product selection.");
            return;
        }

        Product selectedProduct = productList.get(productIndex);

        switch (choice) {
            case 1: // view
                System.out.println(selectedProduct); 
                break;

            case 2: // add stock
                System.out.print("Enter quantity to add: ");
                int addQuantity = scanner.nextInt();
                scanner.nextLine();
                selectedProduct.addStock(addQuantity);
                updateProductInDB(selectedProduct);
                break;

            case 3: // deduct stock
                System.out.print("Enter quantity to deduct: ");
                int deductQuantity = scanner.nextInt();
                scanner.nextLine(); 
                selectedProduct.deductStock(deductQuantity);
                updateProductInDB(selectedProduct);
                break;

            case 4: // discontinue
                selectedProduct.discontinue(false);
                updateProductInDB(selectedProduct);
                break;

            default:
                errorPrint();
        }
    }

    private static void addProduct() {
        int choice, capacity = 0;
        String doorDesign = null, color = null, screenType = null, resolution = null;
        double displaySize = 0.0;
 
        // Product type selection
        while (true) {
            System.out.println("\nSelect a product to add:");
            System.out.println("1. Refrigerator");
            System.out.println("2. TV");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1 || choice == 2) {
                break; 
            } else {
                errorPrint();
            }
        }

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
 
        if (choice == 1) {
            System.out.print("Enter Door Design: ");
            doorDesign = scanner.nextLine();
            
            System.out.print("Enter Color: ");
            color = scanner.nextLine();
            
            System.out.print("Enter Capacity (in Litres): ");
            capacity = scanner.nextInt();
            scanner.nextLine();
        } else if (choice == 2) {
            System.out.print("Enter Screen Type: ");
            screenType = scanner.nextLine();
            System.out.print("Enter Resolution: ");
            resolution = scanner.nextLine();
            System.out.print("Enter Display Size: ");
            displaySize = scanner.nextInt();
            scanner.nextLine();         
        }
     
        System.out.print("Enter stock quantity: ");
        int stockQuantity = scanner.nextInt();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        String itemNumber;
        
        while (true) {
            System.out.print("Enter item number (e.g., TV001): ");
            itemNumber = scanner.nextLine().trim();

            // Check format
            if (!itemNumber.matches("^[A-Za-z]{2}\\d{3}$")) {
                System.out.println("Invalid format. Please enter two letters followed by three digits (e.g., TV001).");
                continue;
            }

            // Make first two char become UPPERCASE
            itemNumber = itemNumber.substring(0, 2).toUpperCase() + itemNumber.substring(2);

            // check for exists 
            boolean exists = false;
            for (TV tv : DatabaseHandler.tvs) {
                if (tv.getItemNumber().equalsIgnoreCase(itemNumber)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                for (Refrigerator fridge : DatabaseHandler.refrigerators) {
                    if (fridge.getItemNumber().equalsIgnoreCase(itemNumber)) {
                        exists = true;
                        break;
                    }
                }
            }

            if (exists) {
                System.out.println("Item Number already exists. Please use a unique Item Number.");
            } else {
                break; // exits loop
            }
        }  

        Product newProduct = null;

        if (choice == 1) {
            newProduct = new Refrigerator(itemNumber, name, stockQuantity, price, doorDesign, color, capacity);
            System.out.println("Refrigerator added successfully!");
        } else if (choice == 2) {
            newProduct = new TV(itemNumber, name, stockQuantity, price, screenType, resolution, displaySize);
            System.out.println("TV added successfully!");
        }
        
        productList.add(newProduct);
        
        // Add to database
        if (DatabaseHandler.isConnect()) { //the field DatabaseHandler.connect is not visible
            try {
                // Add to Product table
                String productQuery = String.format(
                    "INSERT INTO Product VALUES ('%s', '%s', %d, %.2f, %b)",
                    newProduct.getItemNumber(),
                    newProduct.getName(),
                    newProduct.getStockQuantity(),
                    newProduct.getPrice(),
                    newProduct.getStatus()
                );
                DatabaseHandler.addDatabase(productQuery);
                
                // Add to specific product table
                if (newProduct instanceof Refrigerator) {
                    Refrigerator r = (Refrigerator)newProduct;
                    String fridgeQuery = String.format(
                        "INSERT INTO Refrigerator VALUES ('%s', '%s', '%s', %d)",
                        r.getItemNumber(),
                        r.getDoorDesign(),
                        r.getColor(),
                        r.getCapacity()
                    );
                    DatabaseHandler.addDatabase(fridgeQuery);
                } 
                else if (newProduct instanceof TV) {
                    TV tv = (TV)newProduct;
                    String tvQuery = String.format(
                        "INSERT INTO TV VALUES ('%s', '%s', '%s', %.1f)",
                        tv.getItemNumber(),
                        tv.getScreenType(),
                        tv.getResolution(),
                        tv.getDisplaySize()
                    );
                    DatabaseHandler.addDatabase(tvQuery);
                }
            } catch (Exception e) {
                System.err.println("Error saving to database: " + e.getMessage());
            }
        }
    }

    public static void updateProductInDB(Product product) {
        if (DatabaseHandler.isConnect()) { //the field DatabaseHandler.connect is not visible
            try {
                String updateQuery = String.format(
                    "UPDATE Product SET stock_quantity = %d, status = %b WHERE item_number = '%s'",
                    product.getStockQuantity(),
                    product.getStatus(),
                    product.getItemNumber()
                );
                DatabaseHandler.addDatabase(updateQuery);
                
                // Record stock management action
                String action = "";
                if (product.getStatus()) {
                    action = product.getStockQuantity() > 0 ? "Stock Updated" : "Stock Depleted";
                } else {
                    action = "Product Discontinued";
                }
                
                String stockActionQuery = String.format(
                    "INSERT INTO StockManagement VALUES ('%s', '%s', '%s', %d, #%s#)",
                    product.getItemNumber(),
                    product.getName(),
                    action,
                    product.getStockQuantity(),
                    java.time.LocalDate.now().toString()
                );
                DatabaseHandler.addDatabase(stockActionQuery);
            } catch (Exception e) {
                System.err.println("Error updating database: " + e.getMessage());
            }
        }
    }

    private static void exitProgram() {
        System.out.println("Exiting program. Thank you for using our system.");
        System.exit(0);
    }

    private static void exitUser() {
        System.out.println("\n=== EXITING SYSTEM ===");
        System.out.println("User ID: " + user.getUserID());     
        System.out.println("User Name: " + user.getName()); 
        System.out.println("Thank you for using the Stock Management System!");
        System.exit(0);
    }

    private static void errorPrint() {
        System.out.println("Invalid choice. Please try again.");
    }
}