package FinalProject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GoNoise {

    public static void crawl() {
        // Set the path to the ChromeDriver executable
        WebDriver driver = new ChromeDriver();

        // List to hold product data for CSV
        List<String[]> productData = new ArrayList<>();
        productData.add(new String[]{"Product Name", "Price"}); // CSV headers

        try {
            // Open the Suunto website
            driver.get("https://www.gonoise.com/collections/casual-wear-smartwatch");
            driver.manage().window().maximize();

            // Set up an explicit wait
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait until the product grid section is visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.collection-products-list")));

            // Collect product links
            List<WebElement> productElements = driver.findElements(By.cssSelector("a.product-head-wrapper"));
            List<String> productLinks = new ArrayList<>();

            for (WebElement product : productElements) {
                String link = product.getAttribute("href");
                if (link != null && !link.isEmpty()) {
                    productLinks.add(link);
                }
            }

            // Open each product link and extract details
            for (String productLink : productLinks) {
                System.out.println("Opening product: " + productLink);
                driver.get(productLink);
                Thread.sleep(5000); // Wait for 2 seconds (adjust as needed)

                // Extract product name
                String productName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.product-title"))).getText();
                
                double CONVERSION_RATE = 0.016;
                // Extract price
                String price;
                double dollar=0.0;
                try {
                    price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.product-actual-price"))).getText();
                    price = price.replaceAll("[^\\d.]", ""); // Remove any non-numeric characters
                    double priceInRupees = Double.parseDouble(price); // Convert to double

                    // Convert to Canadian Dollars
                    dollar = priceInRupees * CONVERSION_RATE;
                } catch (Exception e) {
                    price = "Price not listed";
                }
                
                String productColor = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.varint-color-value"))).getText();
                
                String Review = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.review-text"))).getText();
                
                String Countryoforigin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class='product-specification Specs'] p:nth-child(1)"))).getText();

                // Print the extracted details
                System.out.println("Product Name: " + productName + " | Price: " + dollar + " | Color: " + productColor + " | Review:" + Review + " | Origin:" + Countryoforigin ) ;

                // Store the details in the productData list
                productData.add(new String[]{productName, String.format("%.2f", dollar) ,productColor, Review, Countryoforigin});

                // Return to the product listing page
                driver.navigate().back();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.collection-products-list")));
            }

            // Save extracted data to a CSV file
            saveDataToCSV("GoNoise.csv", productData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // Close the browser after scraping is complete
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
