package org.cyberanima.iii.algorithm.classifier.weka;

import weka.core.Instances;

public class LabeledInstances {
	private Instances rawSet;
	private Instances filteredSet;
	private Instances trainSet;
	private Instances testSet;
	
	public Instances getRawSet() {
		return this.rawSet;
	}
	
	public void setRawSet(Instances rawSet) {
		this.rawSet = rawSet;
	}
	
	public Instances getFilteredSet() {
		return this.filteredSet;
	}
	
	public void setFilteredSet(Instances filteredSet) {
		this.filteredSet = filteredSet;
	}
	
	public Instances getTrainSet() {
		return this.trainSet;
	}
	
	public void setTrainSet(Instances trainSet) {
		this.trainSet = trainSet;
	}
	
	public Instances getTestSet() {
		return this.testSet;
	}
	
	public void setTestSet(Instances testSet) {
		this.testSet = testSet;
	}
}
