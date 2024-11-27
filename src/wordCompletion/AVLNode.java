package wordCompletion;

public class AVLNode {
    String word;
    int frqncy;
    AVLNode left, right;
    int height;

    public AVLNode(String word2) {
        this.word = word2;
        this.frqncy = 1; // Initialize frequency to 1
        this.height = 1; // New node is initially added at leaf
    }
}
