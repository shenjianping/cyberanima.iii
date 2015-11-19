package org.cyberanima.iii.algorithm.nlp.fudan;

public class TaggedWord {
	private String word;
	private String tag;
	private String ner;
	public String word() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String tag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String ner() {
		return ner;
	}
	public void setNer(String ner) {
		this.ner = ner;
	}
}
