/**
 * Displays details of a user's adoption application.
 * Allows the user to track application status and view feedback from the admin.
 * 
 * @author Chew Sin Ai (finalize, and debug)
 * @version 2.0
 */
package com.mycompany.petAdoptionSystem.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private TableView<Adoption> table;

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

    /**
     * Initialize the UI for the My Adoption Applications screen.
     *
     * Creates a vertical box layout with a title, a card with a table, and a
     * set of buttons at the bottom.
     *
     * The title is a 28-point bold font with the text "My Adoption
     * Applications", and is displayed in the accent color.
     *
     * The card is a vertical box layout with a 10-pixel padding and a 10-pixel
     * border radius. The card contains a table with columns for the pet name,
     * pet type, apply date, status, and remarks. The table is styled with a
     * 10-pixel padding and a 10-pixel border radius, and has a 1-pixel border
     * at the bottom.
     *
     * The buttons are a horizontal box layout with a 10-pixel gap between
     * buttons. The buttons are styled as "button-primary" and have a 10-pixel
     * padding.
     */
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
            TableRow<Adoption> row = new TableRow<>();

            String defaultStyle
                    = "-fx-background-color: transparent;"
                    + "-fx-border-color: transparent transparent #FAD9DD transparent;"
                    + "-fx-border-width: 0 0 1 0;"
                    + "-fx-text-fill: black;"
                    + "-fx-font-weight: normal;";

            String hoverStyle
                    = "-fx-background-color: #FAD9DD;"
                    + "-fx-border-color: transparent transparent #FAD9DD transparent;"
                    + "-fx-border-width: 0 0 1 0;"
                    + "-fx-text-fill: black;"
                    + "-fx-font-weight: bold;";

            String selectedStyle
                    = "-fx-background-color: #FAD9DD;"
                    + "-fx-border-color: transparent transparent #F4ACB5 transparent;"
                    + "-fx-selection-border-color: transparent transparent #F4ACB5 transparent;"
                    + "-fx-border-width: 0 0 1 0;"
                    + "-fx-text-fill: black;"
                    + "-fx-font-weight: bold;";

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
        TableColumn<Adoption, String> petNameCol = new TableColumn<>("Pet Name");
        petNameCol.setCellValueFactory(new PropertyValueFactory<>("petName"));

        TableColumn<Adoption, String> petTypeCol = new TableColumn<>("Pet Type");
        petTypeCol.setCellValueFactory(new PropertyValueFactory<>("petType"));

        TableColumn<Adoption, String> applyDateCol = new TableColumn<>("Apply Date");
        applyDateCol.setCellValueFactory(new PropertyValueFactory<>("adoptTime"));

        TableColumn<Adoption, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Adoption, String> remarksCol = new TableColumn<>("Remarks");
        remarksCol.setCellValueFactory(new PropertyValueFactory<>("remark"));

        table.getColumns().addAll(petNameCol, petTypeCol, applyDateCol, statusCol, remarksCol);

        // Load applications
        loadApplications();

        // Add components to content
        card.getChildren().addAll(titleText, table);
        content.getChildren().addAll(card);
    }

    /**
     * Loads all adoption applications of the current user from the database and
     * populates them into the table.
     *
     * This method executes a SQL query to retrieve adoption details including
     * user and pet information. It converts the result set into a list of
     * Adoption objects and updates the table view with this data. The adoption
     * status is interpreted from the 'state' field and mapped to a textual
     * representation. In case of a SQL exception, an error message is
     * displayed.
     */
    private void loadApplications() {
        if (!UserSession.isLoggedIn()) {
            showError("Please login to view your applications");
            return;
        }

        int userId = UserSession.getCurrentUserId();
        List<Adoption> applications = new ArrayList<>();

        try {
            String query = "SELECT a.id, p.petName, p.petType, a.adoptTime, a.state, p.remark, "
                    + "u.realName, u.userName, u.telephone, u.email, u.sex, u.age, u.address, u.state AS userState, u.petHave, u.experience, u.pic "
                    + "FROM adoptanimal a "
                    + "JOIN pet p ON a.petId = p.id "
                    + "JOIN user u ON a.userId = u.id "
                    + "WHERE a.userId = ? "
                    + "ORDER BY a.adoptTime DESC";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String status;
                switch (rs.getInt("state")) {
                    case 0:
                        status = "Rejected";
                        break;
                    case 1:
                        status = "Pending";
                        break;
                    case 2:
                        status = "Approved";
                        break;
                    default:
                        status = "Unknown";
                }

                applications.add(new Adoption(
                        rs.getInt("id"),
                        rs.getString("realName"),
                        rs.getString("petName"),
                        rs.getDate("adoptTime").toString(),
                        status,
                        rs.getString("userName"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("sex"),
                        rs.getInt("age"),
                        rs.getString("address"),
                        rs.getInt("userState"),
                        rs.getInt("petHave"),
                        rs.getInt("experience"),
                        rs.getString("pic"),
                        rs.getString("petType"),
                        rs.getString("remark")
                ));
            }

            table.getItems().setAll(applications);

        } catch (SQLException e) {
            showError("Error loading applications: " + e.getMessage());
        }
    }

    /**
     * Shows an error message in an alert dialog.
     *
     * @param message The error message to be displayed.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show the stage with the content, set the stage title and scene.
     */
    public void show() {
        Scene scene = new Scene(content, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("My Adoption Applications");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gets the content of the applications screen, which is a VBox containing
     * the table of applications.
     *
     * @return The content of the applications screen.
     */
    public VBox getContent() {
        return content;
    }
}
