package FinalProject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RankingSystem {

	public static void liveRanking() {
		// Sample Map to insert into AVL Tree
		Map<String, Map<String, Integer>> crawlResult = WebCrawler.crawl();

		Map<String, AVLTree> pageMapping = new HashMap<>();

		AVLTree avlTree;

		for (Map.Entry<String, Map<String, Integer>> data : crawlResult.entrySet()) {
			avlTree = new AVLTree();
			// Insert entries from the Map into the AVL tree
			for (Map.Entry<String, Integer> entry : data.getValue().entrySet()) {
				avlTree.insert(entry);
			}

			pageMapping.put(data.getKey(), avlTree);

		}


	}

	public static void loadRanking(String input) {
		File htmlDir = new File("pages");
		File[] htmlFiles = htmlDir.listFiles((dir, name) -> name.endsWith(".txt"));

		File linkDir = new File("links");
		File[] linkFiles = linkDir.listFiles((dir, name) -> name.endsWith(".txt"));
		if (linkFiles.length != htmlFiles.length) {
			System.out.println("Please rerun the crawler as there is some problem with the current files.");
			return;
		}

		try {
			int loopCount = 0;
			// Create a DirectoryStream to iterate over the files in the directory
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("pages"));

			Map<String, Map<String, Integer>> output = new HashMap<>();
			for (Path filePath : directoryStream) {
				Map<String, Integer> keywordFrequency = new HashMap<>();
				// Check if the path is a file (not a directory)
				if (Files.isRegularFile(filePath)) {

					// Read all lines of the file
					List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
					
					// Print the content of the file
					for (String line : lines) {
						// Map to store keyword frequencies

						// Iterate through words and count their frequency
						for (String word : line.split(" ")) {
							if (word.length() > 1) { // Ignore stopwords and single letters
								keywordFrequency.put(word, keywordFrequency.getOrDefault(word, 0) + 1);
							}
						}

					}

				}
				output.put(Files.readString(linkFiles[loopCount++].toPath()), keywordFrequency);
			}

			Map<String, AVLTree> pageMapping = new HashMap<>();

			AVLTree avlTree;

			for (Map.Entry<String, Map<String, Integer>> data : output.entrySet()) {
				avlTree = new AVLTree();
				// Insert entries from the Map into the AVL tree
				for (Map.Entry<String, Integer> entry : data.getValue().entrySet()) {
					avlTree.insert(entry);
				}

				pageMapping.put(data.getKey(), avlTree);

			}
			PageRank.pageRank(pageMapping, input);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}