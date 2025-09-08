package com.automation.utils;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

import java.util.Objects;

/**
 * Utility to read Android logcat entries via Appium driver.
 * Uses raw "logcat" name (no LogType.LOGCAT enum).
 */
public class LogcatUtils {

    private static final long DEFAULT_POLL_INTERVAL_MS = 400L;

    /**
     * Clears current logcat buffer by reading and discarding existing entries.
     * Safe to call before performing an action that will emit new log messages.
     */
    public static void clearLogcat() {
        try {
            if (DriverManager.getDriver() != null) {
                // read and discard
                DriverManager.getDriver().manage().logs().get("logcat").getAll();
            }
        } catch (Exception ignored) {
            // ignore: some setups may not expose logcat immediately
        }
    }

    /**
     * Polls logcat until a log entry containing `keyword` appears or timeout elapses.
     * Returns the first full log message containing the keyword (original case), or null if not found.
     *
     * @param keyword        substring to search for (case-insensitive)
     * @param timeoutSeconds max seconds to wait
     */
    public static String getLogContaining(String keyword, int timeoutSeconds) {
        if (keyword == null || keyword.isEmpty()) return null;
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        String lowerKeyword = keyword.toLowerCase();

        while (System.currentTimeMillis() < end) {
            try {
                if (DriverManager.getDriver() == null) break;
                LogEntries entries = DriverManager.getDriver().manage().logs().get("logcat"); // raw "logcat"
                for (LogEntry entry : entries) {
                    String msg = entry.getMessage();
                    if (msg == null) continue;
                    if (msg.toLowerCase().contains(lowerKeyword)) {
                        return msg;
                    }
                }
            } catch (Exception ignored) {
                // occasionally driver.manage().logs() may throw transient exceptions; ignore and retry until timeout
            }

            try {
                Thread.sleep(DEFAULT_POLL_INTERVAL_MS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return null;
    }
}
