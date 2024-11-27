package FinalProject;

import java.io.*;
import java.util.*;

public class Suggestions {
    private Set<String> voc_bset_Bz = new HashSet<>();
    private String[] hTabl_Ez;
    private int tabl_Cap;

    public Suggestions(int cap) {
        this.tabl_Cap = cap;
        hTabl_Ez = new String[cap];
    }

    // Loads words from a CSV or text file into the vocabulary set
    public void pop_Bank_Vocab(String loc_File) {
        try (BufferedReader br_Buffer = new BufferedReader(new FileReader(loc_File))) {
            String r_rec_Line;
            while ((r_rec_Line = br_Buffer.readLine()) != null) {
                // Split words by spaces or commas, and convert to lowercase
                String[] t_splitArr = r_rec_Line.toLowerCase().split("\",\"");
                t_splitArr[0] = t_splitArr[0].substring(1);
                t_splitArr[t_splitArr.length-1] = t_splitArr[t_splitArr.length-1].substring(0, t_splitArr[t_splitArr.length-1].length()-1);
                for (String data : t_splitArr) {
                	for(String wrd: data.split(" "))
                    voc_bset_Bz.add(wrd.toLowerCase());
                }
            }
            for (String wrd : voc_bset_Bz) {
                add_Word_Hash(wrd);
            }
        } catch (IOException e_IO) {
            e_IO.printStackTrace();
        }
    }

    // Adds words into the hash table using modified cuckoo hashing
    private void add_Word_Hash(String wrd_Txt) {
        int idx_PHash = get_PHash(wrd_Txt);
        int idx_SHash = get_SHash(wrd_Txt);
        int count_Exchange = 0;

        while (count_Exchange < tabl_Cap) {
            if (hTabl_Ez[idx_PHash] == null) {
                hTabl_Ez[idx_PHash] = wrd_Txt;
                return;
            } else if (hTabl_Ez[idx_SHash] == null) {
                hTabl_Ez[idx_SHash] = wrd_Txt;
                return;
            } else {
                // Swap and re-calculate hash positions
                String tmp_Hold = hTabl_Ez[idx_PHash];
                hTabl_Ez[idx_PHash] = wrd_Txt;
                wrd_Txt = tmp_Hold;
                idx_PHash = get_PHash(wrd_Txt);
                idx_SHash = get_SHash(wrd_Txt);
                count_Exchange++;
            }
        }
    }

    // Computes the primary hash location
    private int get_PHash(String wrd_Txt) {
        return Math.abs(wrd_Txt.hashCode()) % tabl_Cap;
    }

    // Computes the secondary hash location
    private int get_SHash(String wrd_Txt) {
        return Math.abs((wrd_Txt.hashCode() * 31) % tabl_Cap);
    }

    // Checks if a word exists in the vocabulary set, case-insensitive
    public boolean chk_Vocab_Present(String wrd_Txt) {
        wrd_Txt = wrd_Txt.toLowerCase();
        return hTabl_Ez[get_PHash(wrd_Txt)] != null && hTabl_Ez[get_PHash(wrd_Txt)].equals(wrd_Txt) ||
                hTabl_Ez[get_SHash(wrd_Txt)] != null && hTabl_Ez[get_SHash(wrd_Txt)].equals(wrd_Txt);
    }

    // Calculates the Levenshtein edit distance between two words
    private int calc_LDist(String orig, String comp) {
        int[][] dGrid = new int[orig.length() + 1][comp.length() + 1];

        for (int i = 0; i <= orig.length(); i++) dGrid[i][0] = i;
        for (int j = 0; j <= comp.length(); j++) dGrid[0][j] = j;

        for (int i = 1; i <= orig.length(); i++) {
            for (int j = 1; j <= comp.length(); j++) {
                if (orig.charAt(i - 1) == comp.charAt(j - 1)) {
                    dGrid[i][j] = dGrid[i - 1][j - 1];
                } else {
                    dGrid[i][j] = 1 + Math.min(dGrid[i - 1][j],
                            Math.min(dGrid[i][j - 1], dGrid[i - 1][j - 1]));
                }
            }
        }
        return dGrid[orig.length()][comp.length()];
    }

    // Inner class to store words with their calculated edit distances
    private class Wr_Suggest {
        String term_S;
        int prox_Dist;

        Wr_Suggest(String term, int dist) {
            this.term_S = term;
            this.prox_Dist = dist;
        }
    }

    // Recursive merge sort function to order suggestions by distance
    private void sort_SuggestsByDist(List<Wr_Suggest> suggest_List, int srt, int end) {
        if (srt < end) {
            int mid = (srt + end) / 2;
            sort_SuggestsByDist(suggest_List, srt, mid);
            sort_SuggestsByDist(suggest_List, mid + 1, end);
            merge_Suggest(suggest_List, srt, mid, end);
        }
    }

    // Helper method for merging sorted parts of suggestions
    private void merge_Suggest(List<Wr_Suggest> sugg_List, int start, int middle, int last) {
        List<Wr_Suggest> left_P = new ArrayList<>(sugg_List.subList(start, middle + 1));
        List<Wr_Suggest> right_P = new ArrayList<>(sugg_List.subList(middle + 1, last + 1));

        int i = 0, j = 0, k = start;

        while (i < left_P.size() && j < right_P.size()) {
            if (left_P.get(i).prox_Dist <= right_P.get(j).prox_Dist) {
                sugg_List.set(k++, left_P.get(i++));
            } else {
                sugg_List.set(k++, right_P.get(j++));
            }
        }

        while (i < left_P.size()) {
            sugg_List.set(k++, left_P.get(i++));
        }

        while (j < right_P.size()) {
            sugg_List.set(k++, right_P.get(j++));
        }
    }

    // Retrieve suggestions for misspelled words, ordered by distance
    public List<Wr_Suggest> fetch_Word_Suggestions(String misspell) {
        misspell = misspell.toLowerCase();
        List<Wr_Suggest> suggest_List = new ArrayList<>();
        for (String wrd : voc_bset_Bz) {
            if (!wrd.equals(misspell)) {
                suggest_List.add(new Wr_Suggest(wrd, calc_LDist(misspell, wrd)));
            }
        }
        sort_SuggestsByDist(suggest_List, 0, suggest_List.size() - 1);
        return suggest_List;
    }

    // Main function to test spell checker functionality
    public static void Search(String input) {
    	Suggestions vocabChecker = new Suggestions(10000);  // Initialize with a limit of 10000 for the hash table
        String loc_File = "Output.csv";

        // Load words into the vocabulary set
        vocabChecker.pop_Bank_Vocab(loc_File);

        
        // Check if word is in vocabulary and suggest alternatives if needed
        if (vocabChecker.chk_Vocab_Present(input)) {
            System.out.println(input + " is spelled correctly.");
        } else {
            System.out.println(input + " is not in the dictionary. Here are some suggestions:");
            List<Wr_Suggest> suggestions = vocabChecker.fetch_Word_Suggestions(input);
            int max = -1, count = 0;
            if(suggestions.get(0).prox_Dist > input.length()-2) {
            	System.out.println("No suggestions found");
//            	return;
            }
            for (Wr_Suggest suggest : suggestions) {
            	if(suggest.prox_Dist > input.length()-2 & suggest.prox_Dist > suggest.term_S.length()-2) continue;
            	if(max == -1) max = suggest.prox_Dist;
            	if(suggest.prox_Dist>max || count > 5) break;
                System.out.println(suggest.term_S);
                count++;
            }
        }
    }
}
