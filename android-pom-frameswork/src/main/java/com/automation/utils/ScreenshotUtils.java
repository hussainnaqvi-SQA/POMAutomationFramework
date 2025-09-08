package com.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    public static String captureScreenshot(String screenshotName) {
        try {
        	 if (DriverManager.getDriver() == null) {
                 System.err.println("❌ Screenshot failed: Driver is null or already quit.");
                 return null;
             }
            File src = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";

            String destDir = System.getProperty("user.dir") + "/reports/screenshots/";
            File destFile = new File(destDir + fileName);

            destFile.getParentFile().mkdirs();
            Files.copy(src.toPath(), destFile.toPath());

            // ✅ Return relative path for Extent (important)
            return "screenshots/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
