package com.imageencryption;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.util.Optional;

public class MainGUI extends Application {
    
    private String imagePath = "";
    private String encryptedFilePath = "";
    private Label fileLabel;
    private Label encryptedFileLabel;
    private Label strengthLabel;
    private PasswordField pinEncryptField;
    private PasswordField pinDecryptField;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Encryption & Decryption");
        primaryStage.setResizable(false);
        
        // Main container
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #dfdddd;");
        
        // Title
        Label titleLabel = new Label("Image Encryption & Decryption");
        titleLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #333333;");
        
        // File selection section
        VBox fileSection = createFileSection();
        
        // Encryption section
        VBox encryptionSection = createEncryptionSection();
        
        // Decryption section
        VBox decryptionSection = createDecryptionSection();
        
        // Log button
        Button logButton = new Button("View Activity Log");
        logButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        logButton.setOnAction(e -> showLogAuthentication(primaryStage));
        
        // Add all components
        mainContainer.getChildren().addAll(
            titleLabel,
            fileSection,
            encryptionSection,
            decryptionSection,
            logButton
        );
        
        Scene scene = new Scene(mainContainer, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createFileSection() {
        VBox section = new VBox(5);
        section.setAlignment(Pos.CENTER);
        
        fileLabel = new Label("No original image selected");
        fileLabel.setStyle("-fx-text-fill: #666666;");
        
        Button browseButton = new Button("Browse Original Image");
        browseButton.setStyle("-fx-background-color: #e28743; -fx-text-fill: white;");
        browseButton.setOnAction(e -> browseImage());
        
        section.getChildren().addAll(fileLabel, browseButton);
        return section;
    }
    
    private VBox createEncryptionSection() {
        VBox section = new VBox(5);
        section.setAlignment(Pos.CENTER);
        
        Label pinLabel = new Label("Enter Encryption PIN:");
        pinEncryptField = new PasswordField();
        pinEncryptField.setPromptText("Enter PIN");
        pinEncryptField.textProperty().addListener((observable, oldValue, newValue) -> 
            updateStrengthLabel(newValue));
        
        strengthLabel = new Label("PIN Strength: ");
        strengthLabel.setStyle("-fx-text-fill: #666666;");
        
        Button encryptButton = new Button("Encrypt Image");
        encryptButton.setStyle("-fx-background-color: #e28743; -fx-text-fill: white;");
        encryptButton.setOnAction(e -> encryptImage());
        
        section.getChildren().addAll(pinLabel, pinEncryptField, strengthLabel, encryptButton);
        return section;
    }
    
    private VBox createDecryptionSection() {
        VBox section = new VBox(5);
        section.setAlignment(Pos.CENTER);
        
        encryptedFileLabel = new Label("No encrypted file selected");
        encryptedFileLabel.setStyle("-fx-text-fill: #666666;");
        
        Button browseEncButton = new Button("Browse Encrypted File");
        browseEncButton.setStyle("-fx-background-color: #e28743; -fx-text-fill: white;");
        browseEncButton.setOnAction(e -> browseEncryptedFile());
        
        Label pinDecryptLabel = new Label("Enter Decryption PIN:");
        pinDecryptField = new PasswordField();
        pinDecryptField.setPromptText("Enter PIN");
        
        Button decryptButton = new Button("Decrypt Image");
        decryptButton.setStyle("-fx-background-color: #e28743; -fx-text-fill: white;");
        decryptButton.setOnAction(e -> decryptImage());
        
        section.getChildren().addAll(
            encryptedFileLabel, 
            browseEncButton, 
            pinDecryptLabel, 
            pinDecryptField, 
            decryptButton
        );
        return section;
    }
    
    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            fileLabel.setText(selectedFile.getName());
        }
    }
    
    private void browseEncryptedFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Encrypted File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Encrypted Files", "*.enc"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            encryptedFilePath = selectedFile.getAbsolutePath();
            encryptedFileLabel.setText(selectedFile.getName());
        }
    }
    
    private void updateStrengthLabel(String pin) {
        String strength = KeyUtils.checkPinStrength(pin);
        strengthLabel.setText("PIN Strength: " + strength);
        
        switch (strength) {
            case "Strong":
                strengthLabel.setStyle("-fx-text-fill: green;");
                break;
            case "Medium":
                strengthLabel.setStyle("-fx-text-fill: orange;");
                break;
            default:
                strengthLabel.setStyle("-fx-text-fill: red;");
                break;
        }
    }
    
    private void encryptImage() {
        if (imagePath.isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "No image selected!");
            return;
        }
        if (pinEncryptField.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "Enter a PIN for encryption!");
            return;
        }
        
        try {
            ImageEncryption.encryptImage(imagePath, pinEncryptField.getText());
            showAlert(AlertType.INFORMATION, "Success", "Image Encrypted and Saved!");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Encryption failed: " + e.getMessage());
        }
    }
    
    private void decryptImage() {
        if (encryptedFilePath.isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "No encrypted file selected!");
            return;
        }
        if (pinDecryptField.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "Enter the correct PIN to decrypt!");
            return;
        }
        
        try {
            ImageDecryption.decryptImage(encryptedFilePath, pinDecryptField.getText());
            showAlert(AlertType.INFORMATION, "Success", "Image decrypted successfully!");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Decryption failed: " + e.getMessage());
        }
    }
    
    private void showLogAuthentication(Stage parentStage) {
        Stage authStage = new Stage();
        authStage.setTitle("Log Access Authentication");
        authStage.initOwner(parentStage);
        authStage.setResizable(false);
        
        VBox authContainer = new VBox(10);
        authContainer.setAlignment(Pos.CENTER);
        authContainer.setPadding(new Insets(20));
        authContainer.setStyle("-fx-background-color: #f0f0f0;");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (ConfigManager.authenticateUser(username, password)) {
                authStage.close();
                showLogViewer();
            } else {
                showAlert(AlertType.ERROR, "Access Denied", "Invalid credentials.");
            }
        });
        
        authContainer.getChildren().addAll(
            new Label("Username:"),
            usernameField,
            new Label("Password:"),
            passwordField,
            loginButton
        );
        
        Scene authScene = new Scene(authContainer, 300, 200);
        authStage.setScene(authScene);
        authStage.show();
    }
    
    private void showLogViewer() {
        Stage logStage = new Stage();
        logStage.setTitle("Activity Log");
        logStage.setResizable(true);
        
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(20);
        logArea.setPrefColumnCount(80);
        
        try {
            String logContent = LogManager.readLogFile();
            logArea.setText(logContent);
        } catch (Exception e) {
            logArea.setText("Log file is empty or missing.");
        }
        
        Scene logScene = new Scene(logArea, 600, 400);
        logStage.setScene(logScene);
        logStage.show();
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
