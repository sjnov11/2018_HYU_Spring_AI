package word_embedding_clustering;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class Clustering {
	public HashMap<String, Integer> word_to_cluster_num;	// not used (word, clusterNumber) 
	public HashMap<Integer, ArrayList<String>> cluster;		// (clusterNumber, words)	
	private HashSet<String> checklist;

	private int cluster_cnt = 0;
	
	public CompleteLink cl;
	
	public Clustering() {
		this.word_to_cluster_num = new HashMap<String, Integer>();
		this.cluster = new HashMap<Integer, ArrayList<String>>();
		this.checklist = new HashSet<String>();
		this.cl = new CompleteLink();
	}
	
	// �Է¹��� max heap���� ���� complete link clustering ����
	public void buildCompleteLink(PriorityQueue<QueueNode> similarity_max_heap) {
		QueueNode node = null;
		while(!similarity_max_heap.isEmpty()) {
			node = similarity_max_heap.remove();
			//System.out.println(node.word1 +"," +node.word2);
			// checklist�� ���� add
			String concat_word = concatAlphaOrder(node.word1, node.word2);
			checklist.add(concat_word);
			
			if (linkable(node.word1, node.word2)) {
				//System.out.println("linkable");
				this.cl.addNode(node.word1, node.word2, node.similarity);
			}			
		}
		cl.setRoot(node.word1);
	}
	
	// check_words�� check list�� �����ϴ��� �Ǻ�
	private boolean linkable(String w1, String w2) {
		LinkedList<String> check_words = cl.getCheckWords(w1, w2);
		Iterator<String> check_word_it = check_words.iterator();
		//System.out.print("check word list : ");
//		while(check_word_it.hasNext()) {
//			String cur = check_word_it.next();
//			//System.out.print(cur+ " ");
//		}
//		check_word_it = check_words.iterator();
		while(check_word_it.hasNext()) {
			String cur = check_word_it.next();
			
			if (!checklist.contains(cur)) {
				//System.out.println("not in list : " + cur);
				return false;
			}
		}
		return true;
	}
	
	private String concatAlphaOrder(String w1, String w2) {
		if (w1.compareTo(w2) > 0) {
			return w2 + w1;
		}
		else {
			return w1 + w2;
		}
	}
	
	// ���� node ������ ��� terminal node�� �־��� clusterNum���� clustering �Ѵ�.
	public void nodeClustering(Node node, int clusterNum) {
		if (!node.isTerminal) {
			Iterator<Node> it = node.children.iterator();
			while(it.hasNext()) {
				nodeClustering(it.next(), clusterNum);
			}
		} else if (node.isTerminal) {
			ArrayList<String> clusted_words = cluster.get(clusterNum);
			// clusterNum�� cluster�� �������� �ʴ� ���� ���� �Ҵ� 
			if (clusted_words == null) {
				clusted_words = new ArrayList<String>();
				cluster.put(clusterNum, clusted_words);
			}
			// cluster�� �ش� �ܾ� �߰�
			clusted_words.add(node.word);
			word_to_cluster_num.put(node.word, clusterNum);
		}
	}
	
	// �־��� threshold ������ clustering �Ѵ�.
	public void clustering(double threshold) {
		this.cluster.clear();
		this.clustering(cl.root, threshold);
	}
	
	// DFS�� complete link structure�� Ž���ϸ鼭 clustering ����
	// terminal node�̰ų�, threshold ���� ū similarity node�� ���
	// �ش� node ������ ��� terminal node�� clustering �Ѵ�.
	private void clustering(Node node, double threshold) {
		if (node.isTerminal) {
			nodeClustering(node, cluster_cnt++);
		} else {
			if (node.similarity > threshold) {
				nodeClustering(node, cluster_cnt++);
			} else {
				Iterator<Node> it = node.children.iterator();
				while (it.hasNext()) {
					clustering(it.next(), threshold);
				}
			}
		}
	}
	
	
	// cluster���� ������ clusted_words 
	// �ش� Cluster�� entropy�� ����Ѵ�.  
	public double getClusterEntropy(ArrayList<String> clusted_words, HashMap<String, String> word_topic) {
		HashMap<String, Integer> topic_cnt = new HashMap<String, Integer>();	// (Topic, ����) Map
		
		// cluster�� �ִ� �ܾ��� topic ������ ��Ƹ���. 
		Iterator<String> word_it = clusted_words.iterator();
		while (word_it.hasNext()) {
			String word = word_it.next();
			String topic = word_topic.get(word);
			//System.out.println(word + ": " +topic);
			if (topic_cnt.containsKey(topic)) {
				int cnt = topic_cnt.get(topic);
				topic_cnt.put(topic, cnt + 1);
			} else {
				topic_cnt.put(topic, 1);
			}
		}
		
		Iterator<String> topic_cnt_it = topic_cnt.keySet().iterator();
		int total_clusted_words = clusted_words.size();
		//System.out.println("total : " + total_clusted_words);
		double entropy = 0;
		while (topic_cnt_it.hasNext()) {
			int cnt = topic_cnt.get(topic_cnt_it.next());		// ���� topic�� ����
			double prob = (double)cnt / (double)total_clusted_words;			// topic probability
			//System.out.println(prob);
			entropy += (-1) * prob * (Math.log(prob)/Math.log(2));		
		}
		//System.out.println("�� Ŭ�������� entropy : " + entropy);
		return entropy;
	}
	
	public double getTotalClusterEntropy(HashMap<String, String> word_topic) {
		Iterator<Integer> cluster_it = cluster.keySet().iterator();
		
		double entropy = 0;
		int total_words = word_topic.size();
		while (cluster_it.hasNext()) {
			ArrayList<String> clusted_words = cluster.get(cluster_it.next());
			int clusted_words_cnt = clusted_words.size();
			entropy += ((double)clusted_words_cnt / (double)total_words) * this.getClusterEntropy(clusted_words, word_topic);
		}
		
		return entropy;
	}
	
	public void evaluateCluster(LinkedList<LinkedList<String>> topic_words) {
		Iterator<LinkedList<String>> topic_words_it = topic_words.iterator();
		while (topic_words_it.hasNext()) {
			LinkedList<String> words = topic_words_it.next();
			Iterator<String> words_it = words.iterator();
			HashMap<Integer, Integer> cluster_num_cnt = new HashMap<Integer, Integer>();
			while(words_it.hasNext()) {
				String word = words_it.next();
				int cluster_num = word_to_cluster_num.get(word);
				if (cluster_num_cnt.containsKey(cluster_num)) {
					int cnt = cluster_num_cnt.get(cluster_num);
					cluster_num_cnt.put(cluster_num, cnt);
				} else
					cluster_num_cnt.put(cluster_num, 1);
			}
			int total = cluster_num_cnt.size();
			int max_cnt = 0;
			for (int cnt : cluster_num_cnt.values()) {
				if (cnt > max_cnt) {
					max_cnt = cnt;
				}
			}
			System.out.println(max_cnt + "/" + total);
		}
	}
	
	public void printClusters() {
		System.out.println("Print!");
		Iterator<Integer> it = this.cluster.keySet().iterator();
		System.out.println(this.cluster.size());
		while (it.hasNext()) {
			int cluster_num = it.next();
			System.out.println("cluster num : " + cluster_num);
			ArrayList<String> words = this.cluster.get(cluster_num);
			Iterator<String> word = words.iterator();
			while (word.hasNext()) {
				System.out.println(cluster_num + ": " +word.next());
			}
		}
	}
	
}
