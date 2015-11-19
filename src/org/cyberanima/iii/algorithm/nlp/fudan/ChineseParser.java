package org.cyberanima.iii.algorithm.nlp.fudan;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.nlp.cn.tag.NERTagger;

public class ChineseParser {
	public static void Parse(String inputFileName){
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
			
		} catch (FileNotFoundException e) {
			System.out.println("no file found");
		} catch (IOException e) {
			System.out.println("io exception");
		}
	    
	    List<TaggedWord> results = new ArrayList<TaggedWord>();
	    
	    try {
			CWSTagger cws = new CWSTagger("./model/nlp/seg.m");	
			POSTagger tag = new POSTagger(cws,"./model/nlp/pos.m");
			NERTagger ner = new NERTagger(cws,"./model/nlp/pos.m");
				
			String[][] tags = tag.tag2Array(text);
			HashMap<String, String> hm = ner.tag(text);
			for(int i = 0; i < tags[0].length; i++) {
	
				TaggedWord tw = new TaggedWord();
				tw.setTag(tags[1][i]);
				tw.setWord(tags[0][i]);
				tw.setNer(hm.get(tags[0][i]));
				results.add(tw);
				
				System.out.println(tw.word() + " " + tw.tag() + " " + tw.ner());
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		ChineseParser.Parse("./testcase/nlp/zh.txt");
	}
}
