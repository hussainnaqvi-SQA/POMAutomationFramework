package com.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config.properties file: " + e.getMessage());
        }
    }

    // --- Strict get (throws if missing) ---
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("❌ Missing config property: " + key);
        }
        return value.trim();
    }

    // --- Get with default ---
    public static String get(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return (value != null) ? value.trim() : defaultValue;
    }

    // --- Alias for compatibility ---
    public static String getOrDefault(String key, String defaultValue) {
        return get(key, defaultValue);
    }
}
