import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Web {
    public static void main(String[] args) {

        // Create an instance of ChromeDriver (launch the Chrome browser)
        WebDriver driver = new ChromeDriver();

        try {
            // Function used to navigate the page that contains the list of smartwatches
            NavigateToSmartwatchSection.navigate(driver);

            // File to store the data in.
            File output = new File("Output.csv");
            output.createNewFile();

            // File writer that is used and passed throughout the process
            FileWriter fw = new FileWriter(output);

            // Writing the header for the CSV.
            fw.write(
                    "\"name\",\"price(in CAD)\",\"BATTERY LIFE (SMARTWATCH MODE)\",\"WATCH DISPLAY TYPE\",\"WATCH WATER RATING\",\"PHYSICAL SIZE\",\"WEIGHT\",\"DISPLAY SIZE\",\"DISPLAY RESOLUTION\",\"BATTERY LIFE\",\"MEMORY/HISTORY\"\n");

            // Loop to iterate through multiple pages of smartwatch category
            while (true) {

                // Method used to collect data from the currently shown page.
                DataCollection.collect(driver, fw);

                // Getting next page button
                WebElement nextButton = driver.findElement(By.cssSelector("g-button[aria-label=\"Go to next page\"]"));

                // Checking if the button is disabled. Disabled button indicates last page and
                // as we have collected the data we can
                // exit the loop
                if (nextButton.findElements(By.className("g__button--contained--disabled")).toArray().length > 0) {
                    break;
                }

                // Change the page if the button is not disabled
                nextButton.click();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}

public class NavigateToSmartwatchSection {
    // Method to launch garmin website and navigate to all smartwatch page.
    // It takes webdriver as an input
    public static void navigate(WebDriver driver) {

        // Get request to reach the home page too start scraping
        driver.get("https://www.garmin.com/en-CA/");

        // Web driver wait object that is initialized to wait for 10 seconds for an
        // action to perform. Used to explicitly wait for a
        // task to happen.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Hovering over the smartwatches button to get the menu to show up.
        WebElement hoverOver = driver.findElement(By.cssSelector("li[data-gatext=\"Smartwatches\"]"));
        Actions action = new Actions(driver);
        action.moveToElement(hoverOver).perform();

        // Waiting for all watches link to appear so that we can click it and go to the
        // page will all the watches.
        WebElement element = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector("a[data-gatext=\"Global Navigation - All Smartwatches\"]")));

        // Perform actions on the element once it is visible
        element.click();
    }
}

public class DataCollection {

    // Method to collect the data from the list of watches displayed.
    // Takes input of webdriver and filewriter.
    public static void collect(WebDriver driver, FileWriter fw) {

        // Web driver wait object that is initialized to wait for 10 seconds for an
        // action to perform. Used to explicitly wait for a
        // task to happen.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Waiting for the page to load by checking if the series-banner element is
        // present or not
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("series-banner")));

        // Finding the list of all the products by using find elements which have class
        // name product-card. It is the class associated
        // with the products.
        List<WebElement> elements = driver.findElements(By.className("product-card"));

        try {

            // Iterating through each item in the list of products.
            for (int i = 0; i < elements.toArray().length; i++) {

                // Getting the right item from the order.
                WebElement element = driver.findElements(By.className("product-card")).get(i);

                // Checking if the item is a product or a blank card.
                if (element.getAttribute("data-product-details-name") != null) {

                    // Storing the name of the product and navigating to the product page
                    String name = element.getAttribute("data-product-details-name");
                    element.click();

                    // Method used to scrape the data from the product page.
                    scrapeData(driver, fw, name);

                    // Navigating back to the menu page and waiting for the page to load.
                    driver.navigate().back();
                    driver.navigate().back();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.className("series-banner")));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method used to scrape data from the product page.
    // Inputs are the name of the product, webdriver and filewriter.
    public static void scrapeData(WebDriver driver, FileWriter fw, String name) {

        // Web driver wait object that is initialized to wait for 10 seconds for an
        // action to perform. Used to explicitly wait for a
        // task to happen.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Getting the price element to get the price of the product.
        WebElement price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.amount")));

        // Getting the button with the link to the specs of the watch for more details.
        WebElement specs = driver.findElement(By.cssSelector("a#js__tabs__nav__link__specs"));

        // Scrolling to the button and clicking it to make the specs page visible.
        Actions scroll = new Actions(driver);
        scroll.scrollToElement(specs).perform();
        wait.until(ExpectedConditions.visibilityOf(specs));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", specs); // Using this because of the
                                                                                     // problem with .click method where
                                                                                     // it randomly bugs and register
                                                                                     // the element unclickable.

        // Getting all the table rows that has a th tag as those are the specs rows.
        List<WebElement> dataRows = driver.findElements(By.cssSelector("tr:has(th)"));

        // Building the output string using the data rows to find specific specs that
        // are needed.
        String output;
        try {
            output = "\"" + name + "\",\"" + price.getText();
            for (WebElement element : dataRows) {
                String header = element.findElement(By.cssSelector("th")).getText();
                if (header.equals("BATTERY LIFE (SMARTWATCH MODE)")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText();
                } else if (header.equals("WATCH DISPLAY TYPE")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText();
                } else if (header.equals("WATCH WATER RATING")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText();
                } else if (header.equals("PHYSICAL SIZE")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText();
                } else if (header.equals("WEIGHT")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText();
                } else if (header.equals("DISPLAY SIZE")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText();
                } else if (header.equals("DISPLAY RESOLUTION")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText();
                } else if (header.equals("BATTERY LIFE")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText();
                } else if (header.equals("MEMORY/HISTORY")) {
                    output = output + "\",\"" + element.findElement(By.cssSelector("td")).getText() + "\"";
                }
            }

            // Writing the created string in the output file.
            fw.write(output + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
