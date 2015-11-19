package org.cyberanima.iii.algorithm.lda.mahout;

import java.util.HashMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.mahout.common.Pair;

public class TopTopics {
	private IntWritable docKey;
	private HashMap<Integer, Pair<Integer, Double>> topics;
	
	public IntWritable docKey() {
		return docKey;
	}
	public void setDocKey(IntWritable docKey) {
		this.docKey = docKey;
	}
	public HashMap<Integer, Pair<Integer, Double>> topics() {
		return topics;
	}
	public void setTopics(HashMap<Integer, Pair<Integer, Double>> topics) {
		this.topics = topics;
	}
}
