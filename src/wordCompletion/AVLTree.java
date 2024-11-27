package wordCompletion;

import java.util.ArrayList;
import java.util.List;

public class AVLTree {
    private AVLNode root;

    public void insert(String word) {
        root = insert_node(root, word);
    }
    public List<String> getTopCompletions(String sub_strng, int max_suggestions) {
        MinHeap heap = new MinHeap(max_suggestions);
     gather_completion_by_substrng(root, sub_strng, heap);

        List<String> suggestions = new ArrayList<>();
        while (!heap.isEmpty()) {
            suggestions.add(heap.extractMin().word);
        }
        return suggestions;
    }

    private AVLNode insert_node(AVLNode node, String word) {
        if (node == null) {
            return new AVLNode(word);
        }

        if (word.equals(node.word)) {
            node.frqncy++;
        } else if (word.compareTo(node.word) < 0) {
            node.left = insert_node(node.left, word);
        } else {
            node.right = insert_node(node.right, word);
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        return blnc_nods(node);
    }

    private AVLNode blnc_nods(AVLNode node) {
        int blnc = getBalance(node);

        if (blnc > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (blnc < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private AVLNode rotateLeft(AVLNode node) {
        AVLNode new_root = node.right;
        node.right = new_root.left;
        new_root.left = node;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        new_root.height = Math.max(getHeight(new_root.left), getHeight(new_root.right)) + 1;
        return new_root;
    }

    private AVLNode rotateRight(AVLNode node) {
        AVLNode new_root = node.left;
        node.left = new_root.right;
        new_root.right = node;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        new_root.height = Math.max(getHeight(new_root.left), getHeight(new_root.right)) + 1;
        return new_root;
    }

    public void gather_completion_by_substrng(AVLNode node, String sub_strng, MinHeap heap) {
        if (node == null) {
            return;
        }

        String node_word_lower = node.word.toLowerCase().trim(); // Trim leading/trailing whitespace
        String pre_fix_lower = sub_strng.toLowerCase();

        // Check if the current word starts with the prefix (case-insensitive)
        if (node_word_lower.startsWith(pre_fix_lower)) {
            heap.insert(new Completion(node.word, node.frqncy));
        }

        // Recursively search both subtrees
     gather_completion_by_substrng(node.left, sub_strng, heap);
     gather_completion_by_substrng(node.right, sub_strng, heap);
    }

    // Public method to call from outside
    public List<Completion> getCompletionsBySubstring(String sub_strng, int limit) {
        MinHeap heap = new MinHeap(limit);
     gather_completion_by_substrng(root, sub_strng, heap);
        return heap.getTopCompletions();
    }
    
    

    // In-Order Traversal for Debugging
    public void in_order() {
        in_orderTraversal(root);
    }

    private void in_orderTraversal(AVLNode node) {
        if (node != null) {
            in_orderTraversal(node.left);
            System.out.println(node.word + ": " + node.frqncy);
            in_orderTraversal(node.right);
        }
    }
}
