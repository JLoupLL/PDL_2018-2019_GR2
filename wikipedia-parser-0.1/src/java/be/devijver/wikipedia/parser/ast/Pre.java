package be.devijver.wikipedia.parser.ast;

public class Pre implements Content {

	private Nowiki nowiki;

	public Pre(Nowiki nowiki) {
		super();
		this.nowiki = nowiki;
	}

	public Nowiki getNowiki() {
		return nowiki;
	}
}
