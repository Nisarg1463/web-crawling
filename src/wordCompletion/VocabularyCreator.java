package wordCompletion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VocabularyCreator {
    public static Map<String, Integer> createVocabulary(List<String> file_Paths) {
        Map<String, Integer> vocabulary = new HashMap<>();
        
        for (String file_path : file_Paths) {
            try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
                String ln;
                while ((ln = br.readLine()) != null) {
                    // Split by commas (or other delimiters if necessary)
                    String[] words = ln.split(","); // Adjust delimiter as needed
                    for (String word : words) {
                        word = word.trim().toLowerCase(); // Normalize to lower case
                        if (!word.isEmpty()) {
                            vocabulary.put(word, vocabulary.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return vocabulary;
    }
}
