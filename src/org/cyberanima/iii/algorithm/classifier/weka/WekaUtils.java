package org.cyberanima.iii.algorithm.classifier.weka;

import java.util.Random;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaUtils {
	public static LabeledInstances GetInstancesSetFromARFF(String filename, float pctTrain) {
		LabeledInstances lblInstances = new LabeledInstances();
		Instances labeledData;
		
		try {
			labeledData = DataSource.read(filename);
			labeledData = labeledData.resample(new Random());
			labeledData.setClassIndex(labeledData.size() - 1);
			int trainSetSize = Math.round(pctTrain * labeledData.size());
			int testSetSize = labeledData.size() - trainSetSize;

			lblInstances.setTrainSet(new Instances(labeledData, 0, trainSetSize));
			lblInstances.setTestSet(new Instances(labeledData, trainSetSize - 1, testSetSize));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return lblInstances;
	}
}
