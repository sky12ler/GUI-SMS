package store;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUIWelcomePage extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane layout = new BorderPane();
        
        // =================== Background Picture ===================
        Image bgImage = new Image("file:" + System.getProperty("user.dir").replace("\\", "/") + "/src/resources/welcomepage.jpg");
        
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
  
        // =================== Show Welcome Message ===================
        Label welcomeLabel = new Label("Welcome to Stock Management System!");
        welcomeLabel.setTextFill(Color.web("#333333")); // grey color
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 50)); // Arial Bold
        welcomeLabel.setAlignment(Pos.CENTER);
        
        // use a vbox to put welcomeLabel, locate at the top of screen
        VBox topBox = new VBox(welcomeLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setStyle("-fx-padding: 40 0 20 0;");
        layout.setTop(topBox);
        
        // ===================== Show System Date & Time ===============
        Label dateLabel = new Label();
        dateLabel.setTextFill(Color.web("#333333"));
        dateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        Label timeLabel = new Label();
        timeLabel.setTextFill(Color.web("#333333"));
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        
        // Create a VBox to put dateLabel, timeLabel
        VBox dateTimeBox = new VBox(dateLabel, timeLabel);
        layout.setLeft(dateTimeBox);
        
        // Set the VBox to position - top left
        dateTimeBox.setAlignment(Pos.TOP_LEFT);
        dateTimeBox.setStyle("-fx-padding: 20 0 0 20;");
        dateTimeBox.setTranslateY(45);
        dateTimeBox.setTranslateX(5);
        
        // Dynamic update the time
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        	LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            dateLabel.setText("Date:     " + date.format(dateFormatter));
            timeLabel.setText("Time:     " + time.format(timeFormatter));
        }));
        
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
        
        // ===================== Developer List ========================
        Text nameList = new Text("DEVELOPED BY:\n\nChia Yue Sheng	2204673  \nHii Zi Wei        	2204587  \nLee Hien Leong  	2204958  \nTeh Bee Ling   	2204237  ");
        nameList.setFill(Color.web("#003366"));
        nameList.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        StackPane devInfoBox = new StackPane();
        devInfoBox.getChildren().addAll(nameList);
        StackPane.setAlignment(nameList, Pos.CENTER);
        devInfoBox.setTranslateX(190);
        devInfoBox.setTranslateY(580);

        layout.getChildren().add(devInfoBox);
        
        // ===================== Log in function =======================
        // name label
        Label nameLabel = new Label("Full Name:");
        nameLabel.setStyle("-fx-text-fill: #003366; -fx-font-weight: bold; -fx-font-size: 20px;");
        
        Label passwordLabel = new Label("Password :");
        passwordLabel.setStyle("-fx-text-fill: #003366; -fx-font-weight: bold; -fx-font-size: 20px;");
        
        // textfield to key in Username
        TextField nameInput = new TextField();
        nameInput.setPromptText("Username");
        nameInput.setPrefWidth(180);
        nameInput.setMaxWidth(180);
        nameInput.setMinWidth(180); 
        
        // textfield to key in Password
        TextField passwordInput = new TextField();
        passwordInput.setPromptText("Password");
        passwordInput.setPrefWidth(180);
        passwordInput.setMaxWidth(180);
        passwordInput.setMinWidth(180); 
        
        HBox nameHBox = new HBox(10);
        nameHBox.getChildren().addAll(nameLabel, nameInput);
        
        HBox passwordHBox = new HBox(10);
        passwordHBox.getChildren().addAll(passwordLabel, passwordInput); 
        
        // Add Log In button
        Button loginButton = new Button("Log In");
        loginButton.setStyle("-fx-background-color: #003366; -fx-text-fill: #F9F2E0; -fx-font-weight: bold;");
        loginButton.setPrefWidth(130);

        VBox loginBox = new VBox(loginButton);
        loginBox.setTranslateX(140);
        
        VBox allLogin = new VBox(10, nameHBox,passwordHBox,loginBox);
        allLogin.setAlignment(Pos.TOP_CENTER);
        allLogin.setTranslateX(280);
        allLogin.setTranslateY(-150);
        
        // ===================== Register function =======================
        Label regNameLabel = new Label("Full Name:");
        regNameLabel.setStyle("-fx-text-fill: #003366; -fx-font-weight: bold; -fx-font-size: 20px;");
        
        Label regPasswordLabel = new Label("Password :");
        regPasswordLabel.setStyle("-fx-text-fill: #003366; -fx-font-weight: bold; -fx-font-size: 20px;");
        
        Label regConPasswordLabel = new Label("Password :");
        regConPasswordLabel.setStyle("-fx-text-fill: #003366; -fx-font-weight: bold; -fx-font-size: 20px;");
        
        // textfield to key in Username
        TextField regNameInput = new TextField();
        regNameInput.setPromptText("Username");
        regNameInput.setPrefWidth(180);
        regNameInput.setMaxWidth(180);
        regNameInput.setMinWidth(180); 
        
        // textfield to key in Password
        TextField regPasswordInput = new TextField();
        regPasswordInput.setPromptText("Password");
        regPasswordInput.setPrefWidth(180);
        regPasswordInput.setMaxWidth(180);
        regPasswordInput.setMinWidth(180); 
        
        // textfield to key in Confirm Password
        TextField regConPasswordInput = new TextField();
        regConPasswordInput.setPromptText("Confirm Password");
        regConPasswordInput.setPrefWidth(180);
        regConPasswordInput.setMaxWidth(180);
        regConPasswordInput.setMinWidth(180); 
        
        // Place them side by side
        HBox RegNameHBox = new HBox(10);
        RegNameHBox.getChildren().addAll(regNameLabel, regNameInput);
        
        HBox RegPasswordHBox = new HBox(10);
        RegPasswordHBox.getChildren().addAll(regPasswordLabel, regPasswordInput); 
        
        HBox RegConPasswordHBox = new HBox(10);
        RegConPasswordHBox.getChildren().addAll(regConPasswordLabel, regConPasswordInput); 
        
        // Add Register button
        Button RegButton = new Button("Register");
        RegButton.setStyle("-fx-background-color: #003366; -fx-text-fill: #F9F2E0; -fx-font-weight: bold;");
        RegButton.setPrefWidth(130);

        VBox RegButtonVBox = new VBox(RegButton);
        RegButtonVBox.setTranslateX(140);
        
        VBox allReg = new VBox(10, RegNameHBox,RegPasswordHBox,RegConPasswordHBox, RegButtonVBox);
        allReg.setAlignment(Pos.TOP_CENTER);
        allReg.setTranslateX(280);
        allReg.setTranslateY(-150);
        
        HBox regLog = new HBox(135, allLogin, allReg);
        regLog.setTranslateX(-160);
        regLog.setTranslateY(280);
        
        // Place to Center of BorderPane
        layout.setCenter(regLog);
        
     // ===================== Login Button Clicked =======================
        loginButton.setOnAction(e -> {
            String username = nameInput.getText().trim();
            String password = passwordInput.getText();
            // Error Handling - Empty Text Field
            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter both username and password.");
                alert.showAndWait();
                return;
            }
            else { // if both information entered, check for username&password validation
                UserInfo user = new UserInfo();
                boolean validate =user.authenticateUser(username,password);
                if(validate) { // Validation Success
                	Alert successAlert = new Alert(AlertType.INFORMATION);
                	successAlert.setTitle("Success");
                	successAlert.setContentText("Login Successful!\nYour generated User ID: " + user.getUserID());
                	successAlert.showAndWait();
                	GUIMenuPage.show(primaryStage, username, user.getUserID());
                }
                else { // Validation Failed
                	passwordInput.clear();
                	Alert errorAlert = new Alert(AlertType.ERROR);
                	errorAlert.setTitle("Error");
                	errorAlert.setContentText("Login Failed. Invalid Username or Password!"
                			+ "\nAccount not found. Please register first.");
                	errorAlert.showAndWait();
                }
            }
        });
        
     // ===================== Register Button Clicked =======================
        RegButton.setOnAction(e -> {
            String regUserName = regNameInput.getText().trim();
            String regPassword = regPasswordInput.getText();
            String regConPassword = regConPasswordInput.getText();
            // Error Handling - Empty Text Field
            if (regUserName.isEmpty() || regPassword.isEmpty() || regConPassword.isEmpty() || !(regPassword.equals(regConPassword))) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please ensure all information are correct!.");
                alert.showAndWait();
                return;
            }
            else {
                // Check if user already exists in the Database&ArrayList
            	boolean userExists = false;
                if (DatabaseHandler.isConnect()) { // the field DatabaseHandler.connect is not visible
                    for (UserInfo user : DatabaseHandler.userInfos) {
                        if (user.getUsername().equals(regUserName)) { // if user exists
                            userExists = true;
                            regNameInput.clear();
                            regPasswordInput.clear();
                            regConPasswordInput.clear();
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Input Error");
                            alert.setHeaderText(null);
                            alert.setContentText("User Exists! Please proceed to Login.");
                            alert.showAndWait();
                            break;
                        }
                    }
                    if (!userExists) { // if user not exists, continue to register
                    	UserInfo user = new UserInfo(regUserName, UserInfo.hashPassword(regPassword), "user");
                    	regNameInput.clear();
                    	regPasswordInput.clear();
                    	regConPasswordInput.clear();
                    	
                    	Alert errorAlert = new Alert(AlertType.INFORMATION);
                    	errorAlert.setTitle("Register Succesfful !");
                    	errorAlert.setContentText("Register Succesfful!\nYou can procced to Login now!");
                    	errorAlert.showAndWait();
                    	
                    	//Store user to array
                    	DatabaseHandler.userInfos.add(user);
                    	
                    	//Store user to DB
                        try {
                            String query = String.format(
                                "INSERT INTO UserInfo VALUES ('%s', '%s','%s')",
                                regUserName,
                                UserInfo.hashPassword(regPassword),
                                user.getuserRole()
                            );
                            DatabaseHandler.addDatabase(query);
                        } catch (Exception x) {
                            System.err.println("Error saving user to database: " + x.getMessage());
                        }
                    }
            }
            }
            
            

        });
        
        Scene scene = new Scene(layout, 1280, 720);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchGUI(String[] args) {
        launch(args);
    }
}