package com.automation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LocatorUtils {

    public static WebElement findById(String id) {
        return DriverManager.getDriver().findElement(By.id(id));
    }

    public static WebElement findByText(String text) {
        return DriverManager.getDriver().findElement(By.xpath("//*[@text='" + text + "']"));
    }

    public static WebElement findByXpath(String xpath) {
        return DriverManager.getDriver().findElement(By.xpath(xpath));
    }
}
