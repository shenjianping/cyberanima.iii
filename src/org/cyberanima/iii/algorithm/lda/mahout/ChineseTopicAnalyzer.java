package org.cyberanima.iii.algorithm.lda.mahout;

import org.cyberanima.iii.algorithm.nlp.stanford.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.lda.cvb.CVB0Driver;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.utils.vectors.RowIdJob;

public class ChineseTopicAnalyzer {
	public static void Analyze(String docFileDirectory, String seqFileDirectory, String vecFileDirectory, String matFileDirectory, String disbFileDirectory){		
		
		
		
		
		//Convert SequenceFile<Text, VectorWritable> to  SequenceFile<IntWritable, VectorWritable>
		RowID(vecFileDirectory, matFileDirectory);
		
		//Invoking the CVB algorithm
		CVBInvocation(vecFileDirectory, matFileDirectory, disbFileDirectory);
		
	}
	
	public static void DocSegmentFromDir(String docFileDirectory, String segDocFileDirectory) {
		File segDocDir = new File(docFileDirectory);
		File[] segDocFiles  = segDocDir.listFiles();
		
		for (int i = 0; i < segDocFiles.length; i++) {
			String fileName = segDocFiles[i].getName();
			ChineseParser.Segment(docFileDirectory + fileName, segDocFileDirectory + fileName);
		}
	}
	
	
	public static void GenSparseVecFromSegDocDir(String segDocFileDirectory, String vecFileDirectory) {
			
		HashMap<Text, VectorWritable> tfVecHM = new HashMap<Text, VectorWritable>();
		
		try {
			File segDocDir = new File(segDocFileDirectory);
			File[] segDocFiles  = segDocDir.listFiles();
			String segDocTextAll = "";
			String[] segDocText = new String[segDocFiles.length];
			
			for (int i = 0; i < segDocFiles.length; i++) {			
				FileInputStream f = new FileInputStream(segDocFiles[i]); 
				BufferedReader br = new BufferedReader(new InputStreamReader(f));
				String line = br.readLine();
				StringBuilder sb = new StringBuilder();
				
				while (line != null ) { 
					sb.append(line);
					line = br.readLine();
				}
				
				segDocText[i] = sb.toString(); // add the input string
				segDocTextAll += sb.toString();		
				
				br.close();
				f.close();				
			}
			
			String[] segDocWordsAll = segDocTextAll.split(" ");
			HashMap<Integer, String> dicHM = new HashMap<Integer, String>();
			HashMap<String, Integer> dicHMRev = new HashMap<String, Integer>();
			HashMap<Integer, Long> freqHM = new HashMap<Integer, Long>();
			
			for (int i = 0; i < segDocWordsAll.length; i++) {
				String segDocWord = segDocWordsAll[i];
				int dicHMRevValue = -1; 
				if (!dicHMRev.containsKey(segDocWord)) {
					dicHMRevValue = dicHMRev.size();
					dicHMRev.put(segDocWord, dicHMRevValue);
					//System.out.println(segDocWord + ": " + dicHMRev.get(segDocWord) + " (" + dicHMRev.size() + ") -- NOT Contain");
				} else {
					dicHMRevValue = dicHMRev.get(segDocWord);
					//System.out.println(segDocWord + ": " + dicHMRev.get(segDocWord) + " (" + dicHMRev.size() + ") -- Contain");
				}
				
				
				long freqHMValue = -1;
				if (!freqHM.containsKey(dicHMRevValue)) {
					freqHMValue = 1;
					freqHM.put(dicHMRevValue, freqHMValue);
				} else {
					freqHMValue = freqHM.get(dicHMRevValue) + 1;
					freqHM.replace(dicHMRevValue, freqHMValue);
				}
				

			}
			
			Iterator iter = dicHMRev.entrySet().iterator();
			while (iter.hasNext()) {
				HashMap.Entry entry = (HashMap.Entry) iter.next();
				int key = Integer.parseInt(entry.getValue().toString());
				String val = entry.getKey().toString();
				dicHM.put(key, val);
			}
			
			for (int i = 0; i < segDocText.length; i++) {
				String[] segDocWords = segDocText[i].split(" ");
				Vector tfVector = new RandomAccessSparseVector(dicHM.size());
				
				for (int j = 0; j < segDocWords.length; j++) {
					String segDocWord = segDocWords[j];
					if (dicHMRev.containsKey(segDocWord)) {
						int dicIndex = dicHMRev.get(segDocWord);
						if (freqHM.containsKey(dicIndex)) {
							tfVector.set(dicIndex, freqHM.get(dicIndex));
						}		
					}
				}
				
				tfVecHM.put(new Text(segDocFiles[i].getName()), new VectorWritable(tfVector));		
			}
			
			// Write the hashmaps into the sequencefile
			
			SequenceFileUtils.WriteTextIntSeqFileByHashMap(dicHM, vecFileDirectory + "dictionary.file-0");
			SequenceFileUtils.WriteIntLongSeqFileByHashMap(freqHM, vecFileDirectory + "frequency.file-0");
			SequenceFileUtils.WriteTextVectorSeqFileByHashMap(tfVecHM, vecFileDirectory + "tf-vectors/part-r-00000");
			
			PrintHashMap(dicHMRev);
			//PrintHashMap(freqHM);
			//PrintHashMap(tfVecHM);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void PrintHashMap(HashMap hm) {
		Iterator iter = hm.entrySet().iterator();
		System.out.println("Print HashMap: Total " + hm.size());
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			System.out.println("Key " + key.toString() + ": " + val.toString());
		}
	}
	
	//Convert SequenceFile<Text, VectorWritable> to  SequenceFile<IntWritable, VectorWritable>
	public static void RowID(String vecFileDirectory, String matFileDirectory)	{
		
		try {
			String[] para = {"--input",vecFileDirectory + "/tf-vectors/", "--output",matFileDirectory};
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
	    
	    System.out.println("Start to get top topics: " + docTopTopics.size());
	 
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
