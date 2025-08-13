/**
 * Provides the user interface for new user registration. (no include admin registration)
 * Handles input validation and creates new user accounts.
 * 
 * @author Chew Sin Ai (finalize, and debug)
 * @version 2.0
 */
package com.mycompany.petAdoptionSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RegisterScreen {

    private static final String PRIMARY_COLOR = "#FAD9DD";
    private static final String SECONDARY_COLOR = "#F5F5F5";
    private static final String ACCENT_COLOR = "#2C3E50";
    private static final int INPUT_WIDTH = 320;
    private Stage stage;
    private TextField fullNameField, usernameField, emailField, phoneField, ageField, petHaveField, experienceField;
    private ComboBox<String> genderComboBox;
    private TextArea addressField;
    private PasswordField passwordField, confirmPasswordField;
    private Label errorLabel;
    private Button browsePicButton;
    private Label fileNameLabel;
    private String selectedProfilePicFileName = null;
    private Connection conn;

    public RegisterScreen(Stage primaryStage) {
        this.stage = primaryStage;
        try {
            // Initialize database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    /**
     * Displays the registration screen for creating a new user account.
     *
     * <p>
     * This method initializes the input fields and sets up the layout for the
     * registration form. It includes fields for user details such as full name,
     * username, email, phone number, age, gender, address, pets owned, and
     * experience. The layout also allows users to select a profile picture,
     * enter a password, and confirm the password. An error label is included
     * for displaying validation errors.
     *
     * <p>
     * The registration form is styled with custom fonts and colors. The form
     * includes a register button to handle form submission and a hyperlink to
     * navigate back to the login screen.
     *
     * <p>
     * The registration screen is displayed using a JavaFX Scene with specified
     * dimensions and stylesheets.
     */
    public void show() {
        initializeFields();
        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20, 50, 20, 50));
        mainContainer.setSpacing(25);
        mainContainer.setStyle("-fx-background-color: #FAD9DD" + SECONDARY_COLOR + ";");

        // Logo
        ImageView logoView = new ImageView(new Image("file:Logo.png"));
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);

        // Form card container
        VBox formContainer = new VBox(20);
        formContainer.setMaxWidth(600);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 4);");

        // Title
        Text titleText = new Text("Create Account");
        titleText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 28));
        titleText.setFill(Color.web(ACCENT_COLOR));

        // Field container
        VBox fieldContainer = new VBox(15);
        fieldContainer.setAlignment(Pos.CENTER);

        // Helper for row
        int labelWidth = 160;
        Font labelFont = Font.font("System", FontWeight.BOLD, 14);
        Color labelColor = Color.web(ACCENT_COLOR);

        // Full Name
        Label nameLabel = new Label("Full Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setTextFill(labelColor);
        nameLabel.setMinWidth(labelWidth);
        HBox nameRow = new HBox(10, nameLabel, fullNameField);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(nameRow);

        // Username
        Label userLabel = new Label("Username:");
        userLabel.setFont(labelFont);
        userLabel.setTextFill(labelColor);
        userLabel.setMinWidth(labelWidth);
        HBox userRow = new HBox(10, userLabel, usernameField);
        userRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(userRow);

        // Email
        Label emailLabel = new Label("Email Address:");
        emailLabel.setFont(labelFont);
        emailLabel.setTextFill(labelColor);
        emailLabel.setMinWidth(labelWidth);
        HBox emailRow = new HBox(10, emailLabel, emailField);
        emailRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(emailRow);

        // Phone
        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setFont(labelFont);
        phoneLabel.setTextFill(labelColor);
        phoneLabel.setMinWidth(labelWidth);
        HBox phoneRow = new HBox(10, phoneLabel, phoneField);
        phoneRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(phoneRow);

        // Age/Gender
        Label ageGenderLabel = new Label("Age / Gender:");
        ageGenderLabel.setFont(labelFont);
        ageGenderLabel.setTextFill(labelColor);
        ageGenderLabel.setMinWidth(labelWidth);
        HBox ageGenderBox = new HBox(10, ageField, genderComboBox);
        ageGenderBox.setAlignment(Pos.CENTER_LEFT);
        HBox ageGenderRow = new HBox(10, ageGenderLabel, ageGenderBox);
        ageGenderRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(ageGenderRow);

        // Address
        Label addressLabel = new Label("Address:");
        addressLabel.setFont(labelFont);
        addressLabel.setTextFill(labelColor);
        addressLabel.setMinWidth(labelWidth);
        HBox addressRow = new HBox(10, addressLabel, addressField);
        addressRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(addressRow);

        // Pets Owned
        Label petsLabel = new Label("Pets Owned:");
        petsLabel.setFont(labelFont);
        petsLabel.setTextFill(labelColor);
        petsLabel.setMinWidth(labelWidth);
        HBox petsRow = new HBox(10, petsLabel, petHaveField);
        petsRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(petsRow);

        // Experience
        Label expLabel = new Label("Experience (years):");
        expLabel.setFont(labelFont);
        expLabel.setTextFill(labelColor);
        expLabel.setMinWidth(labelWidth);
        HBox expRow = new HBox(10, expLabel, experienceField);
        expRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(expRow);

        // Profile picture row
        Label picLabel = new Label("Profile Picture:");
        picLabel.setFont(labelFont);
        picLabel.setTextFill(labelColor);
        picLabel.setMinWidth(labelWidth);
        HBox picRow = new HBox(10, browsePicButton, fileNameLabel);
        picRow.setAlignment(Pos.CENTER_LEFT);
        picRow.setPrefWidth(INPUT_WIDTH);
        HBox picHBox = new HBox(10, picLabel, picRow);
        picHBox.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(picHBox);

        // Password
        Label passLabel = new Label("Password:");
        passLabel.setFont(labelFont);
        passLabel.setTextFill(labelColor);
        passLabel.setMinWidth(labelWidth);
        HBox passRow = new HBox(10, passLabel, passwordField);
        passRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(passRow);

        // Confirm Password
        Label confirmLabel = new Label("Confirm Password:");
        confirmLabel.setFont(labelFont);
        confirmLabel.setTextFill(labelColor);
        confirmLabel.setMinWidth(labelWidth);
        HBox confirmRow = new HBox(10, confirmLabel, confirmPasswordField);
        confirmRow.setAlignment(Pos.CENTER_LEFT);
        fieldContainer.getChildren().add(confirmRow);

        // Error label styled like LoginScreen
        errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#D32F2F"));
        errorLabel.setVisible(false);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #FFEBEE; -fx-border-color: #FFCDD2; -fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 8 0 8 0;");
        fieldContainer.getChildren().add(errorLabel);

        // Register button and login link in VBox
        Button registerButton = new Button("Register");
        registerButton.setStyle(
                "-fx-background-color: #F4ACB5;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 8;"
                + "-fx-cursor: hand;"
                + "-fx-padding: 12 0;"
        );
        registerButton.setPrefWidth(200);
        registerButton.setOnMouseEntered(e -> registerButton.setStyle(
                "-fx-background-color: " + PRIMARY_COLOR + ";"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 8;"
                + "-fx-cursor: hand;"
                + "-fx-padding: 12 0;"
        ));
        registerButton.setOnMouseExited(e -> registerButton.setStyle(
                "-fx-background-color: #F4ACB5;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 8;"
                + "-fx-cursor: hand;"
                + "-fx-padding: 12 0;"
        ));
        Hyperlink backToLogin = new Hyperlink("Already have an account? Login");
        backToLogin.setFont(Font.font("System", 14));
        backToLogin.setStyle("-fx-text-fill: #007fff" + PRIMARY_COLOR + ";");
        VBox actionsBox = new VBox(5, registerButton, backToLogin);
        actionsBox.setAlignment(Pos.CENTER);
        fieldContainer.getChildren().add(actionsBox);

        formContainer.getChildren().setAll(titleText, fieldContainer);
        mainContainer.getChildren().setAll(logoView, formContainer);

        // Register button action
        registerButton.setOnAction(e -> handleRegistration());
        backToLogin.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        });

        // Scene size matches LoginScreen
        Scene scene = new Scene(mainContainer, 600, 800); // Set height to 800 to match LoginScreen/ProjectGUI
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Pet Adoption System - Register");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes all the fields in the registration form, including text
     * fields, combo box, text area, password fields, and profile picture browse
     * button. Also sets up the necessary styles and event handlers for these
     * fields.
     */
    private void initializeFields() {
        fullNameField = createStyledTextField(INPUT_WIDTH);
        usernameField = createStyledTextField(INPUT_WIDTH);
        emailField = createStyledTextField(INPUT_WIDTH);
        phoneField = createStyledTextField(INPUT_WIDTH);
        ageField = createStyledTextField(INPUT_WIDTH);
        petHaveField = createStyledTextField(INPUT_WIDTH);
        experienceField = createStyledTextField(INPUT_WIDTH);

        genderComboBox = new ComboBox<>(FXCollections.observableArrayList("Male", "Female"));
        genderComboBox.setPrefWidth(INPUT_WIDTH);
        genderComboBox.setValue("Male");
        genderComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        genderComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                genderComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: pink;  -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-highlight-fill: pink; -fx-highlight-text-fill: white;");
            } else {
                genderComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
            }
        });

        addressField = createStyledTextArea(INPUT_WIDTH);
        passwordField = createStyledPasswordField(INPUT_WIDTH);
        confirmPasswordField = createStyledPasswordField(INPUT_WIDTH);

        // Profile picture browse button and file name label
        browsePicButton = new Button("Choose File");
        browsePicButton.setPrefWidth(110);
        browsePicButton.setStyle("-fx-background-radius: 8; -fx-background-color: #f5f5f5; -fx-text-fill: black; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #bdbdbd; -fx-border-width: 1; -fx-border-radius: 8;");
        fileNameLabel = new Label("No file chosen");
        fileNameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
        browsePicButton.setOnAction(e -> handleBrowseProfilePic());
    }

    /**
     * Opens a file chooser for selecting a profile picture. If a file is
     * selected, copies it to the src/main/resources directory and updates the
     * file name label. If a file with the same name exists, adds a number to
     * the file name. If an error occurs during the copy, shows an error
     * message.
     */
    private void handleBrowseProfilePic() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                String fileName = selectedFile.getName();
                File dest = new File("src/main/resources/" + fileName);
                // If file with same name exists, add a number
                int count = 1;
                String baseName = fileName;
                String ext = "";
                int dot = fileName.lastIndexOf('.');
                if (dot > 0) {
                    baseName = fileName.substring(0, dot);
                    ext = fileName.substring(dot);
                }
                while (dest.exists()) {
                    fileName = baseName + "_" + count + ext;
                    dest = new File("src/main/resources/" + fileName);
                    count++;
                }
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                selectedProfilePicFileName = fileName;
                fileNameLabel.setText(fileName);
            } catch (IOException ex) {
                showError("Failed to copy image: " + ex.getMessage());
            }
        }
    }

    /**
     * Creates a styled TextField with a white background, 14px font size, and a
     * pink color when focused. When not focused, the border is a
     * light gray color (#E0E0E0). The border radius is 8 for both states.
     *
     * @param width the preferred width of the TextField
     * @return the styled TextField
     */
    private TextField createStyledTextField(int width) {
        TextField field = new TextField();
        field.setPrefWidth(width);
        field.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: pink; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-highlight-fill: pink; -fx-highlight-text-fill: white;");
            } else {
                field.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
            }
        });
        return field;
    }

    /**
     * Creates a styled PasswordField with a white background, 14px font size,
     * and a pink border when focused. When not focused, the border is
     * a light gray color (#E0E0E0). The border radius is 8 for both states.
     *
     * @param width the preferred width of the PasswordField
     * @return the styled PasswordField
     */
    private PasswordField createStyledPasswordField(int width) {
        PasswordField field = new PasswordField();
        field.setPrefWidth(width);
        field.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: pink; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-highlight-fill: pink; -fx-highlight-text-fill: white;");
            } else {
                field.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
            }
        });
        return field;
    }

    /**
     * Creates a styled TextArea with a white background and a specified width.
     * The TextArea has a font size of 14px and rounded corners with a radius of
     * 8. When focused, the border color changes to pink and the
     * border width increases to 2. When not focused, the border color is light
     * gray (#E0E0E0). The TextArea wraps text and has a default preferred row
     * count of 2.
     *
     * @param width the preferred width of the TextArea
     * @return the styled TextArea
     */
    private TextArea createStyledTextArea(int width) {
        TextArea area = new TextArea();
        area.setPrefRowCount(2);
        area.setPrefWidth(width);
        area.setWrapText(true);
        area.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        area.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                area.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: pink; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-highlight-fill: pink; -fx-highlight-text-fill: white;");
            } else {
                area.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
            }
        });
        return area;
    }

    /**
     * Handles the registration process. The method first checks if the username
     * already exists in the database. If it does, the method shows an error
     * message and returns. If the username does not exist, the method inserts
     * the new user record into the database using the provided field values.
     * Finally, the method shows a success message and navigates to the login
     * screen.
     */
    private void handleRegistration() {
        errorLabel.setVisible(false);
        if (!validateFields()) {
            return;
        }
        try {
            String checkQuery = "SELECT * FROM user WHERE userName = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, usernameField.getText());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                showError("Username already exists");
                return;
            }
            String insertQuery = "INSERT INTO user (realName, userName, password, sex, age, telephone, Email, address, pic, state, petHave, experience) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, fullNameField.getText());
            insertStmt.setString(2, usernameField.getText());
            insertStmt.setString(3, passwordField.getText());
            insertStmt.setString(4, genderComboBox.getValue().equals("Male") ? "Male" : "Female");
            insertStmt.setInt(5, Integer.parseInt(ageField.getText()));
            insertStmt.setString(6, phoneField.getText());
            insertStmt.setString(7, emailField.getText());
            insertStmt.setString(8, addressField.getText());
            insertStmt.setString(9, selectedProfilePicFileName != null ? selectedProfilePicFileName : "t0.jpg");
            insertStmt.setInt(10, 0); // Default state (no adoption experience)
            insertStmt.setInt(11, Integer.parseInt(petHaveField.getText()));
            insertStmt.setInt(12, Integer.parseInt(experienceField.getText()));
            insertStmt.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your account has been created successfully!");
            alert.showAndWait();
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    /**
     * Validates the user registration form fields.
     *
     * This method checks if all required fields are filled, ensures that the
     * username, password, email, phone number, age, address, and other fields
     * meet specific criteria. If any field is invalid, an appropriate error
     * message is displayed.
     *
     * Validation includes: - Non-empty check for all required fields. -
     * Username must be 20 characters or less. - Password must be 8 to 30
     * characters and match the confirmation field. - Valid email format and max
     * 30 characters. - Valid phone number format (10-20 digits). - Age must be
     * between 18 and 120. - Address must be 50 characters or less. - Number of
     * pets must be between 0 and 100. - Experience must be between 0 and 100
     * years. - A profile picture must be selected.
     *
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateFields() {
        if (fullNameField.getText().isEmpty() || usernameField.getText().isEmpty()
                || emailField.getText().isEmpty() || phoneField.getText().isEmpty()
                || addressField.getText().isEmpty() || passwordField.getText().isEmpty()
                || confirmPasswordField.getText().isEmpty() || petHaveField.getText().isEmpty()
                || experienceField.getText().isEmpty()) {
            showError("Please fill in all required fields");
            return false;
        }
        if (usernameField.getText().length() > 20) {
            showError("Username must be 20 characters or less");
            return false;
        }
        if (passwordField.getText().length() > 30) {
            showError("Password must be 30 characters or less");
            return false;
        }
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$") || emailField.getText().length() > 30) {
            showError("Please enter a valid email address (max 30 characters)");
            return false;
        }
        if (!phoneField.getText().matches("^\\d{10,20}$")) {
            showError("Please enter a valid phone number (10-20 digits)");
            return false;
        }
        try {
            int age = Integer.parseInt(ageField.getText());
            if (age < 18 || age > 120) {
                showError("Age must be between 18 and 120");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid age");
            return false;
        }
        if (addressField.getText().length() > 50) {
            showError("Address must be 50 characters or less");
            return false;
        }
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Passwords do not match");
            return false;
        }
        if (passwordField.getText().length() < 8) {
            showError("Password must be at least 8 characters long");
            return false;
        }
        try {
            int petHave = Integer.parseInt(petHaveField.getText());
            if (petHave < 0 || petHave > 100) {
                showError("Number of pets must be between 0 and 100");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid number for pets owned");
            return false;
        }
        try {
            int exp = Integer.parseInt(experienceField.getText());
            if (exp < 0 || exp > 100) {
                showError("Experience must be between 0 and 100 years");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid number for experience");
            return false;
        }
        if (selectedProfilePicFileName == null || selectedProfilePicFileName.isEmpty()) {
            showError("Please select a profile picture");
            return false;
        }
        return true;
    }

    /**
     * Sets the error label text and style, and makes it visible.
     *
     * @param message the error message to be displayed
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red;");
        errorLabel.setVisible(true);
    }
}
