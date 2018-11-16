package be.devijver.wikipedia.parser.wikitext;

import java.util.Iterator;
import java.util.List;

public abstract class ParserSupport {

	protected final String toParse;
	protected int position = 0;

	protected ParserSupport(String toParse) {
		this.toParse = toParse;
	}

	protected boolean isNextChar(char c) {
	    if (!isNextBeyondEnd()) {
	        moveToNextChar();
	        boolean result = currentChar() == c;
	        position -= 1;
	        return result;
	    } else {
	        return false;
	    }
	}

	protected boolean hasNextChar() {
	    return position < toParse.length();
	}

	protected char currentChar() {
	    return toParse.charAt(position);
	}

	protected boolean isNextBeyondEnd() {
	    return position + 1 >= toParse.length();
	}

	protected void moveToNextChar() {
	    if (hasNextChar()) {
	        position++;
	    } else {
	        new MarkupParseException("At end of content. Can't move to next character. Current position is " + position + ".\n " +
	                "ContentHolder is:\n " +
	                "\n" +
	                toParse);
	    }
	}

	protected int find(char[] sequence) {
		int counter = 0;

		int atStart = position;
		while (!isNextBeyondEnd()) {
			counter++;
			int initialPosition = position;
			boolean found = false;
			for (int i = 0; !isNextBeyondEnd() && (i == 0 || found) && i < sequence.length; i++) {
				moveToNextChar();
				char c = currentChar();
				found = (i == 0 || found) && c == sequence[i];
				if (found && i + 1 == sequence.length) {
					position = atStart;
					return counter - 1;
				}
			}
			position = initialPosition;
			moveToNextChar();
		}

		position = atStart;
		return -1;
	}

	protected boolean consumeWhitespaces() {
		boolean consumed = false;
		int initialPosition = position;
		while (
	    		!isNextBeyondEnd() &&
	    		(currentChar() == ' ' ||
	    		 currentChar() == '\t' ||
	    		 currentChar() == '\n')) {
				consumed = true;
	    		moveToNextChar();
	    	}
		if (!consumed) {
			position = initialPosition;
		}
		return consumed;
	}

	protected boolean consumeWhitespacesOr(char c) {
		consumeWhitespaces();
		return !isNextBeyondEnd() && currentChar() == c;
	}

	protected String asString(List characters) {
	    StringBuffer sb = new StringBuffer();
	    for (Iterator iter = characters.iterator(); iter.hasNext();) {
	        Character character = (Character) iter.next();
	        sb.append(character);
	    }
	    return sb.toString();
	}

}
