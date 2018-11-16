package be.devijver.wikipedia.parser.ast;

import java.util.ArrayList;
import java.util.List;


public class Row implements Content {
	private AttributeList rowOptions;
	private List cells = new ArrayList();

	public Row() { }

	public Row(AttributeList rowOptions) {
		super();
		this.rowOptions = rowOptions;
	}

	public AttributeList getRowOptions() {
		return rowOptions;
	}

	public Cell[] getCells() {
		return (Cell[]) cells.toArray(new Cell[cells.size()]);
	}

	public void addCell(Cell cell) {
		cells.add(cell);
	}

	@Override
	public String toString() {
		String s = "";
		s += "<tr ";
		if (getRowOptions() != null) {
			s += getRowOptions().toString();
		}
		s += " >\n";
		Cell[] cells = getCells();
		for (int i = 0; i < cells.length; i++) {
			s += cells[i].toString();
		}
		s += "</tr>\n";
		return s;
	}
}
