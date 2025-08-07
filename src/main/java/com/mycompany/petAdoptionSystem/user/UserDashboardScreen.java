package com.mycompany.petAdoptionSystem.user;

import com.mycompany.petAdoptionSystem.MainScreen;
import com.mycompany.petAdoptionSystem.UserSession;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class UserDashboardScreen extends MainScreen {
    
    public UserDashboardScreen(Stage primaryStage) {
        super(primaryStage);
        // Override the menu bar with user-specific menu
        this.menuBar.getMenus().clear();
        this.menuBar.getMenus().addAll(createUserMenuBar().getMenus());
        showPetGallery();
    }

    @Override
    protected MenuBar createMenuBar() {
        return createUserMenuBar();
    }

    private MenuBar createUserMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: white; font-color: #4A90E2; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0; -fx-padding: 5 0;");

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
        Menu userMenu = new Menu("User");
        MenuItem profileItem = new MenuItem("User Profile");
        MenuItem myApplicationsItem = new MenuItem("My Adoption Applications");
        MenuItem viewAdoptedPetsItem = new MenuItem("View Adopted Pets");
        MenuItem updatePetStatusItem = new MenuItem("Update Pet Status");
        profileItem.setOnAction(e -> showUserProfile());
        myApplicationsItem.setOnAction(e -> showMyApplications());
        viewAdoptedPetsItem.setOnAction(e -> showAdoptedPetList());
        updatePetStatusItem.setOnAction(e -> showUpdatePetStatus());
        userMenu.getItems().addAll(profileItem, myApplicationsItem, viewAdoptedPetsItem, updatePetStatusItem);

        // Notification Menu
        Menu notificationMenu = new Menu("Notification");
        MenuItem notificationItem = new MenuItem("Notification");
        notificationItem.setOnAction(e -> showNotification());
        notificationMenu.getItems().add(notificationItem);

        // Logout Menu
        Menu logoutMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> handleLogout());
        logoutMenu.getItems().add(logoutItem);

        // Add menus in logical order
        menuBar.getMenus().addAll(homeMenu, knowledgeMenu, adoptionMenu, userMenu, notificationMenu, logoutMenu);
        return menuBar;
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

                MainScreen mainScreen = new MainScreen(stage);
                mainScreen.show();
            }
        });
    }

    @Override
    public void show() {
        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("User Dashboard");
        stage.show();
    }
} 