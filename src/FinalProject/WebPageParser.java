package FinalProject;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class WebPageParser {

    public static Map<String, Integer> parse(WebDriver driver) {

     // Use JavaScript Executor to ensure that we get the dynamically loaded content
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String content = (String) js.executeScript("return document.body.innerText;");
        // Close the browser
        
        // Convert the content to lowercase for uniformity
        content = content.toLowerCase();

        // Remove non-word characters (punctuation, special characters, etc.)
        content = content.replaceAll("[^a-zA-Z0-9\\s]", " ");
        
        // Split the content into individual words (tokens)
        String[] words = content.split("\\s+");
        
        try {
            // Create the directory if it doesn't exist
            File directory = new File("pages");
            if (!directory.exists()) {
                directory.mkdir();
            }
            directory = new File("links");
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Generate a safe filename using the URL
            String safeFileName = driver.getCurrentUrl().replaceAll("[^a-zA-Z0-9.-]", "_");
            File file = new File("pages/" + File.separator + safeFileName + ".txt");
            File link = new File("links/" + File.separator + safeFileName + ".txt");
            
            // Save HTML content to a file
            FileWriter writer = new FileWriter(file);
            
            writer.write(String.join(" ", words));                       
            
            writer.close();
            
            writer = new FileWriter(link);
            writer.write(driver.getCurrentUrl());
            writer.close();

            System.out.println("Saved page to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving file for URL: " + driver.getCurrentUrl().replaceAll("[^a-zA-Z0-9.-]", "_") + " - " + e.getMessage());
        }
        

        // Map to store keyword frequencies
        Map<String, Integer> keywordFrequency = new HashMap<>();

        // Iterate through words and count their frequency
        for (String word : words) {
            if (word.length() > 1) { // Ignore stopwords and single letters
                keywordFrequency.put(word, keywordFrequency.getOrDefault(word, 0) + 1);
            }
        }
        
        return keywordFrequency;

    }
}
