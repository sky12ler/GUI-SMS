package store;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GUIDiscontinuePage {
    public static void show(Stage primaryStage, String userName, String userID) {
        ComboBox<String> typeSelector = new ComboBox<>();
        typeSelector.getItems().addAll("TV", "Refrigerator");
        typeSelector.setValue("TV");

        BorderPane root = new BorderPane();
        VBox availableBox = new VBox(10);
        VBox discontinuedBox = new VBox(10);
        
        // =================== Background Picture ===================
        Image bgImage = new Image("file:" + System.getProperty("user.dir").replace("\\", "/") + "/src/resources/disCon.jpg");
        
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

        // ========================= Title =========================
        Label title = new Label("Discontinue / Continue Product");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 55));
        title.setTextFill(Color.web("#003366"));
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);       

        // ========================= Button - Back Menu =========================
        Button backButton = new Button("Back to Main Menu");
        backButton.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
        backButton.setOnAction(e -> GUIMenuPage.show(primaryStage, userName, userID));
        HBox topRightBox = new HBox(backButton);
        topRightBox.setAlignment(Pos.BOTTOM_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(new Pane()); // empty to push title to center
        topPane.setCenter(titleBox);
        topPane.setRight(topRightBox);
        topPane.setPadding(new Insets(10, 20, 10, 20));

        // ========================= Type Selector - Select type of product =========================
        typeSelector.setOnAction(e -> updateDisplay(typeSelector.getValue(), availableBox, discontinuedBox));
        updateDisplay("TV", availableBox, discontinuedBox);

        // ========================= Display Area =========================
        // Main product display area: left = available, right = discontinued
        Label avaProducts = new Label("Available Products:");
        avaProducts.setFont(Font.font("Consolas", FontWeight.BOLD, 23));
        VBox leftBox = new VBox(10, avaProducts, availableBox);
        Label disProducts = new Label("Discontinued Products:");
        disProducts.setFont(Font.font("Consolas", FontWeight.BOLD, 23));
        VBox rightBox = new VBox(10, disProducts, discontinuedBox);
        
        leftBox.setPadding(new Insets(10));
        rightBox.setPadding(new Insets(10));
        leftBox.setPrefWidth(600);
        rightBox.setPrefWidth(600);

        HBox productDisplay = new HBox(40, leftBox, rightBox);
        productDisplay.setAlignment(Pos.TOP_CENTER);
        productDisplay.setTranslateX(30);
        productDisplay.setPadding(new Insets(10));

        VBox centerContent = new VBox(20, typeSelector, productDisplay);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(10));

        root.setTop(topPane);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Product Status");
        primaryStage.show();
    }

    // ========================= Update the Display =========================
    private static void updateDisplay(String type, VBox topBox, VBox bottomBox) {
        topBox.getChildren().clear(); // reset the previous output - Left
        bottomBox.getChildren().clear(); // reset the previous output - Right

        ToggleGroup topGroup = new ToggleGroup();
        ToggleGroup bottomGroup = new ToggleGroup();

        // TV
        if (type.equals("TV")) {
            for (TV tv : DatabaseHandler.tvs) { // get all the data from arrays
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);

                // Show all the product
                String formattedText = String.format("%-6s %-28s", tv.getItemNumber(), tv.getName());
                Label label = new Label(formattedText);
                label.setFont(Font.font("Consolas", FontWeight.BOLD, 23));
                RadioButton rb = new RadioButton();
                rb.setToggleGroup(tv.getStatus() ? topGroup : bottomGroup); // sort them to different group based on status

                Button actionBtn = new Button(tv.getStatus() ? "Discontinue" : "Continue"); // create a button for each product
                
                // When user click the button
                actionBtn.setOnAction(e -> {
                    String itemNumber = tv.getItemNumber();
                    tv.setStatus(!tv.getStatus()); // set the status to the opposite
                    if (DatabaseHandler.isConnect()) {
                        try { // Update status into DB
                            String updateQuery = String.format(
                                    "UPDATE Product SET status = %b WHERE item_number = '%s'",
                                    tv.getStatus(), itemNumber);
                            DatabaseHandler.addDatabase(updateQuery);

                            // Update the log record to the DB
                            String action = !tv.getStatus() ? "Discontinue Product" : "Continue Product";
                            String stockActionQuery = String.format(
                                    "INSERT INTO StockManagement VALUES ('%s', '%s', '%s', %d, #%s#)",
                                    itemNumber,
                                    tv.getName(),
                                    action,
                                    tv.getStockQuantity(),
                                    java.time.LocalDate.now().toString()
                            );
                            DatabaseHandler.addDatabase(stockActionQuery);
                        } catch (Exception e2) { // If any error
                            System.err.println("Error updating database: " + e2.getMessage());
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update database: " + e2.getMessage());
                            alert.showAndWait();
                        }
                    }
                    updateDisplay(type, topBox, bottomBox); // recall the function everytime, ensure GUI up-to-date
                });

                row.getChildren().addAll(label, actionBtn);
                if (tv.getStatus()) topBox.getChildren().add(row); // sort them to different group(left, right) based on status
                else bottomBox.getChildren().add(row);
            }

        } else if (type.equals("Refrigerator")) {
        	// Refrigerator
            for (Refrigerator ref : DatabaseHandler.refrigerators) { // get all the data from arrays
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);

                // Show all the product
                String formattedText = String.format("%-6s %-28s", ref.getItemNumber(), ref.getName());
                Label label = new Label(formattedText);
                label.setFont(Font.font("Consolas", FontWeight.BOLD, 23));
                RadioButton rb = new RadioButton();
                rb.setToggleGroup(ref.getStatus() ? topGroup : bottomGroup); // sort them to different group based on status

                Button actionBtn = new Button(ref.getStatus() ? "Discontinue" : "Continue");  // create a button for each product
                
                // When user click the button
                actionBtn.setOnAction(e -> {
                    String itemNumber = ref.getItemNumber();
                    ref.setStatus(!ref.getStatus()); // set the status to the opposite
                    if (DatabaseHandler.isConnect()) {
                        try {  // Update status into DB
                            String updateQuery = String.format(
                                    "UPDATE Product SET status = %b WHERE item_number = '%s'",
                                    ref.getStatus(), itemNumber);
                            DatabaseHandler.addDatabase(updateQuery);

                            // Update the log record to the DB
                            String action = !ref.getStatus() ? "Discontinue Product" : "Continue Product";
                            String stockActionQuery = String.format(
                                    "INSERT INTO StockManagement VALUES ('%s', '%s', '%s', %d, #%s#)",
                                    itemNumber,
                                    ref.getName(),
                                    action,
                                    ref.getStockQuantity(),
                                    java.time.LocalDate.now().toString()
                            );
                            DatabaseHandler.addDatabase(stockActionQuery);
                        } catch (Exception e2) {  // If any error
                            System.err.println("Error updating database: " + e2.getMessage());
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update database: " + e2.getMessage());
                            alert.showAndWait();
                        }
                    }
                    updateDisplay(type, topBox, bottomBox);  // recall the function everytime, ensure GUI up-to-date
                });

                row.getChildren().addAll(label, actionBtn);
                if (ref.getStatus()) topBox.getChildren().add(row);  // sort them to different group(left, right) based on status
                else bottomBox.getChildren().add(row);
            }
        }
    }
}