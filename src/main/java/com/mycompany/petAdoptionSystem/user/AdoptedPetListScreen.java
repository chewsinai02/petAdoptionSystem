package com.mycompany.petAdoptionSystem.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mycompany.petAdoptionSystem.Pet;
import com.mycompany.petAdoptionSystem.PetGalleryScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AdoptedPetListScreen {
    private static final String PRIMARY_COLOR = "#F4ACB5";
    private static final String ACCENT_COLOR = "#2C3E50";
    private VBox content;
    private Connection conn;
    private List<Pet> adoptedPets;
    private Consumer<javafx.scene.Node> contentSetter;

    public AdoptedPetListScreen(Consumer<javafx.scene.Node> contentSetter) {
        this.contentSetter = contentSetter;
        try {
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
        content.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 12; -fx-border-radius: 12;");

        Text titleText = new Text("My Adopted Pets");
        titleText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 32));
        titleText.setStyle("-fx-font-weight: bold;");
        titleText.setFill(Color.web(ACCENT_COLOR));

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        // Remove grid background, let cards float

        loadAdoptedPets(grid);

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 10;");
        scrollPane.setPadding(new Insets(10));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        content.getChildren().addAll(titleText, scrollPane);
    }

    private void loadAdoptedPets(GridPane grid) {
        adoptedPets = new ArrayList<>();
        if (!PetGalleryScreen.UserSession.isLoggedIn()) {
            showError("Please login to view your adopted pets.");
            return;
        }

        int userId = PetGalleryScreen.UserSession.getCurrentUserId();

        try {
            String query = "SELECT p.* FROM pet p JOIN adoptanimal a ON p.id = a.petId WHERE a.userId = ? AND a.state = 2";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
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
                adoptedPets.add(pet);
                VBox petCard = createPetCard(pet);
                grid.add(petCard, col, row);
                col++;
                if (col > 3) { // 4 per row
                    col = 0;
                    row++;
                }
            }
            if (adoptedPets.isEmpty()) {
                grid.add(new Label("You have no adopted pets yet."), 0, 0);
            }

        } catch (SQLException e) {
            showError("Error loading adopted pets: " + e.getMessage());
        }
    }

    private VBox createPetCard(Pet pet) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5); -fx-background-radius: 10;");
        card.setPrefWidth(220);
        card.setPrefHeight(320);

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

        Text nameText = new Text(pet.getName());
        nameText.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameText.setFill(Color.web(ACCENT_COLOR));

        Text detailsText = new Text(pet.getType() + " • " + pet.getSex());
        detailsText.setFont(Font.font("System", 15));
        detailsText.setFill(Color.web("#666666"));

        Label statusLabel = new Label("Adopted");
        statusLabel.setStyle("-fx-background-color: #f195a1; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 12px;");

        Button updateStatusButton = new Button("Update Status");
        String normalStyle =
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;";

        String hoverStyle =
            "-fx-background-color: derive(" + PRIMARY_COLOR + ", -20%);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;";
        updateStatusButton.setStyle(normalStyle);
        updateStatusButton.setOnMouseEntered(e -> updateStatusButton.setStyle(hoverStyle));
        updateStatusButton.setOnMouseExited(e -> updateStatusButton.setStyle(normalStyle));
        updateStatusButton.setOnAction(e -> showUpdatePetStatus(pet));

        card.getChildren().addAll(imageView, nameText, detailsText, statusLabel, updateStatusButton);

        return card;
    }

    private void showUpdatePetStatus(Pet pet) {
        UpdatePetStatus UpdateScreen = new UpdatePetStatus(pet.getId());
        contentSetter.accept(UpdateScreen.getContent());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getContent() {
        return content;
    }
}