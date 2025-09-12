package com.automation.reports;

import com.aventstack.extentreports.ExtentTest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ExtentTestManager {
    private static final Map<Long, ExtentTest> extentTestMap = new ConcurrentHashMap<>();

    public static ExtentTest getTest() {
        return extentTestMap.get(Thread.currentThread().getId());
    }

    public static void setTest(ExtentTest test) {
        extentTestMap.put(Thread.currentThread().getId(), test);
    }

    public static void removeTest() {
        extentTestMap.remove(Thread.currentThread().getId());
    }
}
