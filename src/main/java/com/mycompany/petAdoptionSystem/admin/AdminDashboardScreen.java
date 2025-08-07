package com.mycompany.petAdoptionSystem.admin;

import com.mycompany.petAdoptionSystem.MainScreen;
import com.mycompany.petAdoptionSystem.UserSession;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboardScreen extends MainScreen {
    
    public AdminDashboardScreen(Stage primaryStage) {
        super(primaryStage);
        // Override the menu bar with admin-specific menu
        this.menuBar.getMenus().clear();
        this.menuBar.getMenus().addAll(createAdminMenuBar().getMenus());
    }

    @Override
    public MenuBar createMenuBar() {
        return createAdminMenuBar();
    }

    @Override
    public void showDefaultContent() {
        showWelcome();
    }

    @Override
    public String getDashboardTitle() {
        return "Admin Dashboard";
    }

    private MenuBar createAdminMenuBar() {
        MenuBar adminMenuBar = new MenuBar();
        adminMenuBar.setStyle("-fx-background-color: white; font-color: #4A90E2; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0; -fx-padding: 5 0;");

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
        adminMenuBar.getMenus().addAll(managePetsMenu, viewWellbeingMenu, manageApprovalsMenu, adminMenu);
        return adminMenuBar;
    }

    private void showWelcome() {
        VBox adminContent = new VBox(20);
        adminContent.setAlignment(Pos.CENTER);
        adminContent.setPadding(new Insets(20));
        adminContent.setStyle("-fx-background-color: #FAD9DD; -fx-background-radius: 12;-fx-border-radius: 12;");
        adminContent.setAlignment(Pos.CENTER);
        adminContent.setPadding(new Insets(40));
        Label welcomeLabel = new Label("Welcome to the Admin Dashboard!\nPlease select an action above.");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C3E50; -fx-text-alignment: center;-fx-font-family: 'Cherry Bomb One';");
        adminContent.getChildren().add(welcomeLabel);
        contentArea.getChildren().setAll(adminContent);
    }

    private void showManagePets() {
        ManagePetsScreen petsScreen = new ManagePetsScreen(stage);
        petsScreen.showDefaultContent();
    }

    private void showManageApprovals() {
        ManageAdoptionsScreen adoptionsScreen = new ManageAdoptionsScreen(stage);
        adoptionsScreen.showDefaultContent();
    }

    private void showWellbeing() {
        ViewPetUpdate updateScreen = new ViewPetUpdate(stage);
        updateScreen.showDefaultContent();
    }

    private void showRequest() {
        RequestPetUpdate requestScreen = new RequestPetUpdate(stage);
        requestScreen.showDefaultContent();
    }

    @Override
    protected void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                UserSession.setLoggedIn(false);
                UserSession.setCurrentUserId(-1);
                UserSession.setAdmin(false);

                MainScreen mainScreen = new MainScreen(stage) {
                    @Override
                    protected MenuBar createMenuBar() {
                        MenuBar localMenuBar = new MenuBar();
                        localMenuBar.setStyle("-fx-background-color: white; font-color: #4A90E2;-fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0; -fx-padding: 5 0;");

                        // Home Menu
                        Menu homeMenu = new Menu("Home");
                        MenuItem homeItem = new MenuItem("Back to Home");
                        homeItem.setOnAction(e -> showPetGallery());
                        homeMenu.getItems().add(homeItem);

                        // Pet Knowledge Menu
                        Menu knowledgeMenu = new Menu("Pet Knowledge");
                        MenuItem dogCareItem = new MenuItem("Dog Care");
                        MenuItem catCareItem = new MenuItem("Cat Care");
                        dogCareItem.setOnAction(e -> showPetCareInfo("dog"));
                        catCareItem.setOnAction(e -> showPetCareInfo("cat"));
                        knowledgeMenu.getItems().addAll(dogCareItem, catCareItem);

                        // Adoption Menu
                        Menu adoptionMenu = new Menu("Adoption");
                        MenuItem adoptionProcessItem = new MenuItem("Adoption Process");
                        adoptionProcessItem.setOnAction(e -> showAdoptionProcess());
                        adoptionMenu.getItems().add(adoptionProcessItem);

                        // User Account Menu
                        Menu userAccountMenu = new Menu("Account");
                        MenuItem loginRegisterItem = new MenuItem("Login/Register");
                        loginRegisterItem.setOnAction(e -> showLoginScreen());
                        userAccountMenu.getItems().add(loginRegisterItem);

                        // Notification Menu
                        Menu notificationMenu = new Menu("Notification");
                        MenuItem notificationItem = new MenuItem("Notification");
                        notificationItem.setOnAction(e -> showNotification());
                        notificationMenu.getItems().add(notificationItem);

                        // Show/hide menus based on login state
                        boolean loggedIn = UserSession.isLoggedIn();
                        if (loggedIn) {
                            localMenuBar.getMenus().addAll(homeMenu, knowledgeMenu, adoptionMenu, notificationMenu);
                        } else {
                            localMenuBar.getMenus().addAll(homeMenu, knowledgeMenu, adoptionMenu, userAccountMenu);
                        }
                        return localMenuBar;
                    }

                    @Override
                    protected void showDefaultContent() {
                        showPetGallery();
                    }

                    @Override
                    protected String getDashboardTitle() {
                        return "Pet Adoption System";
                    }
                };
                mainScreen.show();
            }
        });
    }

    @Override
    public void show() {
        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
} 