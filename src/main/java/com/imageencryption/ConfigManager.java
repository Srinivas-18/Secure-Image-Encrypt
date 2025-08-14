package com.imageencryption;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            // Use default values if config file doesn't exist
            properties.setProperty("log.username", "admin");
            properties.setProperty("log.password", "admin123");
        }
    }
    
    public static boolean authenticateUser(String username, String password) {
        String expectedUsername = properties.getProperty("log.username", "admin");
        String expectedPassword = properties.getProperty("log.password", "admin123");
        
        return expectedUsername.equals(username) && expectedPassword.equals(password);
    }
}
