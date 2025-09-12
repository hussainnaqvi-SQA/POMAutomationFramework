package com.automation.pages;

import com.automation.base.BasePage;
import com.automation.utils.LogcatUtils;
import com.automation.utils.LoggerUtil;
import com.automation.utils.MitmproxyClient;
import com.aventstack.extentreports.ExtentTest;

import io.appium.java_client.AppiumBy;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

/**
 * LoginPage - Handles login flows (Email + Phone).
 * Uses native Appium locators (By) to keep Flutter-agnostic.
 */
public class LoginPage extends BasePage {

    private static final Logger log = LoggerUtil.getLogger(LoginPage.class);

    // ---------------- Locators ----------------
    private final By usernameField     = By.xpath("//android.widget.ScrollView/android.widget.EditText[1]");
    private final By passwordField     = By.xpath("//android.widget.ScrollView/android.widget.EditText[2]");
    private final By loginBtn          = By.xpath("//android.widget.Button[@content-desc=\"Log In\"]");
    private final By loginSuccessCheck = By.xpath("//android.view.View[@content-desc=\"Two Step Authentication\"]");
    private final By loginSwitch       = By.xpath("//android.widget.ImageView[@content-desc=\"Sign In With Phone Number\"]");

    // Validation messages
    private final By emailValidation        = By.xpath("//android.view.View[@content-desc=\"Please enter an email address\"]");
    private final By phoneValidation        = By.xpath("//android.view.View[@content-desc=\"Please enter a phone number\"]");
    private final By passwordValidation     = By.xpath("//android.view.View[@content-desc=\"Please enter a valid password!\"]");
    private final By invalidEmailCheck      = By.xpath("//android.view.View[@content-desc=\"Please enter a valid email address\"]");
    private final By invalidPhoneCheck      = By.xpath("//android.view.View[@content-desc=\"Please enter a valid phone number\"]");
    private final By invalidPasswordCheck   = By.xpath("//android.view.View[@content-desc=\"Password: 8 characters, uppercase, lowercase, number, special character\"]");

    // ---------------- Actions ----------------
    public void enterEmail(String email, ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Entering email: " + email);
        click(usernameField, extentTest);
        type(usernameField, email, extentTest);
    }

    public void enterPhone(String phone, ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Entering phone: " + phone);
        click(usernameField, extentTest);
        type(usernameField, phone, extentTest);
    }

    public void enterPassword(String password, ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Entering password: " + password);
        click(passwordField, extentTest);
        type(passwordField, password, extentTest);
    }

    public void tapLogin(ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Clicking Login button.");
        click(loginBtn, extentTest);
    }

    public boolean isLoginButtonVisible(ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Checking if Login button is visible.");
        return isDisplayed(loginBtn, extentTest);
    }

    public void loginWithEmail(String email, String password, ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Performing login with email: " + email);
        enterEmail(email, extentTest);
        enterPassword(password, extentTest);
        tapLogin(extentTest);
    }

    public void loginWithPhone(String phone, String password, ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Performing login with phone: " + phone);
        enterPhone(phone, extentTest);
        enterPassword(password, extentTest);
        tapLogin(extentTest);
    }

    // ---------------- Validation ----------------
    public String getSnackBarOrToastMessage(String expectedSubstring, int timeoutSeconds) {
        // 1. Try direct UI element (preferred)
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.xpath("//*[contains(@text,'" + expectedSubstring + "')]")
                ));
            return element.getText();
        } catch (Exception uiMiss) {
            // 2. Fallback: Search logcat
            String logMessage = LogcatUtils.getLogContaining(expectedSubstring, timeoutSeconds);
            if (logMessage != null) {
                return logMessage;
            }
        }
        return null;
    }

    public String getLoginSuccessText() {
        return getText(loginSuccessCheck);
    }

    public String getEmailValidationText() {
        return getText(emailValidation);
    }

    public String getPhoneValidationText() {
        return getText(phoneValidation);
    }

    public String getPasswordValidationText() {
        return getText(passwordValidation);
    }

    public String getInvalidEmailValidationText() {
        return getText(invalidEmailCheck);
    }

    public String getInvalidPhoneValidationText() {
        return getText(invalidPhoneCheck);
    }

    public String getInvalidPasswordValidationText() {
        return getText(invalidPasswordCheck);
    }

    // ---------------- Utility ----------------
    public void switchLoginMode(ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Switching Login Mode.");
        click(loginSwitch, extentTest);
    }
}
