package com.imageencryption;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.regex.Pattern;

public class KeyUtils {
    
    private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    
    public static SecretKey generateKeyFromPin(String pin) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(pin.getBytes("UTF-8"));
        
        // Use first 16 bytes for AES-128 key
        byte[] keyBytes = new byte[16];
        System.arraycopy(hash, 0, keyBytes, 0, 16);
        
        return new SecretKeySpec(keyBytes, "AES");
    }
    
    public static String checkPinStrength(String pin) {
        if (pin == null || pin.isEmpty()) {
            return "Weak";
        }
        
        int length = pin.length();
        boolean hasUpperCase = UPPER_CASE_PATTERN.matcher(pin).find();
        boolean hasLowerCase = LOWER_CASE_PATTERN.matcher(pin).find();
        boolean hasDigit = DIGIT_PATTERN.matcher(pin).find();
        boolean hasSpecialChar = SPECIAL_CHAR_PATTERN.matcher(pin).find();
        
        if (length >= 8) {
            if (hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar) {
                LogManager.logEvent("PIN Strength Checked: Strong");
                return "Strong";
            } else if ((hasUpperCase || hasLowerCase) && hasDigit) {
                LogManager.logEvent("PIN Strength Checked: Medium");
                return "Medium";
            }
        } else if (length >= 5 && (hasUpperCase || hasLowerCase) && hasDigit) {
            LogManager.logEvent("PIN Strength Checked: Medium");
            return "Medium";
        }
        
        LogManager.logEvent("PIN Strength Checked: Weak");
        return "Weak";
    }
}
