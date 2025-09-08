package com.automation.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class DriverManager {
    private static final ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();

    public static void initDriver() {
        try {
            String platform = ConfigReader.get("platform");
            DesiredCapabilities caps = new DesiredCapabilities();

            caps.setCapability("platformName", platform);
            caps.setCapability("deviceName", ConfigReader.get("deviceName"));
            caps.setCapability("automationName", ConfigReader.get("automationName"));
            caps.setCapability("noReset", Boolean.parseBoolean(ConfigReader.get("noReset")));

            if ("Android".equalsIgnoreCase(platform)) {
                caps.setCapability("appPackage", ConfigReader.get("appPackage"));
                caps.setCapability("appActivity", ConfigReader.get("appActivity"));

                AppiumDriver driver = new AndroidDriver(new URL(ConfigReader.get("appiumServer")), caps);
                tlDriver.set(driver);

            } else if ("iOS".equalsIgnoreCase(platform)) {
                AppiumDriver driver = new IOSDriver(new URL(ConfigReader.get("appiumServer")), caps);
                tlDriver.set(driver);

            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }

            getDriver().manage().timeouts().implicitlyWait(
                java.time.Duration.ofSeconds(ConfigReader.getInt("implicitWait", 10))
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to start Appium driver", e);
        }
    }

    public static AppiumDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        AppiumDriver driver = tlDriver.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {}
            tlDriver.remove();
        }
    }
}
