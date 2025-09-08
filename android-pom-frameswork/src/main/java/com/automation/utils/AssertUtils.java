package com.automation.utils;

import com.automation.reports.ExtentTestManager;
import com.aventstack.extentreports.ExtentTest;
import org.testng.Assert;

public class AssertUtils {

	public static void assertTrue(boolean condition, String message) {
	    if (!condition) {
	        LoggerUtil.logError(LoggerUtil.getLogger(AssertUtils.class), "❌ Assertion failed: " + message);
	        Assert.fail("Assertion failed: " + message);
	    } else {
	        LoggerUtil.logPass(LoggerUtil.getLogger(AssertUtils.class), "✅ " + message);
	    }
	}

	public static void assertEquals(String actual, String expected, String message, ExtentTest extentTest) {
	    try {
	        Assert.assertEquals(actual, expected, message);
	        if (extentTest != null) {
	            extentTest.info("Validation passed: " + message +
	                            " | Expected: " + expected + " | Actual: " + actual);
	        }
	    } catch (AssertionError e) {
	        if (extentTest != null) {
	            extentTest.fail("❌ Assertion failed: " + message +
	                            " | Expected: " + expected + " but got: " + actual);
	        }
	        throw e;
	    }
	}
	public static void assertContains(String actual, String expectedSubstring, String message, ExtentTest extentTest) {
	    try {
	        if (actual == null || !actual.contains(expectedSubstring)) {
	            throw new AssertionError("Expected text to contain: '" + expectedSubstring + "', but got: '" + actual + "'");
	        }
	        LoggerUtil.logPass(LoggerUtil.getLogger(AssertUtils.class), "✅ " + message + " | Found substring: " + expectedSubstring);
	        if (extentTest != null) {
	            extentTest.info("✅ " + message + " | Found substring: " + expectedSubstring);
	        }
	    } catch (AssertionError e) {
	        LoggerUtil.logInfo(LoggerUtil.getLogger(AssertUtils.class), "❌ Assertion failed: " + message + " | Expected substring: " + expectedSubstring + " | Actual: " + actual);
	        if (extentTest != null) {
	            extentTest.info("❌ " + message + " | Expected substring: " + expectedSubstring + " | Actual: " + actual);
	        }
	        throw e;
	    }
	}


}
