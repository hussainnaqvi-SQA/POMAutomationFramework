package com.automation.base;

import com.automation.reports.ExtentManager;
import com.automation.reports.ExtentTestManager;
import com.automation.utils.ConfigReader;
import com.automation.utils.DriverManager;
import com.automation.utils.LoggerUtil;
import com.automation.utils.ScreenshotUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class BaseTest {
    protected static ExtentReports extent;

    public ExtentTest getExtentTest() {
        return ExtentTestManager.getTest();
    }

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        extent = ExtentManager.getInstance();
        if (extent == null) {
            throw new RuntimeException("❌ Extent Report initialization failed!");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        if (extent != null) {
            extent.flush();
            System.out.println("📄 Final Extent report flushed.");
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setupTest(Method method) {
        if (extent == null) {
            extent = ExtentManager.getInstance(); // fallback safety net
        }

        ExtentTest test = extent.createTest(method.getName());
        ExtentTestManager.setTest(test);

        LoggerUtil.logInfo(LoggerUtil.getLogger(getClass()), "===== Starting Test: " + method.getName() + " =====");
        test.info("Starting Test: " + method.getName());

        DriverManager.quitDriver();
        DriverManager.initDriver();

        AppiumDriver driver = DriverManager.getDriver();

        try {
            InteractsWithApps appDriver = (InteractsWithApps) driver;
            String appPackage = ConfigReader.getOrDefault("appPackage", "");
            if (!appPackage.isEmpty()) {
                appDriver.terminateApp(appPackage);
                appDriver.activateApp(appPackage);
                LoggerUtil.logInfo(LoggerUtil.getLogger(getClass()), "App restarted successfully.");
            } else {
                LoggerUtil.logInfo(LoggerUtil.getLogger(getClass()), "⚠️ Skipping app restart, appPackage missing.");
            }
        } catch (Exception e) {
            LoggerUtil.logError(LoggerUtil.getLogger(getClass()), "⚠️ Failed to reset app: " + e.getMessage());
        }

        System.out.println("Driver hash: " + DriverManager.getDriver().hashCode());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();

        if (test != null) {
            switch (result.getStatus()) {
                case ITestResult.FAILURE:
                    String shortMessage = (result.getThrowable() != null && result.getThrowable().getMessage() != null)
                            ? result.getThrowable().getMessage().split("\n")[0]
                            : "Test Failed";
                    test.fail("❌ " + shortMessage);
                    String screenshotPath = ScreenshotUtils.captureScreenshot(result.getName());
                    if (screenshotPath != null) {
                        test.addScreenCaptureFromPath(screenshotPath);
                    }
                    break;

                case ITestResult.SKIP:
                    test.skip("⚠️ Test Skipped");
                    break;

                case ITestResult.SUCCESS:
                    test.pass("✅ Test Passed");
                    break;
            }
        }

        DriverManager.quitDriver();
        if (extent != null) extent.flush();
        ExtentTestManager.removeTest();
    }
}
