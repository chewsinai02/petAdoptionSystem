package com.mycompany.petAdoptionSystem.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.petAdoptionSystem.user.Adoption;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManageAdoptionsScreen extends AdminDashboardScreen {
    private BorderPane content;
    private Connection conn;
    private final TableView<Adoption> table = new TableView<>();

    @SuppressWarnings("unchecked")
    public ManageAdoptionsScreen(Stage stage) {
        super(stage);
        content = new BorderPane();
        content.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 12;-fx-border-radius: 12;");
        
        // Title
        Text titleText = new Text("Manage Adoption Approvals");
        titleText.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleText.setStyle("-fx-font-family: 'Cherry Bomb One';");
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));
        titleBox.getChildren().add(titleText);
        content.setTop(titleBox);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            showError("Database connection error: " + e.getMessage());
            return;
        }

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setEditable(false);
        table.setStyle("-fx-background-color: white; "
                + "-fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); "
                + "-fx-selection-bar: #F4ACB5;"
                + "-fx-padding: 10;"
                + "-fx-border-radius:1px;"
                + "-fx-border-width:1px;"
                + "-fx-border-color: white;");
        TableColumn<Adoption, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idCol.setMinWidth(40);
        idCol.setPrefWidth(40);
        idCol.setMaxWidth(40);
        idCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, Adoption> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        userCol.setCellFactory(col -> new TableCell<>() {
            private final HBox box = new HBox(6);
            private final ImageView imageView = new ImageView();
            private final Label nameLabel = new Label();
            {
                imageView.setFitWidth(28);
                imageView.setFitHeight(28);
                imageView.setPreserveRatio(false);
                box.setAlignment(Pos.CENTER_LEFT);
                box.getChildren().addAll(imageView, nameLabel);
            }
            @Override
            protected void updateItem(Adoption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image("file:src/main/resources/" + item.getPic()));
                    nameLabel.setText(item.getUserName());
                    setGraphic(box);
                }
            }
        });
        userCol.setPrefWidth(160);
        userCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, String> picCol = new TableColumn<>("Profile Pic");
        picCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPic()));
        picCol.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(32);
                imageView.setFitHeight(32);
                imageView.setPreserveRatio(false);
            }
            @Override
            protected void updateItem(String pic, boolean empty) {
                super.updateItem(pic, empty);
                if (empty || pic == null || pic.isEmpty()) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image("file:src/main/resources/" + pic));
                    setGraphic(imageView);
                }
            }
        });
        TableColumn<Adoption, String> sexCol = new TableColumn<>("Sex");
        sexCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSex()));
        sexCol.setMinWidth(50);
        sexCol.setPrefWidth(50);
        sexCol.setMaxWidth(50);
        sexCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));
        ageCol.setMinWidth(50);
        ageCol.setPrefWidth(50);
        ageCol.setMaxWidth(50);
        ageCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, String> telephoneCol = new TableColumn<>("Telephone");
        telephoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        telephoneCol.setMinWidth(120);
        telephoneCol.setPrefWidth(130);
        telephoneCol.setMaxWidth(150);
        telephoneCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        emailCol.setPrefWidth(180);
        emailCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, String> petCol = new TableColumn<>("Pet Name");
        petCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPetName()));
        petCol.setMinWidth(80);
        petCol.setPrefWidth(80);
        petCol.setMaxWidth(80);
        petCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, String> petTypeCol = new TableColumn<>("Pet Type");
        petTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPetType()));
        petTypeCol.setMinWidth(80);
        petTypeCol.setPrefWidth(80);
        petTypeCol.setMaxWidth(80);
        petTypeCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0; -fx-padding: 5px 0;");
        TableColumn<Adoption, String> dateCol = new TableColumn<>("Adopt Date");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdoptTime()));
        dateCol.setMinWidth(80);
        dateCol.setPrefWidth(90);
        dateCol.setMaxWidth(100);
        dateCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, Void> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellFactory(param -> new TableCell<>() {
            private final Button detailsBtn = new Button("View Details");
            private final HBox box = new HBox(detailsBtn);
            {
                box.setAlignment(Pos.CENTER);
                detailsBtn.setOnAction(e -> {
                    Adoption request = getTableView().getItems().get(getIndex());
                    showUserDetailsDialog(request);
                });
            }
        /**
         * If the row is empty, remove the graphic, otherwise set the graphic to
         * the button box.
         *
         * @param item
         *            the item in the row
         * @param empty
         *            whether or not the row is empty
         */
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
        detailsCol.setMinWidth(90);
        detailsCol.setPrefWidth(100);
        detailsCol.setMaxWidth(110);
        detailsCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<Adoption, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button approveBtn = new Button("Approve");
            private final Button rejectBtn = new Button("Reject");
            private final HBox buttonBox = new HBox(10, approveBtn, rejectBtn);
            private final Label statusLabel = new Label();
            private final HBox labelBox = new HBox(statusLabel);
            {
                buttonBox.setAlignment(Pos.CENTER);
                labelBox.setAlignment(Pos.CENTER);
                approveBtn.setStyle("-fx-background-color: #85f075; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 15;");
                rejectBtn.setStyle("-fx-background-color: #f26174; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 15;");
                approveBtn.setOnAction(e -> handleApproval(getTableView().getItems().get(getIndex()), true));
                rejectBtn.setOnAction(e -> handleApproval(getTableView().getItems().get(getIndex()), false));
            }
        /**
         * Update the graphic of the cell depending on the status of the adoption request.
         * If the status is "Pending", show the buttons to approve or reject the request.
         * Otherwise, show a label with the status of the request.
         *
         * @param item
         *            the item in the row
         * @param empty
         *            whether or not the row is empty
         */
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Adoption request = getTableView().getItems().get(getIndex());
                    String status = request.getStatus();
                    if ("Pending".equals(status)) {
                        setGraphic(buttonBox);
                    } else {
                        switch (status) {
                            case "Approved":
                                statusLabel.setText("Approved");
                                statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                                break;
                            case "Rejected":
                                statusLabel.setText("Rejected");
                                statusLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
                                break;
                            case "Returned":
                                statusLabel.setText("Returned");
                                statusLabel.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
                                break;
                            default:
                                statusLabel.setText(status);
                                statusLabel.setStyle("");
                                break;
                        }
                        setGraphic(labelBox);
                    }
                }
            }
        });
        actionCol.setMinWidth(160);
        actionCol.setPrefWidth(180);
        actionCol.setMaxWidth(220);
        actionCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");

        TableColumn<Adoption, Void> dummyCol = new TableColumn<>("");
        dummyCol.setMinWidth(0);
        dummyCol.setMaxWidth(0);
        dummyCol.setPrefWidth(0);
        dummyCol.setVisible(false);
        dummyCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        
        table.getColumns().setAll(
            idCol, userCol, sexCol, ageCol, telephoneCol, emailCol, petCol, petTypeCol, dateCol, detailsCol, actionCol, dummyCol
        );
        content.setCenter(table);
        loadAdoption();
    }

    /**
     * Loads adoption requests from the database and populates them into the table.
     * This method executes a SQL query to retrieve adoption details including
     * user and pet information. It converts the result set into a list of 
     * Adoption objects and updates the table view with this data. The adoption 
     * status is interpreted from the 'state' field and mapped to a textual 
     * representation. In case of a SQL exception, an error message is displayed.
     */
    private void loadAdoption() {
        List<Adoption> requests = new ArrayList<>();
        try {
            String query = "SELECT a.*, p.petName AS petName, p.petType AS petType, p.remark AS remark, a.adoptTime AS adoptTime, " +
                            "u.realName, u.userName, u.sex, u.age, u.address, u.telephone, u.email, u.state AS userState, u.petHave, u.experience, u.pic " +
                            "FROM adoptanimal a " +
                            "LEFT JOIN user u ON a.userId = u.id " +
                            "LEFT JOIN pet p ON a.petId = p.id " +
                            "ORDER BY a.adoptTime DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String statusText;
                switch (rs.getInt("state")) {
                    case 0: statusText = "Rejected"; break;
                    case 1: statusText = "Pending"; break;
                    case 2: statusText = "Approved"; break;
                    case 3: statusText = "Returned"; break;
                    default: statusText = "Unknown"; break;
                }
                requests.add(new Adoption(
                        rs.getInt("id"),
                        rs.getString("realName"),
                        rs.getString("petName") != null ? rs.getString("petName") : "Unknown",
                        rs.getDate("adoptTime").toString(),
                        statusText,
                        rs.getString("userName"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("sex"),
                        rs.getInt("age"),
                        rs.getString("address"),
                        rs.getInt("state"),
                        rs.getInt("petHave"),
                        rs.getInt("experience"),
                        rs.getString("pic"),
                        rs.getString("petType"),
                        rs.getString("remark")
                ));
            }
            table.getItems().setAll(requests);
        } catch (SQLException e) {
            showError("Error loading adoption requests: " + e.getMessage());
        }
    }

     /**
         * Handle approval or rejection of an adoption request.
         * 
         * If approved, sets the adoption request status to 2 (approved) and the pet status to 2 (adopted).
         * Also updates the user's number of pets and sets the user's status to 1 (adopted).
         * If rejected, sets the adoption request status to 0 (rejected) and the pet status to 0 (available).
         * 
         * @param request The adoption request to be approved or rejected.
         * @param approve True if approving the adoption, false if rejecting.
         */
    private void handleApproval(Adoption request, boolean approve) {
        try {
            conn.setAutoCommit(false); // Start transaction

            String updateAdoptAnimal = "UPDATE adoptanimal SET state = ? WHERE id = ?";
            PreparedStatement stmtAdopt = conn.prepareStatement(updateAdoptAnimal);
            
            String updatePet = "UPDATE pet SET state = ? WHERE id = (SELECT petId FROM adoptanimal WHERE id = ?)";
            PreparedStatement stmtPet = conn.prepareStatement(updatePet);

            //0= disagree, 1= under review, 2= agree, 3= return back for adoptanimal
            //0= Current status or not applied for adoption, 1=applied for adoption, 2=has been adopted
            if (approve) {
                stmtAdopt.setInt(1, 2);
                stmtPet.setInt(1, 2);
                // Update user: petHave += 1, state = 1
                String updateUser = "UPDATE user SET petHave = petHave + 1, state = 1 WHERE userName = ?";
                PreparedStatement stmtUser = conn.prepareStatement(updateUser);
                stmtUser.setString(1, request.getUserName());
                stmtUser.executeUpdate();
            } else {
                stmtAdopt.setInt(1, 0); // 0 = rejected for adoptanimal
                stmtPet.setInt(1, 0); // 0 = available for pet
            }
            stmtAdopt.setInt(2, request.getId());
            stmtAdopt.executeUpdate();

            stmtPet.setInt(2, request.getId());
            stmtPet.executeUpdate();

            conn.commit(); // Commit transaction
            loadAdoption();
            showMessage("Success", approve ? "Adoption approved and pet status updated." : "Adoption rejected and pet status reset.");
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
            showError("Error updating adoption status: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Reset auto-commit
            } catch (SQLException e) {
                showError(e.getMessage());
            }
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
     * Shows an information message in an alert dialog.
     *
     * @param title The title of the alert dialog.
     * @param content The content of the alert dialog.
     */
    @Override
    protected void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Sets the content of the content area to the default content.
     * <p>
     * This method is called when the user navigates to the Manage Adoptions screen.
     * It sets the content area to the default content, which is the table of adoption requests.
     */
    @Override
    public void showDefaultContent() {
        contentArea.getChildren().setAll(content);
    }



    /**
     * Gets the content of this Manage Adoptions screen.
     *
     * @return The BorderPane that contains the content of this screen.
     */
    public BorderPane getContent() {
        return content;
    }

    /**
     * Shows a dialog with user details, including profile image, real name,
     * username, sex, age, address, telephone, email, whether they have
     * adopted before, number of pets they have, and their experience.
     * 
     * @param request The adoption request for which to show user details.
     */
    private void showUserDetailsDialog(Adoption request) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("User Details");
        dialog.setHeaderText(null);

        VBox contentBox = new VBox(12);
        contentBox.setPadding(new Insets(24, 32, 24, 32));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // Profile image (circular avatar)
        ImageView avatar = new ImageView();
        try {
            String picPath = request.getPic();
            if (picPath != null && !picPath.isEmpty()) {
                avatar.setImage(new Image("file:src/main/resources/" + picPath));
            }
        } catch (Exception e) {
            // ignore, show empty avatar
        }
        avatar.setFitWidth(80);
        avatar.setFitHeight(80);
        avatar.setPreserveRatio(false);
        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(40, 40, 40);
        avatar.setClip(clip);
        avatar.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 6, 0, 0, 2);");

        contentBox.getChildren().add(avatar);

        // Helper for each row
        Font labelFont = Font.font("System", FontWeight.BOLD, 15);
        Font valueFont = Font.font("System", FontWeight.NORMAL, 15);

        contentBox.getChildren().addAll(
            createDetailRow("Real Name", request.getRealName(), labelFont, valueFont),
            createDetailRow("Username", request.getUserName(), labelFont, valueFont),
            createDetailRow("Sex", request.getSex(), labelFont, valueFont),
            createDetailRow("Age", String.valueOf(request.getAge()), labelFont, valueFont),
            createDetailRow("Address", request.getAddress(), labelFont, valueFont),
            createDetailRow("Telephone", request.getTelephone(), labelFont, valueFont),
            createDetailRow("Email", request.getEmail(), labelFont, valueFont),
            createDetailRow("Adopted Before", request.getUserState() == 1 ? "Yes" : "No", labelFont, valueFont),
            createDetailRow("Pet Have", String.valueOf(request.getPetHave()), labelFont, valueFont),
            createDetailRow("Experience", request.getExperience() + " years", labelFont, valueFont)
        );

        dialog.getDialogPane().setContent(contentBox);
        dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);
        dialog.getDialogPane().setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 12;");
        dialog.showAndWait();
    }

    // Helper method for a row
    private HBox createDetailRow(String label, String value, Font labelFont, Font valueFont) {
        Label labelNode = new Label(label + ":");
        labelNode.setFont(labelFont);
        labelNode.setStyle("-fx-text-fill: #444;");
        labelNode.setMinWidth(120);

        Label valueNode = new Label(value);
        valueNode.setFont(valueFont);
        valueNode.setStyle("-fx-text-fill: #222;");

        HBox row = new HBox(10, labelNode, valueNode);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}