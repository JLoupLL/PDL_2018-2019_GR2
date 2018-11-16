package be.devijver.wikipedia.parser.ast;

import java.util.List;



public class Table implements Content {

	private String caption;
	private AttributeList tableOptions;
	private AttributeList captionOptions;
	private List rows;

	public Table(
		String caption,
		AttributeList tableOptions,
		AttributeList captionOptions,
		List rows) {
		this.caption = caption;
		this.tableOptions = tableOptions;
		this.captionOptions = captionOptions;
		this.rows = rows;
	}

	public String getCaption() {
		return caption;
	}

	public AttributeList getTableOptions() {
		return tableOptions;
	}

	public AttributeList getCaptionOptions() {
		return captionOptions;
	}

	public Row[] getRows() {
		return (Row[]) rows.toArray(new Row[rows.size()]);
	}

	@Override
	public String toString() {
		String s = "";

		s += "<table ";
		if (getTableOptions() != null) {
			s += getTableOptions().toString();
		}
		s += " >\n";
		if (getCaption() != null) {
			s += "<caption ";
			if (getCaptionOptions() != null) {
				s += getCaptionOptions().toString();
			}
			s += " >";
			s += getCaption();
			s += "</caption>\n";
		}
		Row[] rows = getRows();
		for (int i = 0; i < rows.length; i++) {
			s += rows[i].toString();
		}
		s += "</table>";
		return s;
	}
}
