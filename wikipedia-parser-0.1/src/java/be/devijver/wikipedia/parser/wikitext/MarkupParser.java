package be.devijver.wikipedia.parser.wikitext;

import be.devijver.wikipedia.parser.ast.*;

import java.util.List;
import java.util.ArrayList;

/**
 * <p>Parser for the Wikipedia markup language that produces an AST tree.
 *
 * <p>This parser is a mess, unfortunately, but it's well tested and has a very good performance.
 * It is however largely experimental and can only handle the basic subset of Wikipedia markup.
 *
 * <p>Currently this parser recognizes this markup:
 *
 * <ul>
 *  <li>Heading of limitless depth: <code>==Article heading==</code>, <code>===Section heading===</code>, ...
 *  <li>Simple paragraphs: <code>This is a paragraph.</code>
 *  <li>Strong: <code>'''This will be printed in bold'''</code>
 *  <li>Emphasis: <code>''This will be printed in italics''</code>
 *  <li><b>Note:</b> strong and emphasis can be combined
 *  <li>Unordered list items of limitless depth: <code>* Item 1</code>, <code>** Sub item 1</code>, ...
 *  <li>Ordered list items of limitless depth: <code># Item 1</code>, <code>## Sub item 1</code>, ...
 *  <li>Combined ordered and unordered list items: <code>*# Item 1</code>
 *  <li>Indenting of limitless depth: <code>: One indent</code>, <code>:: Two indents</code>
 *  <li>Normal links: <code>[http://www.google.com]</code>, <code>[http://www.google.com Google]</code>
 *  <li>'Smart' links: <code>[[Java programming language]]</code>, <code>[[Java programming language|Java]]</code>
 * </ul>
 *
 * <p><b>Note:</b> paragraphs, list items and indentions are terminated by a newline character or when
 * the end of the parsed text is reached.
 *
 * <p>See http://en.wikipedia.org/wiki/Help:Editing for the entire Wikipedia markup.
 *
 * <p>This parser only parses the markup without giving it any meaning. See <code>AbstractASTParser</code> which is double-pass
 * parser based on the output of this parser.
 *
 * <p>The main reason for the limited markup support of this parser is that the Wikipedia markup is very rich and the
 * original parser is written in OCaml, a powerful functional programming language. Mimicing the same behavior in an
 * object-oriented programming language like Java results in a mess as the source code of this parser demonstrates.
 * This makes that adding new features to this parser becomes harder and harder as it grows.
 *
 * @author Steven Devijver
 * @since 5-11-2006
 */
public class MarkupParser extends ParserSupport {

    public MarkupParser(String toParse) {
        super(toParse);
    }

    public Document parseDocument() {
        Document doc = new Document();

        while (hasNextChar()) {
            Content result = parseStartOfLine(doc);
            doc.addContent(result);

            moveToNextChar();
        }


        return doc;
    }

