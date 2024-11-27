package FinalProject;

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

public class Garmin {
	public static void crawl() {

		// Create an instance of ChromeDriver (launch the Chrome browser)
		WebDriver driver = new ChromeDriver();

		try {
			// Function used to navigate the page that contains the list of smartwatches
			NavigateToSmartwatchSection.navigate(driver);

			// File to store the data in.
			File output = new File("Garmin.csv");
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
