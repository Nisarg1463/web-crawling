package FinalProject;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WordFrequencyCounter {

    private static final String pathToFolder = "pages"; // Path to the folder containing .txt files
    private static final int TOP_WORDS = 40; // Number of top frequent words to display

    public static void frequency() {

        Map<String, Integer> wordCountHashMap = new HashMap<>(); // Create a hash table to store word frequencies

        try {

            Files.walk(Paths.get(pathToFolder))  // List all .txt files in the specified folder
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".txt"))
                    .forEach(file -> processTxtFile(file.toFile(), wordCountHashMap));
        } catch (IOException e) {
            System.err.println("Error accessing folder: " + e.getMessage());
            return;
        }

        // Convert the word frequency map to a list and sort it
        List<Map.Entry<String, Integer>> sortedWordList = new ArrayList<>(wordCountHashMap.entrySet());
        quickSort(sortedWordList, 0, sortedWordList.size() - 1);

        // The below block of code prints the top N most frequent words
        System.out.println("--------------------------------");
        System.out.println("Top " + TOP_WORDS + " most frequent words across all files:");
        for (int i = 0; i < Math.min(TOP_WORDS, sortedWordList.size()); i++) {
            Map.Entry<String, Integer> tempEntry = sortedWordList.get(i);
            System.out.println(tempEntry.getKey() + ": " + tempEntry.getValue());
        }
        System.out.println("--------------------------------");
    }

    private static void processTxtFile(File file, Map<String, Integer> wordCountHashMap) {
        try (BufferedReader buffReadr = new BufferedReader(new FileReader(file))) {
            String tempLine; // To read each line of the file
            while ((tempLine = buffReadr.readLine()) != null) {
                // Split the line into words using whitespace as the delimiter
                String[] words = tempLine.split("\\s+");
                for (String word : words) {
                    // Convert to lowercase and remove non-alphabetic characters
                    word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
                    if (!word.isEmpty()) {
                        // Increment the word count in the hash table
                        wordCountHashMap.put(word, wordCountHashMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("File Read error " + file.getName() + ": " + e.getMessage()); // erro message
        }
    }

    private static void quickSort(List<Map.Entry<String, Integer>> list, int start, int end) {
        if (start < end) {
            int indexofPivot = partition(list, start, end);
            quickSort(list, start, indexofPivot - 1);
            quickSort(list, indexofPivot + 1, end);
        }
    }

    private static int partition(List<Map.Entry<String, Integer>> list, int low, int high) {
        int pivotValue = list.get(high).getValue(); // Last element is the pivot
        int i = low - 1; // Index for smaller element
        for (int j = low; j < high; j++) {
            if (list.get(j).getValue() > pivotValue) {
                i++;
                Collections.swap(list, i, j); // Swap if current element is greater than the pivot
            }
        }
        Collections.swap(list, i + 1, high); // Place pivot in its correct position
        return i + 1; // Return index of the pivot
    }
}
