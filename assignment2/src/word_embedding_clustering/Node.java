package word_embedding_clustering;

import java.util.ArrayList;

public class Node {
	public boolean isTerminal;
	
	public double similarity;
	public String word;
	
	public Node parent;
	public ArrayList<Node> children;	
	
	public Node(String word) {
		this.word = word;
		this.isTerminal = true;
		this.parent = null;
	}
	
	public Node(double similarity) {
		this.similarity = similarity;
		this.isTerminal = false;
		this.children = new ArrayList<Node>();
		this.parent = null;
	}
}
