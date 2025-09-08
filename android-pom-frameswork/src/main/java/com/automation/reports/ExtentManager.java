package com.automation.reports;

import com.automation.utils.ConfigReader;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    private static String reportPath; // Store path so we can log it later

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static void createInstance() {
        try {
            // Create /reports folder if not exists
            String reportsDir = System.getProperty("user.dir") + "/reports";
            new File(reportsDir).mkdirs();

            // Create file name with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportName = ConfigReader.get("projectName") + "_TestReport_" + timestamp + ".html";
            reportPath = reportsDir + "/" + reportName;

            // Create reporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK); // 🌙 Dark mode
            sparkReporter.config().setDocumentTitle(ConfigReader.get("projectName") + " Automation Report");
            sparkReporter.config().setReportName(ConfigReader.get("projectName") + " Test Execution");

            // Create ExtentReports instance
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Add metadata
            extent.setSystemInfo("Project", ConfigReader.get("projectName"));
            extent.setSystemInfo("Environment", ConfigReader.get("environment"));
            extent.setSystemInfo("Tester", ConfigReader.get("testerName"));

            System.out.println("✅ Extent report will be generated at: " + reportPath);

        } catch (Exception e) {
            System.err.println("❌ Failed to initialize Extent Report: " + e.getMessage());
        }
    }

    static {
        // Add shutdown hook to always flush the report even if suite crashes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (extent != null) {
                    extent.flush();
                    System.out.println("📄 Extent report saved to: " + reportPath);
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to flush Extent Report: " + e.getMessage());
            }
        }));
    }
}
