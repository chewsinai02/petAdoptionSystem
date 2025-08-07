package com.mycompany.petAdoptionSystem.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.petAdoptionSystem.PetUpdate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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

public class ViewPetUpdate {
    private BorderPane content;
    private TableView<PetUpdate> updateTable;
    private Connection conn;
    
    public ViewPetUpdate() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adopt", "root", "");
            initializeUI();
        } catch (ClassNotFoundException | SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    private void initializeUI() {
        content = new BorderPane();
        content.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 12;-fx-border-radius: 12;");

        // Title
        Text titleText = new Text("Pet Updates Management");
        titleText.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleText.setStyle("-fx-font-family: 'Cherry Bomb One';");
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));
        titleBox.getChildren().add(titleText);
        content.setTop(titleBox);

        // Create table
        createUpdateTable();
        content.setCenter(updateTable);
    }

    @SuppressWarnings("unchecked")
    private void createUpdateTable() {
        updateTable = new TableView<>();
        updateTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        updateTable.setEditable(false);
        updateTable.setStyle("-fx-background-color: white; "
                + "-fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); "
                + "-fx-selection-bar: #fad9dd;"
                + "-fx-padding: 10;"
                + "-fx-border-radius:1px;"
                + "-fx-border-width:1px;"
                + "-fx-border-color: white;");

        // Create columns
        TableColumn<PetUpdate, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: lightgray; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");

        TableColumn<PetUpdate, String> petNameCol = new TableColumn<>("Pet Name");
        petNameCol.setCellValueFactory(cellData -> cellData.getValue().petNameProperty());
        petNameCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: lightgray; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");

        TableColumn<PetUpdate, String> userNameCol = new TableColumn<>("User");
        userNameCol.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        userNameCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: lightgray; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");

        TableColumn<PetUpdate, String> updateTimeCol = new TableColumn<>("Update Time");
        updateTimeCol.setCellValueFactory(cellData -> cellData.getValue().updateTimeProperty());
        updateTimeCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: lightgray; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");

        TableColumn<PetUpdate, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            {
                viewBtn.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #F4ACB5;" +
                    "-fx-border-width: 1;" +
                    "-fx-text-fill: #F4ACB5;" +
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 5 12;" + 
                    "-fx-background-insets: 0"
                );
                viewBtn.setOnAction(e -> showUpdateDetails(getTableView().getItems().get(getIndex())));
                viewBtn.setOnMouseEntered(e -> viewBtn.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #F4ACB5;" +
                    "-fx-border-width: 1;" +
                    "-fx-text-fill: #F4ACB5;" +
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 5 12;" + 
                    "-fx-background-insets: 0"
                ));
                viewBtn.setOnMouseExited(e -> viewBtn.setStyle(            
                    "-fx-background-color: white;" +
                    "-fx-border-color: #F4ACB5;" +
                    "-fx-border-width: 1;" +
                    "-fx-text-fill: #F4ACB5;" +
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 5 12;" + 
                    "-fx-background-insets: 0"
                ));
                viewBtn.setOnMousePressed(e -> viewBtn.setStyle(
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
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10);
                    buttons.setAlignment(Pos.CENTER);
                    buttons.getChildren().add(viewBtn);
                    setGraphic(buttons);
                }
            }
        });
        actionCol.setStyle("-fx-background-radius: 5pt 5pt 0 0; "
                + "-fx-border-color: lightgray; "
                + "-fx-border-width: 0 0 1 0; "
                + "-fx-padding: 5px 0;");

        updateTable.getColumns().addAll(idCol, petNameCol, userNameCol, updateTimeCol, actionCol);
        loadUpdates();
    }

    private void loadUpdates() {
        String query = "SELECT pu.id, pu.adoptId, pu.updateTime, pu.updateContent, pu.updatePic, pu.remark, p.petName, u.userName FROM petupdate pu JOIN adoptanimal a ON pu.adoptId = a.id JOIN pet p ON a.petId = p.id JOIN user u ON a.userId = u.id ORDER BY pu.updateTime DESC";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<PetUpdate> updates = new ArrayList<>();
            while (rs.next()) {
                updates.add(new PetUpdate(
                    rs.getInt("id"),
                    rs.getString("petName"),
                    rs.getString("userName"),
                    rs.getDate("updateTime"),
                    rs.getString("updateContent"),
                    rs.getString("updatePic"),
                    rs.getString("remark")
                ));
            }
            updateTable.getItems().setAll(updates);
        } catch (SQLException e) {
            // log SQL query if needed: // System.out.println("SQL: " + query);
            showError("Error loading updates: " + e.getMessage());
        }
    }

    private void showUpdateDetails(PetUpdate update) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Update Details");
        dialog.setHeaderText("Pet Update from " + update.getUserName());

        VBox localContent = new VBox(15);
        localContent.setPadding(new Insets(20));
        localContent.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 10;");

        // Pet info
        Text petInfo = new Text("Pet: " + update.getPetName());
        petInfo.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Update time
        Text updateTime = new Text("Update Time: " + update.getUpdateTime());
        updateTime.setFont(Font.font("System", 15));

        // Update content
        Text contentText = new Text("Update Content:\n" + update.getUpdateContent());
        contentText.setWrappingWidth(400);
        contentText.setFont(Font.font("System", 14));

        // Update image if available
        if (update.getUpdatePic() != null && !update.getUpdatePic().isEmpty()) {
            String imagePath = "src/main/resources/" + update.getUpdatePic();
            ImageView imageView = new ImageView(new Image("file:" + imagePath));
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-background-radius: 8;");
            localContent.getChildren().add(imageView);
        }

        localContent.getChildren().addAll(petInfo, updateTime, contentText);

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.getDialogPane().setContent(localContent);
        dialog.getDialogPane().setStyle("-fx-background-radius: 10;");
        dialog.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getContent() {
        return content;
    }
}
