/**
 * The main entry point for the Pet Adoption System application.
 * Initializes the application and displays the login screen.
 * 
 * @author Chew Sin Ai (finalize, and debug)
 * @version 1.0
 */
package com.mycompany.petAdoptionSystem;

import java.util.Objects;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private TextArea outputArea;
    private static final String SECONDARY_COLOR = "#F5F5F5";
    private static final String ACCENT_COLOR = "#2C3E50";

    /**
     * Shows the main screen with pet gallery.
     * <p>
     * This method creates a new instance of the {@link MainScreen} class and
     * calls its {@link #show()} method to display the main screen.
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Show main screen directly with pet gallery
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Logo.png"))));
        MainScreen mainScreen = new MainScreen(primaryStage) {
            /**
             * Creates a menu bar for the main screen. The menu bar includes
             * options for user navigation and interaction within the
             * application.
             *
             * @return the menu bar for the main screen
             */
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
             * Shows the default content of the main screen.
             * <p>
             * This method is called when the user navigates to the main screen.
             * It displays the pet gallery by calling the
             * {@link #showPetGallery()} method.
             */
            @Override
            protected void showDefaultContent() {
                showPetGallery();
            }

            /**
             * {@inheritDoc}
             *
             * @return the title of the main screen dashboard
             */
            @Override
            protected String getDashboardTitle() {
                return "Pet Adoption System";
            }
        };
        mainScreen.initializeUI();
        mainScreen.show();
    }

    // Add a new method to start the main application
    public void showMainApplication(Stage primaryStage) {
        // Title Section
        Text titleText = new Text("Pet Adoption System");
        titleText.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleText.setFill(Color.web(ACCENT_COLOR));

        // Output Text Area with styling
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);
        outputArea.setWrapText(true);
        outputArea.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");

        ScrollPane scrollPane = new ScrollPane(outputArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");

        // Organize buttons in a grid
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setPadding(new Insets(10));

        // Main layout
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: " + SECONDARY_COLOR + ";");

        HBox topSection = new HBox(30);
        topSection.setAlignment(Pos.CENTER);
        topSection.getChildren().addAll(buttonGrid);

        mainContent.getChildren().addAll(titleText, topSection, scrollPane);

        Scene scene = new Scene(mainContent, 900, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Starts the JavaFX application.
     * <p>
     * This is the entry point for the application. It simply calls the
     * {@link Application#launch(String[])} method to start the application.
     *
     * @param args the arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
