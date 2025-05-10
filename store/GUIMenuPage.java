package store;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class GUIMenuPage {
    public static void show(Stage primaryStage, String username, String userID) {
        BorderPane root = new BorderPane();
        
        // =================== Background Picture ===================
        Image bgImage = new Image("file:" + System.getProperty("user.dir").replace("\\", "/") + "/src/resources/menupage.jpg");
        
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
        Label title = new Label("MENU");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 55));
        title.setTextFill(Color.web("#003366"));
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setStyle("-fx-padding: 30 0 20 0;");
        root.setTop(topBox);

     // ============ User Info (Top-Left) ============
        String firstName = username.split(" ")[0];
        Label welcomeLabel = new Label("Welcome " + firstName + " !");
        Label userIDLabel = new Label("User ID: " + userID);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        userIDLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        VBox userInfoBox = new VBox(5, welcomeLabel, userIDLabel);
        userInfoBox.setAlignment(Pos.TOP_LEFT);
        userInfoBox.setStyle("-fx-padding: 20 0 0 20;");
        userInfoBox.setTranslateY(-35);
        userInfoBox.setTranslateX(4);
        root.setLeft(userInfoBox);
        
     // ============ Log Out Button (Top-Right) ============
        Button logoutBtn = new Button("Log Out");
        logoutBtn.setPrefWidth(120);
        logoutBtn.setPrefHeight(15);
        logoutBtn.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        logoutBtn.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
        	GUIWelcomePage welcome = new GUIWelcomePage();
            welcome.start(primaryStage);
        });
        
        HBox logoutBox = new HBox(logoutBtn);
        logoutBox.setAlignment(Pos.TOP_RIGHT);
        logoutBox.setStyle("-fx-padding: 20 20 0 0;");
        logoutBox.setTranslateY(-60);
        root.setRight(logoutBox);
        
        // ============ Buttons ============

        Button viewProductBtn = new Button("VIEW PRODUCT");
        Button addProdcutBtn = new Button("ADD PRODUCT");
        Button adjustStockBtn = new Button("ADJUST STOCK");
        Button discontinueProductBtn = new Button("DISCONTINUE PRODUCT");
        Button exitBtn = new Button("EXIT");

        viewProductBtn.setOnAction(e -> { 
        	GUIViewProductPage.show(primaryStage, username, userID);
        });
        
        addProdcutBtn.setOnAction(e -> {
        	GUIAddProductPage.show(primaryStage, username, userID);
        });
        
        adjustStockBtn.setOnAction(e -> {
        	GUIStockAdjustPage.show(primaryStage, username, userID);
        });
        
        discontinueProductBtn.setOnAction(e -> {
        	GUIDiscontinuePage.show(primaryStage, username, userID);
        });

        // Set button style
        Button[] buttons = {viewProductBtn, addProdcutBtn, adjustStockBtn, discontinueProductBtn, exitBtn};
        for (Button btn : buttons) {
            btn.setPrefWidth(400);
            btn.setPrefHeight(50);
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 26));
            btn.setStyle("-fx-background-color: #003366; -fx-text-fill: #F9F2E0;" + "-fx-background-radius: 20; "+ "-fx-border-radius: 20;");
            btn.setTranslateX(-21);
            btn.setTranslateY(-44);
        }

        // Exit button action
        exitBtn.setOnAction(e -> {
            primaryStage.close();
        });

        VBox buttonBox = new VBox(20, viewProductBtn, addProdcutBtn, adjustStockBtn, discontinueProductBtn, exitBtn);
        buttonBox.setAlignment(Pos.CENTER);
        root.setCenter(buttonBox);

        // ============ Show scene ============
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}