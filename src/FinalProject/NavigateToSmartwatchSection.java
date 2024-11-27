package FinalProject;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NavigateToSmartwatchSection {
	// Method to launch garmin website and navigate to all smartwatch page.
	// It takes webdriver as an input
	public static void navigate(WebDriver driver) {
		
		//Get request to reach the home page too start scraping
		driver.get("https://www.garmin.com/en-CA/");
		
		//Web driver wait object that is initialized to wait for 10 seconds for an action to perform. Used to explicitly wait for a 
		//task to happen.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 

        //Hovering over the smartwatches button to get the menu to show up.
        WebElement hoverOver = driver.findElement(By.cssSelector("li[data-gatext=\"Smartwatches\"]"));                    
        Actions action = new Actions(driver);
        action.moveToElement(hoverOver).perform();
        
        //Waiting for all watches link to appear so that we can click it and go to the page will all the watches.
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[data-gatext=\"Global Navigation - All Smartwatches\"]")));

        // Perform actions on the element once it is visible
        element.click();
	}
}
