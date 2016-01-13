package org.cyberanima.iii.algorithm.nlp.stanford;

import java.util.ArrayList;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;

public class SentenceInfo {
	private ArrayList<String[]> wordposes;
	private Tree tree;
	private SemanticGraph dependencies;
	
	public ArrayList<String[]> getWordposes() {
		return this.wordposes;
	}
	public void setWordposes(ArrayList<String[]> wordposes) {
		this.wordposes = wordposes;
	}
	public Tree getTree() {
		return this.tree;
	}
	public void setTree(Tree tree) {
		this.tree = tree;
	}
	public SemanticGraph getDependencies() {
		return this.dependencies;
	}
	public void setDependencies(SemanticGraph dependencies) {
		this.dependencies = dependencies;
	}
}
