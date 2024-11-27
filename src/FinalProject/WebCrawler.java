package FinalProject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebCrawler {

	public static Map<String, Map<String, Integer>> crawl() {
		Map<String, Map<String, Integer>> outputMap = new HashMap<String, Map<String, Integer>>();

		outputMap.putAll(Garmin());
		outputMap.putAll(Noise());
		outputMap.putAll(Hammer());
		outputMap.putAll(Samsung());
		outputMap.putAll(Sunnto());

		return outputMap;
	}

	public static Map<String, Map<String, Integer>> Samsung() {
		// Open the webpage
		WebDriver driver = new ChromeDriver();
		String url = "https://www.samsung.com/ca/watches/all-watches/"; // Replace with the URL of the web page
																		// you want to parse
		driver.get(url);
		// Locate the common class for all Watches


		// Web driver wait object that is initialized to wait for 10 seconds for an
		// action to perform. Used to explicitly wait for a
		// task to happen.
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Map<String, Map<String, Integer>> output = new HashMap<>();

		// Waiting for the page to load by checking if the series-banner element is
		// present or not
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("icon")));

		// Finding the list of all the products by using find elements which have class
		// name product-card. It is the class associated
		// with the products.
		List<WebElement> elements = driver.findElements(By.className("pd03-product-card__item"));
		Map<String, Integer> keywords;

		try {

			// Iterating through each item in the list of products.
			for (int i = 0; i < elements.toArray().length; i++) {

				// Getting the right item from the order.
				WebElement element = driver.findElements(By.className("pd03-product-card__item")).get(i);
				// Storing the name of the product and navigating to the product page

				element.click();
				String link = driver.getCurrentUrl();

				keywords = WebPageParser.parse(driver);

				output.put(link, keywords);

				// Navigating back to the menu page and waiting for the page to load.
				while(!driver.getCurrentUrl().contains("all-watches"))driver.navigate().back();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.className("icon")));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		driver.quit();
		return output;

	}

	public static Map<String, Map<String, Integer>> Sunnto() {
		// Set the path to the ChromeDriver executable
		WebDriver driver = new ChromeDriver();

		Map<String, Map<String, Integer>> output = new HashMap<>();
		Map<String, Integer> keywords;

		try {
			// Open the Suunto website and maximize the window
			driver.get("https://www.suunto.com/en-us/");
			driver.manage().window().maximize();

			// Set up an explicit wait to handle dynamic page elements
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

			// Close popups and reject cookie consent if present
			handlePopupsAndCookies(driver, wait);

			// Wait for the main categories section to load and become visible
			WebElement categoriesBlock = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.categories-block__grid")));

			// Find the "Watches" link inside the categories block
			WebElement watchesLink = categoriesBlock
					.findElement(By.cssSelector("a[href='/en-us/Product-search/See-all-Sports-Watches/']"));

			// Scroll the page to ensure the "Watches" link is in view before clicking
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", watchesLink);

			// Attempt to click the "Watches" link (fall back to JS click if normal click
			// fails)
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
			WebElement productList = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.product-anchor-list__carousel")));

			// Collect product names and their respective links
			List<WebElement> productLinkElements = driver
					.findElements(By.cssSelector("div.u-flex-1 a.btn.btn--secondary.u-width-100"));

			List<String> uniqueProductLinks = new ArrayList<>();

			// Loop through product names and links
			for (int i = 0; i < productLinkElements.size(); i++) {
				String productLink = productLinkElements.get(i).getAttribute("href");

				uniqueProductLinks.add(productLink);

			}

			// Show the count of unique products
			System.out.println("Total Unique Products Found: " + uniqueProductLinks.size());

			// Visit each product page to extract details
			for (String url : uniqueProductLinks) {
				System.out.println("Navigating to: " + url);
				driver.get(url);

				keywords = WebPageParser.parse(driver);

				output.put(url, keywords);

				driver.navigate().back();
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.cssSelector("div.product-anchor-list__carousel")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit(); // Close the browser after scraping is complete
		}
		return output;

	}

	public static Map<String, Map<String, Integer>> Hammer() {
		// Open the webpage
		WebDriver driver = new ChromeDriver();
		String url = "https://hammeronline.in/collections/smart-watch"; // Replace with the URL of the web page
																		// you want to parse
		driver.get(url);

		// Web driver wait object that is initialized to wait for 10 seconds for an
		// action to perform. Used to explicitly wait for a
		// task to happen.
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Map<String, Map<String, Integer>> output = new HashMap<>();
		while (true) {
			// Waiting for the page to load by checking if the series-banner element is
			// present or not
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("hero__slide-link")));

			// Finding the list of all the products by using find elements which have class
			// name product-card. It is the class associated
			// with the products.
			List<WebElement> elements = driver.findElements(By.className("grid-product__content"));
			Map<String, Integer> keywords;

			try {

				// Iterating through each item in the list of products.
				for (int i = 0; i < elements.toArray().length; i++) {

					// Getting the right item from the order.
					WebElement element = driver.findElements(By.className("grid-product__content")).get(i);

					// Storing the name of the product and navigating to the product page
					String link = element.getAttribute("href");
					element.click();

					// Getting the price element to get the price of the product.
					link = driver.getCurrentUrl();
					keywords = WebPageParser.parse(driver);

					output.put(link, keywords);

					// Navigating back to the menu page and waiting for the page to load.
					while (driver.getCurrentUrl().contains("product"))
						driver.navigate().back();

					wait.until(ExpectedConditions.presenceOfElementLocated(By.className("hero__slide-link")));

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				WebElement nextButton = driver.findElement(By.cssSelector("a[title=\"Next\"]"));// Getting next page
																								// button

				// Checking if the button is disabled. Disabled button indicates last page and
				// as we have collected the data we can
				// exit the loop
				if (nextButton.findElements(By.className("g__button--contained--disabled")).toArray().length > 0) {
					break;
				} // Change the page if the button is not disabled
				nextButton.click();
			} catch (Exception e) {
				break;
			}

		}
		driver.quit();
		return output;
	}

	public static Map<String, Map<String, Integer>> Noise() {
		WebDriver driver = new ChromeDriver();

		// List to hold product data for CSV
		List<String[]> productData = new ArrayList<>();
		productData.add(new String[] { "Product Name", "Price" }); // CSV headers

		Map<String, Map<String, Integer>> output = new HashMap<>();
		try {
			driver.get("https://www.gonoise.com/collections/casual-wear-smartwatch");
			driver.manage().window().maximize();
			Map<String, Integer> keywords;

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
				String productName = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.product-title")))
						.getText();

				keywords = WebPageParser.parse(driver);

				output.put(productLink, keywords);

				driver.navigate().back();
				wait.until(
						ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.collection-products-list")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit(); // Close the browser after scraping is complete
		}
		return output;
	}

	public static Map<String, Map<String, Integer>> Garmin() {
		// Open the webpage
		WebDriver driver = new ChromeDriver();
		String url = "https://www.garmin.com/en-CA/c/wearables-smartwatches/"; // Replace with the URL of the web page
																				// you want to parse
		driver.get(url);

		// Web driver wait object that is initialized to wait for 10 seconds for an
		// action to perform. Used to explicitly wait for a
		// task to happen.
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Map<String, Map<String, Integer>> output = new HashMap<>();
		while (true) {
			// Waiting for the page to load by checking if the series-banner element is
			// present or not
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("series-banner")));

			// Finding the list of all the products by using find elements which have class
			// name product-card. It is the class associated
			// with the products.
			List<WebElement> elements = driver.findElements(By.className("product-card"));
			Map<String, Integer> keywords;

			try {

				// Iterating through each item in the list of products.
				for (int i = 0; i < elements.toArray().length; i++) {

					// Getting the right item from the order.
					WebElement element = driver.findElements(By.className("product-card")).get(i);

					// Checking if the item is a product or a blank card.
					if (element.getAttribute("data-product-details-name") != null) {

						// Storing the name of the product and navigating to the product page
						String link = element.getAttribute("href");

						element.click();

						keywords = WebPageParser.parse(driver);

						output.put(link, keywords);

						// Navigating back to the menu page and waiting for the page to load.
						driver.navigate().back();
						wait.until(ExpectedConditions.presenceOfElementLocated(By.className("series-banner")));
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
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
		driver.quit();
		return output;

	}

	// Helper method to handle popups and cookie banners
	private static void handlePopupsAndCookies(WebDriver driver, WebDriverWait wait) {
		try {
			WebElement popupCloseButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Close']")));
			popupCloseButton.click();
			System.out.println("Initial popup closed successfully.");
		} catch (Exception e) {
			System.out.println("No initial popup found, proceeding...");
		}

		try {
			WebElement rejectCookiesButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-reject-all-handler")));
			rejectCookiesButton.click();
			System.out.println("Cookie consent banner rejected successfully.");
		} catch (Exception e) {
			System.out.println("No cookie consent banner found, proceeding...");
		}
	}
}
