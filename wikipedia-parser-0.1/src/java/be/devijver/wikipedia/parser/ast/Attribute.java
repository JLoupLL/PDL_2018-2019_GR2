package be.devijver.wikipedia.parser.ast;



public class Attribute implements Content {

	private final String name;
	private final String value;
	public Attribute(final String name, final String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return
			getName() + "=\"" +
			(getValue() != null ? getValue() : "") +
			"\"";
	}
}
