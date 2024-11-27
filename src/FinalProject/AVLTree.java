package FinalProject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class AVLTree {
    class Node {
        Entry<String, Integer> data;
        Node left, right;
        int height;

        Node(Entry<String, Integer> entry) {
            this.data = entry;
            this.height = 1;
        }
    }

    private Node root;

    // Helper method to get the height of the node
    private int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    // Helper method to get the maximum of two integers
    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Right rotate subtree rooted with y
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    // Left rotate subtree rooted with x
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    // Get balance factor of node N
    private int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    // Insert a key-value pair from the Map into the AVL tree
    public void insert(Map.Entry<String, Integer> entry) {
        root = insert(root, entry);
    }

    private Node insert(Node node, Map.Entry<String, Integer> entry) {
        // Perform the normal BST insertion
        if (node == null)
            return new Node(entry);

        // Compare the keys lexicographically
        if (entry.getKey().compareTo(node.data.getKey()) < 0)
            node.left = insert(node.left, entry);
        else if (entry.getKey().compareTo(node.data.getKey()) > 0)
            node.right = insert(node.right, entry);
        else
            return node; // Duplicate keys are not allowed

        // Update height of this ancestor node
        node.height = 1 + max(height(node.left), height(node.right));

        // Get the balance factor of this ancestor node to check whether it became unbalanced
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases:

        // Left Left Case
        if (balance > 1 && entry.getKey().compareTo(node.left.data.getKey()) < 0)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && entry.getKey().compareTo(node.right.data.getKey()) > 0)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && entry.getKey().compareTo(node.left.data.getKey()) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && entry.getKey().compareTo(node.right.data.getKey()) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // Return the (unchanged) node pointer
        return node;
    }

    // Search for a key in the AVL tree
    public Integer search(String key) {
        Node result = search(root, key);
        return result != null ? result.data.getValue() : null;
    }

    private Node search(Node node, String key) {
        // Base case: node is null or key is present at node
        if (node == null || node.data.getKey().equals(key)) {
            return node;
        }

        // Key is lexicographically smaller than current node's key
        if (key.compareTo(node.data.getKey()) < 0) {
            return search(node.left, key);
        }

        // Key is lexicographically greater than current node's key
        return search(node.right, key);
    }

}
