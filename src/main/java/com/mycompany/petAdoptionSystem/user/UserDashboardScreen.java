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
    }

    /**
     * Initializes the UI by calling the superclass's method and then
     * customizing the menu bar for the user dashboard.
     * <p>
     * In particular, it clears the menus in the menu bar and replaces them with
     * the menus returned by {@link #createUserMenuBar()}.
     */
    @Override
    public void initializeUI() {
        super.initializeUI();
        this.menuBar.getMenus().clear();
        this.menuBar.getMenus().addAll(createUserMenuBar().getMenus());
    }

    /**
     * Overrides the {@link MainScreen#createMenuBar()} to provide a custom menu
     * bar specific to the user dashboard. The menu bar includes various options
     * for user navigation and interaction within the application.
     *
     * @return the menu bar for the user dashboard
     */
    @Override
    public MenuBar createMenuBar() {
        return createUserMenuBar();
    }

    /**
     * Displays the pet gallery as the default content of the user dashboard.
     */
    @Override
    public void showDefaultContent() {
        showPetGallery();
    }

    /**
     * Returns the title of the user dashboard.
     *
     * @return the title of the user dashboard.
     */
    @Override
    public String getDashboardTitle() {
        return "User Dashboard";
    }

    /**
     * Creates a custom menu bar for the user dashboard. The menu bar includes
     * options for user navigation and interaction within the application.
     *
     * @return the menu bar for the user dashboard
     */
    private MenuBar createUserMenuBar() {
        MenuBar userMenuBar = new MenuBar();

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

        // Notification Menu
        Menu notificationMenu = new Menu("Notification");
        MenuItem notificationItem = new MenuItem("Notification");
        notificationItem.setOnAction(e -> showNotification());
        notificationMenu.getItems().add(notificationItem);

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

        // Logout Menu
        Menu logoutMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> handleLogout());
        logoutMenu.getItems().add(logoutItem);

        // Add menus in logical order
        userMenuBar.getMenus().addAll(homeMenu, knowledgeMenu, adoptionMenu, notificationMenu, userMenu, logoutMenu);
        return userMenuBar;
    }

    /**
     * Handles the logout process for the user.
     *
     * <p>
     * This method displays a confirmation dialog to the user asking if they are
     * sure they want to log out. If the user confirms, it logs the user out by
     * resetting the session state in the {@link UserSession} and then
     * initializes and displays the main screen.</p>
     */
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

                    /**
                     * Displays the default content of the user dashboard.
                     * <p>
                     * This method is called when the user navigates to the user
                     * dashboard. It displays the pet gallery by calling the
                     * {@link #showPetGallery()} method.
                     */
                    @Override
                    protected void showDefaultContent() {
                        showPetGallery();
                    }

                    /**
                     * Returns the title of the main screen dashboard.
                     *
                     * @return the title of the main screen dashboard.
                     */
                    @Override
                    protected String getDashboardTitle() {
                        return "Pet Adoption System";
                    }
                };
                mainScreen.initializeUI();
                mainScreen.show();
            }
        });
    }

    /**
     * Shows the user dashboard.
     * <p>
     * This method is called when the user navigates to the user dashboard. It
     * creates a new scene with the main layout as its root and sets the scene
     * on the given {@link Stage}. The scene is styled with the CSS file located
     * at {@code /styles.css}. The stage is set to have a title of "User
     * Dashboard" and is shown.
     */
    @Override
    public void show() {
        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("User Dashboard");
        stage.show();
    }
}
