package FinalProject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Suunto {
    public static void crawl() {
        // Set the path to the ChromeDriver executable
        WebDriver driver = new ChromeDriver();

        // Create a list to hold product details for CSV (with column headers)
        List<String[]> productData = new ArrayList<>();
        productData.add(new String[]{"Product Title", "Price", "Case Material", "Dimensions", "Features"});

        try {
            // Open the Suunto website and maximize the window
            driver.get("https://www.suunto.com/en-us/");
            driver.manage().window().maximize();

            // Set up an explicit wait to handle dynamic page elements
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Close popups and reject cookie consent if present
            handlePopupsAndCookies(driver, wait);

            // Wait for the main categories section to load and become visible
            WebElement categoriesBlock = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.categories-block__grid")));

            // Find the "Watches" link inside the categories block
            WebElement watchesLink = categoriesBlock.findElement(By.cssSelector("a[href='/en-us/Product-search/See-all-Sports-Watches/']"));

            // Scroll the page to ensure the "Watches" link is in view before clicking
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", watchesLink);

            // Attempt to click the "Watches" link (fall back to JS click if normal click fails)
            try {
                watchesLink.click();
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                System.out.println("Standard click failed, using JavaScript to click...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", watchesLink);
            }

            System.out.println("Successfully clicked on the 'Watches' link!");
            Thread.sleep(3000); // Optional pause to allow the next page to load

            // Confirm navigation to the correct page
            System.out.println("Navigated to: " + driver.getCurrentUrl());

            // Wait until the product grid section is visible
            WebElement productList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.product-anchor-list__carousel")));

            // Collect product names and their respective links
            List<WebElement> productNameElements = driver.findElements(By.cssSelector("p.product-listing-item-card__product-name"));
            List<WebElement> productLinkElements = driver.findElements(By.cssSelector("div.u-flex-1 a.btn.btn--secondary.u-width-100"));

            // Use a set to ensure we don't process duplicate products
            Set<String> uniqueProductNames = new HashSet<>();
            List<String> uniqueProductLinks = new ArrayList<>();

            // Loop through product names and links
            for (int i = 0; i < productNameElements.size(); i++) {
                String productName = productNameElements.get(i).getText().trim();
                String productLink = productLinkElements.get(i).getAttribute("href");

                // Add only unique product names and their links
                if (!uniqueProductNames.contains(productName)) {
                    uniqueProductNames.add(productName);
                    uniqueProductLinks.add(productLink);
                }
            }

            // Show the count of unique products
            System.out.println("Total Unique Products Found: " + uniqueProductLinks.size());

            // Visit each product page to extract details
            for (String url : uniqueProductLinks) {
                System.out.println("Navigating to: " + url);
                driver.get(url);

                try {
                    // Locate and extract product details
                    WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.md-f-30.xs-f-22.f-extra-bold.f-uppercase.m-v-0")));
                    String title = productTitle.getText();

                    // Extract price (or display if not available)
                    String price = "";
                    try {
                        WebElement productPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#price")));
                        price = productPrice.getText();
                    } catch (Exception e) {
                        price = "Price not listed";
                    }

                    // Extract case material info
                    String caseMaterial = "";
                    try {
                        WebElement caseMaterialRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                                "//table[@class='table table-borderless table-striped m-b-0']//th[text()='Case material:']/following-sibling::td")));
                        caseMaterial = caseMaterialRow.getText().trim();
                    } catch (Exception e) {
                        caseMaterial = "Not available";
                    }

                    // Extract product dimensions
                    String dimensions = "";
                    try {
                        WebElement dimensionsRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                                "//table[@class='table table-borderless table-striped m-b-0']//th[text()='Measurements']/following-sibling::td")));
                        dimensions = dimensionsRow.getText().trim();
                    } catch (Exception e) {
                        dimensions = "Not available";
                    }

                    // Extract features if present
                    String features = "";
                    try {
                        WebElement featureElement = driver.findElement(By.cssSelector("div.content-with-icon-block__title.f-extra-bold"));
                        features = featureElement.getText();
                    } catch (Exception e) {
                        features = "Features not available";
                    }

                    // Print the extracted details
                    System.out.println("Product Title: " + title + " | Price: " + price + " | Case Material: " + caseMaterial + " | Dimensions: " + dimensions +
                            " | Features: " + features);

                    // Store the details in the productData list
                    productData.add(new String[]{title, price, caseMaterial, dimensions, features});

                } catch (Exception e) {
                    System.out.println("Error: Could not locate product details on the page.");
                    System.out.println("Current URL: " + driver.getCurrentUrl());
                }

                // Return to the product grid to continue scraping
                driver.navigate().back();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.product-anchor-list__carousel")));
            }

            // Save extracted data to a CSV file
            saveDataToCSV("Suunto_Watches_Data.csv", productData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // Close the browser after scraping is complete
        }
    }

    // Helper method to handle popups and cookie banners
    private static void handlePopupsAndCookies(WebDriver driver, WebDriverWait wait) {
        try {
            WebElement popupCloseButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Close']")));
            popupCloseButton.click();
            System.out.println("Initial popup closed successfully.");
        } catch (Exception e) {
            System.out.println("No initial popup found, proceeding...");
        }

        try {
            WebElement rejectCookiesButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-reject-all-handler")));
            rejectCookiesButton.click();
            System.out.println("Cookie consent banner rejected successfully.");
        } catch (Exception e) {
            System.out.println("No cookie consent banner found, proceeding...");
        }
    }

    // Helper method to save data to a CSV file
    private static void saveDataToCSV(String fileName, List<String[]> data) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String[] row : data) {
                writer.append(String.join(",", row)).append("\n");
            }
            System.out.println("Data successfully saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error while saving data to CSV: " + e.getMessage());
        }
    }
}
