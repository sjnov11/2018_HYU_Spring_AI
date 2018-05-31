package word_embedding_clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;

class QueueNode implements Comparable<QueueNode> {
	String word1;
	String word2;
	double similarity;
	
	QueueNode(String word1, String word2, double similarity) {
		this.word1 = word1;
		this.word2 = word2;
		this.similarity = similarity;
	}

	@Override
	public int compareTo(QueueNode target) {
		// TODO Auto-generated method stub
		if (this.similarity < target.similarity) {
			return 1;
		} else if (this.similarity > target.similarity) {
			return -1;
		}
		return 0;
	}
}

class WordVectorsTuple {
	String word;
	double[] vectors;
	
	WordVectorsTuple(String word, double[] vectors) {
		this.word = word; 
		this.vectors = vectors;
	}
}

public class Main {
	private String path;
	private String vector_path = "\\WordEmbedding.txt";
	private String topic_path = "\\WordTopic.txt";
	//private HashMap<String, double[]> word_vec_map = new HashMap<String, double[]>();	// (word, 300 dimension vec)
	private ArrayList<WordVectorsTuple> word_vec_list = new ArrayList<WordVectorsTuple>();	// (word, 300 dimension vec)
	private HashMap<String, String> word_topic = new HashMap<String, String>();
	private LinkedList<LinkedList<String>> topic_word = new LinkedList<LinkedList<String>>();
	
	//TODO
	// boolean matrix for check all word 
	public PriorityQueue<QueueNode> cosine_similarity_queue = new PriorityQueue<QueueNode>();
	public PriorityQueue<QueueNode> euclidean_similarity_queue = new PriorityQueue<QueueNode>();
	// TODO : euclidean과 cosine heap 두개 동시에 build
	private Clustering cosine_clustering = new Clustering();
	private Clustering euclidean_clustering = new Clustering();
	
