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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboardScreen extends MainScreen {

    public AdminDashboardScreen(Stage primaryStage) {
        super(primaryStage);
        // Do NOT touch menuBar here!
    }

    /**
     * Initializes the UI by calling the superclass's method and then
     * customizing the menu bar for the admin dashboard.
     * <p>
     * In particular, it clears the menus in the menu bar and replaces them with
     * the menus returned by {@link #createAdminMenuBar()}.
     */
    @Override
    public void initializeUI() {
        super.initializeUI();
        this.menuBar.getMenus().clear();
        this.menuBar.getMenus().addAll(createAdminMenuBar().getMenus());
    }

    /**
     * Override of {@link MainScreen#createMenuBar()} to create a menu bar that
     * is specific to the admin dashboard.
     *
     * @return the menu bar for the admin dashboard
     */
    @Override
    public MenuBar createMenuBar() {
        return createAdminMenuBar();
    }

    /**
     * Show the welcome message and the content area to the user.
     * <p>
     * This is called when the user navigates to the admin dashboard.
     */
    @Override
    public void showDefaultContent() {
        showWelcome();
    }

    /**
     * {@inheritDoc}
     *
     * @return the title of the admin dashboard
     */
    @Override
    public String getDashboardTitle() {
        return "Admin Dashboard";
    }

    /**
     * Create a menu bar for the admin dashboard. This menu bar contains several
     * menus:
     * <ol>
     * <li>Pets: Manage pets</li>
     * <li>Pet Well-being: View and request updates to pet well-being</li>
     * <li>Approvals: Manage adoption approvals</li>
     * <li>Admin: Logout</li>
     * </ol>
     *
     * @return the menu bar for the admin dashboard
     */
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

    /**
     * Displays the welcome message on the admin dashboard.
     *
     * This method sets up the VBox container with a welcome message label
     * styled for the admin dashboard. The message prompts the admin to select
     * an action from the menu above. The content is visually centered and
     * styled with specific background and text properties.
     */
    protected void showWelcome() {
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

    /**
     * Displays the Manage Pets screen on the admin dashboard.
     *
     * <p>
     * This method creates a new instance of {@link ManagePetsScreen} and sets
     * its content as the content of the admin dashboard. The content is set to
     * cover the entire content area and the margins are set to zero.
     */
    protected void showManagePets() {
        ManagePetsScreen petsScreen = new ManagePetsScreen(stage);
        contentArea.getChildren().setAll(petsScreen.getContent());
        StackPane.setMargin(petsScreen.getContent(), new Insets(0));
    }

    /**
     * Displays the Manage Adoption Approvals screen on the admin dashboard.
     *
     * <p>
     * This method creates a new instance of {@link ManageAdoptionsScreen} and
     * sets its content as the content of the admin dashboard. The content is
     * set to cover the entire content area with zero margins.
     */
    protected void showManageApprovals() {
        ManageAdoptionsScreen adoptionsScreen = new ManageAdoptionsScreen(stage);
        contentArea.getChildren().setAll(adoptionsScreen.getContent());
        StackPane.setMargin(adoptionsScreen.getContent(), new Insets(0));
    }

    /**
     * Displays the View Pet Well-being screen on the admin dashboard.
     *
     * <p>
     * This method creates a new instance of {@link ViewPetUpdate} and sets its
     * content as the content of the admin dashboard. The content is set to
     * cover the entire content area and the margins are set to zero.
     */
    protected void showWellbeing() {
        ViewPetUpdate updateScreen = new ViewPetUpdate(stage);
        contentArea.getChildren().setAll(updateScreen.getContent());
        StackPane.setMargin(updateScreen.getContent(), new Insets(0));
    }

    /**
     * Displays the Request Pet Status Update screen on the admin dashboard.
     *
     * <p>
     * This method creates a new instance of {@link RequestPetUpdate} and sets
     * its content as the content of the admin dashboard. The content is set to
     * cover the entire content area and the margins are set to zero.
     */
    protected void showRequest() {
        RequestPetUpdate requestScreen = new RequestPetUpdate(stage);
        contentArea.getChildren().setAll(requestScreen.getContent());
        StackPane.setMargin(requestScreen.getContent(), new Insets(0));
    }

    /**
     * Logs the user out of the system and shows the main screen.
     *
     * <p>
     * This method displays a confirmation dialog to the user to confirm whether
     * they want to log out. If the user confirms, the user is logged out, and
     * the main screen is shown.
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

                    /**
                     * Creates and returns a styled menu bar for the
                     * application.
                     *
                     * <p>
                     * The menu bar includes the following menus:
                     * <ul>
                     * <li><strong>Home Menu</strong>: Allows navigation back to
                     * the pet gallery.</li>
                     * <li><strong>Pet Knowledge Menu</strong>: Provides
                     * information on dog and cat care.</li>
                     * <li><strong>Adoption Menu</strong>: Displays the adoption
                     * process.</li>
                     * <li><strong>User Account Menu</strong>: Contains login
                     * and registration options. Only shown if the user is not
                     * logged in.</li>
                     * <li><strong>Notification Menu</strong>: Offers a
                     * notification view. Only shown if the user is logged
                     * in.</li>
                     * </ul>
                     *
                     * <p>
                     * The visibility of the "User Account" and "Notification"
                     * menus is determined by the user's login state.
                     *
                     * @return the constructed and styled MenuBar.
                     */
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

                    /**
                     * Displays the pet gallery when the user navigates to the
                     * admin dashboard.
                     * <p>
                     * This method is called when the user navigates to the
                     * admin dashboard. It displays the pet gallery by calling
                     * the {@link #showPetGallery()} method.
                     */
                    @Override
                    protected void showDefaultContent() {
                        showPetGallery();
                    }

                    /**
                     * Returns the title of the admin dashboard.
                     *
                     * @return the title of the admin dashboard.
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
     * Displays the admin dashboard.
     *
     * <p>
     * This method creates a new {@link Scene} with the main layout as its root
     * and sets the scene on the given {@link Stage}. The scene is styled with
     * the CSS file located at {@code /styles.css}. The stage is set to have a
     * title of "Admin Dashboard" and is shown.
     */
    @Override
    public void show() {
        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
}
