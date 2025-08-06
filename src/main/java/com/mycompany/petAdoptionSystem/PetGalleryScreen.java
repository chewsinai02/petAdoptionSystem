package com.mycompany.petAdoptionSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PetGalleryScreen {
    private static final String PRIMARY_COLOR = "#4A90E2";
    private static final String ACCENT_COLOR = "#2C3E50";
    private final Stage stage;
    private VBox content;
    private Connection conn;
    private List<Pet> pets;

    public PetGalleryScreen(Stage primaryStage) {
        this.stage = primaryStage;
        try {
            // Initialize database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
            initializeUI();
        } catch (ClassNotFoundException | SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    private void initializeUI() {
        content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 12;-fx-border-radius: 12;");

        // Title
        Text titleText = new Text("Pet Gallery");
        titleText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 32));
        titleText.setStyle("-fx-font-weight: bold;");
        titleText.setFill(Color.web(ACCENT_COLOR));

        // Create grid for pet cards
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        // Load pets from database
        loadPets(grid);

        // Wrap grid in a scroll pane for vertical scrolling
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 10;");
        scrollPane.setPadding(new Insets(10));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Add components to content
        content.getChildren().addAll(titleText, scrollPane);
    }

    private void loadPets(GridPane grid) {
        pets = new ArrayList<>();
        try {
            String query = "SELECT p.id, p.petName, p.petType, p.sex, p.birthday, p.pic, p.remark, " +
                           "CASE " +
                           "    WHEN EXISTS (SELECT 1 FROM adoptanimal a WHERE a.petId = p.id AND a.state = 3) THEN 3 " +
                           "    WHEN EXISTS (SELECT 1 FROM adoptanimal a WHERE a.petId = p.id AND a.state = 2) THEN 2 " +
                           "    WHEN EXISTS (SELECT 1 FROM adoptanimal a WHERE a.petId = p.id AND a.state = 1) THEN 1 " +
                           "    ELSE 0 " +
                           "END AS state " +
                           "FROM pet p";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            int col = 0;
            int row = 0;
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
                VBox petCard = createPetCard(pet);
                grid.add(petCard, col, row);
                col++;
                if (col > 3) { // 4 per row
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            showError("Error loading pets: " + e.getMessage());
        }
    }

    public void show() {
        // Just show the content VBox in a scene
        Scene scene = new Scene(content, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("Pet Gallery");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createPetCard(Pet pet) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5); -fx-background-radius: 10;");
        card.setPrefWidth(220);
        card.setPrefHeight(320);

        // Pet image (only first image)
        String[] images = pet.getPic().split(",");
        String firstImage = images[0].trim();
        String imagePath = "/" + firstImage;
        java.net.URL imageUrl = getClass().getResource(imagePath);
        ImageView imageView;
        if (imageUrl != null) {
            imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        } else {
            java.net.URL placeholderUrl = getClass().getResource("/placeholder.jpg");
            if (placeholderUrl != null) {
                imageView = new ImageView(new Image(placeholderUrl.toExternalForm()));
            } else {
                imageView = new ImageView();
            }
        }
        imageView.setFitWidth(190);
        imageView.setFitHeight(190);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-radius: 8;");

        // Pet name
        Text nameText = new Text(pet.getName());
        nameText.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameText.setFill(Color.web(ACCENT_COLOR));

        // Pet type and sex
        Text detailsText = new Text(pet.getType() + " • " + pet.getSex());
        detailsText.setFont(Font.font("System", 15));
        detailsText.setFill(Color.web("#666666"));

        // Status
        Label statusLabel = new Label(getStatusText(pet.getState()));
        statusLabel.setStyle("-fx-background-color: " +
                (pet.getState() == 0 ? "#a1f195" :
                        pet.getState() == 1 ? "#f1e695" :
                                pet.getState() == 2 ? "#f195a1" :
                                        "#95a3f1") +
                "; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 12px;");

        // View details button
        Button detailsButton = new Button("View Details");
       
        String normalStyle1 =
            "-fx-background-color: transparent;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: #F4ACB5;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;";

        String hoverStyle1 =
            "-fx-background-color: #F4ACB5;" +
            "-fx-border-color: #F4ACB5;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;";

        String pressedStyle1 =
            "-fx-background-color: #eab9c1;" +
            "-fx-border-color: #eab9c1;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;";

        detailsButton.setStyle(normalStyle1);

        detailsButton.setOnMouseEntered(e -> detailsButton.setStyle(hoverStyle1));
        detailsButton.setOnMouseExited(e -> detailsButton.setStyle(normalStyle1));
        detailsButton.setOnMousePressed(e -> detailsButton.setStyle(pressedStyle1));
        detailsButton.setOnMouseReleased((MouseEvent e) -> {
            if (detailsButton.isHover()) {
                detailsButton.setStyle(hoverStyle1);
            } else {
                detailsButton.setStyle(normalStyle1);
            }
        });
        detailsButton.setOnAction(e -> showPetDetails(pet));

        card.getChildren().addAll(imageView, nameText, detailsText, statusLabel, detailsButton);

        // Add Wish to Adopt button only if state == 0 (Available)
        if (pet.getState() == 0) {
            Button adoptButton = new Button("Wish to Adopt");

            String normalStyle =
                "-fx-background-color: transparent;" +
                "-fx-border-color: #F4ACB5;" +
                "-fx-border-width: 1;" +
                "-fx-text-fill: #F4ACB5;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 20;";

            String hoverStyle =
                "-fx-background-color: #F4ACB5;" +
                "-fx-border-color: #F4ACB5;" +
                "-fx-border-width: 1;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 20;";

            String pressedStyle =
                "-fx-background-color: #eab9c1;" +
                "-fx-border-color: #eab9c1;" +
                "-fx-border-width: 1;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 20;";

            adoptButton.setStyle(normalStyle);

            adoptButton.setOnMouseEntered(e -> adoptButton.setStyle(hoverStyle));
            adoptButton.setOnMouseExited(e -> adoptButton.setStyle(normalStyle));
            adoptButton.setOnMousePressed(e -> adoptButton.setStyle(pressedStyle));
            adoptButton.setOnMouseReleased(e -> {
                if (adoptButton.isHover()) {
                    adoptButton.setStyle(hoverStyle);
                } else {
                    adoptButton.setStyle(normalStyle);
                }
            });

            adoptButton.setOnAction(e -> handleWishToAdopt(pet));
            card.getChildren().add(adoptButton);
        }
        return card;
    }

    private void showPetDetails(Pet pet) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Pet Details");
        dialog.setHeaderText(pet.getName());

        VBox localContent = new VBox(15);
        localContent.setPadding(new Insets(20));
        localContent.setStyle("-fx-background-color: #FAD9DD;");

        // Pet images (show all)
        HBox imageBox = new HBox(10);
        imageBox.setAlignment(Pos.CENTER);
        String[] images = pet.getPic().split(",");
        for (String image : images) {
            String img = image.trim();
            String imagePath = "/" + img;
            java.net.URL imageUrl = getClass().getResource(imagePath);
            ImageView imageView;
            if (imageUrl != null) {
                imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            } else {
                java.net.URL placeholderUrl = getClass().getResource("/placeholder.jpg");
                if (placeholderUrl != null) {
                    imageView = new ImageView(new Image(placeholderUrl.toExternalForm()));
                } else {
                    imageView = new ImageView();
                }
            }
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            imageBox.getChildren().add(imageView);
        }

        // Pet details
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(10);
        detailsGrid.setPadding(new Insets(10));

        addDetailRow(detailsGrid, "Type:", pet.getType(), 0);
        addDetailRow(detailsGrid, "Sex:", pet.getSex(), 1);
        addDetailRow(detailsGrid, "Birthday:", pet.getBirthday().toString(), 2);
        addDetailRow(detailsGrid, "Status:", getStatusText(pet.getState()), 3);
        addDetailRow(detailsGrid, "Description:", pet.getRemark(), 4);

        localContent.getChildren().addAll(imageBox, detailsGrid);

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.getDialogPane().setContent(localContent);
        dialog.showAndWait();
    }

    private void addDetailRow(GridPane grid, String label, String value, int row) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: " + ACCENT_COLOR + ";");
        Label valueNode = new Label(value);
        valueNode.setWrapText(true);
        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private String getStatusText(int state) {
        switch (state) {
            case 0: return "Available";
            case 1: return "Under Review";
            case 2: return "Adopted";
            case 3: return "Not Available";
            default: return "Unknown";
        }
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public VBox getContent() {
        return content;
    }

    private void handleWishToAdopt(Pet pet) {
        if (!UserSession.isLoggedIn()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Required");
            alert.setHeaderText(null);
            alert.setContentText("You must be logged in to wish to adopt a pet. Go to login page?");
            ButtonType loginButton = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(loginButton, cancelButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == loginButton) {
                    LoginScreen loginScreen = new LoginScreen(new Stage());
                    loginScreen.show();
                }
            });
            return;
        }

        // Check if pet is still available (state 0)
        if (pet.getState() != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not Available");
            alert.setHeaderText(null);
            alert.setContentText("This pet is no longer available for adoption.");
            alert.showAndWait();
            return;
        }

        // Show confirmation dialog
        Dialog<ButtonType> confirmDialog = new Dialog<>();
        confirmDialog.setTitle("Confirm Adoption Request");
        confirmDialog.setHeaderText(null); // We'll create our own header

        // Create custom dialog content
        VBox localContent = new VBox(15);
        localContent.setPadding(new Insets(20));
        localContent.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // Header with pet name and icon
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setStyle("-fx-padding: 0 0 10 0; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0;");
        
        // Try to load pet image
        String[] images = pet.getPic().split(",");
        String firstImage = images[0].trim();
        String imagePath = "/" + firstImage;
        ImageView petImageView = new ImageView();
        java.net.URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            petImageView.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            java.net.URL placeholderUrl = getClass().getResource("/placeholder.jpg");
            if (placeholderUrl != null) {
                petImageView.setImage(new Image(placeholderUrl.toExternalForm()));
            }
        }
        petImageView.setFitWidth(60);
        petImageView.setFitHeight(60);
        petImageView.setPreserveRatio(true);
        petImageView.setStyle("-fx-background-radius: 5;");

        Text headerText = new Text("Adopt " + pet.getName() + "?");
        headerText.setFont(Font.font("System", FontWeight.BOLD, 18));
        headerText.setFill(Color.web(ACCENT_COLOR));
        headerBox.getChildren().addAll(petImageView, headerText);

        // Pet details section
        VBox detailsBox = new VBox(8);
        detailsBox.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 15; -fx-background-radius: 5;");
        
        Text detailsTitle = new Text("Pet Details");
        detailsTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        detailsTitle.setFill(Color.web(ACCENT_COLOR));

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(5);
        detailsGrid.setPadding(new Insets(5, 0, 0, 0));

        addDetailRow(detailsGrid, "Type:", pet.getType(), 0);
        addDetailRow(detailsGrid, "Sex:", pet.getSex(), 1);
        addDetailRow(detailsGrid, "Birthday:", pet.getBirthday().toString(), 2);

        detailsBox.getChildren().addAll(detailsTitle, detailsGrid);

        // Important notes section
        VBox notesBox = new VBox(8);
        notesBox.setStyle("-fx-background-color: #FFF3E0; -fx-padding: 15; -fx-background-radius: 5;");
        
        Text notesTitle = new Text("Important Notes");
        notesTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        notesTitle.setFill(Color.web("#E65100"));

        VBox notesContent = new VBox(5);
        notesContent.getChildren().addAll(
            createNoteText("• Your request will be reviewed by our staff."),
            createNoteText("• Kindly wait for our response."),
            createNoteText("• Your personal details will affect your chances to adopt this pet.")
        );

        notesBox.getChildren().addAll(notesTitle, notesContent);

        // Add all sections to main content
        localContent.getChildren().addAll(headerBox, detailsBox, notesBox);

        // Set dialog content
        confirmDialog.getDialogPane().setContent(localContent);
        confirmDialog.getDialogPane().setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // Add buttons
        ButtonType confirmButton = new ButtonType("Confirm Request", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmDialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        // Style the buttons
        Button confirmBtn = (Button) confirmDialog.getDialogPane().lookupButton(confirmButton);
        Button cancelBtn = (Button) confirmDialog.getDialogPane().lookupButton(cancelButton);
        confirmBtn.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        );
        cancelBtn.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-text-fill: " + ACCENT_COLOR + ";" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        );

        confirmDialog.showAndWait().ifPresent(type -> {
            if (type == confirmButton) {
                try {
                    // Start transaction
                    conn.setAutoCommit(false);

                    // Get current user ID from session
                    int userId = UserSession.getCurrentUserId();
                    if (userId <= 0) {
                        throw new SQLException("User session invalid");
                    }

                    // Insert adoption request
                    String insertQuery = "INSERT INTO adoptanimal (userId, petId, adoptTime, state) VALUES (?, ?, CURDATE(), 1)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, pet.getId());
                    insertStmt.executeUpdate();

                    // Update pet state to under review
                    String updateQuery = "UPDATE pet SET state = 1 WHERE id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setInt(1, pet.getId());
                    updateStmt.executeUpdate();

                    // Commit transaction
                    conn.commit();

                    // Show success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Your adoption request has been submitted and is under review!");
                    successAlert.showAndWait();

                    // Refresh the pet gallery
                    refreshPetGallery();

                } catch (SQLException e) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        showError(ex.getMessage());
                    }
                    showError("Error submitting adoption request: " + e.getMessage());
                } finally {
                    try {
                        conn.setAutoCommit(true);
                    } catch (SQLException e) {
                        showError(e.getMessage());
                    }
                }
            }
        });
    }

    private void refreshPetGallery() {
        // Clear and reload the grid
        GridPane grid = (GridPane) ((ScrollPane) content.getChildren().get(1)).getContent();
        grid.getChildren().clear();
        loadPets(grid);
    }

    private Text createNoteText(String text) {
        Text noteText = new Text(text);
        noteText.setFont(Font.font("System", 13));
        noteText.setFill(Color.web("#424242"));
        return noteText;
    }
} 