package com.imageencryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageDecryption {
    
    public static void decryptImage(String encryptedFilePath, String pin) throws Exception {
        if (encryptedFilePath == null || encryptedFilePath.isEmpty()) {
            throw new IllegalArgumentException("No encrypted file selected!");
        }
        if (pin == null || pin.isEmpty()) {
            throw new IllegalArgumentException("Enter the correct PIN to decrypt!");
        }
        
        // Generate decryption key from PIN
        SecretKey key = KeyUtils.generateKeyFromPin(pin);
        
        // Read encrypted data
        byte[] encryptedData = Files.readAllBytes(Paths.get(encryptedFilePath));
        double sizeBefore = FileUtils.getFileSizeKB(encryptedFilePath);
        
        // Decrypt the data
        byte[] decryptedData = decryptData(encryptedData, key);
        String decryptedHash = HashUtils.generateSHA256(decryptedData);
        
        LogManager.logEvent("Encrypted file loaded: " + encryptedFilePath);
        LogManager.logEvent("Post-decryption SHA256: " + decryptedHash);
        
        // Check integrity
        String metaPath = encryptedFilePath + ".meta";
        if (Files.exists(Paths.get(metaPath))) {
            String originalHash = new String(Files.readAllBytes(Paths.get(metaPath))).trim();
            if (!originalHash.equals(decryptedHash)) {
                LogManager.logEvent("WARNING: Decrypted image hash mismatch!");
                throw new SecurityException("Hash mismatch detected! File may be tampered with or wrong PIN used.");
            } else {
                LogManager.logEvent("Image integrity verified successfully.");
            }
        } else {
            LogManager.logEvent("No .meta file found. Skipping integrity check.");
        }
        
        // Load and process image
        BufferedImage encryptedImage = ImageUtils.bytesToImage(decryptedData);
        double entropyAfter = ImageUtils.calculateEntropy(encryptedImage);
        
        // Reverse pixel shift
        BufferedImage unshiftedImage = PixelShift.reverseUnshiftPixels(encryptedImage);
        
        // Save decrypted image
        String savePath = FileUtils.showSaveDialog("png");
        if (savePath != null) {
            ImageUtils.saveImage(unshiftedImage, savePath);
            double sizeAfter = FileUtils.getFileSizeKB(savePath);
            
            // Clean up encrypted files
            Files.deleteIfExists(Paths.get(encryptedFilePath));
            Files.deleteIfExists(Paths.get(metaPath));
            
            LogManager.logEvent("Image decrypted and saved to: " + savePath);
            LogManager.logEvent("Encrypted and meta files deleted after successful decryption.");
            
            // Show statistics
            showDecryptionStats(entropyAfter, sizeBefore, sizeAfter);
            
            // Generate and show charts
            ChartGenerator.generateDecryptionCharts(entropyAfter, sizeBefore, sizeAfter);
        }
    }
    
    private static byte[] decryptData(byte[] encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }
    
    private static void showDecryptionStats(double entropyAfter, double sizeBefore, double sizeAfter) {
        System.out.println("=== Decryption Statistics ===");
        System.out.printf("Decrypted Image Entropy: %.4f%n", entropyAfter);
        System.out.printf("Encrypted Size: %.2f KB%n", sizeBefore);
        System.out.printf("Decrypted Size: %.2f KB%n", sizeAfter);
        System.out.println("=============================");
    }
}
