package be.devijver.wikipedia.parser.ast.parser;

import junit.framework.TestCase;
import be.devijver.wikipedia.Visitor;
import be.devijver.wikipedia.parser.ast.AttributeList;
import be.devijver.wikipedia.parser.ast.Document;
import be.devijver.wikipedia.parser.wikitext.MarkupParser;

import static org.easymock.EasyMock.*;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 17:07:18
 * To change this template use File | Settings | File Templates.
 */
public class DefaultASTParserTests extends TestCase {

    private Document parse(String content) {
        return new MarkupParser(content).parseDocument();
    }

    public void testParseLiteral() {
        Document doc = parse(" This is a literal");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startLiteral();
        visitor.handleString("This is a literal");
        visitor.endLiteral();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseParagraph() {
        Document doc = parse("This is a paragraph.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a paragraph.");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseTwoParagraphs() {
        Document doc = parse("This is a paragraph.\n\nThis is another paragraph.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a paragraph.");
        visitor.endParagraph();
        visitor.startParagraph();
        visitor.handleString("This is another paragraph.");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseUnorderedListItem() {
        Document doc = parse("* This is an unordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is an unordered list item.");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseTwoSubsequentUnorderedListItems() {
        Document doc = parse("* This is an unordered list item.\n\n* This is another unordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is an unordered list item.");
        visitor.endUnorderedListItem();
        visitor.startUnorderedListItem();
        visitor.handleString("This is another unordered list item.");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseNestedTwoLevelUnorderedListItems() {
        Document doc = parse("* This is an unordered list item.\n\n** This is a nested unordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is an unordered list item.");
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is a nested unordered list item.");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseOrderedListItem() {
        Document doc = parse("# This is an ordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is an ordered list item.");
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseTwoSubsequentOrderedListItems() {
        Document doc = parse("# This is an ordered list item.\n\n# This is another ordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is an ordered list item.");
        visitor.endOrderedListItem();
        visitor.startOrderedListItem();
        visitor.handleString("This is another ordered list item.");
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseNestedTwoLevelOrderedListItems() {
        Document doc = parse("# This is an ordered list item.\n\n## This is a nested ordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is an ordered list item.");
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is a nested ordered list item.");
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseOrderedAndUnorderedListItems() {
        Document doc = parse("# This is an ordered list item.\n\n* This is an unordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is an ordered list item.");
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is an unordered list item.");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseUnorderedAndOrderedListItems() {
        Document doc = parse("* This is an unordered list item.\n\n# This is an ordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is an unordered list item.");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is an ordered list item.");
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseUnorderedListItemWithNestedOrderedListItem() {
        Document doc = parse("* This is an unordered list item.\n\n## This is a nested ordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is an unordered list item.");
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is a nested ordered list item.");
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseOrderedListItemWithNestedUnorderedListItem() {
        Document doc = parse("# This is an ordered list item.\n\n** This is a nested unordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is an ordered list item.");
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is a nested unordered list item.");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseSingleNestedUnorderedListItem() {
        Document doc = parse("** This is a single nested unordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is a single nested unordered list item.");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseSingleNestedOrderedListItem() {
        Document doc = parse("## This is a single nested ordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.startOrderedList();
        visitor.startOrderedListItem();
        visitor.handleString("This is a single nested ordered list item.");
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endOrderedListItem();
        visitor.endOrderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }


    public void testParseParagraphWithBold() {
        Document doc = parse("This is a '''paragraph''' with bold.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a ");
        visitor.startBold();
        visitor.handleString("paragraph");
        visitor.endBold();
        visitor.handleString(" with bold.");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseParagraphWithItalics() {
        Document doc = parse("This is a ''paragraph'' with italics.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a ");
        visitor.startItalics();
        visitor.handleString("paragraph");
        visitor.endItalics();
        visitor.handleString(" with italics.");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseParagraphWithNestedBoldAndItalics() {
        Document doc = parse("This is a '''''paragraph''''' with bold and italics.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a ");
        visitor.startBold();
        visitor.startItalics();
        visitor.handleString("paragraph");
        visitor.endItalics();
        visitor.endBold();
        visitor.handleString(" with bold and italics.");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseUnorderedListItemWithMarkup() {
        Document doc = parse("* This is a paragraph with '''bold''' and ''italics''.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is a paragraph with ");
        visitor.startBold();
        visitor.handleString("bold");
        visitor.endBold();
        visitor.handleString(" and ");
        visitor.startItalics();
        visitor.handleString("italics");
        visitor.endItalics();
        visitor.handleString(".");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseSingleIndent() {
        Document doc = parse(":This is an indent.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startIndent();
        visitor.handleString("This is an indent.");
        visitor.endIndent();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseDoubleIndent() {
        Document doc = parse("::This is an double indent.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startIndent();
        visitor.startIndent();
        visitor.handleString("This is an double indent.");
        visitor.endIndent();
        visitor.endIndent();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseIndentWithUnorderedListItem() {
        Document doc = parse(":* This is an indented unordered list item.");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startIndent();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is an indented unordered list item.");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endIndent();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testNormalLinkWithCaption() {
        Document doc = parse("[http://www.google.com Google]");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.startNormalLinkWithCaption("http://www.google.com");
        visitor.handleString("Google");
        visitor.endNormalLinkWithCaption();
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testNormalLinkWithoutCaption() {
        Document doc = parse("[http://www.google.com]");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleNormalLinkWithoutCaption("http://www.google.com");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }


    public void testParseParagraphWithNormalLink() {
        Document doc = parse("This is a paragraph with a [http://www.google.com link].");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a paragraph with a ");
        visitor.startNormalLinkWithCaption("http://www.google.com");
        visitor.handleString("link");
        visitor.endNormalLinkWithCaption();
        visitor.handleString(".");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseParagraphWithNormalLinkWithItalics() {
        Document doc = parse("This is a paragraph with a [http://www.google.com ''link''].");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a paragraph with a ");
        visitor.startNormalLinkWithCaption("http://www.google.com");
        visitor.startItalics();
        visitor.handleString("link");
        visitor.endItalics();
        visitor.endNormalLinkWithCaption();
        visitor.handleString(".");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseUnorderedListItemWithNormalLink() {
        Document doc = parse("* This is a paragraph with a [http://www.google.com link].");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startUnorderedList();
        visitor.startUnorderedListItem();
        visitor.handleString("This is a paragraph with a ");
        visitor.startNormalLinkWithCaption("http://www.google.com");
        visitor.handleString("link");
        visitor.endNormalLinkWithCaption();
        visitor.handleString(".");
        visitor.endUnorderedListItem();
        visitor.endUnorderedList();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseHeadingWithMarkupAndLink() {
        Document doc = parse("==This is a ''really special'' heading '''with''' a [http://www.google.com link]==");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startHeading1();
        visitor.handleString("This is a ");
        visitor.startItalics();
        visitor.handleString("really special");
        visitor.endItalics();
        visitor.handleString(" heading ");
        visitor.startBold();
        visitor.handleString("with");
        visitor.endBold();
        visitor.handleString(" a ");
        visitor.startNormalLinkWithCaption("http://www.google.com");
        visitor.handleString("link");
        visitor.endNormalLinkWithCaption();
        visitor.endHeading1();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testSmartLinkWithCaption() {
        Document doc = parse("[[Smart Link|link]]");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.startSmartLinkWithCaption("Smart Link");
        visitor.handleString("link");
        visitor.endSmartLinkWithCaption();
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testSmartLinkWithoutCaption() {
        Document doc = parse("[[Smart Link]]");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleSmartLinkWithoutCaption("Smart Link");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }


    public void testParseParagraphWithSmartLink() {
        Document doc = parse("This is a paragraph with a [[SmartLink]].");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a paragraph with a ");
        visitor.handleSmartLinkWithoutCaption("SmartLink");
        visitor.handleString(".");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseParagraphWithSmartLinkWithCaption() {
        Document doc = parse("This is a paragraph with a [[SmartLink|really smart link]].");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a paragraph with a ");
        visitor.startSmartLinkWithCaption("SmartLink");
        visitor.handleString("really smart link");
        visitor.endSmartLinkWithCaption();
        visitor.handleString(".");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseParagraphWithSmartLinkWithCaptionWithMarkup() {
        Document doc = parse("This is a paragraph with a [[SmartLink|really ''smart'' link]].");

        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startParagraph();
        visitor.handleString("This is a paragraph with a ");
        visitor.startSmartLinkWithCaption("SmartLink");
        visitor.handleString("really ");
        visitor.startItalics();
        visitor.handleString("smart");
        visitor.endItalics();
        visitor.handleString(" link");
        visitor.endSmartLinkWithCaption();
        visitor.handleString(".");
        visitor.endParagraph();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testMultipleConsecutiveLiterals() {
    	Document doc = parse(
    			" public class Test\n" +
    			" {\n" +
    			"    super();\n" +
    			" }");
        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startLiteral();
        visitor.handleString("public class Test");
        visitor.handleString("{");
        visitor.handleString("   super();");
        visitor.handleString("}");
        visitor.endLiteral();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testDoubleMultipleConsecutiveLiteralsWithParagraphInBetween() {
    	Document doc = parse(
    			" public class Test\n" +
    			" {\n" +
    			"    super();\n" +
    			" }\n" +
    			"\n" +
    			"Now let's look at this:\n" +
    			"\n" +
    			" public class Test2\n" +
    			" {\n" +
    			"    super();\n" +
    			" }");
        Visitor visitor = createStrictMock(Visitor.class);

        visitor.startDocument();
        visitor.startLiteral();
        visitor.handleString("public class Test");
        visitor.handleString("{");
        visitor.handleString("   super();");
        visitor.handleString("}");
        visitor.endLiteral();
        visitor.startParagraph();
        visitor.handleString("Now let's look at this:");
        visitor.endParagraph();
        visitor.startLiteral();
        visitor.handleString("public class Test2");
        visitor.handleString("{");
        visitor.handleString("   super();");
        visitor.handleString("}");
        visitor.endLiteral();
        visitor.endDocument();

        replay(visitor);

        new DefaultASTParser(doc).parse(visitor);

        verify(visitor);
    }

    public void testParseNowiki() {
    	Document doc = parse("<nowiki>Test</nowiki>");

    	Visitor visitor = createStrictMock(Visitor.class);

    	visitor.startDocument();
    	visitor.startParagraph();
    	visitor.handleNowiki("Test");
    	visitor.endParagraph();
    	visitor.endDocument();

    	replay(visitor);

    	new DefaultASTParser(doc).parse(visitor);

    	verify(visitor);
    }

    public void testParseNowikiInParagraph() {
    	Document doc = parse("This is a <nowiki>nowiki</nowiki> test.");

    	Visitor visitor = createStrictMock(Visitor.class);

    	visitor.startDocument();
    	visitor.startParagraph();
    	visitor.handleString("This is a ");
    	visitor.handleNowiki("nowiki");
    	visitor.handleString(" test.");
    	visitor.endParagraph();
    	visitor.endDocument();

    	replay(visitor);

    	new DefaultASTParser(doc).parse(visitor);

    	verify(visitor);
    }

    public void testPre() throws Exception {
		Document doc = parse("<pre><nowiki>This is no wiki</nowiki></pre>");

		Visitor visitor = createStrictMock(Visitor.class);

		visitor.startDocument();
		visitor.startParagraph();
		visitor.startPre();
		visitor.handleNowiki("This is no wiki");
		visitor.endPre();
		visitor.endParagraph();
		visitor.endDocument();

		replay(visitor);

		new DefaultASTParser(doc).parse(visitor);

		verify(visitor);
	}

    public void testTable1() throws Exception {
		Document doc = parse("{| |}");

		Visitor visitor = createStrictMock(Visitor.class);

		visitor.startDocument();
		visitor.startParagraph();
		visitor.startTable((AttributeList) isNull());
		visitor.endTable();
		visitor.endParagraph();
		visitor.endDocument();

		replay(visitor);

		new DefaultASTParser(doc).parse(visitor);

		verify(visitor);
	}

    public void testTable2() throws Exception {
		Document doc = parse(
			"{|\n" +
			"|+ This is a caption\n" +
			"|}"
		);

		Visitor visitor = createStrictMock(Visitor.class);

		visitor.startDocument();
		visitor.startParagraph();
		visitor.startTable((AttributeList) isNull());
		visitor.startCaption((AttributeList) isNull());
		visitor.handleString(" This is a caption");
		visitor.endCaption();
		visitor.endTable();
		visitor.endParagraph();
		visitor.endDocument();

		replay(visitor);

		new DefaultASTParser(doc).parse(visitor);

		verify(visitor);
	}

    public void testTable3() throws Exception {
		Document doc = parse(
			"{|\n" +
			"|-\n" +
			"|cell1\n" +
			"|}"
		);

		Visitor visitor = createStrictMock(Visitor.class);

		visitor.startDocument();
		visitor.startParagraph();
		visitor.startTable((AttributeList) isNull());
		visitor.startTableRecord((AttributeList) isNull());
		visitor.startTableData((AttributeList) isNull());
		visitor.handleString("cell1");
		visitor.endTableData();
		visitor.endTableRecord();
		visitor.endTable();
		visitor.endParagraph();
		visitor.endDocument();

		replay(visitor);

		new DefaultASTParser(doc).parse(visitor);

		verify(visitor);
	}

    public void testTable4() throws Exception {
		Document doc = parse(
				"{|\n" +
				"|-\n" +
				"!cell1\n" +
				"|}"
			);

			Visitor visitor = createStrictMock(Visitor.class);

			visitor.startDocument();
			visitor.startParagraph();
			visitor.startTable((AttributeList) isNull());
			visitor.startTableRecord((AttributeList) isNull());
			visitor.startTableHeader((AttributeList) isNull());
			visitor.handleString("cell1");
			visitor.endTableHeader();
			visitor.endTableRecord();
			visitor.endTable();
			visitor.endParagraph();
			visitor.endDocument();

			replay(visitor);

			new DefaultASTParser(doc).parse(visitor);

			verify(visitor);
	}

}
