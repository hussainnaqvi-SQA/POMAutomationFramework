package com.automation.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static void initDriver() {
        if (driver.get() != null) {
            return;
        }

        try {
            String platform = ConfigReader.getOrDefault("platform", "Android");
            String deviceName = ConfigReader.getOrDefault("deviceName", "emulator-5554");
            String automationName = ConfigReader.getOrDefault("automationName", "UiAutomator2");
            String appiumServer = ConfigReader.getOrDefault("appiumServer", "http://127.0.0.1:4723");
            String appPackage = ConfigReader.getOrDefault("appPackage", "");
            String appActivity = ConfigReader.getOrDefault("appActivity", "");
            boolean noReset = Boolean.parseBoolean(ConfigReader.getOrDefault("noReset", "true"));
            int implicitWait = Integer.parseInt(ConfigReader.getOrDefault("implicitWait", "10"));

            if (platform.equalsIgnoreCase("Android")) {
                UiAutomator2Options options = new UiAutomator2Options()
                        .setDeviceName(deviceName)
                        .setAutomationName(automationName)
                        .setAppPackage(appPackage)
                        .setAppActivity(appActivity)
                        .setNoReset(noReset);

                AppiumDriver androidDriver = new AndroidDriver(new URL(appiumServer), options);
                androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
                driver.set(androidDriver);
            } else {
                throw new RuntimeException("❌ Unsupported platform: " + platform);
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to initialize driver: " + e.getMessage(), e);
        }
    }

    public static AppiumDriver getDriver() {
        if (driver.get() == null) {
            initDriver();
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
