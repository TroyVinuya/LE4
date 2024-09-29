package com.guiyomi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    @Override
    public void start(Stage primaryStage) {
        List<App> apps = null;
        try {
            apps = AppLoader.loadApps("apps.json");  // Load JSON file
        } catch (IOException e) {
            System.err.println("Error loading app data: " + e.getMessage());
            e.printStackTrace();
        }

        // App List
        VBox appListVBox = new VBox(10);
        appListVBox.setPadding(new Insets(10));
        appListVBox.setMinWidth(200); 
        appListVBox.setStyle("-fx-background-color: #000000;");

        if (apps != null) {
            for (App app : apps) {
                appListVBox.getChildren().add(createAppItem(app));
            }
        }

        ScrollPane appListScrollPane = new ScrollPane(appListVBox);
        appListScrollPane.setFitToWidth(true);
        appListScrollPane.setPrefWidth(350); 
        appListScrollPane.setMaxWidth(350);  

        // App Details
        appDetailsVBox.setPadding(new Insets(20));
        appDetailsVBox.setMinWidth(550);
        appDetailsVBox.setMaxWidth(550); 
        appDetailsVBox.setStyle("-fx-background-color: #ffffff;");

        // Main Layout
        HBox mainContent = new HBox(20, appListScrollPane, appDetailsVBox);
        HBox.setHgrow(appListScrollPane, Priority.NEVER); 
        HBox.setHgrow(appDetailsVBox, Priority.ALWAYS);

        
        appListScrollPane.setPrefWidth(250); 
        appListScrollPane.setMaxWidth(250); 
        appListScrollPane.setMinWidth(250); 
        
        VBox root = new VBox(mainContent);
        root.setPadding(new Insets(0));

        Scene scene = new Scene(root, 850, 600); 
        primaryStage.setScene(scene);
        primaryStage.setTitle("App Store");

        primaryStage.show();
    }

    private VBox createAppItem(App app) {
        VBox appItem = new VBox(5);
        appItem.setMaxWidth(200); 
        appItem.setPadding(new Insets(10));
        appItem.setStyle(
                "-fx-background-color: #2a2a2a;" + 
                "-fx-background-radius: 10;" +      
                "-fx-padding: 10;" +                 
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0.5, 0, 2);"  
            );
        appItem.setOnMouseClicked(event -> displayAppDetails(app));

        ImageView thumbnailImageView = new ImageView();
        thumbnailImageView.setFitWidth(200);
        thumbnailImageView.setFitHeight(100);
        
        InputStream imageStream = AppStoreApp.class.getResourceAsStream("/images/" + app.getThumbnail());
        if (imageStream == null) {
            System.out.println("Image not found for: " + app.getThumbnail() + ", using default.");
            thumbnailImageView.setImage(new Image(AppStoreApp.class.getResourceAsStream("/images/default_image.png")));
        } else {
            thumbnailImageView.setImage(new Image(imageStream));
        }

        Label nameLabel = new Label(app.getName());
        nameLabel.setStyle(
            "-fx-font-family: 'Arial';" +  
            "-fx-font-size: 16px;" +       
            "-fx-font-weight: bold;" +     
            "-fx-text-fill: white;"        
        );
        
        Label studioLabel = new Label(app.getStudio());
        studioLabel.setStyle(
            "-fx-font-family: 'Arial';" +  
            "-fx-font-size: 12px;" +       
            "-fx-text-fill: lightgray;"    
            );



        appItem.getChildren().addAll(thumbnailImageView, nameLabel, studioLabel);
        return appItem;
    }

    private void displayAppDetails(App app) {
        appDetailsVBox.getChildren().clear();
        appDetailsVBox.setPadding(new Insets(5));
       
        
        HBox imageHBox = new HBox();
        imageHBox.setSpacing(10);

        // Add app image
        ImageView appImageView = new ImageView();
        appImageView.setFitWidth(100); 
        appImageView.setFitHeight(100); 
        appImageView.setPreserveRatio(true); 
       

        ImageView thumbnailImageView = new ImageView();
        thumbnailImageView.setFitWidth(350);
        thumbnailImageView.setFitHeight(200);
        
        InputStream imageStream = AppStoreApp.class.getResourceAsStream("/images/" + app.getThumbnail());
        if (imageStream == null) {
            System.out.println("Image not found for: " + app.getThumbnail() + ", using default.");
            thumbnailImageView.setImage(new Image(AppStoreApp.class.getResourceAsStream("/images/default_image.png")));
        } else {
            thumbnailImageView.setImage(new Image(imageStream));
        }

        imageHBox.getChildren().addAll(appImageView, thumbnailImageView);
    
        Label nameLabel = new Label(app.getName());
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
    
        Label ratingLabel = new Label("★ Rating: " + app.getRating());
        ratingLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        
        Label downloadsLabel = new Label("Downloads: " + app.getDownloads());
        downloadsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
    
        Label descriptionLabel = new Label(app.getDescription());
        descriptionLabel.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextAlignment(TextAlignment.JUSTIFY);
    
        appDetailsVBox.getChildren().addAll(imageHBox, nameLabel, ratingLabel, downloadsLabel, descriptionLabel);
        appDetailsVBox.setId("app-details");
        appDetailsVBox.setStyle("-fx-background-radius: 10px;");
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}