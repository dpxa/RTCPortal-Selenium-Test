package org.rtcportal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Main {

    public static void main(String[] args) {
        // Set the path to your ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\WebDriver\\bin\\chromedriver.exe");

        // Create ChromeOptions instance
        ChromeOptions options = new ChromeOptions();

        // Set the path to your Brave browser executable
        options.setBinary("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");
        // options.addArguments("--headless"); // Optional: run in headless mode

        // Initialize ChromeDriver with Brave browser options
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Explicit wait for up to 10 seconds

        try {
            // Navigate to the URL
            String url = "https://dpxa.github.io/RTCPortal/";
            driver.get(url);
            System.out.println("Navigated to: " + url);

            // 1. Verify the page title
            String expectedTitle = "RTCPortal - P2P File Sharing";
            // Wait for the title to be correct, as it might take a moment for JS to set it
            wait.until(ExpectedConditions.titleIs(expectedTitle));
            String actualTitle = driver.getTitle();
            System.out.println("Page Title: " + actualTitle);
            if (expectedTitle.equals(actualTitle)) {
                System.out.println("Title verification PASSED!");
            } else {
                System.out.println("Title verification FAILED. Expected: '" + expectedTitle + "', but got: '" + actualTitle + "'");
            }

            // 2. Locate and get text from "Your ID" display
            // Wait for the element to be visible and contain some text (as it's dynamically populated)
            WebElement myIdDisplay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myIdDisplay")));
            wait.until(driverLambda -> !myIdDisplay.getText().equals("Waiting for ID") && !myIdDisplay.getText().isEmpty()); // Wait for ID to be generated
            System.out.println("Your ID Display Text: " + myIdDisplay.getText());
            if (!myIdDisplay.getText().equals("Waiting for ID") && !myIdDisplay.getText().isEmpty()) {
                System.out.println("'Your ID' display text has been generated.");
            } else {
                System.out.println("'Your ID' display text is still 'Waiting for ID' or empty.");
            }

            // 3. Locate the "Enter peer ID..." input field and type into it
            WebElement partnerIdField = driver.findElement(By.id("partnerIdField"));
            partnerIdField.sendKeys("testPeerLive123");
            System.out.println("Entered 'testPeerLive123' into partner ID field. Current value: " + partnerIdField.getAttribute("value"));

            // 4. Locate the "Connect" button and check if it's enabled
            // The button becomes enabled after an ID is generated and a peer ID is entered.
            // For this test, we'll just check its initial state after typing in the peer ID field.
            WebElement connectButton = driver.findElement(By.id("connectTrigger"));
            boolean isConnectButtonEnabled = connectButton.isEnabled();
            System.out.println("Connect button enabled (after typing peer ID): " + isConnectButtonEnabled);
            // Note: The connect button's enabled state depends on JavaScript logic (ID generation and peer ID input).
            // If an ID is generated and text is in partnerIdField, it should be enabled.

            // 5. Example: Find the "GitHub" link by its class name
            WebElement githubLink = driver.findElement(By.className("repo-link"));
            System.out.println("Found GitHub link with text: '" + githubLink.getText() + "' and href: " + githubLink.getAttribute("href"));

            System.out.println("\nLive site interactions test completed.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            if (driver != null) {
                // Add a small delay before quitting to see the browser actions if not headless
                try {
                    Thread.sleep(2000); // 2 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                driver.quit();
            }
        }
    }
}
