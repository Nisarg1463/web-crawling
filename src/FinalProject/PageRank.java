package FinalProject;

import java.util.*;
import java.util.Map.Entry;

class PageRank {
    static class Page implements Comparable<Page> {
        String url;
        int rank;

        Page(String url, int rank) {
            this.url = url;
            this.rank = rank;
        }

        @Override
        public int compareTo(Page other) {
            // Compare by rank for the max-heap
            return Integer.compare(other.rank, this.rank);
        }

        @Override
        public String toString() {
            return "Page{" +
                    "url='" + url + '\'' +
                    ", rank=" + rank +
                    '}';
        }
    }

    public static void pageRank(Map<String, AVLTree> pages, String Keyword) {        

        // Calculate ranks and store in a max-heap
        PriorityQueue<Page> maxHeap = new PriorityQueue<>();

        for (Entry<String, AVLTree> entry : pages.entrySet()) {
            String url = entry.getKey();
            AVLTree avlTree = entry.getValue();

            int totalRank = 0;
            // Calculate total rank (sum of keyword frequencies)
            for(String word: Keyword.split(" ")) {
            	totalRank = calculateTotalRank(avlTree, word, totalRank);      
            }

            // Add the page with its rank to the max-heap
            maxHeap.offer(new Page(url, totalRank));
        }

        // Display the highest-ranked pages (let's say we want the top 3)
        int count = 0;
        System.out.println("Top Ranked Pages:");
        for (int i = 0; i < 3 && !maxHeap.isEmpty(); i++) {
        	Page page = maxHeap.poll();
        	if (page.rank > 0) {
        		System.out.println(page.url);
        		count++;
        	}
        }
        if(count==0) {
        	System.out.println("No pages found");
        	Suggestions.Search(Keyword);
        }
    }

    
 // Helper method to calculate total rank from the AVLTree
    private static int calculateTotalRank(AVLTree avlTree, String key, int totalRank) {
        if (avlTree.search(key)!= null) totalRank += avlTree.search(key);
        
        return totalRank;
    }
}
