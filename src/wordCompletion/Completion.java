package wordCompletion;

public class Completion {
    String word;
    int frqncy;

    public Completion(String word, int frqncy) {
        this.word = word;
        this.frqncy = frqncy;
    }

    @Override
    public String toString() {
        return word + ": " + frqncy;
    }
    public int getFrequency() {
        return frqncy;
    }
}
