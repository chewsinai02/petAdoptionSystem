package com.mycompany.petAdoptionSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.petAdoptionSystem.PetGalleryScreen.UserSession;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginScreen {
    private static final String PRIMARY_COLOR = "#FAD9DD";
    private static final String ACCENT_COLOR = "#2C3E50";
    private Stage stage;
    private TextField loginField;
    private PasswordField passwordField;
    private Label errorLabel;
    private Label successLabel;
    private Connection conn;
    private Runnable onLoginSuccess; // Callback for user login success
    private Runnable onAdminLoginSuccess; // Callback for admin login success
    private boolean isAdmin = false;
    private StackPane loginPane;
    private StackPane passPane;

    public LoginScreen(Stage primaryStage) {
        this.stage = primaryStage;
        try {
            // Initialize database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    // Set callback for successful user login
    public void setOnUserLoginSuccess(Runnable callback) {
        this.onLoginSuccess = callback;
    }

    // Set callback for successful admin login
    public void setOnAdminLoginSuccess(Runnable callback) {
        this.onAdminLoginSuccess = callback;
    }
    
    public void show() {
        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(30, 50, 30, 50));
        mainContainer.setSpacing(25);
        mainContainer.setStyle("-fx-background-color: #FAD9DD;");

        // Logo
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/Logo.png")));
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);
        
        // Apply rounded corners using clipping
        Rectangle clip = new Rectangle(150, 150);
        clip.setArcWidth(40);   // Radius X
        clip.setArcHeight(40);  // Radius Y
        logoView.setClip(clip);

        // Login form container
        VBox formContainer = new VBox(20);
        formContainer.setMaxWidth(400);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 4);");

        // Title
        Text titleText = new Text("Pet Adoption System Login");
        titleText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 28));
        titleText.setFill(Color.web(ACCENT_COLOR));
        
        // --- Form elements ---
        VBox fieldContainer = new VBox(15);
        
        // Username
        Label loginLabel = new Label("Username:");
        loginLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        loginLabel.setTextFill(Color.web(ACCENT_COLOR));
        
        loginField = new TextField();
        loginField.setPromptText("Enter your login");
        String fieldStyle = "-fx-font-size: 14px; -fx-background-color: transparent; -fx-border-color: transparent;";
        loginField.setStyle(fieldStyle);

        loginPane = createTextFieldWithIcon(loginField, null);
        
        // Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        passwordLabel.setTextFill(Color.web(ACCENT_COLOR));
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(fieldStyle);

        passPane = createTextFieldWithIcon(passwordField, null);
        
        fieldContainer.getChildren().addAll(loginLabel, loginPane, passwordLabel, passPane);

        // Remember Me & Forgot Password
        CheckBox rememberMe = new CheckBox("Remember Me");
        rememberMe.setFont(Font.font("System", FontWeight.BOLD, 13));
        rememberMe.setStyle("-fx-text-fill: black;");
        rememberMe.getStylesheets().add(getClass().getResource("/styles/checkbox.css").toExternalForm());
        
        Hyperlink forgotPassword = new Hyperlink("Forgot Your Password?");
        forgotPassword.setFont(Font.font("System", 13));
        forgotPassword.setStyle("-fx-text-fill: #007fff;");
        
        BorderPane optionsPane = new BorderPane();
        optionsPane.setLeft(rememberMe);
        optionsPane.setRight(forgotPassword);
        
        // Login Button
        Button loginButton = createStyledButton("Login");

        // Register Link
        Hyperlink registerLink = new Hyperlink("Don't have an account? Register");
        registerLink.setFont(Font.font("System", 14));
        registerLink.setStyle("-fx-text-fill: #007fff;");

        VBox loginActions = new VBox(5);
        loginActions.setAlignment(Pos.CENTER);
        loginActions.getChildren().addAll(loginButton, registerLink);

        // Error and Success Labels
        errorLabel = createFeedbackLabel("-fx-text-fill: #D32F2F; -fx-background-color: #FFEBEE; -fx-border-color: #FFCDD2;");
        successLabel = createFeedbackLabel("-fx-text-fill: #388E3C; -fx-background-color: #E8F5E9; -fx-border-color: #C8E6C9;");
        
        formContainer.getChildren().addAll(logoView, titleText, fieldContainer, optionsPane, loginActions, errorLabel, successLabel);

        // Footer with images
        HBox footerImages = new HBox(15);
        footerImages.setAlignment(Pos.CENTER);
        
        mainContainer.getChildren().addAll(formContainer, footerImages);

        // Actions
        loginButton.setOnAction(e -> handleLogin());
        registerLink.setOnAction(e -> {
            RegisterScreen registerScreen = new RegisterScreen(stage);
            registerScreen.show();
        });

        // Scene
        Scene scene = new Scene(mainContainer, 600, 800); // <-- set height to 800
        stage.setTitle("Pet Adoption System - Login");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Logo.png")));
        stage.setScene(scene);
        stage.show();
    }
    
    private StackPane createTextFieldWithIcon(TextField textField, ImageView icon) {
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: white; -fx-border-color: " + PRIMARY_COLOR + "; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        HBox content;
        if (icon != null) {
            content = new HBox(10, icon, textField);
        } else {
            content = new HBox(textField);
        }
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(5, 10, 5, 10));
        
        pane.getChildren().add(content);
        
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                pane.setStyle("-fx-background-color: white; -fx-border-color: #F4ACB5; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
            } else {
                pane.setStyle("-fx-background-color: white; -fx-border-color: " + PRIMARY_COLOR + "; -fx-border-radius: 8; -fx-background-radius: 8;");
            }
        });
        
        return pane;
    }
    
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        String baseStyle = "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 12 25;";
        button.setStyle("-fx-background-color: #F4ACB5;" + baseStyle);
        button.setPrefWidth(Double.MAX_VALUE);

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + PRIMARY_COLOR+ ";" + baseStyle));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #F4ACB5;" + baseStyle));
        
        return button;
    }
    
    private Label createFeedbackLabel(String style) {
        Label label = new Label();
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 5; -fx-border-radius: 5;" + style);
        label.setVisible(false);
        label.managedProperty().bind(label.visibleProperty());
        label.setMaxWidth(Double.MAX_VALUE);
        label.setWrapText(true);
        return label;
    }

    private void resetFieldStyle(StackPane pane) {
        pane.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    private void handleLogin() {
        String login = loginField.getText();
        String password = passwordField.getText();

        // Reset field styles
        resetFieldStyle(loginPane);
        resetFieldStyle(passPane);
        errorLabel.setVisible(false);
        successLabel.setVisible(false);

        // Basic validation
        if (login.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            highlightErrorField(login.isEmpty() ? loginPane : passPane);
            return;
        }

        try {
            // First try admin login
            String adminQuery = "SELECT * FROM admins WHERE adminName = ? AND adminPwd = ?";
            PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
            adminStmt.setString(1, login);
            adminStmt.setString(2, password);
            ResultSet adminRs = adminStmt.executeQuery();

            if (adminRs.next()) {
                // Admin login successful
                isAdmin = true;
                UserSession.setLoggedIn(true);
                UserSession.setCurrentUserId(adminRs.getInt("id"));
                showSuccessAndProceed();
                return;
            }

            // If admin login fails, try user login
            String userQuery = "SELECT * FROM user WHERE userName = ? AND password = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, login);
            userStmt.setString(2, password);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                // User login successful
                isAdmin = false;
                UserSession.setLoggedIn(true);
                UserSession.setCurrentUserId(userRs.getInt("id"));
                showSuccessAndProceed();
                return;
            }

            // If both logins fail
            showError("Invalid username or password");
            highlightErrorField(loginPane);
            highlightErrorField(passPane);

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private void showSuccessAndProceed() {
        // Show success message
        successLabel.setText("Login successful! Redirecting...");
        successLabel.setVisible(true);
        errorLabel.setVisible(false);

        // Add fade animation to success message
        Timeline fade = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(successLabel.opacityProperty(), 0)),
            new KeyFrame(Duration.millis(500), new KeyValue(successLabel.opacityProperty(), 1))
        );
        fade.play();

        // Proceed to appropriate screen after a short delay
        Timeline delay = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            if (isAdmin) {
                // For admin user: trigger the admin login success callback
                if (onAdminLoginSuccess != null) {
                    onAdminLoginSuccess.run();
                }
            } else {
                // For normal user: trigger the user login success callback
                if (onLoginSuccess != null) {
                    onLoginSuccess.run();
                }
            }
            stage.close(); // close the login window
        }));
        delay.play();
    }

    private void highlightErrorField(StackPane pane) {
        pane.setStyle("-fx-background-color: #FFEBEE; -fx-border-color: #FF5252; -fx-border-radius: 8; -fx-border-width: 2; -fx-background-radius: 8;");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        successLabel.setVisible(false);
        
        // Add shake animation to error label
        Timeline shake = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(errorLabel.translateXProperty(), 0)),
            new KeyFrame(Duration.millis(100), new KeyValue(errorLabel.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(200), new KeyValue(errorLabel.translateXProperty(), 10)),
            new KeyFrame(Duration.millis(300), new KeyValue(errorLabel.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(400), new KeyValue(errorLabel.translateXProperty(), 10)),
            new KeyFrame(Duration.millis(500), new KeyValue(errorLabel.translateXProperty(), 0))
        );
        shake.play();
    }
} 