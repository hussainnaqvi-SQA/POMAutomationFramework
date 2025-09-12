package com.automation.base;

import com.automation.utils.DriverManager;
import com.automation.utils.LoggerUtil;
import com.aventstack.extentreports.ExtentTest;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.Logger;

import java.time.Duration;

/**
 * BasePage - Common reusable methods for all page objects.
 */
public class BasePage {

    protected final AppiumDriver driver;
    private final Logger log;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.log = LoggerUtil.getLogger(getClass());
    }

    // --- Generic wait helper ---
    protected <T> T waitUntil(ExpectedCondition<T> condition, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(condition);
    }

    private WebElement waitForVisible(By locator, int seconds) {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(locator), seconds);
    }

    private WebElement waitForPresence(By locator, int seconds) {
        return waitUntil(ExpectedConditions.presenceOfElementLocated(locator), seconds);
    }

    // --- Action methods ---
    public void click(By locator, ExtentTest extentTest) {
        try {
            WebElement el = waitForVisible(locator, 15);
            el.click();
            log.info("Clicked on element: {}", locator);
            if (extentTest != null) extentTest.info("Clicked: " + locator);
        } catch (Exception e) {
            log.error("❌ Failed to click element: {} - {}", locator, e.getMessage());
            if (extentTest != null) extentTest.info("Failed to click: " + locator);
            throw e;
        }
    }

    public void type(By locator, String text, ExtentTest extentTest) {
        try {
            WebElement el = waitForVisible(locator, 15);
            el.clear();
            el.sendKeys(text);
            log.info("Typed '{}' into element: {}", text, locator);
            if (extentTest != null) extentTest.info("Typed into " + locator + " -> " + text);
        } catch (Exception e) {
            log.error("❌ Failed to type '{}' into {} - {}", text, locator, e.getMessage());
            if (extentTest != null) extentTest.info("Failed to type into " + locator);
            throw e;
        }
    }

    public String getText(By locator) {
        WebElement el = waitForVisible(locator, 15);
        String text = el.getText();
        if (text == null || text.trim().isEmpty()) {
            text = el.getAttribute("content-desc");
        }
        text = (text != null ? text.trim() : "");
        log.info("Fetched text from [{}]: {}", locator, text);
        return text;
    }

    public boolean isDisplayed(By locator, ExtentTest extentTest) {
        try {
            boolean visible = waitForVisible(locator, 15).isDisplayed();
            String message = "Element " + locator + " is displayed: " + visible;
            log.info(message);
            if (extentTest != null) extentTest.info("✅ " + message);
            return visible;
        } catch (TimeoutException | NoSuchElementException e) {
            String message = "❌ Element " + locator + " is NOT displayed";
            log.error(message);
            if (extentTest != null) extentTest.info(message);
            return false;
        }
    }

    public String getValidationText(By locator, ExtentTest extentTest) {
        String text = getText(locator);
        if (extentTest != null) extentTest.info("Validation text: " + text);
        return text;
    }

    // --- Mobile utilities ---
    public void pressEnter(ExtentTest extentTest) {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
        log.info("Pressed Enter key");
        if (extentTest != null) extentTest.info("Pressed Enter");
    }

    public String getToastMessage(int timeoutSeconds) {
        try {
            WebElement toast = waitUntil(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.Toast")),
                timeoutSeconds
            );
            String message = toast.getText();
            log.info("Captured Toast: {}", message);
            return message;
        } catch (Exception e) {
            log.error("❌ Failed to capture toast: {}", e.getMessage());
            return null;
        }
    }

    public void scrollToText(String text, ExtentTest extentTest) {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
              + ".scrollTextIntoView(\"" + text + "\")"
            ));
            log.info("Scrolled to text: {}", text);
            if (extentTest != null) extentTest.info("Scrolled to: " + text);
        } catch (Exception e) {
            log.error("❌ Failed to scroll to text: {}", text);
            if (extentTest != null) extentTest.info("Failed to scroll to: " + text);
        }
    }
}
