package com.mycompany.petAdoptionSystem.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.petAdoptionSystem.Pet;
import com.mycompany.petAdoptionSystem.UserSession;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class UpdatePetStatus {
    private static final String PRIMARY_COLOR = "#F4ACB5";
    private static final String SECONDARY_COLOR = "#FAD9DD";
    private BorderPane content;
    private Connection conn;
    private ComboBox<String> petComboBox;
    private TextArea updateContent;
    private TextField picPathField;
    // New field to hold the pet to update
    private Integer initialPetId = null;
    private Pet initialPet = null;
    // New field to hold adopted pets
    private List<Pet> adoptedPets = new ArrayList<>();

    public UpdatePetStatus() { // Default constructor without Stage
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
            initializeUI();
        } catch (ClassNotFoundException e) {
            showError("Database driver not found: " + e.getMessage());
        } catch (SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    // New constructor to pre-select a pet, without Stage
    public UpdatePetStatus(Pet petToUpdate) {
        this(); // Call the default constructor
        this.initialPet = petToUpdate;
    }

    public UpdatePetStatus(int petId) {
        this.initialPetId = petId;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
            loadInitialPet();
            initializeUI();
        } catch (ClassNotFoundException e) {
            showError("Database driver not found: " + e.getMessage());
        } catch (SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    private void initializeUI() {
        content = new BorderPane();
        content.setStyle("-fx-background-color: " + SECONDARY_COLOR + ";");

        // Title
        Text titleText = new Text("Update Adopted Pet Status");
        titleText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 28));
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));
        titleBox.getChildren().add(titleText);
        content.setTop(titleBox);

        // Create form
        GridPane formGrid = createUpdateForm();
        content.setCenter(formGrid);

        // Add focus styling
        petComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            styleFocusedComboBox(petComboBox, newVal);
        });
        updateContent.focusedProperty().addListener((obs, oldVal, newVal) -> {
            styleFocusedTextArea(updateContent, newVal);
        });

        // Load pets
        loadAdoptedPets();

        // After loading pets, set initial selection if provided
        if (initialPet != null) {
            petComboBox.getSelectionModel().select(initialPet.getName());
        }
    }

    private GridPane createUpdateForm() {
        petComboBox = new ComboBox<>();
        updateContent = new TextArea();
        picPathField = new TextField();

        final int LABEL_WIDTH = 150;
        final int INPUT_WIDTH = 400;

        Font labelFont = Font.font("System", FontWeight.BOLD, 15);
        Font valueFont = Font.font("System", FontWeight.NORMAL, 14);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(28, 36, 28, 36));
        grid.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 2);"
        );

        VBox inputBox = new VBox(6);
        inputBox.setAlignment(Pos.TOP_CENTER);

        // Pet selection
        Label petLabel = new Label("Select Pet:");
        petLabel.setFont(labelFont);
        petLabel.setMinWidth(LABEL_WIDTH);
        petLabel.setMaxWidth(LABEL_WIDTH);
        petLabel.setStyle("-fx-text-fill: #2C3E50;");
        petComboBox.setPrefWidth(INPUT_WIDTH);
        petComboBox.setStyle("-fx-font-size: 15px; -fx-background-radius: 8; -fx-border-color: #F4ACB5; -fx-border-radius: 8;");
        HBox petRow = new HBox(12, petLabel, petComboBox);
        petRow.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(petRow);

        // Animal pictures row
        HBox imageBox = new HBox(10);
        imageBox.setAlignment(Pos.CENTER);
        HBox picRow = new HBox(12, imageBox);
        picRow.setAlignment(Pos.CENTER);
        inputBox.getChildren().add(picRow);

        // Pet details (Name, Type, Sex, Birthday)
        String[] detailLabels = {"Name:", "Type:", "Sex:", "Birthday:"};
        Label[] valueLabels = new Label[4];
        for (int i = 0; i < 4; i++) {
            Label l = new Label(detailLabels[i]);
            l.setFont(labelFont);
            l.setMinWidth(LABEL_WIDTH);
            l.setMaxWidth(LABEL_WIDTH);
            l.setStyle("-fx-text-fill: #2C3E50;");
            Label v = new Label();
            v.setFont(valueFont);
            v.setStyle("-fx-font-size: 12px; -fx-background-radius: 8; -fx-border-color: #F4ACB5; -fx-border-radius: 8; -fx-padding: 8; -fx-background-color: #F5F5F5;");
            v.setPrefWidth(INPUT_WIDTH);
            v.setMinWidth(INPUT_WIDTH);
            v.setMaxWidth(INPUT_WIDTH);
            v.setMinHeight(35);
            v.setPrefHeight(35);
            v.setMaxHeight(35);
            valueLabels[i] = v;
            HBox row = new HBox(12, l, v);
            row.setAlignment(Pos.CENTER_LEFT);
            inputBox.getChildren().add(row);
        }
        final Label nameValue = valueLabels[0], typeValue = valueLabels[1], sexValue = valueLabels[2], birthdayValue = valueLabels[3];

        // Remark as TextArea
        Label remarkLabel = new Label("Characteristics:");
        remarkLabel.setFont(labelFont);
        remarkLabel.setMinWidth(LABEL_WIDTH);
        remarkLabel.setMaxWidth(LABEL_WIDTH);
        remarkLabel.setStyle("-fx-text-fill: #2C3E50;");

        TextArea remarkArea = new TextArea();
        remarkArea.getStyleClass().clear();
        remarkArea.setFont(valueFont);
        remarkArea.setPrefWidth(INPUT_WIDTH);
        remarkArea.setMinWidth(INPUT_WIDTH);
        remarkArea.setMaxWidth(INPUT_WIDTH);
        remarkArea.setPrefRowCount(2);
        remarkArea.setWrapText(true);
        remarkArea.setStyle("-fx-font-size: 12px; -fx-background-radius: 8; -fx-border-color: #F4ACB5; -fx-border-radius: 8; -fx-padding: 8; -fx-background-color: #F5F5F5;");
        remarkArea.setEditable(false);

        HBox remarkRow = new HBox(12, remarkLabel, remarkArea);
        remarkRow.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(remarkRow);

        // Update content
        Label contentLabel = new Label("Update Content:");
        contentLabel.setFont(labelFont);
        contentLabel.setMinWidth(LABEL_WIDTH);
        contentLabel.setMaxWidth(LABEL_WIDTH);
        contentLabel.setStyle("-fx-text-fill: #2C3E50;");
        updateContent.setPrefWidth(INPUT_WIDTH);
        updateContent.setPrefRowCount(4);
        updateContent.setWrapText(true);
        updateContent.setStyle("-fx-font-size: 15px; -fx-background-radius: 8; -fx-border-color: #F4ACB5; -fx-border-radius: 8; -fx-padding: 8;");
        HBox contentRow = new HBox(12, contentLabel, updateContent);
        contentRow.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(contentRow);

        // Picture upload
        Label picLabel = new Label("Picture:");
        picLabel.setFont(labelFont);
        picLabel.setMinWidth(LABEL_WIDTH);
        picLabel.setMaxWidth(LABEL_WIDTH);
        picLabel.setStyle("-fx-text-fill: #2C3E50;");
        picPathField.setEditable(false);
        picPathField.setPrefWidth(INPUT_WIDTH - 100);
        picPathField.setStyle("-fx-font-size: 15px; -fx-background-radius: 8; -fx-border-color: #F4ACB5; -fx-border-radius: 8; -fx-background-color: #F5F5F5;");
        Button browseButton = new Button("Browse");
        browseButton.setStyle(
            "-fx-background-color: #fff;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-width: 2;" +
            "-fx-text-fill: #F4ACB5;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 6 18;"
        );
        browseButton.setOnAction(e -> handleBrowse());
        HBox browseRow = new HBox(12, picLabel, picPathField, browseButton);
        browseRow.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(browseRow);

        // Submit button
        Button submitButton = new Button("Submit Update");
        submitButton.setStyle(
            "-fx-background-color: #F4ACB5;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 16;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 32;" +
            "-fx-effect: dropshadow(gaussian, rgba(244,172,181,0.18), 6, 0, 0, 1);"
        );
        submitButton.setOnAction(e -> handleSubmit());
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().add(buttonBox);

        grid.add(inputBox, 0, 0);

        // Update pet details logic
        Runnable updatePetDetails = () -> {
            Pet pet = initialPet != null ? initialPet : getPetByName(petComboBox.getValue());
            if (pet != null) {
                nameValue.setText(pet.getName());
                typeValue.setText(pet.getType());
                sexValue.setText(pet.getSex());
                birthdayValue.setText(pet.getBirthday() != null ? pet.getBirthday().toString() : "");
                remarkArea.setText(pet.getRemark() != null ? pet.getRemark() : "");
                imageBox.getChildren().clear();
                if (pet.getPic() != null && !pet.getPic().isEmpty()) {
                    String[] images = pet.getPic().split(",");
                    for (String img : images) {
                        String imgTrim = img.trim();
                        String imagePath = "/" + imgTrim;
                        java.net.URL imageUrl = getClass().getResource(imagePath);
                        ImageView imageView;
                        if (imageUrl != null) {
                            imageView = new ImageView(new javafx.scene.image.Image(imageUrl.toExternalForm()));
                        } else {
                            java.io.File file = new java.io.File("src/main/resources/" + imgTrim);
                            if (file.exists()) {
                                imageView = new ImageView(new javafx.scene.image.Image(file.toURI().toString()));
                            } else {
                                imageView = new ImageView();
                            }
                        }
                        imageView.setFitWidth(180);
                        imageView.setFitHeight(180);
                        imageView.setPreserveRatio(true);
                        imageView.setStyle(
                            "-fx-background-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 4, 0, 0, 1);" +
                            "-fx-border-color: #F4ACB5;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 12;"
                        );
                        imageBox.getChildren().add(imageView);
                    }
                }
            } else {
                nameValue.setText("");
                typeValue.setText("");
                sexValue.setText("");
                birthdayValue.setText("");
                remarkArea.setText("");
                imageBox.getChildren().clear();
            }
        };

        petComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updatePetDetails.run());
        if (initialPet != null) updatePetDetails.run();

        return grid;
    }

    private void styleFocusedComboBox(ComboBox<String> comboBox, boolean focused) {
        if (focused) {
            comboBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: " + PRIMARY_COLOR + "; -fx-border-width: 2; -fx-border-radius: 5;");
        } else {
            comboBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: " + SECONDARY_COLOR + "; -fx-border-radius: 5;");
        }
    }

    private void styleFocusedTextArea(TextArea textArea, boolean focused) {
        if (focused) {
            textArea.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: " + PRIMARY_COLOR + "; -fx-border-width: 2; -fx-border-radius: 5;");
        } else {
            textArea.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: " + SECONDARY_COLOR + "; -fx-border-radius: 5;");
        }
    }

    private void loadAdoptedPets() {
        try {
            int userId = UserSession.getCurrentUserId();
            if (userId <= 0) {
                showError("Please log in to submit updates");
                return;
            }

            adoptedPets.clear();
            List<String> petNames = new ArrayList<>();

            if (initialPet != null) {
                // Only show the selected pet
                adoptedPets.add(initialPet);
                petNames.add(initialPet.getName());
                petComboBox.getItems().setAll(petNames);
                petComboBox.getSelectionModel().select(initialPet.getName());
                petComboBox.setDisable(true);
            } else {
                // Show all adopted pets
                String query = "SELECT * FROM pet p JOIN adoptanimal a ON p.id = a.petId WHERE a.userId = ? AND a.state = 2";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Pet pet = new Pet(
                        rs.getInt("id"),
                        rs.getString("petName"),
                        rs.getString("petType"),
                        rs.getString("sex"),
                        rs.getDate("birthday"),
                        rs.getString("pic"),
                        rs.getInt("state"),
                        rs.getString("remark")
                    );
                    adoptedPets.add(pet);
                    petNames.add(pet.getName());
                }
                petComboBox.getItems().setAll(petNames);
                petComboBox.setDisable(false);
            }
        } catch (SQLException e) {
            showError("Error loading adopted pets: " + e.getMessage());
        }
    }

    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Pet Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        // The FileChooser needs a parent window to show correctly. 
        // Since updatePetStatus no longer has its own stage, we can pass null or find a parent.
        // For simplicity and assuming the MainScreen is managing this, we will remove stage from here.
        java.io.File file = fileChooser.showOpenDialog(null); // Changed to null
        if (file != null) {
            picPathField.setText(file.getAbsolutePath());
        }
    }

    private void handleSubmit() {
        if (!validateForm()) {
            return;
        }

        try {
            String petName;
            int userId = UserSession.getCurrentUserId();
            if (initialPet != null) {
                petName = initialPet.getName();
            } else {
                petName = petComboBox.getValue();
            }
            String adoptQuery = "SELECT a.id FROM adoptanimal a " +
                              "JOIN pet p ON a.petId = p.id " +
                              "WHERE a.userId = ? AND p.petName = ? AND a.state = 2";
            PreparedStatement adoptStmt = conn.prepareStatement(adoptQuery);
            adoptStmt.setInt(1, userId);
            adoptStmt.setString(2, petName);
            ResultSet rs = adoptStmt.executeQuery();

            if (!rs.next()) {
                showError("Could not find adoption record");
                return;
            }

            int adoptId = rs.getInt("id");

            // Handle image file
            String selectedPath = picPathField.getText().trim();
            String filename = "";
            if (!selectedPath.isEmpty()) {
                java.io.File srcFile = new java.io.File(selectedPath);
                filename = srcFile.getName();
                java.io.File destFile = new java.io.File("src/main/resources/" + filename);
                if (!destFile.exists()) {
                    java.nio.file.Files.copy(srcFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // Insert update (save only filename)
            String insertQuery = "INSERT INTO petupdate (adoptId, updateTime, updateContent, updatePic) VALUES (?, CURDATE(), ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, adoptId);
            insertStmt.setString(2, updateContent.getText().trim());
            insertStmt.setString(3, filename); // Only filename
            insertStmt.executeUpdate();

            showMessage("Success", "Your update has been submitted.");
            clearForm();
        } catch (SQLException e) {
            showError("Error submitting update: " + e.getMessage());
        } catch (java.io.IOException e) {
            showError("Error handling file: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        if (initialPet == null && petComboBox.getValue() == null) {
            showError("Please select a pet");
            return false;
        }
        if (updateContent.getText().trim().isEmpty()) {
            showError("Please enter update content");
            return false;
        }
        return true;
    }

    private void clearForm() {
        petComboBox.getSelectionModel().clearSelection();
        updateContent.clear();
        picPathField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public BorderPane getContent() {
        return content;
    }

    private Pet getPetByName(String name) {
        for (Pet pet : adoptedPets) {
            if (pet.getName().equals(name)) {
                return pet;
            }
        }
        return null;
    }

    private void loadInitialPet() {
        try {
            String query = "SELECT * FROM pet WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, initialPetId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                initialPet = new Pet(
                    rs.getInt("id"),
                    rs.getString("petName"),
                    rs.getString("petType"),
                    rs.getString("sex"),
                    rs.getDate("birthday"),
                    rs.getString("pic"),
                    rs.getInt("state"),
                    rs.getString("remark")
                );
            }
        } catch (SQLException e) {
            showError("Error loading pet details: " + e.getMessage());
        }
    }
}
