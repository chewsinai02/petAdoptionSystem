package com.mycompany.petAdoptionSystem.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

public class ManageAdoptionsScreen {
    private BorderPane content;
    private Connection conn;
    private TableView<AdoptionRequest> table;

    @SuppressWarnings("unchecked")
    public ManageAdoptionsScreen(Stage stage) {
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

        table = new TableView<>();
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
        TableColumn<AdoptionRequest, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idCol.setMinWidth(40);
        idCol.setPrefWidth(40);
        idCol.setMaxWidth(40);
        idCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<AdoptionRequest, AdoptionRequest> userCol = new TableColumn<>("User");
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
            protected void updateItem(AdoptionRequest item, boolean empty) {
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
        TableColumn<AdoptionRequest, String> picCol = new TableColumn<>("Profile Pic");
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
        TableColumn<AdoptionRequest, String> sexCol = new TableColumn<>("Sex");
        sexCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSex()));
        sexCol.setMinWidth(50);
        sexCol.setPrefWidth(50);
        sexCol.setMaxWidth(50);
        sexCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<AdoptionRequest, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));
        ageCol.setMinWidth(50);
        ageCol.setPrefWidth(50);
        ageCol.setMaxWidth(50);
        ageCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<AdoptionRequest, String> telephoneCol = new TableColumn<>("Telephone");
        telephoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        telephoneCol.setMinWidth(120);
        telephoneCol.setPrefWidth(130);
        telephoneCol.setMaxWidth(150);
        telephoneCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<AdoptionRequest, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        emailCol.setPrefWidth(180);
        emailCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<AdoptionRequest, String> petCol = new TableColumn<>("Pet Name");
        petCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPetName()));
        petCol.setMinWidth(80);
        petCol.setPrefWidth(80);
        petCol.setMaxWidth(80);
        petCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<AdoptionRequest, String> dateCol = new TableColumn<>("Adopt Date");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdoptTime()));
        dateCol.setMinWidth(80);
        dateCol.setPrefWidth(90);
        dateCol.setMaxWidth(100);
        dateCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
        TableColumn<AdoptionRequest, Void> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellFactory(param -> new TableCell<>() {
            private final Button detailsBtn = new Button("View Details");
            private final HBox box = new HBox(detailsBtn);
            {
                box.setAlignment(Pos.CENTER);
                detailsBtn.setOnAction(e -> {
                    AdoptionRequest request = getTableView().getItems().get(getIndex());
                    showUserDetailsDialog(request);
                });
            }
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
        TableColumn<AdoptionRequest, Void> actionCol = new TableColumn<>("Action");
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
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    AdoptionRequest request = getTableView().getItems().get(getIndex());
                    if (request.getStatus().equals("Pending")) {
                        setGraphic(buttonBox);
                    } else {
                        if (request.getStatus().equals("Approved")) {
                            statusLabel.setText("Approved");
                            statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                        } else if (request.getStatus().equals("Rejected")) {
                            statusLabel.setText("Rejected");
                            statusLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
                        } else {
                            statusLabel.setText(request.getStatus());
                            statusLabel.setStyle("");
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

        TableColumn<AdoptionRequest, Void> dummyCol = new TableColumn<>("");
        dummyCol.setMinWidth(0);
        dummyCol.setMaxWidth(0);
        dummyCol.setPrefWidth(0);
        dummyCol.setVisible(false);
        dummyCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: #E0E0E0; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");
    
        table.getColumns().setAll(
            idCol, userCol, sexCol, ageCol, telephoneCol, emailCol, petCol, dateCol, detailsCol, actionCol, dummyCol
        );
        content.setCenter(table);
        loadAdoptionRequests();
    }

    private void loadAdoptionRequests() {
        List<AdoptionRequest> requests = new ArrayList<>();
        try {
            String query = "SELECT a.*, p.petName AS petName, " +
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
                    default: statusText = "Unknown"; break;
                }
                requests.add(new AdoptionRequest(
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
                        rs.getString("pic")
                ));
            }
            table.getItems().setAll(requests);
        } catch (SQLException e) {
            showError("Error loading adoption requests: " + e.getMessage());
        }
    }

    private void handleApproval(AdoptionRequest request, boolean approve) {
        try {
            conn.setAutoCommit(false); // Start transaction

            String updateAdoptAnimal = "UPDATE adoptanimal SET state = ? WHERE id = ?";
            PreparedStatement stmtAdopt = conn.prepareStatement(updateAdoptAnimal);
            
            String updatePet = "UPDATE pet SET state = ? WHERE id = (SELECT petId FROM adoptanimal WHERE id = ?)";
            PreparedStatement stmtPet = conn.prepareStatement(updatePet);

            if (approve) {
                stmtAdopt.setInt(1, 2); // 2 = approved for adoptanimal
                stmtPet.setInt(1, 3); // 3 = adopted for pet
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
            loadAdoptionRequests();
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

    // Inner class for adoption request row
    public static class AdoptionRequest {
        private final javafx.beans.property.IntegerProperty id;
        private final javafx.beans.property.StringProperty userName;
        private final javafx.beans.property.StringProperty petName;
        private final javafx.beans.property.StringProperty adoptTime;
        private final javafx.beans.property.StringProperty status;
        private String realName;
        private String telephone;
        private String email;
        private String address;
        private String sex;
        private int age;
        private int petHave;
        private int experience;
        private final int userState;
        private final String pic;
        public AdoptionRequest(
            int id,
            String realName,
            String petName,
            String adoptTime,
            String status,
            String userName,
            String telephone,
            String email,
            String sex,
            int age,
            String address,
            int userState,
            int petHave,
            int experience,
            String pic
        ) {
            this.id = new javafx.beans.property.SimpleIntegerProperty(id);
            this.realName = realName;
            this.petName = new javafx.beans.property.SimpleStringProperty(petName);
            this.adoptTime = new javafx.beans.property.SimpleStringProperty(adoptTime);
            this.status = new javafx.beans.property.SimpleStringProperty(status);
            this.userName = new javafx.beans.property.SimpleStringProperty(userName);
            this.telephone = telephone;
            this.email = email;
            this.sex = sex;
            this.age = age;
            this.address = address;
            this.userState = userState;
            this.petHave = petHave;
            this.experience = experience;
            this.pic = pic;
        }
        public int getId() { return id.get(); }
        public javafx.beans.property.IntegerProperty idProperty() { return id; }
        public String getUserName() { return userName.get(); }
        public javafx.beans.property.StringProperty userNameProperty() { return userName; }
        public String getPetName() { return petName.get(); }
        public javafx.beans.property.StringProperty petNameProperty() { return petName; }
        public String getAdoptTime() { return adoptTime.get(); }
        public javafx.beans.property.StringProperty adoptTimeProperty() { return adoptTime; }
        public String getStatus() { return status.get(); }
        public javafx.beans.property.StringProperty statusProperty() { return status; }
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
        public String getTelephone() { return telephone; }
        public void setTelephone(String telephone) { this.telephone = telephone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getSex() { return sex; }
        public void setSex(String sex) { this.sex = sex; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public int getUserState() { return userState; }
        public void setUserState(int state) { } // This field is no longer used
        public int getPetHave() { return petHave; }
        public void setPetHave(int petHave) { this.petHave = petHave; }
        public int getExperience() { return experience; }
        public void setExperience(int experience) { this.experience = experience; }
        public String getUserStateString() {
            return userState == 1 ? "Yes" : "No";
        }
        public String getPic() { return pic; }
    }

    private void showUserDetailsDialog(AdoptionRequest request) {
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