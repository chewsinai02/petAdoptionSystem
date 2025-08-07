package com.mycompany.petAdoptionSystem.admin;

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
import javafx.stage.Stage;

public class RequestPetUpdate extends AdminDashboardScreen {
    private VBox content;
    private Connection conn;

    public RequestPetUpdate(Stage stage) {
        super(stage);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
        // Do not call initializeUI() here
    }

    public void postConstruct() {
        initializeUI();
    }

    @Override
    protected void initializeUI() {
        content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 12;-fx-border-radius: 12;");

        Text title = new Text("Request Pet Status Update");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-font-family: 'Cherry Bomb One';");
        content.getChildren().add(title);

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        loadAdoptedPets(grid);

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 10;");
        scrollPane.setPadding(new Insets(10));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        content.getChildren().add(scrollPane);
    }

    private void loadAdoptedPets(GridPane grid) {
        List<Pet> adoptedPets = new ArrayList<>();
        if (!UserSession.isLoggedIn()) {
            showError("Please login to view your adopted pets.");
            return;
        }
        try {
            String query = "SELECT p.*, a.userId FROM pet p JOIN adoptanimal a ON p.id = a.petId WHERE a.state = 2";
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
                int adopterUserId = rs.getInt("userId");
                adoptedPets.add(pet);
                VBox petCard = createPetCard(pet, adopterUserId);
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

    private VBox createPetCard(Pet pet, int adopterUserId) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5); -fx-background-radius: 10;");
        card.setPrefWidth(220);
        card.setPrefHeight(340);

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
        nameText.setFill(Color.web("#2C3E50"));

        Text detailsText = new Text(pet.getType() + " • " + pet.getSex());
        detailsText.setFont(Font.font("System", 15));
        detailsText.setFill(Color.web("#666666"));

        Label lastUpdateLabel = new Label("Last Update: " + getLastUpdateDate(pet.getId()));
        lastUpdateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");

        final Label requestLabel = new Label("Last Request: " + getRequestDate(pet.getId()));
        requestLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");

        Button requestUpdateButton = new Button("Request");
        requestUpdateButton.setStyle(
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
        requestUpdateButton.setOnMouseEntered(e -> requestUpdateButton.setStyle(
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
        requestUpdateButton.setOnMouseExited(e -> requestUpdateButton.setStyle(            
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
        requestUpdateButton.setOnMousePressed(e -> requestUpdateButton.setStyle(
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
        requestUpdateButton.setOnAction(e -> sendStatusUpdateRequest(pet, adopterUserId,requestLabel));

        card.getChildren().addAll(imageView, nameText, detailsText, lastUpdateLabel, requestLabel, requestUpdateButton);
        return card;
    }

    private String getLastUpdateDate(int petId) {
        try {
            String query = "SELECT MAX(updateTime) as lastUpdate FROM petupdate WHERE adoptId IN (SELECT id FROM adoptanimal WHERE petId = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                java.sql.Date lastUpdate = rs.getDate("lastUpdate");
                if (lastUpdate != null) {
                    return lastUpdate.toString();
                }
            }
        } catch (SQLException e) {
            // ignore, just show blank
        }
        return "-";
    }

    private String getRequestDate(int petId) {
        try {
            String query = "SELECT createdAt FROM notification WHERE petId = ? ORDER BY createdAt DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                java.sql.Date lastUpdate = rs.getDate("createdAt"); // fixed column name
                if (lastUpdate != null) {
                    return lastUpdate.toString();
                }
            }
        } catch (SQLException e) {
            // ignore, just show blank
        }
        return "-";
    }

    private void sendStatusUpdateRequest(Pet pet, int userId, Label requestLabel) {
        try {
            String insert = "INSERT INTO notification (userId, petId, createdAt) VALUES (?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setInt(1, userId);
            stmt.setInt(2, pet.getId());
            stmt.executeUpdate();
            showInfo("Request sent! The adopter will see this in their notifications.");

            requestLabel.setText("Last Request: " + getRequestDate(pet.getId()));
        } catch (SQLException e) {
            showError("Failed to send request: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
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
