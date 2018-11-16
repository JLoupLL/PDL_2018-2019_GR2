package be.devijver.wikipedia.parser.ast.parser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import be.devijver.wikipedia.Visitor;
import be.devijver.wikipedia.parser.ast.Bold;
import be.devijver.wikipedia.parser.ast.Cell;
import be.devijver.wikipedia.parser.ast.Characters;
import be.devijver.wikipedia.parser.ast.Content;
import be.devijver.wikipedia.parser.ast.Document;
import be.devijver.wikipedia.parser.ast.Heading;
import be.devijver.wikipedia.parser.ast.Indent;
import be.devijver.wikipedia.parser.ast.Italics;
import be.devijver.wikipedia.parser.ast.Literal;
import be.devijver.wikipedia.parser.ast.NormalLink;
import be.devijver.wikipedia.parser.ast.Nowiki;
import be.devijver.wikipedia.parser.ast.OrderedListItem;
import be.devijver.wikipedia.parser.ast.Paragraph;
import be.devijver.wikipedia.parser.ast.Pre;
import be.devijver.wikipedia.parser.ast.Row;
import be.devijver.wikipedia.parser.ast.SingleContentHolder;
import be.devijver.wikipedia.parser.ast.SmartLink;
import be.devijver.wikipedia.parser.ast.SpecialConstruct;
import be.devijver.wikipedia.parser.ast.Table;
import be.devijver.wikipedia.parser.ast.UnorderedListItem;
import be.devijver.wikipedia.parser.javacc.table.JavaCCTableParser;
import be.devijver.wikipedia.parser.javacc.table.ParseException;
import be.devijver.wikipedia.parser.wikitext.MarkupParser;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 16:39:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractASTParser {

    private final Document doc;

    public AbstractASTParser(Document doc) {
        this.doc = doc;
    }

    public void parse(Visitor visitor) {
        if (doc.hasContent()) {
            visitor.startDocument();
            parseDocument(doc, visitor);
            visitor.endDocument();
        }
    }

    private void parseDocument(Document doc, Visitor visitor) {
        ParseContext parseContext = new ParseContext(doc.getContent());
        for (;parseContext.hasCurrent();) {
            Content content = parseContext.current();

            invokeParseMethod(content, visitor, parseContext);
            parseContext.next();
        }
    }

    public void parse(Heading heading, Visitor visitor, ParseContext context) {
        int counter = 1;
        Heading tmpHeading = heading;

        while (tmpHeading.getContent() instanceof Heading) {
            counter++;
            tmpHeading = (Heading) tmpHeading.getContent();
        }

        switch (counter) {
            case 1:
                visitor.startHeading1();
                break;
            case 2:
                visitor.startHeading2();
                break;
            case 3:
                visitor.startHeading3();
                break;
            case 4:
                visitor.startHeading4();
                break;
            case 5:
                visitor.startHeading5();
                break;
            case 6:
                visitor.startHeading6();
                break;
            default:
                throw new ASTParseException("Heading of depth " + counter + " is not supported!");
        }

        Characters characters = (Characters) tmpHeading.getContent();
        parseUnparsedCharacters(characters, visitor);

        switch (counter) {
            case 1:
                visitor.endHeading1();
                break;
            case 2:
                visitor.endHeading2();
                break;
            case 3:
                visitor.endHeading3();
                break;
            case 4:
                visitor.endHeading4();
                break;
            case 5:
                visitor.endHeading5();
                break;
            case 6:
                visitor.endHeading6();
                break;
            default:
                throw new ASTParseException("Heading of depth " + counter + " is not supported!");
        }

    }

    private void parseUnparsedCharacters(Characters characters, Visitor visitor) {
        Content[] content = new MarkupParser(characters.toString()).parseContentList();
        parse(content, visitor);
    }

    public void parse(Content[] content, Visitor visitor) {
        ParseContext newParseContext = new ParseContext(content);
        for (int i = 0; i < content.length; i++) {
            invokeParseMethod(content[i], visitor, newParseContext);
            newParseContext.next();
        }
    }

    public void parse(Literal content, Visitor visitor, ParseContext context) {
        visitor.startLiteral();
        parse(content.getContent(), visitor);
        while (context.isNextContentSameType()) {
        	Literal next = (Literal) context.nextLookAhead();
        	context.next();
        	parse(next.getContent(), visitor);
        }
        visitor.endLiteral();
    }

    public void parse(Pre content, Visitor visitor, ParseContext context) {
    	visitor.startPre();
    	parse(content.getNowiki(), visitor, context);
    	visitor.endPre();
    }

    public void parse(Characters content, Visitor visitor, ParseContext context) {
        visitor.handleString(content.toString());
    }

    public void parse(Paragraph content, Visitor visitor, ParseContext context) {
        visitor.startParagraph();
        parse(content.getContent(), visitor);
        visitor.endParagraph();
    }

    public void parse(UnorderedListItem content, Visitor visitor, ParseContext context) {
        if (!context.isPreviousContentSameType()) {
            visitor.startUnorderedList();
        }
        visitor.startUnorderedListItem();
        if (content.getContent() instanceof Paragraph) {
            Paragraph paragraph = (Paragraph) content.getContent();
            parse(paragraph.getContent(), visitor);
        } else {
            invokeParseMethod(content.getContent(), visitor, new ParseContext(new Content[] {}));
        }
        Content next = context.nextLookAhead();
        if (next != null && next instanceof SingleContentHolder) {
            SingleContentHolder nextContentHolder = (SingleContentHolder) next;
            if (nextContentHolder.getContent() instanceof UnorderedListItem || nextContentHolder.getContent() instanceof OrderedListItem) {
                invokeParseMethod(nextContentHolder.getContent(), visitor, new ParseContext(new Content[] { nextContentHolder.getContent() }));
                context.next();
            }
        }
        visitor.endUnorderedListItem();
        if (!context.isNextContentSameType()) {
            visitor.endUnorderedList();
        }
    }

    public void parse(OrderedListItem content, Visitor visitor, ParseContext context) {
        if (!context.isPreviousContentSameType()) {
            visitor.startOrderedList();
        }
        visitor.startOrderedListItem();
        if (content.getContent() instanceof Paragraph) {
            Paragraph paragraph = (Paragraph) content.getContent();
            parse(paragraph.getContent(), visitor);
        } else {
            invokeParseMethod(content.getContent(), visitor, new ParseContext(new Content[] {}));
        }
        Content next = context.nextLookAhead();
        if (next != null && next instanceof SingleContentHolder) {
            SingleContentHolder nextContentHolder = (SingleContentHolder) next;
            if (nextContentHolder.getContent() instanceof UnorderedListItem || nextContentHolder.getContent() instanceof OrderedListItem) {
                invokeParseMethod(nextContentHolder.getContent(), visitor, new ParseContext(new Content[] { nextContentHolder.getContent() }));
                context.next();
            }
        }
        visitor.endOrderedListItem();
        if (!context.isNextContentSameType()) {
            visitor.endOrderedList();
        }
    }

    public void parse(Bold content, Visitor visitor, ParseContext context) {
        visitor.startBold();
        parse(content.getContent(), visitor);
        visitor.endBold();
    }

    public void parse(Italics content, Visitor visitor, ParseContext context) {
        visitor.startItalics();
        parse(content.getContent(), visitor);
        visitor.endItalics();
    }

    public void parse(Indent content, Visitor visitor, ParseContext context) {
        visitor.startIndent();
        if (content.getContent() instanceof Paragraph) {
            parse(((Paragraph)content.getContent()).getContent(), visitor);
        } else {
            invokeParseMethod(content.getContent(), visitor, context);
        }
        visitor.endIndent();
    }

    public void parse(NormalLink content, Visitor visitor, ParseContext context) {
    	String caption = content.getCaption();
    	if (caption != null && caption.length() > 0) {
	        visitor.startNormalLinkWithCaption(content.getUrl());
	        parseUnparsedCharacters(new Characters(content.getCaption()), visitor);
	        visitor.endNormalLinkWithCaption();
    	} else {
    		visitor.handleNormalLinkWithoutCaption(content.getUrl());
    	}
    }

    public void parse(SmartLink content, Visitor visitor, ParseContext context) {
        String caption = content.getCaption();

        if (caption != null && caption.length() > 0) {
            visitor.startSmartLinkWithCaption(content.getDestination());
            parseUnparsedCharacters(new Characters(caption), visitor);
            visitor.endSmartLinkWithCaption();
        } else {
        	visitor.handleSmartLinkWithoutCaption(content.getDestination());
        }

    }

    public void parse(Nowiki nowiki, Visitor visitor, ParseContext context) {
    	visitor.handleNowiki(nowiki.getNowiki());
    }

    public void parse(SpecialConstruct content, Visitor visitor, ParseContext context) {
    	if ('|' == content.getCharacter()) {
    		Table table= null;
    		try {
				table =
					new JavaCCTableParser(
							new StringReader(
									"{|" + content.getContent() + "|}"
							)
					).parseTable();
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			parse(table, visitor, context);
    	}
    }

    public void parse(Table table, Visitor visitor, ParseContext context) {
    	visitor.startTable(table.getTableOptions());
    	if (table.getCaption() != null || table.getCaptionOptions() != null) {
    		visitor.startCaption(table.getCaptionOptions());
    		parse(
    			parseSingleLineMarkup(new Characters(table.getCaption())),
    			visitor
    		);
    		visitor.endCaption();
    	}
    	parse(table.getRows(), visitor);
    	visitor.endTable();
    }

    public void parse(Row row, Visitor visitor, ParseContext context) {
    	visitor.startTableRecord(row.getRowOptions());
    	parse(row.getCells(), visitor);
    	visitor.endTableRecord();
    }

    public void parse(Cell cell, Visitor visitor, ParseContext context) {
    	if (cell.isStrong()) {
	    	visitor.startTableHeader(cell.getOptions());
    	} else {
	    	visitor.startTableData(cell.getOptions());
    	}

    	Content[] content = cell.getContent();
    	for (int i = 0; i < content.length; i++) {
    		if (content[i] instanceof Characters) {
    			parse(
    				parseSingleLineMarkup((Characters) content[i]),
    				visitor
    			);
    		} else {
    			parse(new Content[] { content[i] }, visitor);
    		}
    	}

    	if (cell.isStrong()) {
    		visitor.endTableHeader();
    	} else {
    		visitor.endTableData();
    	}
    }

    private void invokeParseMethod(Content content, Visitor visitor, ParseContext context) {
        try {
            doInvokeParseMethod(content, visitor, context);
        } finally {

        }
    }

    private Content[] parseSingleLineMarkup(Characters characters) {
    	MarkupParser parser = new MarkupParser(characters.toString());
    	return parser.parseContentList();
    }

    protected abstract void doInvokeParseMethod(Content content, Visitor visitor, Object context);

    protected class ParseContext {

        Map context = new HashMap();
        int position = 0;
        final Content[] content;

        protected ParseContext(Content[] content) {
            this.content = content;
        }

        protected final Content current() {
            return this.content[position];
        }

        protected final void next() {
            position++;
        }

        public final Content nextLookAhead() {
            if (position + 1 >= this.content.length) {
                return null;
            } else {
                return content[position + 1];
            }
        }

        public final boolean hasCurrent() {
            return position < this.content.length;
        }

        public final boolean isNextContentSameType() {
            if (position + 1 >= this.content.length) {
                return false;
            }
            return content[position + 1].getClass() == current().getClass();
        }

        public final boolean isPreviousContentSameType() {
            if (position == 0) {
                return false;
            }
            return content[position - 1].getClass() == current().getClass();
        }
    }
}
