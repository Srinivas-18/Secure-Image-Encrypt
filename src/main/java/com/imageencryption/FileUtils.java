package com.imageencryption;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    
    public static double getFileSizeKB(String filePath) {
        try {
            Path path = Path.of(filePath);
            long sizeInBytes = Files.size(path);
            return Math.round((double) sizeInBytes / 1024.0 * 100.0) / 100.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    public static String showSaveDialog(String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        
        // Set appropriate extension filter
        switch (extension.toLowerCase()) {
            case "enc":
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Encrypted Files", "*.enc")
                );
                break;
            case "png":
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG Image", "*.png")
                );
                break;
            default:
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );
        }
        
        File selectedFile = fileChooser.showSaveDialog(new Stage());
        return selectedFile != null ? selectedFile.getAbsolutePath() : null;
    }
}
