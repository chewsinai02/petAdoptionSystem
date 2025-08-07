package com.mycompany.petAdoptionSystem.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mycompany.petAdoptionSystem.Pet;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ManagePetsScreen extends AdminDashboardScreen {
    private static final String PRIMARY_COLOR = "#F4ACB5";
    private static final String SECONDARY_COLOR = "#fad9dd";
    private static final String ACCENT_COLOR = "#2C3E50";
    private Connection conn;
    private TableView<Pet> petTable;
    private TextField nameField, typeField, remarkField;
    private ComboBox<String> sexComboBox;
    private DatePicker birthdayPicker;
    private TextField picField;
    private ComboBox<String> stateComboBox;
    private VBox content;

    public ManagePetsScreen(Stage primaryStage) {
        super(primaryStage);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
            initializeUI();
        } catch (ClassNotFoundException | SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    @Override
    protected void initializeUI() {
        content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color:" + SECONDARY_COLOR +"; -fx-background-radius: 12;-fx-border-radius: 12;");

        // Title
        Text titleText = new Text("Manage Pets");
        titleText.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleText.setStyle("-fx-font-family: 'Cherry Bomb One';");
        titleText.setFill(Color.web(ACCENT_COLOR));

        // Create table
        createPetTable();

        // Create form for adding/editing pets
        GridPane formGrid = createPetForm();
        formGrid.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); -fx-background-radius: 10;");

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button addButton = createStyledButton("Add Pet", e -> handleAddPet());
        Button updateButton = createStyledButton("Update Pet", e -> handleUpdatePet());
        Button deleteButton = createStyledButton("Delete Pet", e -> handleDeletePet());
        Button clearButton = createStyledButton("Clear Form", e -> clearForm());
        
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

        // Add all components to content
        content.getChildren().addAll(titleText, petTable, formGrid, buttonBox);
    }

    @SuppressWarnings("unchecked")
    private void createPetTable() {
        petTable = new TableView<>();
        petTable.setEditable(false);
        petTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        petTable.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); -fx-selection-bar: #F4ACB5; -fx-padding: 10; -fx-border-radius: 1px; -fx-border-width: 1px; -fx-border-color: white;");

        // Create columns
        TableColumn<Pet, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 5px 0;");

        TableColumn<Pet, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 5px 0;");

        TableColumn<Pet, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        typeCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 5px 0;");

        TableColumn<Pet, String> sexCol = new TableColumn<>("Sex");
        sexCol.setCellValueFactory(cellData -> cellData.getValue().sexProperty());
        sexCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 5px 0;");

        TableColumn<Pet, String> birthdayCol = new TableColumn<>("Birthday");
        birthdayCol.setCellValueFactory(cellData -> cellData.getValue().birthdayProperty().asString());
        birthdayCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 5px 0;");

        TableColumn<Pet, String> stateCol = new TableColumn<>("State");
        stateCol.setCellValueFactory(cellData -> cellData.getValue().stateProperty().asString());
        // Custom cell factory to show text instead of number
        stateCol.setCellFactory(col -> new TableCell<Pet, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    switch (item) {
                        case "0": setText("Available"); break;
                        case "1": setText("Under Review"); break;
                        case "2": setText("Adopted"); break;
                        case "3": setText("Returned"); break;
                        default: setText("Unknown"); break;
                    }
                }
            }
        });
        stateCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 5px 0;");

        petTable.getColumns().addAll(idCol, nameCol, typeCol, sexCol, birthdayCol, stateCol);

        // Add selection listener
        petTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });

        // Load data
        loadPets();
    }

    private GridPane createPetForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Add fields to grid
        int row = 0;

        // Initialize form fields
        nameField = new TextField();
        nameField.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");
        nameField.focusedProperty().addListener((obs, oldVal, newVal) -> styleFocusedField(nameField, newVal));

        typeField = new TextField();
        typeField.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");
        typeField.focusedProperty().addListener((obs, oldVal, newVal) -> styleFocusedField(typeField, newVal));

        sexComboBox = new ComboBox<>();
        sexComboBox.getItems().addAll("male", "female");
        sexComboBox.setStyle(
                "-fx-font-size: 14px; "
                + "-fx-background-color: white; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-radius: 5;");
        sexComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> styleFocusedComboBox(sexComboBox, newVal));
        sexComboBox.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        birthdayPicker = new DatePicker();
        birthdayPicker.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");
        birthdayPicker.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        picField = new TextField();
        picField.setEditable(false);
        picField.setStyle("-fx-font-size: 14px; -fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");
        Button browseButton = new Button("Browse");
        // Image preview box
        HBox imagePreviewBox = new HBox(8);
        imagePreviewBox.setAlignment(Pos.CENTER_LEFT);
        imagePreviewBox.setPadding(new Insets(5, 0, 0, 0));
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Pet Pictures");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.webp", "*.tiff", "*.tif")
            );
            List<File> files = fileChooser.showOpenMultipleDialog(stage);
            if (files != null && !files.isEmpty()) {
                Set<String> fileNames = new LinkedHashSet<>();
                imagePreviewBox.getChildren().clear();
                for (File file : files) {
                    try {
                        String fileName = file.getName();
                        File dest = new File("src/main/resources/" + fileName);
                        // If file exists, add _1, _2, etc. before the extension
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
                        Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        fileNames.add(fileName);
                        // Add preview
                        javafx.scene.image.ImageView imgView = new javafx.scene.image.ImageView();
                        File imgFile = new File("src/main/resources/" + fileName);
                        if (imgFile.exists()) {
                            imgView.setImage(new javafx.scene.image.Image(imgFile.toURI().toString(), 40, 40, true, true));
                        }
                        imgView.setFitWidth(40);
                        imgView.setFitHeight(40);
                        imgView.setPreserveRatio(true);
                        imgView.setStyle("-fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 2, 0, 0, 1);");
                        imagePreviewBox.getChildren().add(imgView);
                    } catch (IOException ex) {
                        showError("Error copying file: " + ex.getMessage());
                    }
                }
                picField.setText(String.join(",", fileNames));
            }
        });
        browseButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: #F4ACB5;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 5 12;"
        );
        browseButton.setOnMouseEntered(e -> browseButton.setStyle(
            "-fx-background-color: #F4ACB5;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 5 12;"
        ));
        browseButton.setOnMouseExited(e -> browseButton.setStyle(            
            "-fx-background-color: transparent;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: #F4ACB5;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 5 12;"
        ));
        browseButton.setOnMousePressed(e -> browseButton.setStyle(
            "-fx-background-color: #eab9c1;" +
            "-fx-border-color: #eab9c1;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 5 12;"
        ));
        HBox picBox = new HBox(10, picField, browseButton);
        grid.add(createLabel("Pictures:"), 0, row);
        grid.add(picBox, 1, row++);
        grid.add(imagePreviewBox, 1, row++);

        stateComboBox = new ComboBox<>();
        stateComboBox.getItems().addAll("Available", "Under Review", "Adopted", "Returned");
        stateComboBox.setStyle("-fx-font-size: 14px; "
                + "-fx-background-color: white; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-radius: 5;");
        stateComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> styleFocusedComboBox(stateComboBox, newVal));
        stateComboBox.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        remarkField = new TextField();
        remarkField.setStyle("-fx-font-size: 14px; "
                + "-fx-background-color: white; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-radius: 5;");
        remarkField.focusedProperty().addListener((obs, oldVal, newVal) -> styleFocusedField(remarkField, newVal));

        // Add fields to grid
        grid.add(createLabel("Name:"), 0, row);
        grid.add(nameField, 1, row++);

        grid.add(createLabel("Type:"), 0, row);
        grid.add(typeField, 1, row++);

        grid.add(createLabel("Sex:"), 0, row);
        grid.add(sexComboBox, 1, row++);

        grid.add(createLabel("Birthday:"), 0, row);
        grid.add(birthdayPicker, 1, row++);

        grid.add(createLabel("State:"), 0, row);
        grid.add(stateComboBox, 1, row++);
        

        grid.add(createLabel("Remark:"), 0, row);
        grid.add(remarkField, 1, row++);

        return grid;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + ACCENT_COLOR + ";");
        return label;
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        );
        button.setOnAction(handler);
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: #F4ACB5;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;" + 
            "-fx-background-insets: 0"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #F4ACB5;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        ));
        return button;
    }

    private void styleFocusedField(TextField field, boolean focused) {
        if (focused) {
            field.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: " + PRIMARY_COLOR + "; -fx-border-width: 2; -fx-border-radius: 5;");
        } else {
            field.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");
        }
    }

    private void styleFocusedComboBox(ComboBox<String> comboBox, boolean focused) {
        if (focused) {
            comboBox.setStyle("-fx-font-size: 14px; "
                    + "-fx-background-color: white; "
                    + "-fx-border-color: " + PRIMARY_COLOR + "; "
                    + "-fx-border-width: 2; "
                    + "-fx-border-radius: 5;");
        } else {
            comboBox.setStyle("-fx-font-size: 14px; "
                    + "-fx-background-color: white; "
                    + "-fx-border-color: #E0E0E0; "
                    + "-fx-border-radius: 5;");
        }
    }

    private void loadPets() {
        try {
            String query = "SELECT * FROM pet";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<Pet> pets = new ArrayList<>();
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
                pets.add(pet);
            }
            petTable.getItems().setAll(pets);
        } catch (SQLException e) {
            showError("Error loading pets: " + e.getMessage());
        }
    }

    private void handleAddPet() {
        // Validate form
        if (!validateForm()) {
            return;
        }

        try {
            // Check if pet name already exists
            String checkQuery = "SELECT * FROM pet WHERE petName = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, nameField.getText().trim());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showError("A pet with this name already exists");
                return;
            }

            String query = "INSERT INTO pet (petName, petType, sex, birthday, pic, state, remark) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, nameField.getText().trim());
            stmt.setString(2, typeField.getText().trim());
            stmt.setString(3, sexComboBox.getValue());
            stmt.setDate(4, java.sql.Date.valueOf(birthdayPicker.getValue()));
            stmt.setString(5, picField.getText().trim());
            int stateValue = 0; // Default to Available
            if (stateComboBox.getValue() != null) {
                switch (stateComboBox.getValue()) {
                    case "Available": stateValue = 0; break;
                    case "Under Review": stateValue = 1; break;
                    case "Adopted": stateValue = 2; break;
                    case "Returned": stateValue = 3; break;
                }
            }
            stmt.setInt(6, stateValue);
            stmt.setString(7, remarkField.getText().trim());

            stmt.executeUpdate();
            loadPets();
            clearForm();
            showMessage("Success", "Pet added successfully!");
        } catch (SQLException e) {
            showError("Error adding pet: " + e.getMessage());
        }
    }

    private void handleUpdatePet() {
        Pet selectedPet = petTable.getSelectionModel().getSelectedItem();
        if (selectedPet == null) {
            showError("Please select a pet to update");
            return;
        }

        // Validate form
        if (!validateForm()) {
            return;
        }

        try {
            // Check if new name conflicts with other pets
            String checkQuery = "SELECT * FROM pet WHERE petName = ? AND id != ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, nameField.getText().trim());
            checkStmt.setInt(2, selectedPet.getId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showError("A pet with this name already exists");
                return;
            }

            String query = "UPDATE pet SET petName=?, petType=?, sex=?, birthday=?, pic=?, state=?, remark=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, nameField.getText().trim());
            stmt.setString(2, typeField.getText().trim());
            stmt.setString(3, sexComboBox.getValue());
            stmt.setDate(4, java.sql.Date.valueOf(birthdayPicker.getValue()));
            stmt.setString(5, picField.getText().trim());
            int stateValue = 0; // Default to Available
            if (stateComboBox.getValue() != null) {
                switch (stateComboBox.getValue()) {
                    case "Available": stateValue = 0; break;
                    case "Under Review": stateValue = 1; break;
                    case "Adopted": stateValue = 2; break;
                    case "Returned": stateValue = 3; break;
                }
            }
            stmt.setInt(6, stateValue);
            stmt.setString(7, remarkField.getText().trim());
            stmt.setInt(8, selectedPet.getId());

            stmt.executeUpdate();
            int oldState = selectedPet.getState();
            int newState = stateValue;

            if (oldState == 2 && newState == 3) {
                // Fetch owner name from database
                String ownerName = "Unknown";
                String getOwnerNameSQL = "SELECT u.realName FROM user u JOIN adoptanimal a ON u.id = a.userId WHERE a.petId = ? AND a.state = 2";
                PreparedStatement ownerStmt = conn.prepareStatement(getOwnerNameSQL);
                ownerStmt.setInt(1, selectedPet.getId());
                rs = ownerStmt.executeQuery();
                if (rs.next()) {
                    ownerName = rs.getString("realName");
                }

                // Confirm status change from Adopted to Returned
                Alert confirmReturn = new Alert(Alert.AlertType.CONFIRMATION);
                confirmReturn.setTitle("Confirm Return");
                confirmReturn.setHeaderText("Mark " + selectedPet.getName() + " as returned?");
                confirmReturn.setContentText(selectedPet.getName() + " was previously adopted by " + ownerName + "." +
                        "\nAre you sure you want to mark it as returned?");

                if (confirmReturn.showAndWait().get() != ButtonType.OK) {
                    return; // Cancel update if not confirmed
                }

                // Update adoptanimal state
                String updateAdoptAnimal = "UPDATE adoptanimal SET state = 3 WHERE petId = ? AND state = 2";
                PreparedStatement stmtAdoptAnimal = conn.prepareStatement(updateAdoptAnimal);
                stmtAdoptAnimal.setInt(1, selectedPet.getId());
                stmtAdoptAnimal.executeUpdate();

                // Show confirmation
                showMessage("Returned", "Pet was successfully marked as returned.");
            }else if (oldState == 3 && newState == 2) {
                // Returned → Adopted (re-adopted)
                // Update adoptanimal state from 3 to 2
                String updateAdoptAnimal = "UPDATE adoptanimal SET state = 2 WHERE petId = ? AND state = 3";
                PreparedStatement stmtAdoptAnimal = conn.prepareStatement(updateAdoptAnimal);
                stmtAdoptAnimal.setInt(1, selectedPet.getId());
                stmtAdoptAnimal.executeUpdate();

            } else if ((oldState == 2 || oldState == 3 || oldState == 1) && newState == 0) {
                // Any adoption-related status → Available
                String resetAdoptAnimal = "UPDATE adoptanimal SET state = 0 WHERE petId = ? AND (state = 1 OR state = 2 OR state = 3)";
                PreparedStatement stmtReset = conn.prepareStatement(resetAdoptAnimal);
                stmtReset.setInt(1, selectedPet.getId());
                stmtReset.executeUpdate();
            }
            loadPets();
            clearForm();
            showMessage("Success", "Pet updated successfully!");
        } catch (SQLException e) {
            showError("Error updating pet: " + e.getMessage());
        }
    }

    private void handleDeletePet() {
        Pet selectedPet = petTable.getSelectionModel().getSelectedItem();
        if (selectedPet == null) {
            showError("Please select a pet to delete");
            return;
        }

        // Check if pet has any adoption records
        try {
            String checkQuery = "SELECT * FROM adoptanimal WHERE petId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, selectedPet.getId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showError("Cannot delete pet: This pet has adoption records");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete Pet: " + selectedPet.getName());
            confirm.setContentText("Are you sure you want to delete this pet? This action cannot be undone.");
            
            if (confirm.showAndWait().get() == ButtonType.OK) {
                String query = "DELETE FROM pet WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, selectedPet.getId());
                stmt.executeUpdate();
                loadPets();
                clearForm();
                showMessage("Success", "Pet deleted successfully!");
            }
        } catch (SQLException e) {
            showError("Error deleting pet: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        // Validate name
        if (nameField.getText().trim().isEmpty()) {
            showError("Pet name is required");
            nameField.requestFocus();
            return false;
        }
        if (nameField.getText().trim().length() > 20) {
            showError("Pet name must be 20 characters or less");
            nameField.requestFocus();
            return false;
        }

        // Validate type
        if (typeField.getText().trim().isEmpty()) {
            showError("Pet type is required");
            typeField.requestFocus();
            return false;
        }
        if (typeField.getText().trim().length() > 20) {
            showError("Pet type must be 20 characters or less");
            typeField.requestFocus();
            return false;
        }

        // Validate sex
        if (sexComboBox.getValue() == null) {
            showError("Please select a sex");
            sexComboBox.requestFocus();
            return false;
        }

        // Validate birthday
        if (birthdayPicker.getValue() == null) {
            showError("Birthday is required");
            birthdayPicker.requestFocus();
            return false;
        }

        // Validate pictures
        if (picField.getText().trim().isEmpty()) {
            showError("Picture filename is required");
            picField.requestFocus();
            return false;
        }
        String[] pics = picField.getText().trim().split(",");
        for (String pic : pics) {
            if (!pic.trim().matches("^[a-zA-Z0-9_\\-\\.\\s]+\\.(?i)(jpg|jpeg|png|gif|bmp|webp|tiff|tif)$")) {
                showError("Invalid picture filename format. Use a valid image file (jpg, jpeg, png, gif, bmp, webp, tiff, tif).");
                picField.requestFocus();
                return false;
            }
        }

        // Validate state
        if (stateComboBox.getValue() == null) {
            showError("Please select a state");
            stateComboBox.requestFocus();
            return false;
        }

        // Validate remark
        if (remarkField.getText().trim().length() > 100) {
            showError("Remark must be 100 characters or less");
            remarkField.requestFocus();
            return false;
        }

        return true;
    }

    private void populateForm(Pet pet) {
        nameField.setText(pet.getName());
        typeField.setText(pet.getType());
        sexComboBox.setValue(pet.getSex());
        birthdayPicker.setValue(pet.getBirthday().toLocalDate());
        picField.setText(pet.getPic());
        switch (pet.getState()) {
            case 0: stateComboBox.setValue("Available"); break;
            case 1: stateComboBox.setValue("Under Review"); break;
            case 2: stateComboBox.setValue("Adopted"); break;
            case 3: stateComboBox.setValue("Returned"); break;
            default: stateComboBox.getSelectionModel().clearSelection(); break;
        }
        remarkField.setText(pet.getRemark());
    }

    private void clearForm() {
        nameField.clear();
        typeField.clear();
        sexComboBox.getSelectionModel().clearSelection();
        birthdayPicker.setValue(null);
        picField.clear();
        stateComboBox.getSelectionModel().clearSelection();
        remarkField.clear();
        petTable.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    protected void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void showDefaultContent() {
        contentArea.getChildren().setAll(content);
    }

    public VBox getContent() {
        return content;
    }
}