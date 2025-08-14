package com.imageencryption;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

public class ImageEncryptionTest {
    
    @TempDir
    Path tempDir;
    
    private BufferedImage testImage;
    
    @BeforeEach
    void setUp() {
        // Create a simple test image
        testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                int rgb = (i * 255 / 100) << 16 | (j * 255 / 100) << 8 | ((i + j) * 255 / 200);
                testImage.setRGB(i, j, rgb);
            }
        }
    }
    
    @Test
    void testPinStrengthCheck() {
        assertEquals("Weak", KeyUtils.checkPinStrength("123"));
        assertEquals("Medium", KeyUtils.checkPinStrength("abc123"));
        assertEquals("Strong", KeyUtils.checkPinStrength("Abc123!@#"));
    }
    
    @Test
    void testPixelShiftReversibility() {
        BufferedImage shifted = PixelShift.reverseShiftPixels(testImage);
        BufferedImage unshifted = PixelShift.reverseUnshiftPixels(shifted);
        
        // Check if the image is restored correctly
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                assertEquals(testImage.getRGB(i, j), unshifted.getRGB(i, j));
            }
        }
    }
    
    @Test
    void testEntropyCalculation() {
        double entropy = ImageUtils.calculateEntropy(testImage);
        assertTrue(entropy >= 0.0 && entropy <= 8.0);
    }
    
    @Test
    void testHashGeneration() {
        byte[] testData = "test data".getBytes();
        String hash1 = HashUtils.generateSHA256(testData);
        String hash2 = HashUtils.generateSHA256(testData);
        
        assertEquals(hash1, hash2);
        assertEquals(64, hash1.length()); // SHA256 produces 64 hex characters
    }
}
