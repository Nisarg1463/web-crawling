package FinalProject;

import java.util.Scanner;

import WebDriver.InverseIndexing;
import wordCompletion.AutoComplete;

public class Main {
	public static void main(String[] args) {
		int searchMode = 0;
		System.out.println("Loading.....");
		Scanner sc = new Scanner(System.in);
		InverseIndexing.load();
		SearchFrequency.setup();
		
		help();
		String input = "";
		while (!input.toLowerCase().equals("$exit")) {
			System.out.print("\n\nEnter your command: ");
			input = sc.nextLine().strip();
			System.out.println("\n\n");

			if (input.toLowerCase().equals("$help")) {
				help();
			} else if (input.toLowerCase().equals("$crawl")) {
				Samsung.crawl();
				Garmin.crawl();
				Suunto.crawl();
				hammerSmartwatches.crawl();
				GoNoise.crawl();
				CombineCSV.combine();
			} else if (input.toLowerCase().equals("$all")) {
				searchMode = 1;
			} else if (input.toLowerCase().equals("$ts")) {
				searchMode = 0;
			} else if (input.toLowerCase().equals("$exit")) {
				break;
			} else if (input.toLowerCase().equals("$frs")) {
				SearchFrequency.getFrequency();
			} else if (input.toLowerCase().equals("$frw")) {
				WordFrequencyCounter.frequency();
			} else if (input.charAt(input.length() - 1) == '^') {
				AutoComplete.Complete(input.substring(0, input.length() - 1));
			} else {
				Suggestions.Search(input);
				if (searchMode == 0)
					RankingSystem.loadRanking(input);
				else
					InverseIndexing.search(input);
				SearchFrequency.add(input);
			}
		}
	}

	public static void help() {
		System.out.println("$Help for help.");
		System.out.println("$Crawl to crawl the web for CSV.");
		System.out.println("$RR for count ranking again.");
		System.out.println("$All for searching for all the pages.");
		System.out.println("$TS for searching for top pages.");
		System.out.println("$frs for search frequency.");
		System.out.println("$frw for common word frequency.");
		System.out.println("Use ^ at the end of the query for autocomplete suggestions.");
		System.out.println("$Exit to leave.");

	}
}
