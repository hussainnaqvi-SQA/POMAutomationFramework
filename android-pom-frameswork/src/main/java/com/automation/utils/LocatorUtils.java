package com.automation.utils;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

/**
 * Utility class for handling locators in both Flutter and native contexts.
 * - If flutterTestId is provided → use MobileBy.custom("flutter:<id>")
 * - Otherwise → fallback to native strategies like accessibilityId or xpath.
 */
public class LocatorUtils {

    /**
     * Use for Flutter widgets with a test key or fallback to accessibilityId.
     */
    public static By byTestId(String flutterTestId, String accessibilityId) {
        if (flutterTestId != null && !flutterTestId.isBlank()) {
            return MobileBy.custom("flutter:" + flutterTestId);
        } else {
            return MobileBy.accessibilityId(accessibilityId);
        }
    }

    /**
     * Use for text lookup. Flutter side uses custom locator, native uses xpath.
     */
    public static By byText(String text) {
        return MobileBy.custom("flutter:text=" + text); // works with flutter driver
    }

    /**
     * Use for raw xpath in native apps.
     */
    public static By byXpath(String xpath) {
        return By.xpath(xpath);
    }

    /**
     * Use for resource-id in native apps.
     */
    public static By byId(String id) {
        return By.id(id);
    }
}
