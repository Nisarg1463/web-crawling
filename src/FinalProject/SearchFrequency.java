package FinalProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class SearchQuery {
	String query; // stores the string to be searched
	int frequency; // stores the frequency of the string

	SearchQuery(String query) {
		this.query = query;
		this.frequency = 1; // Initialize frequency to 1
	}

	void incrementFrequency() {
		frequency++;
	}

	@Override
	public String toString() {
		return query + " (" + frequency + ")";
	}
}

class RedBlackTreeNode {
	SearchQuery data;
	RedBlackTreeNode Lnode, Rnode, Pnode; // defining the three pointers left, right and parent
	int color; // will indicate the color. 1 is for red, 0 is for black

	RedBlackTreeNode(SearchQuery data) {
		this.data = data;
		Lnode = Rnode = Pnode = null;
		color = 1; // New node is red
	}
}

class SearchQueryTree {
	private RedBlackTreeNode root; // this is the root of our tree
	private RedBlackTreeNode Rnull; // A node that will be used for balancing

	public SearchQueryTree() {
		Rnull = new RedBlackTreeNode(new SearchQuery(""));
		Rnull.color = 0; // Black
		root = Rnull;
	}

	// Red Black tree implementation
	private void fixInsert(RedBlackTreeNode k) {
		RedBlackTreeNode u;
		while (k != root && k.Pnode.color == 1) {
			if (k.Pnode == k.Pnode.Pnode.Lnode) {
				u = k.Pnode.Pnode.Rnode;
				if (u.color == 1) {
					k.Pnode.color = 0;
					u.color = 0;
					k.Pnode.Pnode.color = 1;
					k = k.Pnode.Pnode;
				} else {
					if (k == k.Pnode.Rnode) {
						k = k.Pnode;
						leftRotate(k);
					}
					k.Pnode.color = 0;
					k.Pnode.Pnode.color = 1;
					rightRotate(k.Pnode.Pnode);
				}
			} else {
				u = k.Pnode.Pnode.Lnode;
				if (u.color == 1) {
					k.Pnode.color = 0;
					u.color = 0;
					k.Pnode.Pnode.color = 1;
					k = k.Pnode.Pnode;
				} else {
					if (k == k.Pnode.Lnode) {
						k = k.Pnode;
						rightRotate(k);
					}
					k.Pnode.color = 0;
					k.Pnode.Pnode.color = 1;
					leftRotate(k.Pnode.Pnode);
				}
			}
		}
		root.color = 0; // Ensuring that the root is black
	}

	private void leftRotate(RedBlackTreeNode x) {
		RedBlackTreeNode y = x.Rnode;
		x.Rnode = y.Lnode;
		if (y.Lnode != Rnull) {
			y.Lnode.Pnode = x;
		}
		y.Pnode = x.Pnode;
		if (x.Pnode == null) {
			root = y;
		} else if (x == x.Pnode.Lnode) {
			x.Pnode.Lnode = y;
		} else {
			x.Pnode.Rnode = y;
		}
		y.Lnode = x;
		x.Pnode = y;
	}

	private void rightRotate(RedBlackTreeNode x) {
		RedBlackTreeNode y = x.Lnode;
		x.Lnode = y.Rnode;
		if (y.Rnode != Rnull) {
			y.Rnode.Pnode = x;
		}
		y.Pnode = x.Pnode;
		if (x.Pnode == null) {
			root = y;
		} else if (x == x.Pnode.Rnode) {
			x.Pnode.Rnode = y;
		} else {
			x.Pnode.Lnode = y;
		}
		y.Rnode = x;
		x.Pnode = y;
	}

	public void insert(String query) {
		RedBlackTreeNode node = new RedBlackTreeNode(new SearchQuery(query));
		node.Pnode = null;

		RedBlackTreeNode y = null;
		RedBlackTreeNode x = root;

		while (x != Rnull) {
			y = x;
			if (node.data.query.compareTo(x.data.query) < 0) {
				x = x.Lnode;
			} else if (node.data.query.compareTo(x.data.query) > 0) {
				x = x.Rnode;
			} else {
				// If the query already exists, the frequency will be updated
				x.data.incrementFrequency();
				return;
			}
		}

		node.Pnode = y;
		if (y == null) {
			root = node; // Tree was empty, new node is root
		} else if (node.data.query.compareTo(y.data.query) < 0) {
			y.Lnode = node;
		} else {
			y.Rnode = node;
		}

		node.Lnode = Rnull;
		node.Rnode = Rnull;
		node.color = 1; // New node is red

		fixInsert(node); // Fix the tree
	}

	public List<SearchQuery> findMostFrequent() {
		List<SearchQuery> topQueries = new ArrayList<>();
		findMostFrequent(root, topQueries);
		return topQueries;
	}

	private void findMostFrequent(RedBlackTreeNode node, List<SearchQuery> topQueries) {
		if (node == Rnull) {
			return;
		}

		// First we find the maximum frequency
		findMostFrequent(node.Lnode, topQueries);
		if (topQueries.isEmpty() || node.data.frequency > topQueries.get(0).frequency) {
			topQueries.clear();
			topQueries.add(node.data);
		} else if (node.data.frequency == topQueries.get(0).frequency) {
			topQueries.add(node.data);
		}
		findMostFrequent(node.Rnode, topQueries);
	}
}

public class SearchFrequency {
	private static SearchQueryTree tree;

	public static void setup() {
		tree = new SearchQueryTree();
	}
	
	public static void add(String input) {

		for (String word : input.split(" ")) {

			tree.insert(word);
		}

	}

	public static void getFrequency() {
		List<SearchQuery> mostFrequent = tree.findMostFrequent(); // will return the multiple searches if the searches
																	// are equal
		if (!mostFrequent.isEmpty()) {
			System.out.println("\nThe most frequent searches are:");
			for (SearchQuery queries : mostFrequent) {

				System.out.println(queries);

			}
		} else {
			System.out.println("No searches");
		}
	}
}