    private Content parseStartOfLine(Document doc) {
        char currentCharacter = currentChar();
        if (currentCharacter == '*') {
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                Content result = new UnorderedListItem(parseBeyondStartOfLine(doc));
                return result;
            }
        } else if (currentCharacter == '#') {
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                Content result = new OrderedListItem(parseBeyondStartOfLine(doc));
                return result;
            }
        } else if (currentCharacter == ':') {
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                return new Indent(parseBeyondStartOfLine(doc));
            }
        } else if (currentCharacter == '=') {
            if (!isNextBeyondEnd()) {
                int initialPosition = position;
                moveToNextChar();
                Content result = parseHeading();
                if (result != null) {
                    return result;
                } else {
                    position = initialPosition;
                }
            }
        } else if (currentCharacter == ' ') {
            // Literal
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                return new Literal(parseContentList());
            }
        }

        if (currentCharacter == '{') {
        	// special construct
        	Content result = parseSpecialConstruct();
        	if (result != null) {
        		return new Paragraph(new Content[] { result });
        	}
        }

        return parseParagraph(doc);

    }

    private Content parseHeading() {
        char currentCharacter = currentChar();
        if (currentCharacter != '=') {
            return null;
        } else {
            return doParseHeading();
        }
    }

    private Content doParseHeading() {
        char currentCharacter = currentChar();
        if (currentCharacter == '=') {
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                Content result = doParseHeading();
                if (result != null) {
                    result = new Heading(result);
                    if (!isNextBeyondEnd()) {
                        moveToNextChar();
                        currentCharacter = currentChar();
                        if (currentCharacter == '=') {
                            return result;
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            List characters = new ArrayList();
            while (hasNextChar()) {
                currentCharacter = currentChar();

                if (currentCharacter == '=' && isNextChar('=')) {
                    break;
                } else {
                    characters.add(new Character(currentCharacter));
                }

                moveToNextChar();
            }
            return new Characters(asString(characters));
        }
    }

    private Content parseBeyondStartOfLine(Document doc) {
        char currentCharacter = currentChar();
        if (currentCharacter == '*') {
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                Content result = parseBeyondStartOfLine(doc);
                if (result != null) {
                    return new UnorderedListItem(result);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else if (currentCharacter == '#') {
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                Content result = parseBeyondStartOfLine(doc);
                if (result != null) {
                    return new OrderedListItem(result);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else if (currentCharacter == ':') {
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                return new Indent(parseBeyondStartOfLine(doc));
            } else {
                return null;
            }
        } else if (currentCharacter == ' ') {
            if (!isNextBeyondEnd()) {
                moveToNextChar();
                return parseParagraph(doc);
            } else {
                return null;
            }
        } else {
            return parseParagraph(doc);
        }
    }

    private ContentHolder parseParagraph(Document doc) {
        Content[] result = parseContentList();
        if (result != null) {
            return new Paragraph(result);
        } else {
            return null;
        }
    }

    public Content[] parseContentList() {
        List characters = new ArrayList();
        List contentList = new ArrayList();

        while (hasNextChar()) {
            char currentCharacter = currentChar();

            for (;;) {
                if (currentCharacter == '\n') {
                    if (!characters.isEmpty()) {
                        contentList.add(new Characters(asString(characters)));
                        return (Content[])contentList.toArray(new Content[contentList.size()]);
                    } else {
                        return null;
                    }
                } else if (currentCharacter == '[') {
                    Content result = parseSquareBracket();
                    if (result != null) {
                    	if (!characters.isEmpty()) {
                    		contentList.add(new Characters(asString(characters)));
                    		characters.clear();
                    	}
                        contentList.add(result);
                        break;
                    }
                } else if (currentCharacter == '\'') {
                    Content result = parseSingleQuote();
                    if (result != null && result != endOfQuotes) {
                        String chars = asString(characters);
                        if (chars != null && chars.length() > 0) {
                            contentList.add(new Characters(chars));
                        }
                        characters.clear();
                        contentList.add(result);
                        break;
                    } else if (result != null && result == endOfQuotes) {
                        String chars = asString(characters);
                        if (chars != null && chars.length() > 0) {
                            contentList.add(new Characters(chars));
                        }
                        return (Content[])contentList.toArray(new Content[contentList.size()]);
                    }
                } else if (currentCharacter == '<') {
                	Content result = parsePre();
                	if (result == null) {
                		result = parseNowiki();
                	}
                	if (result != null) {
                		String chars = asString(characters);
                		if (chars != null && chars.length() > 0) {
                			contentList.add(new Characters(chars));
                			characters.clear();
                		}
                		contentList.add(result);
                		if (!isNextBeyondEnd()) {
                			moveToNextChar();
                			char nextChar = currentChar();
                			position -= 1;
                			if (nextChar == '\n') {
                				return (Content[])contentList.toArray(new Content[contentList.size()]);
                			} else {
                				break;
                			}
                		} else {
                			return (Content[])contentList.toArray(new Content[contentList.size()]);
                		}
                	}
                } else if (currentCharacter == '{') {
                	Content result = parseSpecialConstruct();
                	if (result != null) {
                		String chars = asString(characters);
                		if (chars != null && chars.length() > 0) {
                			contentList.add(new Characters(chars));
                			characters.clear();
                		}
                		contentList.add(result);
                		if (!isNextBeyondEnd()) {
                			moveToNextChar();
                			char nextChar = currentChar();
                			position -= 1;
                			if (nextChar == '\n') {
                				return (Content[])contentList.toArray(new Content[contentList.size()]);
                			} else {
                				break;
                			}
                		} else {
                			return (Content[])contentList.toArray(new Content[contentList.size()]);
                		}
                	}
                }

                characters.add(new Character(currentCharacter));
                if (isNextBeyondEnd()) {
                    contentList.add(new Characters(asString(characters)));
                    return (Content[])contentList.toArray(new Content[contentList.size()]);
                }
                break;
            }



            moveToNextChar();
        }

        if (isNextBeyondEnd()) {
            return (Content[])contentList.toArray(new Content[contentList.size()]);
        }

        throw new IllegalStateException("Unexpected end of parsing!");
    }

	public Content parseSpecialConstruct() {
		int initialPosition = position;
		moveToNextChar();
		char nextChar = currentChar();
		if (nextChar == ' ' || nextChar == '\t' || nextChar == '\n') {
			position = initialPosition;
			return null;
		}
		char charToFind = nextChar;
		if (nextChar == '{') charToFind = '}';
		if (nextChar == '(') charToFind = ')';
		if (nextChar == '[') charToFind = ']';
		int r = find(new char[] { charToFind, '}'});
		int beforeScan = position;
		while (r > 0) {
			int x = find (new char[] { '{', nextChar });
			if (x > 0 && x <= r) {
				int abc = position;
				position = beforeScan + r + 2;
				int y = find(new char[] { charToFind, '}'});
				if (y < 0) {
					throw new RuntimeException("Improper nesting of special constructs!");
				} else {
					r += 2 + y;
				}
				position = abc + x + 2;
			} else {
				break;
			}
		}
		position = beforeScan;
		if (r > 0) {
			List c = new ArrayList();
			for (int i = 0; i < r; i++) {
				moveToNextChar();
				c.add(new Character(currentChar()));
			}
			position += 2;
			return new SpecialConstruct(nextChar, asString(c));
		} else {
			position = initialPosition;
			return null;
		}
	}

    private Content parsePre() {
    	return parseWithTagHandler(
        		"pre",
        		new TagHandler() {

    				public Content handleTag(TagHandlerParserCallback callback) {
    					int initialPosition = position;
    					moveToNextChar();
    					if (!consumeWhitespacesOr('<')) {
    						position = initialPosition;
    						return null;
    					}
						Content result = parseNowiki();
    					if (result != null) {
    						initialPosition = position;
    						moveToNextChar();
    						if (!consumeWhitespaces()) {
    							position = initialPosition;
    						} else {
    							position -= 1;
    						}
    						return new Pre((Nowiki) result);
    					} else {
    						throw new RuntimeException("<pre> tag can only contain <nowiki> tag!");
    					}
    				}

        		});
	}

	private Content parseWithTagHandler(String tagName, TagHandler tagHandler) {
    	if (!isNextBeyondEnd()) {
    		int initialPosition = position;

    		final String startTag = tagName + ">";
    		final String endTag = "</" + tagName + ">";

    		if (!lookaheadForTag(startTag)) {
    			return null;
    		} else {
    			moveToNextChar();
    			position += startTag.length() - 1;
    		}

    		TagHandlerParserCallback callback =
    			new TagHandlerParserCallback() {
					public boolean isNextBeyondEnd() {
						return MarkupParser.this.isNextBeyondEnd();
					}
					public boolean lookaheadForClosingTag() {
						return MarkupParser.this.lookaheadForTag(endTag);
					}
					public void moveToNextChar() {
						MarkupParser.this.moveToNextChar();
					}
    			};

    		Content result = tagHandler.handleTag(callback);

    		if (result == null) {
    			position = initialPosition;
    			return null;
    		} else {
    			position += endTag.length();
    			return result;
    		}

    	}
    	return null;
    }

    private Content parseNowiki() {
    	return parseWithTagHandler(
    		"nowiki",
    		new TagHandler() {

				public Content handleTag(TagHandlerParserCallback callback) {
		    		List characters = new ArrayList();
		    		while (!callback.isNextBeyondEnd() && !callback.lookaheadForClosingTag()) {
		    			callback.moveToNextChar();
		    			characters.add(new Character(currentChar()));
		    		}

		    		if (callback.isNextBeyondEnd()) {
		    			return null;
		    		} else {
		    			return new Nowiki(asString(characters));
		    		}
				}

    		});
    }

    private interface TagHandler {
    	Content handleTag(TagHandlerParserCallback callback);
    }

    private interface TagHandlerParserCallback {
    	boolean lookaheadForClosingTag();
    	boolean isNextBeyondEnd();
    	void moveToNextChar();
    }

    private boolean lookaheadForTag(String tag) {
    	int initialPosition = position;

    	char[] tagAsArray = tag.toCharArray();

    	for (int i = 0; i < tagAsArray.length; i++) {
    		if (isNextBeyondEnd()) {
    			position = initialPosition;
    			return false;
    		}
    		moveToNextChar();
    		char currentChar = currentChar();
    		if (currentChar != tagAsArray[i]) {
    			position = initialPosition;
    			return false;
    		}
    	}
    	position = initialPosition;
    	return true;
    }

	private boolean inBoldSection = false;
    private boolean inItalicsSection = false;

    private Content endOfQuotes = new Content() {};

    private Content parseSingleQuote() {
        if (!isNextBeyondEnd()) {
            moveToNextChar();
            char currentCharacter = currentChar();
            if (currentCharacter != '\'') {
                // it's just one single quote, revert one position.
                position -= 1;
                return null;
            } else if (currentCharacter == '\'') {
                // it's a second single quote -> italics or bold
                if (isNextBeyondEnd() && (inBoldSection || inItalicsSection)) {
                    return endOfQuotes;
                }
                int singleQuoteLookahead = lookaheadForSingleQuote();
                if (singleQuoteLookahead > 0) { // single quote found
                    // bold
                    if (!inBoldSection) {
                        if (!isNextBeyondEnd()) {
                            moveToNextChar();
                            inBoldSection = true;
                            return new Bold(parseContentList());
                        } else {
                            return null;
                        }
                    } else {
                        inBoldSection = false;
                        return endOfQuotes;
                    }
                } else {
                    // italics
                    if (!inItalicsSection) {
                        if (!isNextBeyondEnd()) {
                            moveToNextChar();
                            inItalicsSection = true;
                            return new Italics(parseContentList());
                        } else {
                            return null;
                        }
                    } else {
                        inItalicsSection = false;
                        return endOfQuotes;
                    }
                }
            }
        }
        return null;
    }

    private int lookaheadForSingleQuote() {
        if (!isNextBeyondEnd()) {
            moveToNextChar();
            char currentCharacter = currentChar();
            if (currentCharacter != '\'') {
                position -= 1;
                return 0; // no single quote found
            } else {
                return 1; // single quote found
            }
        }
        return -1; // end of text
    }

    private Content parseSquareBracket() {
        Content result = null;

        if (!isNextBeyondEnd()) {
            int initialPosition = position;
            moveToNextChar();
            char currentChar = currentChar();

            if (currentChar == '[') {
                if (isNextBeyondEnd()) {
                    return null;
                }
                moveToNextChar();
                result = parseSmartLink();
                // closing second ] is totally meaningless and therefor optional
                if (!isNextBeyondEnd()) {
                    moveToNextChar();
                    currentChar = currentChar();
                    if (currentChar == ']') {
                        // ok, swallow
                    } else {
                        // we did a look ahead of one character and it turned out
                        // we can't swallow that character so we recurse one position.
                        position -= 1;
                    }
                }
            } else {
                result = parseNormalLink();
            }

            if (result == null) {
                position = initialPosition;
            }
        }

        return result;
    }

    private Content parseSmartLink() {
        List characters = new ArrayList();
        boolean pipeFound = false;
        String destination = null;

        while (hasNextChar()) {
            char currentCharacter = currentChar();
            if (currentCharacter == '\n' || (currentCharacter != ']' && isNextBeyondEnd())) {
                return null;
            } else if (currentCharacter == ']') {
                break;
            } else if (!pipeFound && currentCharacter == '|') {
                pipeFound = true;
                destination = asString(characters);
                characters.clear();
            } else {
                characters.add(new Character(currentCharacter));
            }

            moveToNextChar();
        }

        if (!pipeFound) {
            return new SmartLink(asString(characters));
        } else {
            return new SmartLink(destination, asString(characters));
        }
    }

    private Content parseNormalLink() {
        List characters = new ArrayList();
        boolean spaceFound = false;
        String url = null;

        while (hasNextChar()) {
            char currentCharacter = currentChar();
            if (currentCharacter == '\n' || (currentCharacter != ']' && isNextBeyondEnd())) {
                return null;
            } else if (currentCharacter == ']') {
                break;
            } else if (!spaceFound && currentCharacter == ' ') {
                spaceFound = true;
                url = asString(characters);
                characters.clear();
            } else {
                characters.add(new Character(currentCharacter));
            }

            moveToNextChar();
        }

        if (!spaceFound) {
            return new NormalLink(asString(characters));
        } else {
            return new NormalLink(url, asString(characters));
        }
    }
}
