package wordCompletion;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class AutoComplete {
    public static void Complete(String substring) {
        // Example single file path
        List<String> file_path = new ArrayList<>();
     file_path.add("Output.csv"); // Change this to your actual CSV file path

        // Create vocabulary
        Map<String, Integer> vocabulary_map = VocabularyCreator.createVocabulary(file_path);
        
        // Create AVL Tree and insert words
        AVLTree avl_Tree = new AVLTree();
        for (Map.Entry<String, Integer> entry : vocabulary_map.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) { // Insert based on frequency
                for(String word: entry.getKey().split(" "))
            	avl_Tree.insert(word);
            }
        }

        List<Completion> completions = avl_Tree.getCompletionsBySubstring(substring, 5);
        Collections.sort(completions, Comparator.comparingDouble(Completion::getFrequency).reversed());
        
        if(completions.isEmpty()) {
        	System.out.println("No completions found");
        	return;
        }
        System.out.println("Top completions for substring '" + substring + "':");
        for (Completion c : completions) {
            System.out.printf("%-30s\n", c.word);
        }
       // avlTree.inOrder();
        
    }
}
