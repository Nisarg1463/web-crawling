package FinalProject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Samsung {
	public static void crawl() {
		WebDriver driver = new ChromeDriver();

		// Use Duration.ofSeconds for WebDriverWait
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));

		// Navigate to the second URL
		driver.get("https://www.samsung.com/ca/watches/all-watches/");

		// Dismiss the cookie consent message on the second website
		try {
			WebElement cookies2 = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Accept')]"))); // Adjust
																														// the
																														// text
																														// if
																														// needed
			cookies2.click();
		} catch (Exception e) {
			System.out.println("Second cookie consent button not found: " + e.getMessage());
		}

		// Locate the common class for all Watches
		WebElement bfc = driver.findElement(By.className("checkbox-radio__label-text"));
		bfc.click();
		try {
			Thread.sleep(3000);
		} catch (Exception e) {

		}
		WebElement WatchesContainer = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".pd03-product-finder__content")));
//        WebElement WatchesContainer = driver.findElement(By.cssSelector(".pd03-product-finder__content"));
		List<WebElement> SmartWatches = WatchesContainer
				.findElements(By.cssSelector("div.pd03-product-card.pd03-product-card--vertical"));

		try {
			// Create a CSV file to store the data that we will scrape
			FileWriter csvWriter = new FileWriter("Samsung_Smart_watches.csv");
			csvWriter.append(
					"Watch Name,Total Price ($),Colour,Rating ,Number of Reviews, Features , Monthly Installment  \n");

			String Watchname;
			String Colour;
			String Price;
			String Rating;
			String Reviews;
			String Features;
			String Installment;

			// Regex pattern to extract only numeric values
			Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?"); // Matches integer and decimal numbers

			// Loop through smart watches to scrape data
			for (WebElement Watch : SmartWatches) {
				Watchname = Watch.findElement(By.cssSelector(".pd03-product-card__product-name-text")).getText();
				Colour = Watch.findElement(By.cssSelector(".option-selector-v2__color-name-text-in")).getText();

				// Get Price and extract only numeric part
				String fullPrice = Watch.findElement(By.cssSelector(".pd03-product-card__price-main")).getText();
				Matcher matcher = pattern.matcher(fullPrice);
				Price = matcher.find() ? matcher.group() : "N/A"; // Extracts first numeric part

				// Get Rating and extract only numeric part
				String fullRatingText = Watch.findElement(By.cssSelector(".rating__point")).getText();
				Matcher matcherr = pattern.matcher(fullRatingText);
				Rating = matcherr.find() ? matcherr.group() : "N/A"; // Extracts first numeric part

				// Get Rating and extract only numeric part
				String fullreviewText = Watch.findElement(By.cssSelector(".rating__review-count")).getText();
				Matcher matcher_ = pattern.matcher(fullreviewText);
				Reviews = matcher_.find() ? matcher_.group() : "N/A"; // Extracts first numeric part

				Features = Watch.findElement(By.cssSelector(".pd03-product-card__spec-item")).getText();
				Installment = Watch.findElement(By.cssSelector(".pd03-product-card__finance")).getText();
				// Write data to CSV
				csvWriter.append("\"").append(Watchname).append("\",\"").append("\",\"").append(Price).append("\",\"")
						.append(Colour.replace("\n", " ")).append(Rating).append("\",\"").append(Reviews)
						.append("\",\"").append(Features).append("\",\"").append(Installment).append("\"\n");

			}

			csvWriter.flush();
			csvWriter.close();

		} catch (Exception e) {

		}

		System.out.println("CSV created successfully and all the data is stored in it");
		System.out.println("CSV File Path: " + new File("Samsung_Smart_watches.csv").getAbsolutePath());

		driver.quit();
	}
}
