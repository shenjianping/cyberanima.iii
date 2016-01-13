package org.cyberanima.iii.algorithm.classifier.weka;

import weka.core.Instances;

public class LabeledInstances {
	private Instances trainSet;
	private Instances testSet;
	
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