	// path 의 파일을 읽어, (word, 300차원 vector) 정보를 word_to_vec map에 넣는다.
	public void makeWordToVecMap() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path + vector_path));
			
		while(true) {
			String word = br.readLine();
			if (word == null) break;			
			String vector_string = br.readLine();
			StringTokenizer tokens = new StringTokenizer(vector_string);
			double[] vectors = new double[300];
			
			for (int i = 0; i < 300; i++) {
				vectors[i] = Double.parseDouble(tokens.nextToken(",")); 
			}
			this.addWordVecList(word, vectors);			
		}
		br.close();
	}
	
	public void getTopicToWords() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path + topic_path));

		String topic = null; 
		LinkedList<String> words =null;
		while(true) {
			String word = br.readLine();
			if (word == null) break;	
			if (word.contains("[")) {
				topic = word;
				//System.out.println(topic);
				
				words = new LinkedList<String>();
				topic_word.add(words);
				
			}
			else {
				word_topic.put(word.toLowerCase(), topic);
				words.add(word.toLowerCase());
			}		
		}
		br.close();
	}
	
	private void addWordVecList(String word, double[] vectors) {
		WordVectorsTuple tuple = new WordVectorsTuple(word, vectors);
		this.word_vec_list.add(tuple);
	}
	
	
	public void buildCosineSimilarityQueue() {
		for (int i = 0; i < word_vec_list.size(); i++) {
			for (int j = i + 1; j < word_vec_list.size(); j++) {
				addCosineSimilarityQueueNode(word_vec_list.get(i), word_vec_list.get(j));
 			}
		}	
	}
	
	public void buildEuclideanSimilarityQueue() {
		// Get Euclidean distance 
		ArrayList<Double> similarity_list = new ArrayList<Double>();
		double min_val = 999999;
		double max_val = -999999;
		for (int i = 0; i < word_vec_list.size(); i++) {
			for (int j = i + 1; j < word_vec_list.size(); j++) {
				double euclidean_similarity = getEuclideanDistance(word_vec_list.get(i).vectors, word_vec_list.get(j).vectors);

				if (euclidean_similarity > max_val) {
					max_val = euclidean_similarity;
				}
				if (euclidean_similarity < min_val) {
					min_val = euclidean_similarity;
				}
				similarity_list.add(euclidean_similarity);
			}
		}
		// Get normalized similarity
		similarity_list = normalize(similarity_list, max_val, min_val);
		
		int idx = 0;
		for (int i = 0; i < word_vec_list.size(); i++) {
			for (int j = i + 1; j < word_vec_list.size(); j++) {
				addEuclideanSimilarityQueueNode(word_vec_list.get(i), word_vec_list.get(j), similarity_list.get(idx++));
			}
		}
		
	}
	public void addEuclideanSimilarityQueueNode(WordVectorsTuple t1, WordVectorsTuple t2, double similarity) {
		this.euclidean_similarity_queue.add(new QueueNode(t1.word, t2.word, similarity));
	}
	
	public void addCosineSimilarityQueueNode(WordVectorsTuple t1, WordVectorsTuple t2) {
		this.cosine_similarity_queue.add(new QueueNode(t1.word, t2.word, getCosineSimilarity(t1.vectors, t2.vectors)));
	}
	
	public double getCosineSimilarity(double[] vectors1, double[] vectors2) {
		// Cosine Similarity
		double v1_size = 0, v2_size = 0, v1v2_inner = 0;
		for (int i = 0; i < 300; i++) {
			v1_size += Math.pow(vectors1[i], 2);
		}
		v1_size = Math.sqrt(v1_size);		
		for (int i = 0; i < 300; i++) {
			v2_size += Math.pow(vectors2[i], 2);
		}
		v2_size = Math.sqrt(v2_size);
		
		for (int i = 0; i < 300; i++) {
			v1v2_inner += vectors1[i] * vectors2[i]; 
		}
		
		double cosine_similarity = Math.abs(v1v2_inner / (v1_size * v2_size));
		return cosine_similarity;
				
	}
	
	
	private double getEuclideanDistance(double[] vectors1, double[] vectors2) {
		double euclidean_dist = 0;
		for (int i = 0; i < 300; i++) {
			euclidean_dist += Math.pow(vectors1[i] - vectors2[i], 2);
		}
		euclidean_dist = Math.sqrt(euclidean_dist);
		return euclidean_dist;
	}
	
	private ArrayList<Double> normalize(ArrayList<Double> euclidean_dists, double max, double min) {
		ArrayList<Double> normalized = new ArrayList<Double>();
		Iterator<Double> dist_it = euclidean_dists.iterator();
		while(dist_it.hasNext()) {
			normalized.add((dist_it.next() - min) / (max - min));
		}
		return normalized;
	}
	private void buildCosineCompleteLink() {
		this.cosine_clustering.buildCompleteLink(this.cosine_similarity_queue);
	}
	private void buildEuclideanCompleteLink() {
		this.euclidean_clustering.buildCompleteLink(this.euclidean_similarity_queue);
	}
	private void clusteringWordsByCosine(double threshold) {
		//this.clustering.buildCompleteLink(this.similarity_max_heap);
		//this.clustering.cl.printAll();
		this.cosine_clustering.clustering(threshold);		
	}
	
	private void clusteringWordsByEuclidean(double threshold) {
		this.euclidean_clustering.clustering(threshold);
	}
	
	private void printOutCosineClusteringResult (double similarity) throws IOException {
		PrintWriter pw = new PrintWriter(path + "\\cosine" + similarity + ".txt");
		Iterator<WordVectorsTuple> word_vec_it = word_vec_list.iterator();
		
		while (word_vec_it.hasNext()) {
			WordVectorsTuple cur = word_vec_it.next();
			int cluster_num = this.cosine_clustering.word_to_cluster_num.get(cur.word);
			pw.println(cur.word);
			for (int i = 0; i < 299; i++) {
				pw.format("%.8e",cur.vectors[i]);
				pw.print(",");
			}
			pw.println(cur.vectors[299]);
			pw.println(cluster_num);
		}
		
		pw.close();
	}
	
	private void printOutEuclideanClusteringResult (double similarity) throws IOException {
		PrintWriter pw = new PrintWriter(path + "\\euclidean" + similarity + ".txt");
		Iterator<WordVectorsTuple> word_vec_it = word_vec_list.iterator();
		
		while (word_vec_it.hasNext()) {
			WordVectorsTuple cur = word_vec_it.next();
			int cluster_num = this.euclidean_clustering.word_to_cluster_num.get(cur.word);
			pw.println(cur.word);
			for (int i = 0; i < 299; i++) {
				pw.format("%.8e",cur.vectors[i]);
				pw.print(",");
			}
			pw.println(cur.vectors[299]);
			pw.println(cluster_num);
		}
		
		pw.close();
	}
	
	
	public static void main (String args[]) throws IOException {
		Main m = new Main();
		Scanner sc = new Scanner(System.in);
		m.path = sc.nextLine();
		sc.close();
		m.makeWordToVecMap();
		m.getTopicToWords();
		m.buildCosineSimilarityQueue();
		m.buildEuclideanSimilarityQueue();
		m.buildCosineCompleteLink();
		m.buildEuclideanCompleteLink();
		
		
		
		double[] threshold_list = {0.2, 0.4, 0.6, 0.8};
		
		for (int i = 0 ; i < 4; i++) {
			System.out.println("Threshold : " + threshold_list[i]);
			m.clusteringWordsByCosine(threshold_list[i]);
			m.clusteringWordsByEuclidean(threshold_list[i]);
			m.printOutCosineClusteringResult(threshold_list[i]);
			m.printOutEuclideanClusteringResult(threshold_list[i]);
//			System.out.println(m.cosine_clustering.cluster.size());
//			System.out.println(m.euclidean_clustering.cluster.size());
			double cosine_entropy = m.cosine_clustering.getTotalClusterEntropy(m.word_topic);
			System.out.println("Cosine Entropy : " + cosine_entropy);
			double euclidean_entropy = m.euclidean_clustering.getTotalClusterEntropy(m.word_topic);
			System.out.println("Euclidean Entropy : " + euclidean_entropy);
		}
		
		
		
//		m.clusteringWordsByEuclidean(0.2);
//		m.buildSimilarityMaxHeap();
//		m.clusteringWords(0.8);

		//m.clustering.printClusters();
		
//		Iterator<String> it = m.word_topic.keySet().iterator();
//		int cnt = 0;
//		
//		while (it.hasNext()) {
//			String cur = it.next();
//			System.out.println("[" + cnt++ + "]" +cur + ": "+  m.word_topic.get(cur));
//		}
		
//		double euclidean_entropy = m.euclidean_clustering.getTotalClusterEntropy(m.word_topic);
//		System.out.println(euclidean_entropy);
//		m.clusteringWords(0.8);
//		 entropy = m.clustering.getTotalClusterEntropy(m.word_topic);
//		System.out.println(entropy);
	}
}
