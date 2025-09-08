package com.automation.pages;

import com.automation.base.BasePage;
import com.automation.utils.LocatorUtils;
import com.automation.utils.LoggerUtil;
import com.automation.utils.MitmproxyClient;
import com.aventstack.extentreports.ExtentTest;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.slf4j.Logger;

public class LoginPage extends BasePage {
    private AppiumDriver driver;
    private static final Logger log = LoggerUtil.getLogger(LoginPage.class);

    // ---------------- Locators ----------------
    // Using LocatorUtils to support Flutter + Native
    private final By usernameField     = LocatorUtils.byTestId("usernameField", "username_input");
    private final By passwordField     = LocatorUtils.byTestId("passwordField", "password_input");
    private final By loginBtn          = LocatorUtils.byTestId("loginButton", "login_button");
    private final By loginSuccessCheck = LocatorUtils.byText("Two Step Authentication");
    private final By loginSwitch       = LocatorUtils.byTestId("loginSwitch", "sign_in_with_phone");

    // Validation messages
    private final By emailValidation         = LocatorUtils.byText("Please enter an email address");
    private final By phoneValidation         = LocatorUtils.byText("Please enter an email address");
    private final By passwordValidation      = LocatorUtils.byText("Please enter a valid password!");
    private final By invalidEmailCheck       = LocatorUtils.byText("Please enter a valid email address");
    private final By invalidPhoneCheck       = LocatorUtils.byText("Please enter a valid email address");
    private final By invalidPasswordCheck    = LocatorUtils.byText("Password: 8 characters, uppercase, lowercase, number, special character");

    // ---------------- Actions ----------------
    public void enterEmail(String email, ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Entering email: " + email);
        click(usernameField, extentTest);
        type(usernameField, email, extentTest);
    }

    public void enterPhone(String phone, ExtentTest extentTest) {
        LoggerUtil.logInfo(log, "Entering phone number: " + phone);
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
        LoggerUtil.logInfo(log, "Performing login with phone number: " + phone);
        enterPhone(phone, extentTest);
        enterPassword(password, extentTest);
        tapLogin(extentTest);
    }

    // ---------------- Validation ----------------
    public String getInvalidCredentialsMessage() {
        return MitmproxyClient.waitForMessage("Invalid credentials", 6);
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
