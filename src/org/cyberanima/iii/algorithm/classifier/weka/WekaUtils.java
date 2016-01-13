package org.cyberanima.iii.algorithm.classifier.weka;

import java.util.Random;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class WekaUtils {
	public static LabeledInstances GetInstancesSetFromARFF(String filename, float pctTrain, int classIndex, int ignoreIndex) {
		LabeledInstances lblInstances = new LabeledInstances();
		Instances labeledData;
		
		try {
			labeledData = DataSource.read(filename);
			labeledData = labeledData.resample(new Random());
			lblInstances.setRawSet(labeledData);
			labeledData.setClassIndex(classIndex);
			
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = ignoreIndex + 1 + "";
			Remove remove = new Remove();
			remove.setOptions(options);
			remove.setInputFormat(labeledData);
			Instances filteredSet = Filter.useFilter(labeledData, remove);
			lblInstances.setFilteredSet(filteredSet);
			
			filteredSet.setClassIndex(classIndex - 1);
			int trainSetSize = Math.round(pctTrain * filteredSet.size());
			int testSetSize = filteredSet.size() - trainSetSize;

			lblInstances.setTrainSet(new Instances(filteredSet, 0, trainSetSize));
			lblInstances.setTestSet(new Instances(filteredSet, trainSetSize - 1, testSetSize));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return lblInstances;
	}
	
	public static LabeledInstances GetInstancesSetFromARFF(String filename, float pctTrain, int classIndex) {
		LabeledInstances lblInstances = new LabeledInstances();
		Instances labeledData;
		
		try {
			labeledData = DataSource.read(filename);
			labeledData = labeledData.resample(new Random());
			lblInstances.setRawSet(labeledData);
			lblInstances.setFilteredSet(labeledData);
			
			labeledData.setClassIndex(classIndex);
			int trainSetSize = Math.round(pctTrain * labeledData.size());
			int testSetSize = labeledData.size() - trainSetSize;

			lblInstances.setTrainSet(new Instances(labeledData, 0, trainSetSize));
			lblInstances.setTestSet(new Instances(labeledData, trainSetSize - 1, testSetSize));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return lblInstances;
	}
}
