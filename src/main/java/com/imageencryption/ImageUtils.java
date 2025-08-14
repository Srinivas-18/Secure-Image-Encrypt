package com.imageencryption;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    
    public static BufferedImage loadImage(String imagePath) throws IOException {
        File file = new File(imagePath);
        if (!file.exists()) {
            throw new IOException("Image file not found: " + imagePath);
        }
        return ImageIO.read(file);
    }
    
    public static void saveImage(BufferedImage image, String filePath) throws IOException {
        File file = new File(filePath);
        String format = getFileExtension(filePath);
        ImageIO.write(image, format, file);
    }
    
    public static byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }
    
    public static BufferedImage bytesToImage(byte[] imageBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(bais);
    }
    
    public static double calculateEntropy(BufferedImage image) {
        // Convert to grayscale for entropy calculation
        BufferedImage grayscale = convertToGrayscale(image);
        
        Map<Integer, Integer> frequency = new HashMap<>();
        int totalPixels = 0;
        
        int width = grayscale.getWidth();
        int height = grayscale.getHeight();
        
        // Count pixel frequencies
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = grayscale.getRGB(j, i);
                int grayValue = rgb & 0xFF;
                frequency.put(grayValue, frequency.getOrDefault(grayValue, 0) + 1);
                totalPixels++;
            }
        }
        
        // Calculate entropy
        double entropy = 0.0;
        for (int count : frequency.values()) {
            double probability = (double) count / totalPixels;
            if (probability > 0) {
                entropy -= probability * (Math.log(probability) / Math.log(2));
            }
        }
        
        return Math.round(entropy * 10000.0) / 10000.0; // Round to 4 decimal places
    }
    
    private static BufferedImage convertToGrayscale(BufferedImage image) {
        BufferedImage grayscale = new BufferedImage(
            image.getWidth(), 
            image.getHeight(), 
            BufferedImage.TYPE_BYTE_GRAY
        );
        
        Graphics2D g2d = grayscale.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        return grayscale;
    }
    
    private static String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot > 0) {
            return filePath.substring(lastDot + 1).toLowerCase();
        }
        return "png"; // Default format
    }
}
