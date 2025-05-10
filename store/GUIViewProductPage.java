package store;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GUIViewProductPage{
    public static void show(Stage primaryStage, String username, String userID) {
        
    	VBox productDisplay = new VBox(5);
        BorderPane layout = new BorderPane();
        
        // =================== Background Picture ===================
        Image bgImage = new Image("file:" + System.getProperty("user.dir").replace("\\", "/") + "/src/resources/viewProduct.jpg");
        
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
        
        layout.setBackground(new Background(backgroundImage));
        
        // ========================= Title =========================
        Label title = new Label("VIEW PRODUCTS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 55));
        title.setTextFill(Color.web("#003366"));
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setStyle("-fx-padding: 30 0 20 0;");
        title.setTranslateX(-230);
        layout.setTop(topBox);
        
        // ========================= Button - Back Menu =========================
        Button backBtn = new Button("Back to Main Menu");
        backBtn.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
        backBtn.setOnAction(e -> GUIMenuPage.show(primaryStage, username, userID));

        // ========================= Type Selector - Select type of product =========================
        ComboBox<String> typeSelector = new ComboBox<>();
        typeSelector.getItems().addAll("TV", "Refrigerator");
        typeSelector.setValue("TV");
        typeSelector.setTranslateY(-26);

        // ========================= Type Selector - Clicked =========================
        typeSelector.setOnAction(e -> {
            String selected = typeSelector.getValue();
            displayProducts(selected, productDisplay);
        });

        // user HBox to store title & backBtn
        HBox topBar = new HBox(20, title, backBtn);
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        
        // user HBox to arrange typeSelector & productDisplay
        VBox centerBox = new VBox(15, typeSelector, productDisplay);
        centerBox.setPadding(new Insets(20));
        displayProducts("TV", productDisplay); // default display
        
        layout.setTop(topBar);
        layout.setCenter(centerBox);
        
        // ============ Show scene ============

        Scene scene = new Scene(layout, 1280, 720);
        primaryStage.setTitle("View Products");
        primaryStage.setScene(scene);
        primaryStage.show();
        }

    // ========================= Show the list of Product =========================
    private static void displayProducts(String type, VBox productDisplay) {
        productDisplay.getChildren().clear(); //clear all the previous output first

        // TV
        if (type.equals("TV")) {
            Label header = new Label(String.format("%-7s %-28s %-6s %-10s %-15s %-14s %-12s", 
                "Item#", "Name", "Stock", "Price(RM)", "Screen", "Resolution", "Size(\")"));
            productDisplay.getChildren().add(header);
            header.setFont(Font.font("Consolas", FontWeight.BOLD, 23));
            productDisplay.setTranslateX(20);
            productDisplay.setTranslateY(-10);
            
            // show output from object array
            for (TV tv : DatabaseHandler.tvs) {
            	if (!tv.getStatus()) continue; // show only valid status product
                Label row = new Label(String.format("%-7s %-28s %-6d %-10.2f %-15s %-14s %-12.1f",
                        tv.getItemNumber(), tv.getName(), tv.getStockQuantity(), tv.getPrice(),
                        tv.getScreenType(), tv.getResolution(), tv.getDisplaySize()));
                productDisplay.getChildren().add(row);
                row.setFont(Font.font("Consolas", FontWeight.NORMAL, 23));
            }
            
            
        // Refrigerator
        } else if (type.equals("Refrigerator")) {
            Label header = new Label(String.format("%-7s %-28s %-6s %-10s %-15s %-14s %-12s",
                "Item#", "Name", "Stock", "Price(RM)", "Door Design", "Color", "Capacity(L)"));
            productDisplay.getChildren().add(header);
            header.setFont(Font.font("Consolas", FontWeight.BOLD, 23));
            productDisplay.setTranslateX(20);
            productDisplay.setTranslateY(-10);
            
            // show output from object array
            for (Refrigerator fridge : DatabaseHandler.refrigerators) {
            	if (!fridge.getStatus()) continue;// show only valid status product
                Label row = new Label(String.format("%-7s %-28s %-6d %-10.2f %-15s %-14s %-12d",
                        fridge.getItemNumber(), fridge.getName(), fridge.getStockQuantity(), fridge.getPrice(),
                        fridge.getDoorDesign(), fridge.getColor(), fridge.getCapacity()));
                productDisplay.getChildren().add(row);
                row.setFont(Font.font("Consolas", FontWeight.NORMAL, 23));

            }
        }
    }
}
