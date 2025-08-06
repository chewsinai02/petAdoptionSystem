package com.mycompany.petAdoptionSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class ProjectGUI extends Application {
    private TextArea outputArea;
    private static final String SECONDARY_COLOR = "#F5F5F5";
    private static final String ACCENT_COLOR = "#2C3E50";

    @Override
    public void start(Stage primaryStage) {
        // Show main screen directly with pet gallery
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Logo.png")));
        MainScreen mainScreen = new MainScreen(primaryStage);
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
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
