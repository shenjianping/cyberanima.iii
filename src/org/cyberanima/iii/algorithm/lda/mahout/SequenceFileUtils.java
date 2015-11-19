package org.cyberanima.iii.algorithm.lda.mahout;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class SequenceFileUtils {
	
	// Print a sequence file with Int key and Text value
	public static void PrintTextIntSeqFile(String seqFilePath) throws IOException {
	    Configuration conf = new Configuration();
	    SequenceFile.Reader seqReader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(seqFilePath)));
	    IntWritable seqKey = new IntWritable();
	    Text seqText = new Text();
	  
	    System.out.println("Start to Print Text Sequence File");
	    
	    while (seqReader.next(seqText, seqKey)) {
	    	System.out.println("Key " + seqKey + ": " + seqText.toString());
	    }
	 
	    seqReader.close();
	}
	
	public static void WriteTextIntSeqFileByHashMap(HashMap hm, String uri) throws IOException {
		Configuration conf = new Configuration();
		
		
		SequenceFile.Writer writer = SequenceFile.createWriter(conf,
				SequenceFile.Writer.file(new Path(uri)), SequenceFile.Writer.keyClass(Text.class),
				SequenceFile.Writer.valueClass(IntWritable.class));
		
		Iterator iter = hm.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			int hmKey = Integer.parseInt(entry.getKey().toString());
			String hmVal = entry.getValue().toString();
			writer.append(new Text(hmVal), new IntWritable(hmKey));
		}
		
		writer.close();
	}
	
	// Print a sequence file with Int key and Long value
	public static void PrintIntLongSeqFile(String seqFilePath) throws IOException {
	    Configuration conf = new Configuration();
	    SequenceFile.Reader seqReader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(seqFilePath)));
	    IntWritable seqKey = new IntWritable();
	    LongWritable seqLong = new LongWritable();
	  
	 
	    System.out.println("Start to Print Long Sequence File");
	    while (seqReader.next(seqKey, seqLong)) {
	    	System.out.println("Key " + seqKey + ": " + seqLong.toString());
	    }
	 
	    seqReader.close();
	}
	
	public static void WriteIntLongSeqFileByHashMap(HashMap hm, String uri) throws IOException {
		Configuration conf = new Configuration();
		
		SequenceFile.Writer writer = SequenceFile.createWriter(conf,
				SequenceFile.Writer.file(new Path(uri)), SequenceFile.Writer.keyClass(IntWritable.class),
				SequenceFile.Writer.valueClass(LongWritable.class));
		
		Iterator iter = hm.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			int hmKey = Integer.parseInt(entry.getKey().toString());
			long hmVal = Long.parseLong(entry.getValue().toString());
			writer.append(new IntWritable(hmKey), new LongWritable(hmVal));
		}
		
		writer.close();
	}
	
	// Print a sequence file with Int key and Vector value
	public static void PrintIntVectorSeqFile(String seqFilePath) throws IOException {
	    Configuration conf = new Configuration();
	    SequenceFile.Reader seqReader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(seqFilePath)));
	    IntWritable seqKey = new IntWritable();
	    VectorWritable seqVector = new VectorWritable();
	    
	    System.out.println("Start to Print Vector Sequence File");
	    while (seqReader.next(seqKey, seqVector)) {
	    	System.out.println("Key " + seqKey + ": " + seqVector.toString());
	    }
	 
	    seqReader.close();
	}
	
	// Print a sequence file with Text key and Vector value
	public static void PrintTextVectorSeqFile(String seqFilePath) throws IOException {
	    Configuration conf = new Configuration();
	    SequenceFile.Reader seqReader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(seqFilePath)));
	    Text seqKey = new Text();
	    VectorWritable seqVector = new VectorWritable();
	    
	    System.out.println("Start to Print Vector Sequence File");
	    while (seqReader.next(seqKey, seqVector)) {
	    	System.out.println("Key " + seqKey + ": " + seqVector.toString());
	    }
	 
	    seqReader.close();
	}
	
	public static void WriteTextVectorSeqFileByHashMap(HashMap hm, String uri) throws IOException {
		Configuration conf = new Configuration();
		
		SequenceFile.Writer writer = SequenceFile.createWriter(conf,
				SequenceFile.Writer.file(new Path(uri)), SequenceFile.Writer.keyClass(Text.class),
				SequenceFile.Writer.valueClass(VectorWritable.class));
		
		Iterator iter = hm.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			String hmKey = entry.getKey().toString();
			VectorWritable hmVal = (VectorWritable) entry.getValue();
			writer.append(new Text(hmKey), hmVal);
		}
		
		writer.close();
	}
}
