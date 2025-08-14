package com.imageencryption;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {
    
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "activity_log.csv";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void logEvent(String event) {
        try {
            // Create logs directory if it doesn't exist
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            Path logPath = logDir.resolve(LOG_FILE);
            boolean fileExists = Files.exists(logPath);
            
            // Write log entry
            try (BufferedWriter writer = Files.newBufferedWriter(logPath, 
                    fileExists ? java.nio.file.StandardOpenOption.APPEND : 
                    java.nio.file.StandardOpenOption.CREATE)) {
                
                if (!fileExists) {
                    writer.write("Timestamp,Event\n");
                }
                
                String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
                writer.write(String.format("%s,%s\n", timestamp, event));
            }
            
        } catch (IOException e) {
            System.err.println("Failed to write log entry: " + e.getMessage());
        }
    }
    
    public static String readLogFile() throws IOException {
        Path logPath = Paths.get(LOG_DIR, LOG_FILE);
        
        if (!Files.exists(logPath)) {
            return "Log file is empty or missing.";
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }
}
