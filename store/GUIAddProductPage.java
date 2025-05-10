package store;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GUIAddProductPage {

	private static VBox defaultProductFields = new VBox(10);
    private static VBox productFields = new VBox(10);
    private static String selectedType = "TV"; // default product type
    private static TextField screenTypeField, resolutionField, displaySizeField;
    private static TextField doorDesignField, colorField, capacityField;

    public static void show(Stage primaryStage,String username, String userID) {
        VBox mainLayout = new VBox(20);
        selectedType = "TV";

        // =================== Background Picture ===================
        Image bgImage = new Image("file:" + System.getProperty("user.dir").replace("\\", "/") + "/src/resources/addProduct.jpg");
        
        BackgroundSize backgroundSize = new BackgroundSize(
                100, 100, true, true, true, false
        );
        
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );
        
        mainLayout.setBackground(new Background(backgroundImage));
        
        // =================== Title Label ===================
        Label title = new Label("Add Product");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 55));
        title.setTextFill(Color.web("#003366"));
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setTranslateY(-300);
        topBox.setStyle("-fx-padding: 30 0 20 0;");
        
        // ============ Button - Back Menu ===========
        // Back to Main Menu Button
        Button backBtn = new Button("Back to Main Menu");
        backBtn.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
        backBtn.setOnAction(e -> GUIMenuPage.show(primaryStage, username, userID));
        
        // Top bar with back button aligned to the right
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.getChildren().add(backBtn);
        
        // ========================= Type Selector - Select type of product =========================
        // Product Type Selector (TV or Refrigerator)
        ComboBox<String> typeSelector = new ComboBox<>();
        typeSelector.getItems().addAll("TV", "Refrigerator");
        typeSelector.setValue(selectedType);
        generateProductFields(selectedType);
        
        // ========================= Type Selector - Clicked =========================
        typeSelector.setOnAction(e -> {
            selectedType = typeSelector.getValue();
            generateProductFields(selectedType);
        });
        
        // ========================= Generate TextFields for Default Attributes =========================
        TextField itemNumberField = new TextField();
        itemNumberField.setPromptText("eg. TV001, RG 001");
        
        TextField nameField = new TextField();
        nameField.setPromptText("eg. Toshiba 2025UHD, Panasonic Premium Froze");
        
        TextField stockField = new TextField();
        stockField.setPromptText("eg. 30");
        
        TextField priceField = new TextField();
        priceField.setPromptText("eg. 2399");
        
        itemNumberField.setMaxWidth(350);
        nameField.setMaxWidth(350);
        stockField.setMaxWidth(350);
        priceField.setMaxWidth(350);

        // Add fields to the layout
        defaultProductFields.getChildren().clear();
        defaultProductFields.getChildren().addAll(
                new Label("Item Number"), itemNumberField,
                new Label("Item Name"), nameField,
                new Label("Stock Quantity"), stockField,
                new Label("Price (MYR)"), priceField
        );   

        // ============ Button - Add New Product ===========
        // Add button to submit the new product
        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
        
        // ============ Clicked - Add New Product ===========
        addButton.setOnAction(e -> {
        	// Error Handling - check defaultProductField (ID, name, stock, price) cannot be empty
            if (itemNumberField.getText().isEmpty() || nameField.getText().isEmpty() || stockField.getText().isEmpty() || priceField.getText().isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter all the relevant information!");
                alert.showAndWait();
                return; // Stop the process if empty found
            }
            
            int stock;
            // Error handling of invalid Stock number (2 letters + 3 digits)
            String itemNumber = itemNumberField.getText().trim();
            if (!itemNumber.matches("^[A-Za-z]{2}\\d{3}$")) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Item Number");
                alert.setHeaderText(null);
                alert.setContentText("Item Number must start with 2 letters followed by 3 digits (e.g., TV001).");
                alert.showAndWait();
                return;
            } else { //ensure upper case
                // Convert first 2 letters to uppercase
                String prefix = itemNumber.substring(0, 2).toUpperCase();
                String digits = itemNumber.substring(2);
                itemNumber = prefix + digits;

                // Update the field
                itemNumberField.setText(itemNumber);
            }
            
            // Error handling of invalid Stock number
            try {
                stock = Integer.parseInt(stockField.getText().trim());
                if (stock < 0) {
                    throw new NumberFormatException("Stock cannot be negative.");
                }
            } catch (NumberFormatException ex) {
            	stockField.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Stock");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid non-negative integer for stock.");
                alert.showAndWait();
                return; // Stop the process if invalid
            }
            
            // Error handling of invalid Price
            double price;
            try {
                price = Double.parseDouble(priceField.getText().trim());
                if (price < 0) throw new NumberFormatException("Negative price");
            } catch (NumberFormatException ex) {
            	priceField.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Price");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid non-negative number for price.");
                alert.showAndWait();
                return;
            }
            
            // Check if itemNumber already exists
            String newItemNumber = itemNumberField.getText();
            boolean exists = DatabaseHandler.tvs.stream().anyMatch(tv -> tv.getItemNumber().equals(newItemNumber)) ||
                             DatabaseHandler.refrigerators.stream().anyMatch(rf -> rf.getItemNumber().equals(newItemNumber));
            if (exists) {
            	itemNumberField.clear();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Duplicate Item");
                alert.setHeaderText(null);
                alert.setContentText("Item Number already exists. Please use a unique Item Number.");
                alert.showAndWait();
                return;
            }
            else {
            	// TV
            	if (selectedType.equals("TV")) {
            		double displaySize;
            		// Error handling for Display Size
            		try {
            			displaySize = Double.parseDouble(displaySizeField.getText().trim());
            		    //displaySize = Double.parseDouble(displaySizeText);
            		    if (displaySize <= 0) throw new NumberFormatException("Non-positive display size");
            		} catch (NumberFormatException ex) {
            			displaySizeField.clear();
            		    Alert alert = new Alert(Alert.AlertType.ERROR);
            		    alert.setTitle("Invalid Display Size");
            		    alert.setHeaderText(null);
            		    alert.setContentText("Please enter a valid positive number for display size.");
            		    alert.showAndWait();
            		    return;
            		}
            		
            		// Store to array and DB
            	    TV tv = new TV(itemNumberField.getText(), nameField.getText().trim(),
            	        Integer.parseInt(stockField.getText()), Double.parseDouble(priceField.getText()),
            	        screenTypeField.getText().trim(), resolutionField.getText().trim(), displaySize);
            	    StockManagement.updateProductInDB(tv);
            	    DatabaseHandler.tvs.add(tv);
            	    
            	    if (DatabaseHandler.isConnect()) { //the field DatabaseHandler.connect is not visible
            	        try {
            	            // Add to Product table
            	            String productQuery = String.format(
            	                "INSERT INTO Product VALUES ('%s', '%s', %d, %.2f, %b)",
            	                tv.getItemNumber(),
            	                tv.getName(),
            	                tv.getStockQuantity(),
            	                tv.getPrice(),
            	                tv.getStatus()
            	            );
            	            DatabaseHandler.addDatabase(productQuery);
            	            
            	            // Add to specific product table
            	            String tvQuery = String.format(
            	                "INSERT INTO TV VALUES ('%s', '%s', '%s', %.1f)",
            	                tv.getItemNumber(),
            	                tv.getScreenType(),
            	                tv.getResolution(),
            	                tv.getDisplaySize()
            	            );
            	            DatabaseHandler.addDatabase(tvQuery);
            	            // reset all the textfield after successful added to DB & array
                            screenTypeField.clear();
                            resolutionField.clear();
                            displaySizeField.clear();
            	        } catch (Exception e2) {
            	            System.err.println("Error saving to database: " + e2.getMessage());
            	        }
            	}}
            	else {
            		// Refrigerator
            		// Error handling for display size
            		int refCapacity;
            		try {
            			refCapacity = Integer.parseInt(capacityField.getText().trim());
            		    if (refCapacity <= 0) throw new NumberFormatException("Non-positive display size");
            		} catch (NumberFormatException ex) {
            			capacityField.clear(); // clear the textfield
            		    Alert alert = new Alert(Alert.AlertType.ERROR);
            		    alert.setTitle("Invalid Display Size");
            		    alert.setHeaderText(null);
            		    alert.setContentText("Please enter a valid positive number for display size.");
            		    alert.showAndWait();
            		    return;
            		}
            	    Refrigerator r = new Refrigerator(itemNumberField.getText(), nameField.getText().trim(),
            	        Integer.parseInt(stockField.getText()), Double.parseDouble(priceField.getText()),
            	        doorDesignField.getText().trim(), colorField.getText().trim(), refCapacity);
            	    StockManagement.updateProductInDB(r);// update log to DB
            	    DatabaseHandler.refrigerators.add(r);// add to array
            	    if (DatabaseHandler.isConnect()) { //the field DatabaseHandler.connect is not visible
            	        try {
            	            // Add to Product table
            	            String productQuery = String.format(
            	                "INSERT INTO Product VALUES ('%s', '%s', %d, %.2f, %b)",
            	                r.getItemNumber(),
            	                r.getName(),
            	                r.getStockQuantity(),
            	                r.getPrice(),
            	                r.getStatus()
            	            );
            	            DatabaseHandler.addDatabase(productQuery);
            	            
            	            // Add to specific product table
            	            String fridgeQuery = String.format(
            	                "INSERT INTO Refrigerator VALUES ('%s', '%s', '%s', %d)",
            	                r.getItemNumber(),
            	                r.getDoorDesign(),
            	                r.getColor(),
            	                r.getCapacity()
            	            );
            	            DatabaseHandler.addDatabase(fridgeQuery);
            	            // reset all the textfield after successful added to DB & array
                            doorDesignField.clear();
                            colorField.clear();
                            capacityField.clear();
            	        } catch (Exception e2) {
            	            System.err.println("Error saving to database: " + e2.getMessage());
            	        }
            	}
            	}
            	}
            
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Product successfully saved to database!");
                alert.showAndWait();
                // reset all the default textfield after successful added to DB & array
                itemNumberField.clear();
                nameField.clear();
                stockField.clear();
                priceField.clear();
        });

        // Layout

        mainLayout.setPadding(new Insets(20));
        defaultProductFields.setAlignment(Pos.CENTER);
        productFields.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(title, topBar,typeSelector, defaultProductFields, productFields, addButton);
        mainLayout.setAlignment(Pos.CENTER);

        // Scene and Stage
        Scene scene = new Scene(mainLayout, 1280, 720);
        primaryStage.setTitle("Add Product");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // ========================= Generate TextFields for Specific Product Attributes =========================
    private static void generateProductFields(String type) {
        productFields.getChildren().clear(); //reset the previous output

        // TV
        if (type.equals("TV")) {
            screenTypeField = new TextField();
            resolutionField = new TextField();
            displaySizeField = new TextField();
            screenTypeField.setPromptText("eg. LCD, LED");
            resolutionField.setPromptText("eg. 4K UHD");
            displaySizeField.setPromptText("eg. 65");
            screenTypeField.setMaxWidth(350);
            resolutionField.setMaxWidth(350);
            displaySizeField.setMaxWidth(350);

            productFields.getChildren().addAll(
                new Label("Screen Type"), screenTypeField,
                new Label("Resolution"), resolutionField,
                new Label("Display Size (inch)"), displaySizeField
            );
        } else { // Refrigerator
            doorDesignField = new TextField();
            colorField = new TextField();
            capacityField = new TextField();
            doorDesignField.setPromptText("eg. Double Door");
            colorField.setPromptText("eg. Matte Black");
            capacityField.setPromptText("eg. 500");
            
            doorDesignField.setMaxWidth(350);
            colorField.setMaxWidth(350);
            capacityField.setMaxWidth(350);

            productFields.getChildren().addAll(
                new Label("Door Design"), doorDesignField,
                new Label("Color"), colorField,
                new Label("Capacity (L)"), capacityField
            );
        }
    }
}

