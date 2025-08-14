package com.imageencryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ImageEncryption {
    
    public static void encryptImage(String imagePath, String pin) throws Exception {
        if (imagePath == null || imagePath.isEmpty()) {
            throw new IllegalArgumentException("No image selected!");
        }
        if (pin == null || pin.isEmpty()) {
            throw new IllegalArgumentException("Enter a PIN for encryption!");
        }
        
        // Load and process image
        BufferedImage originalImage = ImageUtils.loadImage(imagePath);
        double entropyBefore = ImageUtils.calculateEntropy(originalImage);
        double sizeBefore = FileUtils.getFileSizeKB(imagePath);
        
        // Apply pixel shifting
        BufferedImage shiftedImage = PixelShift.reverseShiftPixels(originalImage);
        double entropyAfter = ImageUtils.calculateEntropy(shiftedImage);
        
        // Convert image to bytes
        byte[] imageBytes = ImageUtils.imageToBytes(shiftedImage);
        
        // Generate hash for integrity
        String originalHash = HashUtils.generateSHA256(imageBytes);
        LogManager.logEvent("Image selected: " + imagePath);
        LogManager.logEvent("Pre-encryption SHA256: " + originalHash);
        
        // Generate encryption key from PIN
        SecretKey key = KeyUtils.generateKeyFromPin(pin);
        
        // Encrypt the image data
        byte[] encryptedData = encryptData(imageBytes, key);
        
        // Save encrypted file
        String savePath = FileUtils.showSaveDialog("enc");
        if (savePath != null) {
            Files.write(Paths.get(savePath), encryptedData);
            double sizeAfter = FileUtils.getFileSizeKB(savePath);
            
            // Save metadata file
            String metaPath = savePath + ".meta";
            Files.write(Paths.get(metaPath), originalHash.getBytes());
            
            LogManager.logEvent("Image encrypted and saved to: " + savePath);
            LogManager.logEvent("Hash saved to: " + metaPath);
            
            // Show statistics
            showEncryptionStats(entropyBefore, entropyAfter, sizeBefore, sizeAfter);
            
            // Generate and show charts
            ChartGenerator.generateEncryptionCharts(entropyBefore, entropyAfter, sizeBefore, sizeAfter);
        }
    }
    
    private static byte[] encryptData(byte[] data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    
    private static void showEncryptionStats(double entropyBefore, double entropyAfter, 
                                          double sizeBefore, double sizeAfter) {
        System.out.println("=== Encryption Statistics ===");
        System.out.printf("Original Image Entropy: %.4f%n", entropyBefore);
        System.out.printf("Encrypted Image Entropy: %.4f%n", entropyAfter);
        System.out.printf("Original Size: %.2f KB%n", sizeBefore);
        System.out.printf("Encrypted Size: %.2f KB%n", sizeAfter);
        System.out.println("=============================");
    }
}
