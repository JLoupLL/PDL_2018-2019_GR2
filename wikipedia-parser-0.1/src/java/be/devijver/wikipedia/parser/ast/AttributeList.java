package be.devijver.wikipedia.parser.ast;

import java.util.ArrayList;
import java.util.List;


public class AttributeList implements Content {

	private final List attributes =
		new ArrayList();

	public void addAttribute(Attribute attr) {
		attributes.add(attr);
	}

	public Attribute[] getAttributes() {
		return (Attribute[]) attributes.toArray(new Attribute[attributes.size()]);
	}

	@Override
	public String toString() {
		String result = "";
		Attribute[] attributes = getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			result += attributes[i].toString() + " ";
		}
		return result;
	}
}
