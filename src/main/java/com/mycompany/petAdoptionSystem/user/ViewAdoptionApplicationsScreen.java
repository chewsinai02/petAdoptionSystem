package com.mycompany.petAdoptionSystem.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.petAdoptionSystem.PetGalleryScreen;

import com.mycompany.petAdoptionSystem.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewAdoptionApplicationsScreen {
    private static final String SECONDARY_COLOR = "#FAD9DD";
    private static final String ACCENT_COLOR = "#2C3E50";
    private final Stage stage;
    private VBox content;
    private VBox card;
    private Connection conn;
    private TableView<AdoptionApplication> table;

    public ViewAdoptionApplicationsScreen(Stage primaryStage) {
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

    @SuppressWarnings("unchecked")
    private void initializeUI() {
        content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + SECONDARY_COLOR + ";");
        
        card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); -fx-padding: 10;");

        // Title
        Text titleText = new Text("My Adoption Applications");
        titleText.setFont(Font.loadFont(getClass().getResourceAsStream("/fontStyle/CherryBombOne-Regular.ttf"), 28));
        titleText.setFill(Color.web(ACCENT_COLOR));

        // Create table
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); -fx-padding: 10;");
        
        // CSS for rows and header can be placed in an external stylesheet or programmatically added:
        table.setRowFactory(tv -> {
            TableRow<AdoptionApplication> row = new TableRow<>();

            String defaultStyle =
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent transparent #FAD9DD transparent;" +
                "-fx-border-width: 0 0 1 0;" +
                "-fx-text-fill: black;" +
                "-fx-font-weight: normal;";

            String hoverStyle =
                "-fx-background-color: #FAD9DD;" +
                "-fx-border-color: transparent transparent #FAD9DD transparent;" +
                "-fx-border-width: 0 0 1 0;" +
                "-fx-text-fill: black;" +
                "-fx-font-weight: bold;";

            String selectedStyle =
                "-fx-background-color: #FAD9DD;" +
                "-fx-border-color: transparent transparent #F4ACB5 transparent;" +
                "-fx-border-width: 0 0 1 0;" +
                "-fx-text-fill: black;" +
                "-fx-font-weight: bold;";

            row.setStyle(defaultStyle);

            // Listen to hover and selection changes
            row.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered && !row.isEmpty()) {
                    row.setStyle(hoverStyle);
                } else {
                    row.setStyle(row.isSelected() ? selectedStyle : defaultStyle);
                }
            });

            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    row.setStyle(selectedStyle);
                } else {
                    row.setStyle(row.isHover() ? hoverStyle : defaultStyle);
                }
            });

            return row;
        });


        
        // Create columns
        TableColumn<AdoptionApplication, String> petNameCol = new TableColumn<>("Pet Name");
        petNameCol.setCellValueFactory(new PropertyValueFactory<>("petName"));

        TableColumn<AdoptionApplication, String> petTypeCol = new TableColumn<>("Pet Type");
        petTypeCol.setCellValueFactory(new PropertyValueFactory<>("petType"));

        TableColumn<AdoptionApplication, String> applyDateCol = new TableColumn<>("Apply Date");
        applyDateCol.setCellValueFactory(new PropertyValueFactory<>("applyDate"));

        TableColumn<AdoptionApplication, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<AdoptionApplication, String> remarksCol = new TableColumn<>("Remarks");
        remarksCol.setCellValueFactory(new PropertyValueFactory<>("remarks"));

        table.getColumns().addAll(petNameCol, petTypeCol, applyDateCol, statusCol, remarksCol);

        // Load applications
        loadApplications();

        // Add components to content
        card.getChildren().addAll(titleText, table);
        content.getChildren().addAll(card);
    }

    private void loadApplications() {
        if (!UserSession.isLoggedIn()) {
            showError("Please login to view your applications");
            return;
        }

        int userId = UserSession.getCurrentUserId();
        List<AdoptionApplication> applications = new ArrayList<>();

        try {
            String query = "SELECT a.id, p.petName, p.petType, a.adoptTime, a.state, p.remark " +
                          "FROM adoptanimal a " +
                          "JOIN pet p ON a.petId = p.id " +
                          "WHERE a.userId = ? " +
                          "ORDER BY a.adoptTime DESC";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String status;
                switch (rs.getInt("state")) {
                    case 0: status = "Rejected"; break;
                    case 1: status = "Pending"; break;
                    case 2: status = "Approved"; break;
                    default: status = "Unknown";
                }

                applications.add(new AdoptionApplication(
                    rs.getInt("id"),
                    rs.getString("petName"),
                    rs.getString("petType"),
                    rs.getDate("adoptTime").toString(),
                    status,
                    rs.getString("remark")
                ));
            }

            table.getItems().setAll(applications);

        } catch (SQLException e) {
            showError("Error loading applications: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show() {
        Scene scene = new Scene(content, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("My Adoption Applications");
        stage.setScene(scene);
        stage.show();
    }

    public VBox getContent() {
        return content;
    }

    // Class to hold adoption application data
    public static class AdoptionApplication {
        private final int id;
        private final String petName;
        private final String petType;
        private final String applyDate;
        private final String status;
        private final String remarks;

        public AdoptionApplication(int id, String petName, String petType, String applyDate, String status, String remarks) {
            this.id = id;
            this.petName = petName;
            this.petType = petType;
            this.applyDate = applyDate;
            this.status = status;
            this.remarks = remarks;
        }

        // Getters
        public int getId() { return id; }
        public String getPetName() { return petName; }
        public String getPetType() { return petType; }
        public String getApplyDate() { return applyDate; }
        public String getStatus() { return status; }
        public String getRemarks() { return remarks; }
    }
} 