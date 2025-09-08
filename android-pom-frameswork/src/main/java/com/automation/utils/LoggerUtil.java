package com.automation.utils;

import com.aventstack.extentreports.ExtentTest;
import com.automation.reports.ExtentTestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void logInfo(Logger logger, String message) {
        logger.info(message);
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.info(message);
        }
    }

    public static void logError(Logger logger, String message) {
        logger.error(message);
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.fail(message);
        }
    }

    public static void logPass(Logger logger, String message) {
        logger.info("[PASS] " + message);
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.pass(message);
        }
    }
}
