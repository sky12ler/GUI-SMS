package store;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GUIStockAdjustPage {

    private static ToggleGroup productGroup = new ToggleGroup();
    private static VBox productDisplay = new VBox(10);
    private static String selectedType = "TV"; // default

    public static void show(Stage primaryStage, String username, String userID) {
    	
        // Main layout
        BorderPane root = new BorderPane();
        
        // =================== Background Picture ===================
        Image bgImage = new Image("file:" + System.getProperty("user.dir").replace("\\", "/") + "/src/resources/adjustStock.jpg");
        
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
        
        root.setBackground(new Background(backgroundImage));
        
        // ============ Title ============
        Label title = new Label("ADJUST STOCK");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 55));
        title.setTextFill(Color.web("#003366"));
        title.setTranslateX(25);
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setStyle("-fx-padding: 30 0 20 0;");
        
        // ============ Button - Back Menu ===========
        // Back to Main Menu Button
        Button backBtn = new Button("Back to Main Menu");
        backBtn.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
        backBtn.setTranslateX(280);
        backBtn.setTranslateY(20);
        backBtn.setOnAction(e -> GUIMenuPage.show(primaryStage, username, userID));

        HBox topBar = new HBox(20, title, backBtn);
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.TOP_CENTER);
        
        root.setTop(topBar); //set the title and backBtn to the top
        
        // ========================= Type Selector - Select type of product =========================
        ComboBox<String> typeSelector = new ComboBox<>();
        typeSelector.getItems().addAll("TV", "Refrigerator");
        typeSelector.setValue("TV");

        // ========================= Type Selector - Clicked =========================

        typeSelector.setOnAction(e -> {
            selectedType = typeSelector.getValue();
            displayProducts(selectedType);
        });

        // Top-left: typeSelector
        VBox topLeft = new VBox(typeSelector);
        topLeft.setPadding(new Insets(20));
        topLeft.setTranslateY(80);
        topLeft.setTranslateX(50);

        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setTranslateY(70);

        // ============ Button - Stock Operation Button ===========
        int[] operations = {10, 5, 1, -1, -5, -10};
        for (int op : operations) {
            Button btn = new Button((op > 0 ? "+" : "") + op);
            btn.setMinWidth(100);
            btn.setOnAction(e -> applyStockChange(op));
            rightPanel.getChildren().add(btn);
        }

        root.setLeft(topLeft);
        root.setRight(rightPanel);
        root.setCenter(productDisplay);
        BorderPane.setMargin(productDisplay, new Insets(20));

        displayProducts("TV"); // default

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Adjust Product Stock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ========================= Display all the available product =========================
    private static void displayProducts(String type) {
        productDisplay.getChildren().clear();
        productGroup = new ToggleGroup();

        // Set title of the product attributes
        Label header = new Label(String.format("%-10s %-30s %-10s", "  Item#", "  Name", "  Current Stock"));
        productDisplay.getChildren().add(header);
        header.setFont(Font.font("Consolas", FontWeight.BOLD, 23));

        if (type.equals("TV")) {
            for (TV tv : DatabaseHandler.tvs) {
            	if (!tv.getStatus()) continue; // show only valid status product
                RadioButton rb = new RadioButton();
                rb.setToggleGroup(productGroup);
                rb.setUserData(tv); // Store the TV object
                String itemID = tv.getItemNumber();
                String itemName = tv.getName();
                int currentStock = tv.getStockQuantity();

                Label lbl = new Label(String.format("%-10s %-30s %-10d",
                        itemID, itemName, currentStock));
                lbl.setFont(Font.font("Consolas", FontWeight.BOLD, 23));

                HBox row = new HBox(10, rb, lbl);
                productDisplay.getChildren().add(row);
                productDisplay.setTranslateX(68);
                productDisplay.setTranslateY(30);
            }
        } else if (type.equals("Refrigerator")) {
            for (Refrigerator r : DatabaseHandler.refrigerators) {
            	if (!r.getStatus()) continue;
                RadioButton rb = new RadioButton();
                rb.setToggleGroup(productGroup);
                rb.setUserData(r); // Store Refrigerator object
                String itemID = r.getItemNumber();
                String itemName = r.getName();
                int currentStock = r.getStockQuantity();

                Label lbl = new Label(String.format("%-10s %-30s %-10d",
                		itemID, itemName, currentStock));
                lbl.setFont(Font.font("Consolas", FontWeight.BOLD, 23));

                HBox row = new HBox(10, rb, lbl);
                productDisplay.getChildren().add(row);
                productDisplay.setTranslateX(68);
                productDisplay.setTranslateY(30);
            }
        }
    }

 // ========================= Apply the Change of Stock =========================
    private static void applyStockChange(int amount) {
        Toggle selected = productGroup.getSelectedToggle();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a product.");
            alert.showAndWait();
            return;
        }
        
        Object product = selected.getUserData();
        String itemNumber;
        int newStock;
        String productName;

        // TV
        if (selectedType.equals("TV")) {
            TV tv = (TV) product;
            newStock = Math.max(0, tv.getStockQuantity() + amount);
            tv.setStockQuantity(newStock); // set the stockQuantity to the new one
            itemNumber = tv.getItemNumber();
            productName = tv.getName();
        } else { //Refrigerator
            Refrigerator r = (Refrigerator) product;
            newStock = Math.max(0, r.getStockQuantity() + amount);
            r.setStockQuantity(newStock); // set the stockQuantity to the new one
            itemNumber = r.getItemNumber();
            productName = r.getName();
        }
        
        // Update Stock in DB
        if (DatabaseHandler.isConnect()) {
            try {
                String updateQuery = String.format(
                    "UPDATE Product SET stock_quantity = %d WHERE item_number = '%s'",
                    newStock,
                    itemNumber
                );
                DatabaseHandler.addDatabase(updateQuery);
                
                String action = amount >= 0 ? "Stock Adjustment - Increased" : "Stock Adjustment - Decreased";
                
                String stockActionQuery = String.format(
                	"INSERT INTO StockManagement VALUES ('%s', '%s', '%s', %d, #%s#)",
                    itemNumber,
                    productName,
                    action,
                    newStock,
                    java.time.LocalDate.now().toString()
                );
                DatabaseHandler.addDatabase(stockActionQuery);
                
            } catch (Exception e) {
                System.err.println("Error updating database: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update database: " + e.getMessage());
                alert.showAndWait();
            }
        }

        displayProducts(selectedType);
    }
}