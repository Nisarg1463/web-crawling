package FinalProject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


// Class to perform inverse indexing using Trie for word search across multiple text files
public class InverseIndexing {
	private static Trie trie;
	public static void search(String input) {
		// Search for the input keyword in the Trie
        if (trie.search(input) == null) {
            // If the keyword is not found in the Trie, display "Not found"
            System.out.println("Not found");
            return;
        }

        // If the keyword is found, print all associated file paths (links)
        for (String link : trie.search(input)) {
            System.out.println(link);  // Output the file names where the word was found
        }
	}
    public static void load() {
        trie = new Trie();  // Create a new Trie instance for storing words and file paths

        // Path to the folder where text files are located

        File htmlDir = new File("pages");
		File[] htmlFiles = htmlDir.listFiles((dir, name) -> name.endsWith(".txt"));

		File linkDir = new File("links");
		File[] linkFiles = linkDir.listFiles((dir, name) -> name.endsWith(".txt"));
		if (linkFiles.length != htmlFiles.length) {
			System.out.println("Please rerun the crawler as there is some problem with the current files.");
			return;
		}
        
        try {
            // Get all files from the specified folder using DirectoryStream
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("pages"));
            
            int loopCount = 0;
            // Iterate over each file in the directory
            for (Path filePath : directoryStream) {
                // Check if the current path is a regular file (not a directory)
                if (Files.isRegularFile(filePath)) {

                    // Read all lines from the file into a List of Strings (each line as a String)
                    List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

                    // Iterate over each line of the file
                    for (String line : lines) {
                        // Split the line into words using space as a delimiter
                        for (String word : line.split(" ")) {
                            trie.insert(word, Files.readString(linkFiles[loopCount].toPath()));  // Insert each word along with the file path into the Trie
                        }
                    }
                }
                loopCount++;
            }
        } catch (IOException e) {
            // Catch and print any IO exceptions (e.g., issues with reading files)
            e.printStackTrace();
        }

    }
    
  
}
