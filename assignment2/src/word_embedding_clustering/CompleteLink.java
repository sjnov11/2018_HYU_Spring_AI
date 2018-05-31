package word_embedding_clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class CompleteLink {
	public Node root;
	//private HashMap<Node, Node> nodeParent;
	private HashMap<String, Node> wordNode;
	private HashMap<Double, Node> similarityNode;
	
	public CompleteLink() {		
		this.root = null;
		//this.nodeParent = new HashMap<Node, Node>();
		this.wordNode = new HashMap<String, Node>();
		this.similarityNode = new HashMap<Double, Node>();
	}
	
	public void addNode(String w1, String w2, double similarity) {
		// TODO: similarity가 같을 경우의 처리
		// 1. word에 대한 node가 모두 없을 때
		if (!wordNode.containsKey(w1) && !wordNode.containsKey(w2)) {
			Node top = new Node(similarity);
			Node left = new Node(w1);
			Node right = new Node(w2);
			
			top.children.add(left);
			top.children.add(right);			
			left.parent = top;
			right.parent = top;
			
			this.similarityNode.put(similarity, top);
			this.wordNode.put(w1, left);
			this.wordNode.put(w2, right);		
		}
		// 2. word에 대한 node가 첫번째 w1만 있을 때
		else if (wordNode.containsKey(w1) && !wordNode.containsKey(w2)){
			Node top;
			Node right = new Node(w2);
			if (this.similarityNode.containsKey(similarity)) {
				top = this.similarityNode.get(similarity);
				top.children.add(right);
				right.parent = top;
				this.wordNode.put(w2, right);
			}
			else {
				top = new Node(similarity);
				Node left = wordNode.get(w1);
				while(left.parent != null) {
					left = left.parent;
				}
				
				top.children.add(left);
				top.children.add(right);
				left.parent = top;
				right.parent = top;
				
				this.similarityNode.put(similarity, top);
				this.wordNode.put(w2, right);
			}	
		}
		// 3. word에 대한 node가 두번째 w2만 있을 때
		else if(!wordNode.containsKey(w1) && wordNode.containsKey(w2)) {
			Node top;
			Node right = new Node(w1);
			if (this.similarityNode.containsKey(similarity)) {
				top = this.similarityNode.get(similarity);
				top.children.add(right);
				right.parent = top;
				this.wordNode.put(w1, right);
			}
			else {
				top = new Node(similarity);
				Node left = wordNode.get(w2);
				while(left.parent != null) {
					left = left.parent;
				}
				
				top.children.add(left);
				top.children.add(right);
				left.parent = top;
				right.parent = top;
				
				this.similarityNode.put(similarity, top);
				this.wordNode.put(w1, right);
			}
		}
		// 4. word에 대한 node가 모두 있을 때
		else {
			Node top = new Node(similarity);
			Node left = wordNode.get(w1);
			Node right = wordNode.get(w2);
			
			while(left.parent != null) {
				left = left.parent;
			}
			while(right.parent != null) {
				right = right.parent;
			}
			
			top.children.add(left);
			top.children.add(right);
			
			left.parent = top;
			right.parent = top;
			
			this.similarityNode.put(similarity, top);
		}
	}
	
	// w 는 마지막에 add되는 node
	public void setRoot(String w) {
		Node n = wordNode.get(w);
		this.root = n.parent;
		
		while (this.root.parent != null) {
			this.root = this.root.parent;
		}
	}
	
	public void printAll () {		
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(root);
		
		while(!queue.isEmpty()) {
			Node next = queue.remove();
			if (next.isTerminal) {
				System.out.println(next.word);
			}
			else {
				System.out.println(next.similarity);
			}		
			
			if(next.children == null)
				continue;
			Iterator<Node> it = next.children.iterator();
			while (it.hasNext()) {
				queue.add(it.next());
			}
		}
		
	}
	
	public LinkedList<String> getCheckWords(String w1, String w2) {
		Node n1 = wordNode.get(w1);
		Node n2 = wordNode.get(w2);
		LinkedList<String> n1_words = new LinkedList<String>();
		LinkedList<String> n2_words = new LinkedList<String>();
		LinkedList<String> result = new LinkedList<String>();
		
		if (n1 != null) {
			while (n1.parent != null) {
				n1 = n1.parent;
			}
			this.getDescendentWords(n1, n1_words);
		}
		
		if (n2 != null) {
			while (n2.parent != null) {
				n2 = n2.parent;
			}
			this.getDescendentWords(n2, n2_words);
		}
//		Iterator<String> r = result.iterator();
//		System.out.println("result :");
//		while(r.hasNext()) {
//			System.out.println(r.next());
//		}
		Iterator<String> n1_words_it = n1_words.iterator();
		Iterator<String> n2_words_it = n2_words.iterator();
//		while (n1_words_it.hasNext()) {
//			System.out.println(w1 + "노드의 하위 단어 :" + n1_words_it.next());
//		}
//		while (n2_words_it.hasNext()) {
//			System.out.println(w2 + "노드의 하위 단어:" + n2_words_it.next());
//		}
//		n1_words_it = n1_words.iterator();
//		n2_words_it = n2_words.iterator();
		
		
		if (n1_words.size() > 0 && n2_words.size() >0) {
			while (n1_words_it.hasNext()) {
				String n1_word = n1_words_it.next();
				while (n2_words_it.hasNext()) {
					String n2_word = n2_words_it.next();
					result.add(this.concatAlphaOrder(n1_word, n2_word));
				}
				n2_words_it = n2_words.iterator();
			}		
		} else if (n1_words.size() > 0 && n2_words.size() == 0) {
			while (n1_words_it.hasNext()) {
				String n1_word = n1_words_it.next();
				result.add(this.concatAlphaOrder(n1_word, w2));
			}
		} else if (n1_words.size() == 0 && n2_words.size() > 0) {
			while (n2_words_it.hasNext()) {
				String n2_word = n2_words_it.next();
				result.add(this.concatAlphaOrder(w1, n2_word));
			}
		}
		return result;
		
	}
	private void getDescendentWords(Node node, LinkedList<String> result) {
		if (node.isTerminal) {
			result.add(node.word);
		}
		else {
			Iterator<Node> child_it = node.children.iterator();
			while (child_it.hasNext()) {
				this.getDescendentWords(child_it.next(), result);
			}
		}
	}
	/*
	private void concatDescendent(Node node, String word, LinkedList<String> result, String cur) {
		if (node.isTerminal) {
			if (node.word != cur) {
				result.add(concatAlphaOrder(node.word, word));
			}
		}
		
		else {
			Iterator<Node> child_it = node.children.iterator();
			while (child_it.hasNext()) {
				this.concatDescendent(child_it.next(), word, result, cur);
			}
		}
		
	}
	*/
	private String concatAlphaOrder(String w1, String w2) {
		if (w1.compareTo(w2) > 0) {
//			System.out.println("[concat1]: " +w2+w1);
			return w2 + w1;
		}
		else {
//			System.out.println("[concat2]: " +w1+w2);
			return w1 + w2;
		}
	}
}
