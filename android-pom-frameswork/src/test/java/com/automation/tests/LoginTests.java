package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.utils.AssertUtils;
import com.automation.utils.CSVDataProvider;
import com.automation.utils.DriverManager;
import com.automation.utils.LoggerUtil;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginTests extends BaseTest {

    private static final Logger log = LoggerUtil.getLogger(LoginTests.class);
    private LoginPage loginPage;

    // ---------------- Test Setup ----------------
    @BeforeMethod
    public void initPage() {
        System.out.println("Page sees driver hash: " + DriverManager.getDriver().hashCode());
        loginPage = new LoginPage();
    }

    // ---------------- Positive Tests ----------------
    @Test(dataProvider = "loginData", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "positive"})
    public void PositiveTest_LoginUsingEmail(Map<String, String> data) {
        String email = data.get("email");
        String password = data.get("password");
        LoggerUtil.logInfo(log, "===== Starting Login Test (Email): " + email + " =====");

        ensureLoginButtonVisible();
        loginPage.loginWithEmail(email, password, getExtentTest());

        AssertUtils.assertEquals(
            loginPage.getLoginSuccessText(),
            "Two Step Authentication",
            "Login success validation",
            getExtentTest()
        );
    }

    @Test(dataProvider = "loginData", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "positive"})
    public void PositiveTest_LoginUsingPhone(Map<String, String> data) {
        String phone = data.get("phone");
        String password = data.get("password");
        LoggerUtil.logInfo(log, "===== Starting Login Test (Phone): " + phone + " =====");

        ensureLoginButtonVisible();
        loginPage.switchLoginMode(getExtentTest());
        loginPage.loginWithPhone(phone, password, getExtentTest());

        AssertUtils.assertEquals(
            loginPage.getLoginSuccessText(),
            "Two Step Authentication",
            "Login success validation",
            getExtentTest()
        );
    }

    // ---------------- Negative Tests ----------------
    @Test(dataProvider = "loginData", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "negative"})
    public void NegativeTest_LoginWithEmptyEmail(Map<String, String> data) {
        String password = data.get("password");
        LoggerUtil.logInfo(log, "===== Negative Test: Empty Email =====");

        ensureLoginButtonVisible();
        loginPage.loginWithEmail("", password, getExtentTest());

        AssertUtils.assertEquals(
            loginPage.getEmailValidationText(),
            "Please enter an email address",
            "Email validation message",
            getExtentTest()
        );
    }

    @Test(dataProvider = "loginData", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "negative"})
    public void NegativeTest_LoginWithEmptyPhone(Map<String, String> data) {
        String password = data.get("password");
        LoggerUtil.logInfo(log, "===== Negative Test: Empty Phone =====");

        ensureLoginButtonVisible();
        loginPage.switchLoginMode(getExtentTest());
        loginPage.loginWithPhone("", password, getExtentTest());

        AssertUtils.assertEquals(
            loginPage.getPhoneValidationText(),
            "Please enter a phone number",
            "Phone validation message",
            getExtentTest()
        );
    }

    @Test(dataProvider = "loginData", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "negative"})
    public void NegativeTest_LoginWithValidEmailAndEmptyPassword(Map<String, String> data) {
        String email = data.get("email");
        LoggerUtil.logInfo(log, "===== Negative Test: Valid Email + Empty Password =====");

        ensureLoginButtonVisible();
        loginPage.loginWithEmail(email, "", getExtentTest());

        AssertUtils.assertEquals(
            loginPage.getPasswordValidationText(),
            "Please enter a valid password!",
            "Password validation message",
            getExtentTest()
        );
    }

    @Test(dataProvider = "loginData", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "negative"})
    public void NegativeTest_LoginWithValidPhoneAndEmptyPassword(Map<String, String> data) {
        String phone = data.get("phone");
        LoggerUtil.logInfo(log, "===== Negative Test: Valid Phone + Empty Password =====");

        ensureLoginButtonVisible();
        loginPage.switchLoginMode(getExtentTest());
        loginPage.loginWithPhone(phone, "", getExtentTest());

        AssertUtils.assertEquals(
            loginPage.getPasswordValidationText(),
            "Please enter a valid password!",
            "Password validation message",
            getExtentTest()
        );
    }

    @Test(dataProvider = "InvalidEmails", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "negative"})
    public void NegativeTest_LoginWithInvalidEmail(Map<String, String> data) {
        String email = data.get("email");
        String password = data.get("password");
        LoggerUtil.logInfo(log, "===== Negative Test: Invalid Email (" + email + ") =====");

        ensureLoginButtonVisible();
        loginPage.loginWithEmail(email, password, getExtentTest());

        AssertUtils.assertEquals(
            loginPage.getInvalidEmailValidationText(),
            "Please enter a valid email address",
            "Invalid Email Validation",
            getExtentTest()
        );
    }

    @Test(dataProvider = "InvalidPhoneNumbers", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "negative"})
    public void NegativeTest_LoginWithInvalidPhone(Map<String, String> data) {
        String phone = data.get("phone");
        String password = data.get("password");
        LoggerUtil.logInfo(log, "===== Negative Test: Invalid Phone (" + phone + ") =====");

        ensureLoginButtonVisible();
        loginPage.switchLoginMode(getExtentTest());
        loginPage.loginWithPhone(phone, password, getExtentTest());

        AssertUtils.assertEquals(
            loginPage.getInvalidPhoneValidationText(),
            "Please enter a valid phone number",
            "Invalid Phone Validation",
            getExtentTest()
        );
    }

    @Test(dataProvider = "InvalidPasswords", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "smoke", "negative"})
    public void NegativeTest_LoginWithInvalidPassword(Map<String, String> data) {
        String email = data.get("email");
        String phone = data.get("phone");
        String password = data.get("password");
        LoggerUtil.logInfo(log, "===== Negative Test: Invalid Password (" + password + ") =====");

        ensureLoginButtonVisible();
        if (email != null && !email.isBlank()) {
            loginPage.loginWithEmail(email, password, getExtentTest());
        } else if (phone != null && !phone.isBlank()) {
            loginPage.switchLoginMode(getExtentTest());
            loginPage.loginWithPhone(phone, password, getExtentTest());
        }

        AssertUtils.assertEquals(
            loginPage.getInvalidPasswordValidationText(),
            "Password: 8 characters, uppercase, lowercase, number, special character",
            "Invalid Password Validation",
            getExtentTest()
        );
    }

    @Test(dataProvider = "InvalidCredentials", dataProviderClass = CSVDataProvider.class,
          groups = {"login", "negative"})
    public void NegativeTest_LoginWithWrongCredential(Map<String, String> data) {
        String email = data.get("email");
        String phone = data.get("phone");
        String password = data.get("password");
        String expected = data.get("assert");

        LoggerUtil.logInfo(log, "===== Negative Test: Wrong Credentials =====");

        ensureLoginButtonVisible();
        if (email != null && !email.isBlank()) {
            loginPage.loginWithEmail(email, password, getExtentTest());
        } else if (phone != null && !phone.isBlank()) {
            loginPage.switchLoginMode(getExtentTest());
            loginPage.loginWithPhone(phone, password, getExtentTest());
        }

        String actualMessage = loginPage.getInvalidCredentialsMessage();

        AssertUtils.assertContains(
            actualMessage,
            expected,
            "Snackbar/Toast validation via mitmproxy",
            getExtentTest()
        );
    }

    // ---------------- Utility ----------------
    private void ensureLoginButtonVisible() {
        AssertUtils.assertEquals(
            String.valueOf(loginPage.isLoginButtonVisible(getExtentTest())),
            "true",
            "Login button should be visible",
            getExtentTest()
        );
    }
}
