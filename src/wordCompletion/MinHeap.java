package wordCompletion;

import java.util.ArrayList;
import java.util.List;

public class MinHeap {
    private List<Completion> heap;
    private int max_size;
    private boolean isEmpty;
    
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public MinHeap(int size) {
        this.max_size = size;
        this.heap = new ArrayList<>(size);
    }

    public void insert(Completion completion) {
        if (heap.size() < max_size) {
            heap.add(completion);
            heap_ify_up(heap.size() - 1);
        } else if (completion.frqncy > heap.get(0).frqncy) {
            heap.set(0, completion);
            heap_ify_down(0);
        }
    }
    
    public Completion extractMin() {
        if (heap.isEmpty()) {
            return null; // Or throw an exception
        }

        Completion root = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        heap_ify_down(0);
        return root;
    }


    private void heap_ify_up(int index) {
        while (index > 0) {
            int prnt_indx = (index - 1) / 2;
            if (heap.get(index).frqncy > heap.get(prnt_indx).frqncy) {
                swap(index, prnt_indx);
                index = prnt_indx;
            } else {
                break;
            }
        }
    }

    private void heap_ify_down(int index) {
        int last_index = heap.size() - 1;
        while (index <= last_index) {
            int lft_child = 2 * index + 1;
            int rgt_child = 2 * index + 2;
            int lrgst_index = index;

            if (lft_child <= last_index && heap.get(lft_child).frqncy > heap.get(lrgst_index).frqncy) {
                lrgst_index = lft_child;
            }

            if (rgt_child <= last_index && heap.get(rgt_child).frqncy > heap.get(lrgst_index).frqncy) {
                lrgst_index = rgt_child;
            }

            if (lrgst_index == index) {
                break;
            }
            swap(index, lrgst_index);
            index = lrgst_index;
        }
    }

    private void swap(int i, int j) {
        Completion temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public List<Completion> getTopCompletions() {
        return heap;
    }
}