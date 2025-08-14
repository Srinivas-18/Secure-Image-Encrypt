package com.imageencryption;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class PixelShift {
    
    public static BufferedImage reverseShiftPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage shiftedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int shiftVal = (height * width - (i * width + j)) % 256;
                
                int rgb = image.getRGB(j, i);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                r = (r + shiftVal) % 256;
                g = (g + shiftVal) % 256;
                b = (b + shiftVal) % 256;
                
                int newRgb = (r << 16) | (g << 8) | b;
                shiftedImage.setRGB(j, i, newRgb);
            }
        }
        
        return shiftedImage;
    }
    
    public static BufferedImage reverseUnshiftPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage unshiftedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int shiftVal = (height * width - (i * width + j)) % 256;
                
                int rgb = image.getRGB(j, i);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                r = (r - shiftVal + 256) % 256;
                g = (g - shiftVal + 256) % 256;
                b = (b - shiftVal + 256) % 256;
                
                int newRgb = (r << 16) | (g << 8) | b;
                unshiftedImage.setRGB(j, i, newRgb);
            }
        }
        
        return unshiftedImage;
    }
}
