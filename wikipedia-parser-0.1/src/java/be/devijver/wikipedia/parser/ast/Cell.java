package be.devijver.wikipedia.parser.ast;

import org.apache.commons.lang.StringEscapeUtils;



public class Cell extends AbstractContentHolder {

	private AttributeList options;
	private boolean strong;
	public Cell(Content content, AttributeList options, boolean strong) {
		super(new Content[] { content });
		this.options = options;
		this.strong = strong;
	}

	public Cell(Content[] content, AttributeList options, boolean strong) {
		super(content);
		this.options = options;
		this.strong = strong;
	}

	public AttributeList getOptions() {
		return options;
	}

	public boolean isStrong() {
		return strong;
	}

	@Override
	public String toString() {
		String s = isStrong() ? "<th " : "<td ";
		if (getOptions() != null) {
			s += getOptions().toString();
		}
		s += ">";
		if (getContent() != null) {
			Content[] content = getContent();
			for (int i = 0; i < content.length; i++) {
				s += content[i] != null ? StringEscapeUtils.escapeHtml(content[i].toString()) : "";
			}
		}
		s += isStrong() ? "</th>" : "</td>";
		s += "\n";
		return s;
	}
}
