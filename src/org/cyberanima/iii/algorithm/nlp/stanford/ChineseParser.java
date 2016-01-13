package org.cyberanima.iii.algorithm.nlp.stanford;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.hcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.*;

public class ChineseParser {
	public static void Parse(String inputFileName){
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
		String[] args = new String[]{
			      "-props", "edu/stanford/nlp/hcoref/properties/zh-dcoref-default.properties"};
		Properties props = StringUtils.argsToProperties(args);
	    
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    // read some text in the text variable
	    // read the file in and turn in into a string
	    String text = "";
	    try {
			FileInputStream f = new FileInputStream(inputFileName); 
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
			String line = br.readLine();
			StringBuilder sb = new StringBuilder();
			
			while (line != null ) { 
				sb.append(line + "\n");
				line = br.readLine();
			}
			text = sb.toString(); // add the input string
			System.out.println(sb.toString());
			
			br.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("no file found");
		} catch (IOException e) {
			System.out.println("io exception");
		}
	     
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        // this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        // this is the POS tag of the token
	        String pos = token.get(PartOfSpeechAnnotation.class);
	        // this is the NER label of the token
	        String ne = token.get(NamedEntityTagAnnotation.class);    
	        
	        System.out.println(word + " " + pos + " " + ne);
	      }

	      // this is the parse tree of the current sentence
	      Tree tree = sentence.get(TreeAnnotation.class);

	      // this is the Stanford dependency graph of the current sentence
	      SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	    }

	    // This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	    Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
	}
	
	public static void Segment(String inputFileName, String outputFileName) {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
		String[] args = new String[]{
			      "-props", "edu/stanford/nlp/hcoref/properties/zh-dcoref-default.properties"};
		Properties props = StringUtils.argsToProperties(args);
	    
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    // read some text in the text variable
	    // read the file in and turn in into a string
	    String text = "";
	    try {
			FileInputStream f = new FileInputStream(inputFileName); 
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
			String line = br.readLine();
			StringBuilder sb = new StringBuilder();
			
			while (line != null ) { 
				sb.append(line + "\n");
				line = br.readLine();
			}
			text = sb.toString(); // add the input string
			//System.out.println(sb.toString());
			
			br.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("no file found");
		} catch (IOException e) {
			System.out.println("io exception");
		}
	     
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    String outputText = "";
	    
	    for(CoreMap sentence: sentences) {
	    	// traversing the words in the current sentence
	    	// a CoreLabel is a CoreMap with additional token-specific methods
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		// this is the text of the token
	    		String word = token.get(TextAnnotation.class);
	    		// this is the POS tag of the token
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	        
	    		if (pos.equals("NR") || pos.equals("NN") || pos.equals("VV") || pos.equals("JJ") || pos.equals("VA") ) {
	    			outputText += word + " ";
	    		}
	      	}
	    }
	    
	    try {
			File of = new File(outputFileName);
			FileOutputStream os = new FileOutputStream(outputFileName); 
			if (!of.exists()) {
				of.createNewFile();
			}	    				
			
			byte[] contentInBytes = outputText.getBytes();
			os.write(contentInBytes);
			os.flush();
			os.close();
			
			//System.out.println(outputText);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static StanfordCoreNLP GetPipeline() {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
		String[] args = new String[]{
			      "-props", "edu/stanford/nlp/hcoref/properties/zh-dcoref-default.properties"};
		Properties props = StringUtils.argsToProperties(args);
	    
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    return pipeline;
	}
	
	public static String SegmentString(StanfordCoreNLP pipeline, String inputText) {
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(inputText);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    String outputText = "";
	    
	    for(CoreMap sentence: sentences) {
	    	// traversing the words in the current sentence
	    	// a CoreLabel is a CoreMap with additional token-specific methods
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		// this is the text of the token
	    		String word = token.get(TextAnnotation.class);
	    		// this is the POS tag of the token
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	        
	    		if (pos.equals("NR") || pos.equals("NN") || pos.equals("VV") || pos.equals("JJ") || pos.equals("VA") ) {
	    			outputText += word + "(" + pos + ")" + " ";
	    		}
	      	}
	    }
	    
	    return outputText;
	}
	
	public static ArrayList<String> SegmentStringToArray(StanfordCoreNLP pipeline, String inputText) {
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(inputText);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    ArrayList<String> outputAL = new ArrayList<String>();
	    
	    for(CoreMap sentence: sentences) {
	    	// traversing the words in the current sentence
	    	// a CoreLabel is a CoreMap with additional token-specific methods
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		// this is the text of the token
	    		String word = token.get(TextAnnotation.class);
	    		// this is the POS tag of the token
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	        
	    		if (pos.equals("NR") || pos.equals("NN") || pos.equals("VV") || pos.equals("JJ") || pos.equals("VA") ) {
	    			outputAL.add(word);
	    		}
	      	}
	    }
	    
	    return outputAL;
	}
	
	public static SentenceInfo SegSentence(StanfordCoreNLP pipeline, String sentenceText) {
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(sentenceText);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    ArrayList<String[]> wordposes = new ArrayList<String[]>();
	    
	    SentenceInfo sInfo = new SentenceInfo();
	    
	    for(CoreMap sentence: sentences) {
	    	// traversing the words in the current sentence
	    	// a CoreLabel is a CoreMap with additional token-specific methods
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		// this is the text of the token
	    		String word = token.get(TextAnnotation.class);
	    		// this is the POS tag of the token
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	    		
	    		String[] wordpos = new String[2];
	    		wordpos[0] = word;
	    		wordpos[1] = pos;
	    		wordposes.add(wordpos);
	      	}
	    	
	    	// this is the parse tree of the current sentence
		    Tree tree = sentence.get(TreeAnnotation.class);

		    // this is the Stanford dependency graph of the current sentence
		    SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		    
		    sInfo.setWordposes(wordposes);
		    sInfo.setTree(tree);
		    sInfo.setDependencies(dependencies);
		    
		    break;
	    }
	    
	    return sInfo;
	}
	
	public static void ParseConsoleLine() {
		StanfordCoreNLP pipeline = GetPipeline();
		try {
			while (true) {
				String inputText = "";
				BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in)); 
				inputText = stdin.readLine();
				String outputText = SegmentString(pipeline, inputText);
				//System.out.println(outputText);
			}
		} catch (IOException e) {	
		}
	}
}
