package FinalProject ;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;


public class hammerSmartwatches {
		
	public static void crawl() {
		WebDriver driver = new ChromeDriver(); // creating a new chrome web driver object to perform the activities
		driver.manage().window().maximize(); // Maximising the window
		driver.get("https://hammeronline.in/collections/smart-watch"); //opens the url
		JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
		jsDriver.executeScript("window.scrollBy(0,500)", ""); // Scrolling to 500th pixel using the js executor
		
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // giving a time delay of 10 seconds so that it gets time to load
	    
	    WebElement Item= driver.findElement(By.xpath("/html/body/div[2]/div/main/div[2]/div/div/div/div/div[2]/div[2]/div/div[1]/div[2]/div[2]/div"));
	    Item.click();
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    
	    WebElement colorChange =driver.findElement(By.xpath("/html/body/div[2]/div/main/div[1]/div/div/div/div/div[2]/div/div[2]/div[4]/div/fieldset/div[2]"));//color change element
	    colorChange.click();
	    
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));// A deley is given so that the next function is perform after completing the previous one or else they tend to overlap and break the code.
	    
	    colorChange=driver.findElement(By.xpath("/html/body/div[2]/div/main/div[1]/div/div/div/div/div[2]/div/div[2]/div[4]/div/fieldset/div[3]"));//color change elemnt
	    colorChange.click();
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    WebElement addTocartButton = driver.findElement(By.xpath("/html/body/div[2]/div/main/div[1]/div/div/div/div/div[2]/div/div[2]/div[6]/div[1]/form/div[1]/button"));// add to cart button
	    addTocartButton.click();
	    
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	    
	    WebElement noOfprod=driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/form/div[2]/div[1]/div/div/div/div[2]/div[2]/div[1]/div[1]/button[2]"));//increase prod num button
	   
	   noOfprod.click();
	   driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	   
	   WebElement closeButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/form/div[1]/div/div[2]/button"));
	   closeButton.click();
	   WebElement Gobacktowatches = driver.findElement(By.xpath("/html/body/div[2]/div/main/div[1]/div/div/div/nav/a[3]"));
	   Gobacktowatches.click();
	  
	    System.out.println("checkpoint");//to understand till where the script ran in case of an error
	    
	   
		jsDriver.executeScript("window.scrollBy(0,500)", "");// scrolling to 500th pixel
	    
	    
	    
	    
	 //   Wrting data to smartWatcheDetails.csv
	    
	    String csvFile = "smartwatchDetails.csv"; // location of csv file
	    
	    try (FileWriter fileWriter = new FileWriter(csvFile);// intialisiing a file writer
	             PrintWriter printWriter = new PrintWriter(fileWriter)) {//printer writer is alos intialised so that it is used to write to a file.
	    	// Write the CSV header (first row with column names basically Headers)
            printWriter.println("Model Name           ,    Features                               ");// writing the coloumn headers to the file
	    	
	    
	    for(int i=2;i<=6;i++) { //looping through elements using for loop
	    WebElement  title =driver.findElement(By.xpath("/html/body/div[2]/div/main/div[2]/div/div/div/div/div[2]/div[2]/div/div[1]/div[2]/div["+i+"]/div/div[2]/a/div[1]"));
	   // int j=i+1;
	    WebElement feature =driver.findElement(By.xpath("/html/body/div[2]/div/main/div[2]/div/div/div/div/div[2]/div[2]/div/div[1]/div[2]/div["+i+"]/div/div[2]/product-form/form/div[2]"));
	    										
	    
	    												
	    
	    String temp = title.getText();//geting text from the identified element
	    String temp2 = feature.getText();//geting text from the identified element
	    
	    System.out.println(temp);//printing the title
	    System.out.println(temp2);//printing the features both are in the console
	    printWriter.println(temp + "," + temp2); //writing data to csv file
	    
	    
	    
	    }System.out.println("Data successfully written to " + csvFile);// Success message

            

        } catch (IOException e) {
            e.printStackTrace();
        }
	    
	    
	    
	    WebElement searchButton =driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[3]/div[2]/div/header/div[1]/div/div[3]/div/div/a[1]"));
	    searchButton.click();
	    WebElement searchBar =driver.findElement(By.xpath("//*[@id=\"Search\"]"));// searchbar
	    searchBar.sendKeys("Active 3.0"); // sending text through send keys
	    
	    
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    WebElement enterButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[3]/div[2]/div/header/div[2]/div/div/predictive-search/form/div[1]/button"));
	    enterButton.click();
	    
	    
	  
	    
	    driver.quit();
	    }
	    
	}
