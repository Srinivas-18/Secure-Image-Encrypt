package com.imageencryption;

public class ChartGenerator {
    
    public static void generateEncryptionCharts(double entropyBefore, double entropyAfter, 
                                              double sizeBefore, double sizeAfter) {
        System.out.println("\n=== ENCRYPTION CHARTS ===");
        
        // Entropy comparison chart
        System.out.println("Entropy Comparison:");
        System.out.printf("Original:  [%s] %.4f%n", 
            generateBar(entropyBefore, 8.0), entropyBefore);
        System.out.printf("Encrypted: [%s] %.4f%n", 
            generateBar(entropyAfter, 8.0), entropyAfter);
        
        // File size comparison chart
        System.out.println("\nFile Size Comparison (KB):");
        double maxSize = Math.max(sizeBefore, sizeAfter);
        System.out.printf("Original:  [%s] %.2f KB%n", 
            generateBar(sizeBefore, maxSize), sizeBefore);
        System.out.printf("Encrypted: [%s] %.2f KB%n", 
            generateBar(sizeAfter, maxSize), sizeAfter);
        
        System.out.println("========================\n");
    }
    
    public static void generateDecryptionCharts(double entropyAfter, double sizeBefore, double sizeAfter) {
        System.out.println("\n=== DECRYPTION CHARTS ===");
        
        // Entropy comparison chart
        System.out.println("Entropy Comparison:");
        System.out.printf("Encrypted: [%s] 8.0000%n", 
            generateBar(8.0, 8.0));
        System.out.printf("Decrypted: [%s] %.4f%n", 
            generateBar(entropyAfter, 8.0), entropyAfter);
        
        // File size comparison chart
        System.out.println("\nFile Size Comparison (KB):");
        double maxSize = Math.max(sizeBefore, sizeAfter);
        System.out.printf("Encrypted: [%s] %.2f KB%n", 
            generateBar(sizeBefore, maxSize), sizeBefore);
        System.out.printf("Decrypted: [%s] %.2f KB%n", 
            generateBar(sizeAfter, maxSize), sizeAfter);
        
        System.out.println("========================\n");
    }
    
    private static String generateBar(double value, double maxValue) {
        int barLength = 20;
        int filledLength = (int) Math.round((value / maxValue) * barLength);
        
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barLength; i++) {
            if (i < filledLength) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        return bar.toString();
    }
}
