package be.devijver.wikipedia.parser.ast;

import be.devijver.wikipedia.parser.ast.Content;

public class Nowiki implements Content {

	private final String nowiki;
	public Nowiki(String nowiki) {
		this.nowiki = nowiki;
	}

	public String getNowiki() {
		return nowiki;
	}

	public String toString() {
		return "<nowiki>" + nowiki + "</nowiki>";
	}

}
