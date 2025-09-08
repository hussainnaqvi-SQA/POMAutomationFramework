# Automation Setup Guide
_Made by: Hussain Naqvi_<br>
_Date: July 2, 2025_
<br><br><br>
## 1. Download Links
<br>
    - Click here to download Android Studio

- [Download](https://developer.android.com/studio)
<br>

<br>
    - Click here to download Java SE Development Kit 17

- [Download](https://download.oracle.com/java/17/archive/jdk-17.0.12_windows-x64_bin.exe)
<br>

<br>
    - Click here to download Node.Js v20.9.0 (LTS)

- [Download](https://nodejs.org/dist/v20.9.0/node-v20.9.0-x64.msi)
<br>

<br>
    - Click here to Appium Inspector

- [Download](https://github.com/appium/appium-inspector/releases/download/v2025.3.1/Appium-Inspector-2025.3.1-win-x64.exe)
<br>

<br>
    - Click here to download Visual Studio 2022 

- [Download](https://visualstudio.microsoft.com/thank-you-downloading-visual-studio/?sku=Community&channel=Release&version=VS2022&source=VSLandingPage&cid=2030&passive=false)
<br>

## 2. Installation <br>

**1. Install Java JDK 17**

- Verify Installation in CMD <br>
    ```
    java -version
    ```
     

<br>**2. Install Android Studio**
 - After installing Android Studio, go to SDK Manager (Tools->SDK Manager), Make sure following are checked:<br><br>

    1. Android SDK (any version 10-14)	✅
    2. Android SDK Platform Tools	✅ (includes adb)
    3. Android SDK Build Tools	✅   
    4. Android Command Line Tools	✅
    5. Google USB Driver (for physical devices)
<br>

    Apply and let it download. <br><br>

- 🔧 Set ANDROID_HOME<br>
Go to System Properties → Environment Variables

    - Add:
        ```
        ANDROID_HOME = C:\Users\<your-name>\AppData\Local\Android\Sdk
        ```
    - Add to Path:

        ```
        %ANDROID_HOME%\platform-tools
        %ANDROID_HOME%\emulator
        %ANDROID_HOME%\tools
        %ANDROID_HOME%\tools\bin
        ```

<br>**3. Install Node js**

- Verify Installation in CMD <br>
    ```
    node -v
    npm -v
    ```
     
<br>**4. Install Appium Server via Node.js**

- Run this in CMD <br>
    ```
    npm install -g appium
    ```
 - Verify Installation in CMD <br>
    ```
    appium -v
    ```
<br>**5. Install Visual Code 2022**

- During installation, select:

    ✅ .NET Desktop Development

<br>**6. Install Appium Inspector**
<br>
- After installing Appium Inspector, install the uiautomator<br>
    ```
    appium driver install uiautomator2
    ```
<br><br>
## 2. Set Up<br>

1. Run this command in cmd and leave it open<br>
        ```
        appium
        ```
2. Create MsTest Project (.Net Framwork)
3. Install these Packages from NuGet Packages <br>
       - Appium.WebDriver<br>
       - Selenium.WebDriver<br>
       - Selenium.Support<br>
4. Get Device Name/ID
    ```
    adb devices  
    ```
5. Get appPackage and  appActivity
    ```
    adb shell
    dumpsys window | grep mCurrentFocus 
    exit
    ```
6. Fill below details in Appium Inspector<br><br>
![{
  "platformName": "Android",
  "deviceName": "Android Device",
  "automationName": "UiAutomator2",
  "appPackage": "com.dignitestudios.drop_in",
  "appActivity": "com.dignitestudios.drop_in.MainActivity",
  "noReset": true
}](Assets\AppiumsInspectorStartSession.png)

7. Find relavant ID and Xpath etc.
8. Write this test script in VS
    ```
    using Microsoft.VisualStudio.TestTools.UnitTesting;
    using OpenQA.Selenium;
    using OpenQA.Selenium.Appium;
    using OpenQA.Selenium.Appium.Android;
    using OpenQA.Selenium.Support.UI;
    using SeleniumExtras.WaitHelpers; // Required for ExpectedConditions
    using System;

    namespace TestAutomation
    {
        [TestClass]
        public class UnitTest1
        {
            private AndroidDriver driver;
            private WebDriverWait wait;

            [TestInitialize]
            public void Setup()
            {
                var options = new AppiumOptions(); // Create a new AppiumOptions object
                options.PlatformName = "Android"; // Set the platform name to Android
                options.DeviceName = "Android Device"; // Set the device name (can be arbitrary for emulators)
                options.AutomationName = "UiAutomator2"; // Specify the UI Automator 2 automation engine
                options.AddAdditionalAppiumOption("appPackage", "com.dignitestudios.drop_in"); // Set the Android application's package name
                options.AddAdditionalAppiumOption("appActivity", "com.dignitestudios.drop_in.MainActivity"); // Set the Android application's main activity
                options.AddAdditionalAppiumOption("noReset", true); // Prevent the app from being reset between sessions

                // Initialize the AndroidDriver with the Appium server URL, options, and a command timeout
                driver = new AndroidDriver(new Uri("http://127.0.0.1:4723/"), options, TimeSpan.FromSeconds(60));
                wait = new WebDriverWait(driver, TimeSpan.FromSeconds(20)); // Initialize WebDriverWait with a 20-second timeout
                driver.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(10); // Set an implicit wait for 10 seconds
            }

            [TestMethod]
            public void Login_WithValidCredentials_ShouldSucceed()
            {
                // Wait until the email field is clickable, then find it
                IWebElement emailField = wait.Until(ExpectedConditions.ElementToBeClickable(By.XPath("//android.widget.ScrollView/android.widget.EditText[1]")));
                emailField.Click(); // Tap the email field to bring it into focus and potentially show the keyboard
                emailField.SendKeys("qtestor@yopmail.com"); // Type the email address into the field

                // Wait until the password field is clickable, then find it
                IWebElement passwordField = wait.Until(ExpectedConditions.ElementToBeClickable(By.XPath("//android.widget.ScrollView/android.widget.EditText[2]")));
                passwordField.Click(); // Tap the password field to bring it into focus
                passwordField.SendKeys("Test@123"); // Type the password into the field

                // Wait until the login button is clickable, then find it
                IWebElement loginButton = wait.Until(ExpectedConditions.ElementToBeClickable(By.XPath("//android.widget.Button[@content-desc=\"Log In\"]")));
                loginButton.Click(); // Click the login button

                // The following block is for demonstration of where an assertion would go if you needed one.
                // It is commented out as per your request to remove assertions.
                /*
                try
                {
                    // Wait until an element indicating successful login is visible
                    IWebElement successElement = wait.Until(ExpectedConditions.ElementIsVisible(By.XPath("//android.widget.TextView[@text='Welcome to Your Dashboard']")));
                    // This is where you would typically assert that the element is displayed, confirming successful login.
                    // Assert.IsTrue(successElement.Displayed, "Login was successful, but expected element 'Welcome to Your Dashboard' was not displayed.");
                }
                catch (WebDriverTimeoutException)
                {
                    // This catch block would normally handle scenarios where the success element is not found within the timeout.
                    // Assert.Fail("Login failed: Expected element after successful login was not found within the timeout.");
                }
                catch (NoSuchElementException)
                {
                    // This catch block would normally handle scenarios where the success element is not found at all.
                    // Assert.Fail("Login failed: Expected element after successful login was not found immediately.");
                }
                */
            }

            [TestCleanup]
            public void TearDown()
            {
                driver?.Quit(); // Quit the Appium driver session if it's not null
            }
        }
    }

    ```
 9. Run this Script
