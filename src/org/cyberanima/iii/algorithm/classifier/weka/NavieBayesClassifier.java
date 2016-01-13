package org.cyberanima.iii.algorithm.classifier.weka;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;

public class NavieBayesClassifier {
	public static NaiveBayes TrainClassiferFromARFF(String filename, int classIndex, int ignoreIndex) {
		LabeledInstances li = WekaUtils.GetInstancesSetFromARFF(filename, 2/3f, classIndex, ignoreIndex);
		
		NaiveBayes nb = new NaiveBayes();
		
		try {
			nb.buildClassifier(li.getTrainSet());
			Evaluation eval = new Evaluation(li.getTrainSet());
			eval.evaluateModel(nb, li.getTestSet());
			System.out.println(eval.toSummaryString("\nResults\n\n", false));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return nb;
	}
	
	public static NaiveBayes TrainClassiferFromARFF(String filename, int classIndex) {
		LabeledInstances li = WekaUtils.GetInstancesSetFromARFF(filename, 2/3f, classIndex);
		
		NaiveBayes nb = new NaiveBayes();
		
		try {
			nb.buildClassifier(li.getTrainSet());
			Evaluation eval = new Evaluation(li.getTrainSet());
			eval.evaluateModel(nb, li.getTestSet());
			System.out.println(eval.toSummaryString("\nResults\n\n", false));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return nb;
	}
	
	public static NaiveBayes TrainClassiferFromLabeledInstances(LabeledInstances li) {
		NaiveBayes nb = new NaiveBayes();
		
		try {
			nb.buildClassifier(li.getTrainSet());
			Evaluation eval = new Evaluation(li.getTrainSet());
			eval.evaluateModel(nb, li.getTestSet());
			System.out.println(eval.toSummaryString("\nResults\n\n", false));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return nb;
	}
}
