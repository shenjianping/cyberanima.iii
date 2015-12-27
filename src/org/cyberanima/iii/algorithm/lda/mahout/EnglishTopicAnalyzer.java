package org.cyberanima.iii.algorithm.lda.mahout;

import java.io.IOException;
import java.net.URI;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.utils.vectors.RowIdJob;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;
import org.apache.mahout.clustering.lda.cvb.CVB0Driver;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class EnglishTopicAnalyzer {
	public static void Analyze(String docFileDirectory, String seqFileDirectory, String vecFileDirectory, String matFileDirectory, String disbFileDirectory){		
		
		//Sequence from a directory of text documents
		SeqDirectory(docFileDirectory, seqFileDirectory);
		
		//Creating vectors from sequenceFile
		Seq2Sparse(seqFileDirectory, vecFileDirectory);
		
		//Convert SequenceFile<Text, VectorWritable> to  SequenceFile<IntWritable, VectorWritable>
		RowID(vecFileDirectory, matFileDirectory);
		
		//Invoking the CVB algorithm
		CVBInvocation(vecFileDirectory, matFileDirectory, disbFileDirectory);
		
	}
	
	//Sequence from a Directory of text documents
	public static void SeqDirectory(String docFileDirectory, String seqFileDirectory) {
		try {
			String[] para = {"--input", docFileDirectory, "--output", seqFileDirectory, "--overwrite", "--method", "sequential"};
		    int x = new SequenceFilesFromDirectory().run(para);
		    System.out.println("Sequence From A Directory of Text documents£º" + x);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Creating vectors from sequenceFile
	public static void Seq2Sparse(String seqFileDirectory, String vecFileDirectory)	{
		
		try {
			String[] para = {"--input",seqFileDirectory, "--output",vecFileDirectory, "--weight","TFIDF","--overwrite"};
			int x = new SparseVectorsFromSequenceFiles().run(para);
			System.out.println("Creating Vectors from SequenceFile£º" + x);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Convert SequenceFile<Text, VectorWritable> to  SequenceFile<IntWritable, VectorWritable>
	public static void RowID(String vecFileDirectory, String matFileDirectory)	{
		
		try {
			String[] para = {"--input",vecFileDirectory + "/tfidf-vectors/", "--output",matFileDirectory};
			int x = new RowIdJob().run(para);
			System.out.println("Creating Matrix from Vectors£º" + x);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Invoking the CVB algorithm
	public static void CVBInvocation(String dicFileDirectory, String matFileDirectory, String cvbFileDirectory) {
		try {
			try {
				FileUtils.deleteDirectory(new File(cvbFileDirectory + "distributes"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				FileUtils.deleteDirectory(new File(cvbFileDirectory + "text_cvb_document"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				FileUtils.deleteDirectory(new File(cvbFileDirectory + "text_states"));
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
			
			String[] para = {"-i", matFileDirectory + "matrix", "-dict", dicFileDirectory + "dictionary.file-0", 
					"-o", cvbFileDirectory + "distributes", "-dt", cvbFileDirectory + "text_cvb_document", 
					"-k", "20", "-nt", "29536", "-mt", cvbFileDirectory + "text_states",
					"-x", "5", "-mipd", "5"};
			int x = new CVB0Driver().run(para);
			System.out.println("Invoking the CVB algorithm£º" + x);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<IntWritable, TopTopics> GetTopTopics(int n, String cvbFileDirectory) throws IOException {
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(cvbFileDirectory + "text_cvb_document"), conf);
	    SequenceFile.Reader seqReader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(cvbFileDirectory + "text_cvb_document/part-m-00000")));
	    IntWritable seqKey = new IntWritable();
	    VectorWritable seqVector = new VectorWritable();
	    HashMap<IntWritable, TopTopics> docTopTopics = new HashMap<IntWritable, TopTopics>();
	    
	    //SequenceFile.Sorter seqSorter = new SequenceFile.Sorter(fs, IntWritable.class, VectorWritable.class, conf);
	    
	    while (seqReader.next(seqKey, seqVector)) {
	    	Vector vSource = seqVector.get();
	    	if (n > vSource.size()) {
	    		n = vSource.size();
	    	}	    	
	    	
	    	HashMap<Integer, Pair<Integer, Double>> topics = new HashMap<Integer, Pair<Integer, Double>>();
	    	for (int i = 0; i < n; i++) {
	    		topics.put(i, new Pair<Integer, Double>(vSource.maxValueIndex(), vSource.maxValue()));
	    		vSource.set(vSource.maxValueIndex(), -2);
	    	}
	    	
	    	TopTopics topTopics = new TopTopics();
	    	topTopics.setDocKey(new IntWritable(Integer.parseInt(seqKey.toString())));
	    	topTopics.setTopics(topics);
	    	docTopTopics.put(new IntWritable(Integer.parseInt(seqKey.toString())), topTopics);
	    }
	 
	    seqReader.close();
	    return docTopTopics;
	}
	
	public static void PrintTopTopics(String cvbFileDirectory, HashMap<IntWritable, TopTopics> docTopTopics) throws IOException {
		Configuration conf = new Configuration();
		SequenceFile.Reader seqReader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(cvbFileDirectory + "text_cvb_document/part-m-00000")));
	    IntWritable seqKey = new IntWritable();
	    VectorWritable seqVector = new VectorWritable();
	    
	    while (seqReader.next(seqKey, seqVector)) {
	    	try {
		    	TopTopics topTopics = docTopTopics.get(seqKey);
		    	HashMap<Integer, Pair<Integer, Double>> topics = topTopics.topics();
		    	System.out.println("Key " + topTopics.docKey().toString() + ":");
		    	for (int j = 0; j < topics.size(); j++) {
		    		Pair<Integer, Double> topic = topics.get(j);
		    		System.out.println(topic.getFirst().toString() + ":" + topic.getSecond().toString());
		    	}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    seqReader.close();
	}
}
