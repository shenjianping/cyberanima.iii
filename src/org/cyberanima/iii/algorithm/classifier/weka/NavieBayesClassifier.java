package org.cyberanima.iii.algorithm.classifier.weka;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;

public class NavieBayesClassifier {
	public static NaiveBayes TrainClassiferFromARFF(String filename) {
		LabeledInstances li = WekaUtils.GetInstancesSetFromARFF(filename, 2/3);
		
		NaiveBayes nb = new NaiveBayes();
		
		try {
			nb.buildClassifier(li.getTrainSet());
			Evaluation eval = new Evaluation(li.getTrainSet());
			eval.evaluateModel(nb, li.getTestSet());
			System.out.println(eval.toSummaryString("\nResults\n\n", false));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return nb;
	}
}
