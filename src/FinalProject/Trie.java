package FinalProject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Define a Node class, representing each node in the Trie
class Node {
    Map<Character, Node> children; // A map to store child nodes, each corresponding to a character in the Trie
    Set<String> links = new HashSet<>(); // A set to store links (file paths) associated with words that end at this
                                         // node

    // Constructor for Node, initializes the map to hold child nodes
    public Node() {
        children = new HashMap<>();
    }
}

// Define the Trie class, which represents the entire Trie data structure
public class Trie {
    private final Node root; // The root node of the Trie

    // Constructor for Trie, initializes the root node as an empty Node
    public Trie() {
        root = new Node();
    }

    public void insert(String word, String link) {
        Node current = root; // Start from the root of the Trie

        // Iterate over each character in the word
        for (char ch : word.toCharArray()) {
            // Move to the child node corresponding to the current character,
            // and create a new Node if it doesn't exist
            current = current.children.computeIfAbsent(ch, c -> new Node());
        }

        // When the entire word is processed, add the associated link (file path) to the
        // links set
        current.links.add(link);
    }

    public Set<String> search(String word) {
        Node current = root; // Start from the root node of the Trie

        // Iterate over each character of the word to traverse the Trie
        for (char ch : word.toCharArray()) {
            // Move to the child node corresponding to the current character
            current = current.children.get(ch);

            // If no node exists for the character, the word is not in the Trie, return null
            if (current == null) {
                return null;
            }
        }

        // Return the set of links (file paths) associated with the word, if it exists
        return current.links;
    }

}
