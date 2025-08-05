package com.mycompany.petAdoptionSystem.admin;

import com.mycompany.petAdoptionSystem.MainScreen;
import com.mycompany.petAdoptionSystem.PetGalleryScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboardScreen {
    private final Stage stage;
    private final BorderPane mainLayout;
    private final StackPane contentArea;

    public AdminDashboardScreen(Stage primaryStage) {
        this.stage = primaryStage;
        mainLayout = new BorderPane();
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        mainLayout.setCenter(contentArea);
        mainLayout.setTop(createMenuBar());
        showWelcome();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: white; font-color: #4A90E2; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0; -fx-padding: 5 0;");

        // 1. Pets
        Menu managePetsMenu = new Menu("Pets");
        MenuItem managePetsItem = new MenuItem("Manage");
        managePetsItem.setOnAction(e -> showManagePets());
        managePetsMenu.getItems().add(managePetsItem);

        // 2. Pet Well-being
        Menu viewWellbeingMenu = new Menu("Pet Well-being");
        MenuItem viewWellbeingItem = new MenuItem("View");
        MenuItem requestItem = new MenuItem("Request Update");
        viewWellbeingItem.setOnAction(e -> showWellbeing());
        requestItem.setOnAction(e -> showRequest());
        viewWellbeingMenu.getItems().addAll(viewWellbeingItem, requestItem);

        // 3. Approvals
        Menu manageApprovalsMenu = new Menu("Approvals");
        MenuItem manageApprovalsItem = new MenuItem("Manage");
        manageApprovalsItem.setOnAction(e -> showManageApprovals());
        manageApprovalsMenu.getItems().add(manageApprovalsItem);

        // 4. Admin
        Menu adminMenu = new Menu("Admin");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> handleLogout());
        adminMenu.getItems().add(logoutItem);

        // Add menus in logical order
        menuBar.getMenus().addAll(managePetsMenu, viewWellbeingMenu, manageApprovalsMenu, adminMenu);
        return menuBar;
    }

    private void showWelcome() {
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 12;-fx-border-radius: 12;");
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        Label welcomeLabel = new Label("Welcome to the Admin Dashboard!\nPlease select an action above.");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C3E50; -fx-text-alignment: center;-fx-font-family: 'Cherry Bomb One';");
        content.getChildren().add(welcomeLabel);
        contentArea.getChildren().setAll(content);
    }

    private void showManagePets() {
        ManagePetsScreen petsScreen = new ManagePetsScreen(stage);
        contentArea.getChildren().setAll(petsScreen.getContent());
        StackPane.setMargin(petsScreen.getContent(), new Insets(0));
    }

    private void showManageApprovals() {
        ManageAdoptionsScreen adoptionsScreen = new ManageAdoptionsScreen(stage);
        contentArea.getChildren().setAll(adoptionsScreen.getContent());
        StackPane.setMargin(adoptionsScreen.getContent(), new Insets(0));
    }

    private void showWellbeing() {
        ViewPetUpdate updateScreen = new ViewPetUpdate();
        contentArea.getChildren().setAll(updateScreen.getContent());
        StackPane.setMargin(updateScreen.getContent(), new Insets(0));
    }

    private void showRequest() {
        RequestPetUpdate requestScreen = new RequestPetUpdate(stage);
        contentArea.getChildren().setAll(requestScreen.getContent());
        StackPane.setMargin(requestScreen.getContent(), new Insets(0));
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                PetGalleryScreen.UserSession.setLoggedIn(false);
                PetGalleryScreen.UserSession.setCurrentUserId(-1);

                MainScreen mainScreen = new MainScreen(stage);
                mainScreen.show();
            }
        });
    }

    public void show() {
        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
} 