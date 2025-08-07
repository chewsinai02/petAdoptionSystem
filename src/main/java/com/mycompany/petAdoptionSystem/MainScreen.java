package com.mycompany.petAdoptionSystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.petAdoptionSystem.admin.AdminDashboardScreen;
import com.mycompany.petAdoptionSystem.user.AdoptedPetListScreen;
import com.mycompany.petAdoptionSystem.user.UpdatePetStatus;
import com.mycompany.petAdoptionSystem.user.UserDashboardScreen;
import com.mycompany.petAdoptionSystem.user.ViewAdoptionApplicationsScreen;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public abstract class MainScreen {
    private static final String PRIMARY_COLOR = "#FAD9DD";
    private static final String SECONDARY_COLOR = "#F5F5F5";
    private static final String ACCENT_COLOR = "#2C3E50";
    protected final Stage stage;
    protected BorderPane mainLayout;
    protected StackPane contentArea;
    protected MenuBar menuBar;

    public MainScreen(Stage primaryStage) {
        this.stage = primaryStage;
        initializeUI();
    }

    protected void initializeUI() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + SECONDARY_COLOR + ";");

        // Create top bar with logo and menu
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; font-color: #4A90E2;-fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0; -fx-padding: 0;");

        // Create menu bar
        this.menuBar = createMenuBar();
        
        topBar.getChildren().addAll(menuBar);
        mainLayout.setTop(topBar);

        // Create content area
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: white; font-color: #4A90E2; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        mainLayout.setCenter(contentArea);

        // Show default content
        showDefaultContent();
    }

    // Abstract methods that must be implemented by subclasses
    protected abstract MenuBar createMenuBar();
    protected abstract void showDefaultContent();
    protected abstract String getDashboardTitle();

    protected void showPetGallery() {
        PetGalleryScreen galleryScreen = new PetGalleryScreen(stage);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(galleryScreen.getContent());
        StackPane.setMargin(galleryScreen.getContent(), new Insets(0));
    }

    protected void showPetCareInfo(String petType) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(petType.equals("dog") ? "Dog Care" : "Cat Care");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 8;");
        content.setAlignment(Pos.CENTER);
        
        String imagePath = petType.equals("dog") ? "/d3.jpg" : "/bg3.jpg";
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        // Apply rounded corners using clipping
        Rectangle clip = new Rectangle(150, 150);
        clip.setArcWidth(40);   // Radius X
        clip.setArcHeight(40);  // Radius Y
        imageView.setClip(clip);
        

        Text contentText = new Text(petType.equals("dog") ? 
            "\n Feeding time should be accurate, try to feed them according to the habits of their original owners, and do not feed them too much sweet, salty, and stimulating food. \n" +
            "\n For the new baby, the owner must feed them personally, and over time, they can establish a deep emotional connection and deepen the degree of mutual trust, \n" +
            "\n The other important task of the owner is to help the little one overcome the pain of leaving its mother and adapt to the new environment as soon as possible. \n" :
            "\n We can't keep the kitten at home all the time, we need to take the kitten out for a walk from time to time. \n" +
            "\n Then, when we raise the kitten, we also need to play with the kitten more. \n" +
            "\n When we raise the kitten, we also need to take care of its fur more.");
        contentText.setWrappingWidth(400);
        contentText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 18));
        

        content.getChildren().addAll(imageView, contentText);

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle("-fx-background-radius: 8; -fx-background-color: white");
        Button closeBtn = (Button) dialog.getDialogPane().lookupButton(closeButton);
        closeBtn.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: #FAD9DD;" +
            "-fx-border-color: #FAD9DD;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);"

        );
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: #FAD9DD;" +
            "-fx-text-fill: white;" +
            "-fx-border-color: white;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;"
        ));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-background-color: white;" +
            "-fx-text-fill: #FAD9DD;" +
            "-fx-border-color: #FAD9DD;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);"
        ));
        dialog.showAndWait();
    }

    protected void showAdoptionProcess() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Adoption Process");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 8;");

        Text processText = new Text(
            "1. Browse adoptable pets\n" +
            "2. Select a pet you like\n" +
            "3. Fill out the adoption application form\n" +
            "4. Wait for review\n" +
            "5. Arrange an interview after review is passed\n" +
            "6. Complete the adoption process"
        );
        processText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 15));

        content.getChildren().add(processText);

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle("-fx-background-radius: 8; -fx-background-color: white;");
        Button closeBtn = (Button) dialog.getDialogPane().lookupButton(closeButton);
        closeBtn.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: #FAD9DD;" +
            "-fx-border-color: #FAD9DD;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);"

        );
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: #FAD9DD;" +
            "-fx-text-fill: white;" +
            "-fx-border-color: white;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;"
        ));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-background-color: white;" +
            "-fx-text-fill: #FAD9DD;" +
            "-fx-border-color: #FAD9DD;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);"
        ));
        dialog.showAndWait();
    }

    protected void showAdoptedPetList() {
        AdoptedPetListScreen adoptedPetsScreen = new AdoptedPetListScreen(children -> contentArea.getChildren().setAll(children));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(adoptedPetsScreen.getContent());
        StackPane.setMargin(adoptedPetsScreen.getContent(), new Insets(0));
    }

    protected void showUserProfile() {
        int userId = UserSession.getCurrentUserId();
        if (userId <= 0) {
            showMessage("Not Logged In", "Please log in to view your profile.");
            return;
        }
        VBox profileCard = new VBox(20);
        profileCard.setAlignment(Pos.TOP_CENTER);
        profileCard.setPadding(new Insets(30, 40, 30, 40));
        profileCard.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 4);");

        // Title
        Text titleText = new Text("User Profile");
        titleText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 32));
        titleText.setFill(Color.web(ACCENT_COLOR));
        profileCard.getChildren().add(titleText);

        try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM user WHERE id = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Profile picture
                String picFile = rs.getString("pic");
                ImageView profilePic = new ImageView();
                try {
                    java.net.URL imgUrl = getClass().getResource("/" + picFile);
                    if (imgUrl != null) {
                        profilePic.setImage(new Image(imgUrl.toExternalForm()));
                    } else {
                        profilePic.setImage(new Image(getClass().getResource("/t0.jpg").toExternalForm()));
                    }
                } catch (Exception e) {
                    profilePic.setImage(new Image(getClass().getResource("/t0.jpg").toExternalForm()));
                }
                profilePic.setFitWidth(140);
                profilePic.setFitHeight(140);
                profilePic.setPreserveRatio(false); // Fill the square for a perfect circle
                profilePic.setStyle("-fx-background-radius: 70; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8, 0, 0, 2);");
                javafx.scene.shape.Circle circleClip = new javafx.scene.shape.Circle(70, 70, 70);
                profilePic.setClip(circleClip);

                profileCard.getChildren().add(profilePic);

                VBox infoBox = new VBox(16);
                infoBox.setAlignment(Pos.CENTER_LEFT);
                infoBox.setPadding(new Insets(20, 0, 0, 0));
                infoBox.setMaxWidth(400);

                // Helper for label/value row
                Font labelFont = Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 15);
                Color labelColor = Color.web(ACCENT_COLOR);
                Font valueFont = Font.font("System", FontWeight.BOLD, 15);
                String[][] fields = {
                    {"Full Name", rs.getString("realName")},
                    {"Username", rs.getString("userName")},
                    {"Email", rs.getString("Email")},
                    {"Phone", rs.getString("telephone")},
                    {"Age", String.valueOf(rs.getInt("age"))},
                    {"Gender", rs.getString("sex")},
                    {"Address", rs.getString("address")},
                    {"Pets Owned", String.valueOf(rs.getInt("petHave"))},
                    {"Experience (years)", String.valueOf(rs.getInt("experience"))}
                };
                for (String[] field : fields) {
                    Label label = new Label(field[0] + ":");
                    label.setFont(labelFont);
                    label.setTextFill(labelColor);
                    label.setMinWidth(140);
                    Label value = new Label(field[1]);
                    value.setFont(valueFont);
                    value.setStyle("-fx-text-fill: #333;");
                    HBox row = new HBox(10, label, value);
                    row.setAlignment(Pos.CENTER_LEFT);
                    infoBox.getChildren().add(row);
                }
                profileCard.getChildren().add(infoBox);

                // Edit button
                UserProfileData userData = new UserProfileData(
                    rs.getInt("id"),
                    rs.getString("realName"),
                    rs.getString("userName"),
                    rs.getString("Email"),
                    rs.getString("telephone"),
                    rs.getInt("age"),
                    rs.getString("sex"),
                    rs.getString("address"),
                    rs.getInt("petHave"),
                    rs.getInt("experience"),
                    rs.getString("pic")
                );
                Button editButton = new Button("Edit Profile");
                editButton.setStyle(
                    "-fx-background-color: #F4ACB5;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 10 30;"
                );
                editButton.setOnMouseEntered(e -> editButton.setStyle(
                    "-fx-background-color: #FAD9DD;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 10 30;" +
                    "-fx-border-color: #F4ACB5;" +
                    "-fx-border-radius: 8;"
                ));
                editButton.setOnMouseExited(e -> editButton.setStyle(
                    "-fx-background-color: #F4ACB5;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 10 30;"
                ));
                editButton.setOnAction(e -> showEditUserProfile(userData));
                profileCard.getChildren().add(editButton);
            } else {
                showMessage("Error", "User not found.");
                return;
            }
        } catch (SQLException e) {
            showMessage("Database Error", "Could not load user profile: " + e.getMessage());
            return;
        }
        contentArea.getChildren().setAll(profileCard);
    }

    // Helper class to hold user profile data
    private static class UserProfileData {
        int id;
        String realName;
        String userName;
        String email;
        String phone;
        int age;
        String gender;
        String address;
        int petHave;
        int experience;
        String pic;
        UserProfileData(int id, String realName, String userName, String email, String phone, int age, String gender, String address, int petHave, int experience, String pic) {
            this.id = id;
            this.realName = realName;
            this.userName = userName;
            this.email = email;
            this.phone = phone;
            this.age = age;
            this.gender = gender;
            this.address = address;
            this.petHave = petHave;
            this.experience = experience;
            this.pic = pic;
        }
    }

    // Show edit profile form, pre-filled with user info
    protected void showEditUserProfile(UserProfileData user) {
        try {
            int userId = user.id;
            final int INPUT_WIDTH = 320;
            VBox editCard = new VBox(8); // less vertical spacing
            editCard.setAlignment(Pos.TOP_CENTER);
            editCard.setPadding(new Insets(10, 12, 10, 12)); // smaller padding
            editCard.setMinHeight(700);
            editCard.setMaxHeight(980);
            editCard.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
            Text titleText = new Text("Edit Profile");
            titleText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 32));
            titleText.setFill(Color.web(ACCENT_COLOR));
            editCard.getChildren().add(titleText);
            // Profile picture preview
            ImageView profilePic = new ImageView();
            String[] picFileName = {user.pic}; // use array for lambda mutability
            try {
                java.net.URL imgUrl = getClass().getResource("/" + user.pic);
                if (imgUrl != null) {
                    profilePic.setImage(new Image(imgUrl.toExternalForm()));
                } else {
                    profilePic.setImage(new Image(getClass().getResource("/t0.jpg").toExternalForm()));
                }
            } catch (Exception e) {
                profilePic.setImage(new Image(getClass().getResource("/t0.jpg").toExternalForm()));
            }
            profilePic.setFitWidth(140);
            profilePic.setFitHeight(140);
            profilePic.setPreserveRatio(false); // Fill the square for a perfect circle
            profilePic.setStyle("-fx-background-radius: 70; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8, 0, 0, 2);");
            javafx.scene.shape.Circle circleClip = new javafx.scene.shape.Circle(70, 70, 70);
            profilePic.setClip(circleClip);

            editCard.getChildren().add(profilePic);
            VBox fieldBox = new VBox(6); // less vertical spacing
            fieldBox.setAlignment(Pos.CENTER_LEFT);
            fieldBox.setPadding(new Insets(4, 0, 0, 0));
            fieldBox.setMaxWidth(400);
            Font labelFont = Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 15);
            Color labelColor = Color.web(ACCENT_COLOR);
            // Editable fields (all same width)
            TextField fullNameField = new TextField(user.realName);
            fullNameField.setPrefWidth(INPUT_WIDTH);
            styleInputField(fullNameField);
            TextField usernameField = new TextField(user.userName);
            usernameField.setPrefWidth(INPUT_WIDTH);
            styleInputField(usernameField);
            TextField emailField = new TextField(user.email);
            emailField.setPrefWidth(INPUT_WIDTH);
            styleInputField(emailField);
            TextField phoneField = new TextField(user.phone);
            phoneField.setPrefWidth(INPUT_WIDTH);
            styleInputField(phoneField);
            TextField ageField = new TextField(String.valueOf(user.age));
            ageField.setPrefWidth(INPUT_WIDTH);
            styleInputField(ageField);
            ComboBox<String> genderComboBox = new ComboBox<>(FXCollections.observableArrayList("male", "female"));
            genderComboBox.setPrefWidth(INPUT_WIDTH);
            genderComboBox.setValue(user.gender);
            styleInputField(genderComboBox);
            TextArea addressField = new TextArea(user.address);
            addressField.setPrefRowCount(3);
            addressField.setPrefWidth(INPUT_WIDTH);
            addressField.setWrapText(true); // This ensures text wraps to the next line
            addressField.getStyleClass().clear();
            styleInputField(addressField);
            TextField petHaveField = new TextField(String.valueOf(user.petHave));
            petHaveField.setPrefWidth(INPUT_WIDTH);
            styleInputField(petHaveField);
            TextField experienceField = new TextField(String.valueOf(user.experience));
            experienceField.setPrefWidth(INPUT_WIDTH);
            styleInputField(experienceField);
            // Profile picture input (Choose File + file name label)
            Button browsePicButton = new Button("Choose File");
            browsePicButton.setPrefWidth(120); // wider to fit text
            browsePicButton.setMinWidth(120);
            browsePicButton.setStyle("-fx-background-radius: 8; -fx-background-color: #f5f5f5; -fx-text-fill: black; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #bdbdbd; -fx-border-width: 1; -fx-border-radius: 8;");
            // Pic name label: truncate with ellipsis if too long
            Label fileNameLabel = new Label(user.pic != null ? user.pic : "No file chosen");
            fileNameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
            fileNameLabel.setMaxWidth(120);
            fileNameLabel.setMinWidth(80);
            fileNameLabel.setPrefWidth(120);
            fileNameLabel.setEllipsisString("...");
            fileNameLabel.setWrapText(false);
            browsePicButton.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Profile Picture");
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
                );
                java.io.File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    try {
                        String fileName = selectedFile.getName();
                        java.io.File dest = new java.io.File("src/main/resources/" + fileName);
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
                            dest = new java.io.File("src/main/resources/" + fileName);
                            count++;
                        }
                        java.nio.file.Files.copy(selectedFile.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        picFileName[0] = fileName;
                        fileNameLabel.setText(fileName);
                        // Update preview
                        java.net.URL imgUrl = getClass().getResource("/" + fileName);
                        if (imgUrl != null) {
                            profilePic.setImage(new Image(imgUrl.toExternalForm()));
                        }
                    } catch (IOException ex) {
                        fileNameLabel.setText("Failed to copy image");
                    }
                }
            });
            // Use HBox for button and label, with spacing
            HBox picRow = new HBox(8, browsePicButton, fileNameLabel);
            picRow.setAlignment(Pos.CENTER_LEFT);
            picRow.setPrefWidth(INPUT_WIDTH);
            // Helper for label/field row
            fieldBox.getChildren().addAll(
                createEditRow("Full Name", fullNameField, labelFont, labelColor),
                createEditRow("Username", usernameField, labelFont, labelColor),
                createEditRow("Email", emailField, labelFont, labelColor),
                createEditRow("Phone", phoneField, labelFont, labelColor),
                createEditRow("Age", ageField, labelFont, labelColor),
                createEditRow("Gender", genderComboBox, labelFont, labelColor),
                createEditRow("Address", addressField, labelFont, labelColor),
                createEditRow("Pets Owned", petHaveField, labelFont, labelColor),
                createEditRow("Experience (years)", experienceField, labelFont, labelColor),
                createEditRow("Profile Picture", picRow, labelFont, labelColor)
            );
            // Error label
            Label errorLabel = new Label();
            errorLabel.setTextFill(Color.web("#D32F2F"));
            errorLabel.setVisible(false);
            errorLabel.setMaxWidth(Double.MAX_VALUE);
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-background-color: #FFEBEE; -fx-border-color: #FFCDD2; -fx-background-radius: 8; -fx-border-radius: 8");
            fieldBox.getChildren().add(errorLabel);
            // Save button (centered)
            Button saveButton = new Button("Save");
            saveButton.setStyle(
                "-fx-background-color: #F4ACB5;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 30;"
            );
            saveButton.setOnMouseEntered(e -> saveButton.setStyle(
                "-fx-background-color: " + PRIMARY_COLOR + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 30;" +
                "-fx-border-color: #F4ACB5;" +
                "-fx-border-radius: 8;"
            ));
            saveButton.setOnMouseExited(e -> saveButton.setStyle(
                "-fx-background-color: #F4ACB5;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 30;"
            ));
            HBox saveBox = new HBox(saveButton);
            saveBox.setAlignment(Pos.CENTER);
            saveButton.setOnAction(ev -> {
                // Validation
                if (fullNameField.getText().isEmpty() || usernameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                    ageField.getText().isEmpty() || addressField.getText().isEmpty() || petHaveField.getText().isEmpty() || experienceField.getText().isEmpty()) {
                    errorLabel.setText("Please fill in all required fields");
                    errorLabel.setVisible(true);
                    return;
                }
                if (usernameField.getText().length() > 20) {
                    errorLabel.setText("Username must be 20 characters or less");
                    errorLabel.setVisible(true);
                    return;
                }
                // Check username uniqueness
                try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
                    String checkQuery = "SELECT id FROM user WHERE userName = ? AND id <> ?";
                    java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                    checkStmt.setString(1, usernameField.getText());
                    checkStmt.setInt(2, userId);
                    java.sql.ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        errorLabel.setText("Username already exists");
                        errorLabel.setVisible(true);
                        return;
                    }
                } catch (SQLException ex) {
                    errorLabel.setText("Database error: " + ex.getMessage());
                    errorLabel.setVisible(true);
                    return;
                }
                try {
                    int age = Integer.parseInt(ageField.getText());
                    int petHave = Integer.parseInt(petHaveField.getText());
                    int exp = Integer.parseInt(experienceField.getText());
                    if (age < 18 || age > 120) {
                        errorLabel.setText("Age must be between 18 and 120");
                        errorLabel.setVisible(true);
                        return;
                    }
                    if (petHave < 0 || petHave > 100) {
                        errorLabel.setText("Number of pets must be between 0 and 100");
                        errorLabel.setVisible(true);
                        return;
                    }
                    if (exp < 0 || exp > 100) {
                        errorLabel.setText("Experience must be between 0 and 100 years");
                        errorLabel.setVisible(true);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Please enter valid numbers for age, pets owned, and experience");
                    errorLabel.setVisible(true);
                    return;
                }
                // Save to DB
                try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
                    String update = "UPDATE user SET realName=?, userName=?, Email=?, telephone=?, age=?, sex=?, address=?, petHave=?, experience=?, pic=? WHERE id=?";
                    java.sql.PreparedStatement stmt = conn.prepareStatement(update);
                    stmt.setString(1, fullNameField.getText());
                    stmt.setString(2, usernameField.getText());
                    stmt.setString(3, emailField.getText());
                    stmt.setString(4, phoneField.getText());
                    stmt.setInt(5, Integer.parseInt(ageField.getText()));
                    stmt.setString(6, genderComboBox.getValue());
                    stmt.setString(7, addressField.getText());
                    stmt.setInt(8, Integer.parseInt(petHaveField.getText()));
                    stmt.setInt(9, Integer.parseInt(experienceField.getText()));
                    stmt.setString(10, picFileName[0]);
                    stmt.setInt(11, userId);
                    stmt.executeUpdate();
                    showUserProfile();
                } catch (SQLException ex) {
                    errorLabel.setText("Database error: " + ex.getMessage());
                    errorLabel.setVisible(true);
                }
            });
            // Cancel button
            Button cancelButton = new Button("Cancel");
            cancelButton.setStyle(
                "-fx-background-color: #F5F5F5;" +
                "-fx-text-fill: " + ACCENT_COLOR + ";" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 30;"
            );
            cancelButton.setOnAction(ev -> showUserProfile());
            editCard.getChildren().add(fieldBox);
            
            HBox buttonBox = new HBox(10, saveButton, cancelButton);
            buttonBox.setAlignment(Pos.CENTER);
            fieldBox.getChildren().add(buttonBox);
            contentArea.getChildren().setAll(editCard);
        } catch (Exception e) {
            showMessage("Error", "Could not load user info for editing: " + e.getMessage());
        }
    }
    
    protected void styleInputField(Control field) {
        field.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-radius: 6;" +
            "-fx-padding: 6;"
        );
    }


    // Helper for edit row
    protected HBox createEditRow(String labelText, javafx.scene.Node field, Font labelFont, Color labelColor) {
        Label label = new Label(labelText + ":");
        label.setFont(labelFont);
        label.setTextFill(labelColor);
        label.setMinWidth(140);
        HBox row = new HBox(10, label, field);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    protected void showLoginScreen() {
        Stage loginStage = new Stage();
        LoginScreen loginScreen = new LoginScreen(loginStage);
        loginScreen.setOnUserLoginSuccess(() -> {
            UserDashboardScreen userDashboard = new UserDashboardScreen(stage);
            userDashboard.show();
            loginStage.close();
        });
        loginScreen.setOnAdminLoginSuccess(() -> {
            AdminDashboardScreen adminDashboard = new AdminDashboardScreen(stage);
            adminDashboard.show();
            loginStage.close();
        });
        loginScreen.show();
    }

    protected void updateMenuBarAfterLogin() {
        // Remove all menus and recreate based on login state
        this.menuBar.getMenus().clear();
        MenuBar newMenuBar = createMenuBar();
        this.menuBar.getMenus().addAll(newMenuBar.getMenus());
        // If logged in, add user menu items as before
        if (UserSession.isLoggedIn()) {
            Menu userMenu = new Menu("User");
            MenuItem profileItem = new MenuItem("User Profile");
            MenuItem myApplicationsItem = new MenuItem("My Adoption Applications");
            MenuItem viewAdoptedPetsItem = new MenuItem("View Adopted Pets");
            MenuItem updatePetStatusItem = new MenuItem("Update Pet Status");
            MenuItem logoutItem = new MenuItem("Logout");
            profileItem.setOnAction(e -> showUserProfile());
            myApplicationsItem.setOnAction(e -> showMyApplications());
            viewAdoptedPetsItem.setOnAction(e -> showAdoptedPetList());
            updatePetStatusItem.setOnAction(e -> showUpdatePetStatus());
            logoutItem.setOnAction(e -> handleLogout());
            userMenu.getItems().addAll(profileItem, myApplicationsItem, viewAdoptedPetsItem, updatePetStatusItem, logoutItem);
            this.menuBar.getMenus().add(userMenu);
        }
    }

    protected void showMyApplications() {
        ViewAdoptionApplicationsScreen applicationsScreen = new ViewAdoptionApplicationsScreen(new Stage());
        applicationsScreen.show();
    }

    protected void showUpdatePetStatus() {
        UpdatePetStatus UpdateScreen = new UpdatePetStatus();
        contentArea.getChildren().setAll(UpdateScreen.getContent());
    }

    protected void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                UserSession.setLoggedIn(false);
                UserSession.setCurrentUserId(-1);
                UserSession.setAdmin(false);
                // Create a concrete implementation for the main screen
                MainScreen mainScreen = new MainScreen(stage) {
                    @Override
                    protected MenuBar createMenuBar() {
                        MenuBar localMenuBar = new MenuBar();
                        localMenuBar.setStyle("-fx-background-color: white; font-color: #4A90E2;-fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0; -fx-padding: 5 0;");

                        // Home Menu
                        Menu homeMenu = new Menu("Home");
                        MenuItem homeItem = new MenuItem("Back to Home");
                        homeItem.setOnAction(e -> showPetGallery());
                        homeMenu.getItems().add(homeItem);

                        // Pet Knowledge Menu
                        Menu knowledgeMenu = new Menu("Pet Knowledge");
                        MenuItem dogCareItem = new MenuItem("Dog Care");
                        MenuItem catCareItem = new MenuItem("Cat Care");
                        dogCareItem.setOnAction(e -> showPetCareInfo("dog"));
                        catCareItem.setOnAction(e -> showPetCareInfo("cat"));
                        knowledgeMenu.getItems().addAll(dogCareItem, catCareItem);

                        // Adoption Menu
                        Menu adoptionMenu = new Menu("Adoption");
                        MenuItem adoptionProcessItem = new MenuItem("Adoption Process");
                        adoptionProcessItem.setOnAction(e -> showAdoptionProcess());
                        adoptionMenu.getItems().add(adoptionProcessItem);

                        // User Account Menu
                        Menu userAccountMenu = new Menu("Account");
                        MenuItem loginRegisterItem = new MenuItem("Login/Register");
                        loginRegisterItem.setOnAction(e -> showLoginScreen());
                        userAccountMenu.getItems().add(loginRegisterItem);

                        // Notification Menu
                        Menu notificationMenu = new Menu("Notification");
                        MenuItem notificationItem = new MenuItem("Notification");
                        notificationItem.setOnAction(e -> showNotification());
                        notificationMenu.getItems().add(notificationItem);

                        // Show/hide menus based on login state
                        boolean loggedIn = UserSession.isLoggedIn();
                        if (loggedIn) {
                            localMenuBar.getMenus().addAll(homeMenu, knowledgeMenu, adoptionMenu, notificationMenu);
                        } else {
                            localMenuBar.getMenus().addAll(homeMenu, knowledgeMenu, adoptionMenu, userAccountMenu);
                        }
                        return localMenuBar;
                    }

                    @Override
                    protected void showDefaultContent() {
                        showPetGallery();
                    }

                    @Override
                    protected String getDashboardTitle() {
                        return "Pet Adoption System";
                    }
                };
                mainScreen.show();
            }
        });
    }

    protected void showNotification() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT n.*, u.realName, p.petName " +
                            "FROM notification n " +
                            "JOIN user u ON n.userId = u.id " +
                            "JOIN pet p ON n.petId = p.id " +
                            "WHERE n.userId = ? " +
                            "AND EXISTS (SELECT 1 FROM adoptanimal a WHERE a.userId = n.userId AND a.petId = n.petId AND a.state = 2) " +
                            "AND ( " +
                            "  NOT EXISTS ( " +
                            "    SELECT 1 FROM petupdate pu " +
                            "    JOIN adoptanimal aa ON pu.adoptId = aa.id " +
                            "    WHERE aa.userId = n.userId AND aa.petId = n.petId " +
                            "  ) " +
                            "  OR n.createdAt > ( " +
                            "    SELECT MAX(pu.updateTime) " +
                            "    FROM petupdate pu " +
                            "    JOIN adoptanimal aa ON pu.adoptId = aa.id " +
                            "    WHERE aa.userId = n.userId AND aa.petId = n.petId " +
                            "  ) " +
                            ")"
            );
            stmt.setInt(1, UserSession.getCurrentUserId());
            ResultSet rs = stmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            int count = 0;
            while (rs.next()) {
                String petName = rs.getString("petName");
                String createdAt = rs.getString("createdAt");
                sb.append("Please update the status for pet: ").append(petName)
                        .append(" (requested at ").append(createdAt).append(")\n");
                count++;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText(null);
            if (count == 0) {
                alert.setContentText("You have no new notifications.");
            } else {
                alert.setContentText(sb.toString());
            }
            alert.showAndWait();
        } catch (SQLException | RuntimeException e) {
            showMessage("Error", "Could not load notifications: " + e.getMessage());
        }
    }

    protected void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void show() {
        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("Pet Adoption System");
        stage.setScene(scene);
        stage.show();
    }
}