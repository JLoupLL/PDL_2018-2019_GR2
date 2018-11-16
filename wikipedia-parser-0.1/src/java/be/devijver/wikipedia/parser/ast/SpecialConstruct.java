package be.devijver.wikipedia.parser.ast;

public class SpecialConstruct implements Content {

	private char character;
	private String content;
	public SpecialConstruct(char character, String content) {
		super();
		this.character = character;
		this.content = content;
	}

	public char getCharacter() {
		return character;
	}

	public String getContent() {
		return content;
	}
}
