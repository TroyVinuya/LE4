package com.guiyomi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AppStoreApp extends Application {

    private VBox appDetailsVBox = new VBox(10);
    VBox appListVBox = new VBox(15);

    @Override
    public void start(Stage primaryStage) {
        List<App> apps = null;
        try {
            apps = AppLoader.loadApps("apps.json");  // Load JSON file
        } catch (IOException e) {
            System.err.println("Error loading app data: " + e.getMessage());
            e.printStackTrace();
        }

        ScrollPane appListScrollPane = new ScrollPane(appListVBox);

        //Scroll Pane 
        appListScrollPane.setFitToWidth(true);
        appListScrollPane.setPrefWidth(245); 
        appListScrollPane.setMaxWidth(245);  
        appListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        appListScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        //appListBox details
        appListVBox.setPadding(new Insets(15));
        appListVBox.setPrefWidth(appListScrollPane.getWidth());
        appListVBox.setStyle("-fx-background-color: #000000");

        // Listen for width changes in the ScrollPane and adjust the VBox width
        appListScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            appListVBox.setPrefWidth(newVal.doubleValue());
        });


        if (apps != null) {
            for (App app : apps) {
                appListVBox.getChildren().add(createAppItem(app));
            }
        }

        //App Details
        appDetailsVBox.setPadding(new Insets(20));
        appDetailsVBox.setMinWidth(510);
        appDetailsVBox.setMaxWidth(510); 

        //Main Layout
        HBox mainContent = new HBox(20, appListScrollPane, appDetailsVBox);
        HBox.setHgrow(appListScrollPane, Priority.NEVER); 
        HBox.setHgrow(appDetailsVBox, Priority.ALWAYS);
        

        VBox root = new VBox(mainContent);
        root.setPadding(new Insets(0));
        root.setStyle("-fx-background-color: #ffffff");

        Scene scene = new Scene(root, 800, 600); 
        primaryStage.setScene(scene);
        primaryStage.setTitle("App Store");

        primaryStage.show();
    }

    private VBox createAppItem(App app) {
        VBox appItem = new VBox(5);
        appItem.setPadding(new Insets(10));

        appItem.prefWidthProperty().bind(appListVBox.widthProperty());
        appItem.maxWidthProperty().bind(appListVBox.widthProperty());
        
        appItem.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0.5, 0, 0);");
        appItem.setOnMouseClicked(event -> displayAppDetails(app));

        ImageView thumbnailImageView = new ImageView();
        thumbnailImageView.setFitWidth(190);
        thumbnailImageView.setFitHeight(100);
        
        InputStream imageStream = AppStoreApp.class.getResourceAsStream("/images/" + app.getThumbnail());
        if (imageStream == null) {
            System.out.println("Image not found for: " + app.getThumbnail() + ", using default.");
            thumbnailImageView.setImage(new Image(AppStoreApp.class.getResourceAsStream("/images/default_image.png")));
        } else {
            thumbnailImageView.setImage(new Image(imageStream));
        }

        Label nameLabel = new Label(app.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.WHITE);

        Label studioLabel = new Label(app.getStudio());
        studioLabel.setFont(Font.font("Arial", 12));
        studioLabel. setTextFill(Color.LIGHTGRAY);

        appItem.getChildren().addAll(thumbnailImageView, nameLabel, studioLabel);
        return appItem;
    }

    private void displayAppDetails(App app) {
        appDetailsVBox.getChildren().clear();

        HBox headerHBox = new HBox(15);
        
        ImageView logoImageView = new ImageView();
        logoImageView.setFitHeight(100);
        logoImageView.setFitWidth(100);

        InputStream logoImageStream = AppStoreApp.class.getResourceAsStream("/images/" + app.getThumbnail());
        if (logoImageStream == null) {
            System.out.println("Image not found for: " + app.getThumbnail() + ", using default.");
            logoImageView.setImage(new Image(AppStoreApp.class.getResourceAsStream("/images/default_image.png")));
        } else {
            logoImageView.setImage(new Image(logoImageStream));
        }

        VBox headerVBox = new VBox(10);

        Label nameLabel = new Label(app.getName());
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 24));

        Label ratingAndDownloadsLabel = new Label("★ " + app.getRating() + " - " + app.getDownloads());
        ratingAndDownloadsLabel.setFont(Font.font("Verdana", 14));
        
        Label studioAndGenreLabel = new Label(app.getStudio() + " - " + app.getGenre());
        studioAndGenreLabel.setFont(Font.font("Verdana", 16));
        VBox.setMargin(studioAndGenreLabel, new Insets(0, 0, 10, 0));

        headerVBox.getChildren().addAll(nameLabel, studioAndGenreLabel, ratingAndDownloadsLabel);

        headerHBox.getChildren().addAll(logoImageView, headerVBox);

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        VBox.setMargin(descriptionLabel, new Insets(10, 0, 0, 0));

        String descriptionText = app.getDescription().replaceAll("(?<!\\w\\.)\\s+", " ").replaceAll("\\.\\s+", ".\n\n");

        Label descriptionTextLabel = new Label(descriptionText);
        descriptionTextLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        descriptionTextLabel.setWrapText(true);
        descriptionTextLabel.setTextAlignment(TextAlignment.LEFT);

        appDetailsVBox.getChildren().addAll(headerHBox, descriptionLabel, descriptionTextLabel);
        appDetailsVBox.setId("app-details");
        appDetailsVBox.setStyle("-fx-background-radius: 10px;"); // Add this line
    }

    public static void main(String[] args) {
        launch(args);
    }
}