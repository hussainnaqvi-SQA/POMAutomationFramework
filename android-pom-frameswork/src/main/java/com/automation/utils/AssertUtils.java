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
	        LoggerUtil.logInfo(LoggerUtil.getLogger(AssertUtils.class), "✅ " + message);
	    }
	}


	    public static void assertEquals(String actual, String expected, String message, ExtentTest extentTest) {
	        try {
	            Assert.assertEquals(actual, expected, message);
	            String infoMsg = "✅ " + message + " | Expected: " + expected + " | Actual: " + actual;
	            LoggerUtil.logInfo(LoggerUtil.getLogger(AssertUtils.class), infoMsg);
	        } catch (AssertionError e) {
	            String failMsg = "❌ " + message + " | Expected: " + expected + " but got: " + actual;
	            LoggerUtil.logInfo(LoggerUtil.getLogger(AssertUtils.class), failMsg);
	            throw e; // TestNG will mark test as FAIL
	        }
	    }

	    public static void assertContains(String actual, String expectedSubstring, String message, ExtentTest extentTest) {
	        try {
	            if (actual == null || !actual.contains(expectedSubstring)) {
	                throw new AssertionError("Expected text to contain: '" + expectedSubstring + "', but got: '" + actual + "'");
	            }
	            String infoMsg = "✅ " + message + " | Found substring: " + expectedSubstring;
	            LoggerUtil.logInfo(LoggerUtil.getLogger(AssertUtils.class), infoMsg);
	        } catch (AssertionError e) {
	            String failMsg = "❌ " + message + " | Expected substring: " + expectedSubstring + " | Actual: " + actual;
	            LoggerUtil.logInfo(LoggerUtil.getLogger(AssertUtils.class), failMsg);
	            throw e;
	        }
	    }
	




}
