package be.devijver.wikipedia.parser.wikitext;

import junit.framework.TestCase;
import be.devijver.wikipedia.parser.ast.Characters;
import be.devijver.wikipedia.parser.ast.ContentIterator;
import be.devijver.wikipedia.parser.ast.Document;
import be.devijver.wikipedia.parser.ast.Heading;
import be.devijver.wikipedia.parser.ast.Indent;
import be.devijver.wikipedia.parser.ast.Literal;
import be.devijver.wikipedia.parser.ast.NormalLink;
import be.devijver.wikipedia.parser.ast.Nowiki;
import be.devijver.wikipedia.parser.ast.OrderedListItem;
import be.devijver.wikipedia.parser.ast.Paragraph;
import be.devijver.wikipedia.parser.ast.Pre;
import be.devijver.wikipedia.parser.ast.SmartLink;
import be.devijver.wikipedia.parser.ast.SpecialConstruct;
import be.devijver.wikipedia.parser.ast.UnorderedListItem;
import be.devijver.wikipedia.parser.wikitext.MarkupParser;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 4-nov-2006
 * Time: 18:02:02
 * To change this template use File | Settings | File Templates.
 */
public class MarkupParserTests extends TestCase {

    public void testEmptyDocument() {
        Document doc = new MarkupParser("").parseDocument();

        assertFalse(doc.hasContent());
    }

    public void testDocumentWithOneParagraph() {
        Document doc = new MarkupParser("This is a test.").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a test.", para.toString());
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithTwoParagraphs() {
        Document doc = new MarkupParser("This is a test.\nThis is another test.").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para1 = (Paragraph) contentIterator.next();
        Paragraph para2 = (Paragraph) contentIterator.next();

        assertEquals("This is a test.", para1.toString());
        assertEquals("This is another test.", para2.toString());
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithUnclosedCurlyBracket() {
        Document doc = new MarkupParser("This is a test.[This is another test.").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a test.[This is another test.", para.toString());
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithNormalLink() {
    	Document doc = new MarkupParser("[http://www.google.com Google]").parseDocument();

    	assertTrue(doc.hasContent());

    	ContentIterator contentIterator = doc.getContentIterator();
    	Paragraph para = (Paragraph) contentIterator.next();

    	assertEquals("[http://www.google.com Google]", para.toString());
    	assertEquals("[http://www.google.com Google]", para.getContent()[0].toString());
    	assertEquals(1, para.getContent().length);

    	assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithOneParagraphWithOneNormalLink() {
        Document doc = new MarkupParser("This is a test [http://www.google.com].").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a test [http://www.google.com].", para.toString());
        assertEquals("This is a test ", para.getContent()[0].toString());
        assertEquals("[http://www.google.com]", para.getContent()[1].toString());
        assertEquals(3, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneNormalLinkWithCaption() {
        Document doc = new MarkupParser("This is a test [http://www.google.com Google site].").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();
        NormalLink link = (NormalLink) para.getContent()[1];

        assertEquals("This is a test [http://www.google.com Google site].", para.toString());
        assertEquals("This is a test ", para.getContent()[0].toString());
        assertEquals("[http://www.google.com Google site]", link.toString());
        assertEquals("http://www.google.com", link.getUrl());
        assertEquals("Google site", link.getCaption());
        assertEquals(3, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneSmartLink() {
        Document doc = new MarkupParser("This is a test [[Java]].").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();
        SmartLink link = (SmartLink) para.getContent()[1];

        assertEquals("This is a test [[Java]].", para.toString());
        assertEquals("This is a test ", para.getContent()[0].toString());
        assertEquals("[[Java]]", link.toString());
        assertEquals("Java", link.getDestination());
        assertEquals(3, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneSmartLinkWithCaption() {
        Document doc = new MarkupParser("This is a test [[Java|Java programming language]].").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();
        SmartLink link = (SmartLink) para.getContent()[1];

        assertEquals("This is a test [[Java|Java programming language]].", para.toString());
        assertEquals("This is a test ", para.getContent()[0].toString());
        assertEquals("[[Java|Java programming language]]", link.toString());
        assertEquals("Java", link.getDestination());
        assertEquals("Java programming language", link.getCaption());
        assertEquals(3, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneBoldSection() {
        Document doc = new MarkupParser("This is a '''test'''.").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a '''test'''.", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("'''test'''", para.getContent()[1].toString());
        assertEquals(".", para.getContent()[2].toString());
        assertEquals(3, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneBoldSectionThatNeverCloses() {
        Document doc = new MarkupParser("This is a '''test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a '''test'''", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("'''test'''", para.getContent()[1].toString());
        assertEquals(2, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneBoldSectionThatClosesWithOneSingleQuote() {
        Document doc = new MarkupParser("This is a '''test'").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a '''test''''", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("'''test''''", para.getContent()[1].toString());
        assertEquals(2, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneBoldSectionThatClosesWithTwoSingleQuotes() {
        Document doc = new MarkupParser("This is a '''test''").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a '''test'''", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("'''test'''", para.getContent()[1].toString());
        assertEquals(2, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneBoldSectionWith4SingleQuotes() {
        Document doc = new MarkupParser("This is a ''''test''''.").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a ''''test''''.", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("''''test'''", para.getContent()[1].toString());
        assertEquals("'.", para.getContent()[2].toString());
        assertEquals(3, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneItalicsSection() {
        Document doc = new MarkupParser("This is a ''test''.").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a ''test''.", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("''test''", para.getContent()[1].toString());
        assertEquals(".", para.getContent()[2].toString());
        assertEquals(3, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneItalicsSectionThatNeverCloses() {
        Document doc = new MarkupParser("This is a ''test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a ''test''", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("''test''", para.getContent()[1].toString());
        assertEquals(2, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneItalicsSectionThatClosesWithOneSingleQuote() {
        Document doc = new MarkupParser("This is a ''test'.").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a ''test'.''", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("''test'.''", para.getContent()[1].toString());
        assertEquals(2, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneParagraphWithOneBoldAndOneItalicsSection() {
        Document doc = new MarkupParser("This is a '''''test'''''.").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("This is a '''''test'''''.", para.toString());
        assertEquals("This is a ", para.getContent()[0].toString());
        assertEquals("'''''test'''''", para.getContent()[1].toString());
        assertEquals(".", para.getContent()[2].toString());
        assertEquals(3, para.getContent().length);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneUnorderedListItem() {
        Document doc = new MarkupParser("* This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        UnorderedListItem listItem = (UnorderedListItem) contentIterator.next();

        assertEquals("* This is a test", listItem.toString());
        assertEquals("This is a test", listItem.getContent().toString());
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithTwoUnorderedListItems() {
        Document doc = new MarkupParser("** This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        UnorderedListItem listItem = (UnorderedListItem) contentIterator.next();

        assertEquals("* * This is a test", listItem.toString());
        assertEquals("* This is a test", listItem.getContent().toString());
        assertTrue(listItem.getContent() instanceof UnorderedListItem);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneUnorderedListItemWithoutSpace() {
        Document doc = new MarkupParser("*This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        UnorderedListItem listItem = (UnorderedListItem) contentIterator.next();

        assertEquals("* This is a test", listItem.toString());
        assertEquals("This is a test", listItem.getContent().toString());
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithTwoUnorderedListItemsWithoutSpace() {
        Document doc = new MarkupParser("**This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        UnorderedListItem listItem = (UnorderedListItem) contentIterator.next();

        assertEquals("* * This is a test", listItem.toString());
        assertEquals("* This is a test", listItem.getContent().toString());
        assertTrue(listItem.getContent() instanceof UnorderedListItem);
        assertFalse(contentIterator.hasNext());

    }



    //====================

    public void testDocumentWithOneOrderedListItem() {
        Document doc = new MarkupParser("# This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        OrderedListItem listItem = (OrderedListItem) contentIterator.next();

        assertEquals("# This is a test", listItem.toString());
        assertEquals("This is a test", listItem.getContent().toString());
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithTwoOrderedListItems() {
        Document doc = new MarkupParser("## This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        OrderedListItem listItem = (OrderedListItem) contentIterator.next();

        assertEquals("# # This is a test", listItem.toString());
        assertEquals("# This is a test", listItem.getContent().toString());
        assertTrue(listItem.getContent() instanceof OrderedListItem);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithOneOrderedListItemWithoutSpace() {
        Document doc = new MarkupParser("#This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        OrderedListItem listItem = (OrderedListItem) contentIterator.next();

        assertEquals("# This is a test", listItem.toString());
        assertEquals("This is a test", listItem.getContent().toString());
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithTwoOrderedListItemsWithoutSpace() {
        Document doc = new MarkupParser("##This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        OrderedListItem listItem = (OrderedListItem) contentIterator.next();

        assertEquals("# # This is a test", listItem.toString());
        assertEquals("# This is a test", listItem.getContent().toString());
        assertTrue(listItem.getContent() instanceof OrderedListItem);
        assertFalse(contentIterator.hasNext());

    }

    public void testDocumentWithParagraphWithOneIndent() {
        Document doc = new MarkupParser(": This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Indent indent = (Indent) contentIterator.next();

        assertEquals(": This is a test", indent.toString());
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithParagraphWithTwoIndents() {
        Document doc = new MarkupParser(":: This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Indent indent = (Indent) contentIterator.next();

        assertEquals(": : This is a test", indent.toString());
        assertEquals(": This is a test", indent.getContent().toString());
        assertTrue(indent.getContent() instanceof Indent);
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithParagraphWithOneIndentWithoutSpace() {
        Document doc = new MarkupParser(":This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Indent indent = (Indent) contentIterator.next();

        assertEquals(": This is a test", indent.toString());
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithParagraphWithTwoIndentsWithoutSpace() {
        Document doc = new MarkupParser("::This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Indent indent = (Indent) contentIterator.next();

        assertEquals(": : This is a test", indent.toString());
        assertEquals(": This is a test", indent.getContent().toString());
        assertTrue(indent.getContent() instanceof Indent);
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithOneLiteral() {
        Document doc = new MarkupParser(" This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Literal literal = (Literal) contentIterator.next();

        assertEquals(" This is a test", literal.toString());
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithOneLiteralWithMarkup() {
        Document doc = new MarkupParser(" This ''is'' a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Literal literal = (Literal) contentIterator.next();

        assertEquals(" This ''is'' a test", literal.toString());
        assertEquals(3, literal.getContent().length);
        assertEquals("This ", literal.getContent()[0].toString());
        assertEquals("''is''", literal.getContent()[1].toString());
        assertEquals(" a test", literal.getContent()[2].toString());
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithOneParagraphThatStartsWithEqualSign() {
        Document doc = new MarkupParser("= This is a test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Paragraph paragraph = (Paragraph) contentIterator.next();

        assertEquals("= This is a test", paragraph.toString());
        assertEquals(1, paragraph.getContent().length);
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithOneHeading() {
        Document doc = new MarkupParser("==This is a test==").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Heading heading = (Heading) contentIterator.next();

        assertEquals("=This is a test=", heading.toString());
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithOneHeadingWithTrailingText() {
        Document doc = new MarkupParser("==This is a test==test").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Heading heading = (Heading) contentIterator.next();
        Paragraph para = (Paragraph) contentIterator.next();

        assertEquals("=This is a test=", heading.toString());
        assertEquals("test", para.toString());
        assertFalse(contentIterator.hasNext());
    }


    public void testDocumentWithTwoHeadings() {
        Document doc = new MarkupParser("==This is a test==\n===This is another test===").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Heading heading1 = (Heading) contentIterator.next();
        Heading heading2 = (Heading) contentIterator.next();

        assertEquals("=This is a test=", heading1.toString());
        assertEquals("==This is another test==", heading2.toString());
        assertFalse(contentIterator.hasNext());
    }

    public void testDocumentWithTwoHeadingsWithoutNewLineOrOtherSeparator() {
        Document doc = new MarkupParser("==This is a test=====This is another test===").parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();
        Heading heading1 = (Heading) contentIterator.next();
        Heading heading2 = (Heading) contentIterator.next();

        assertEquals("=This is a test=", heading1.toString());
        assertEquals("==This is another test==", heading2.toString());
        assertFalse(contentIterator.hasNext());
    }


    // ==== Complex content ====

    public void testComplextContent1() {
        String content = "==This is a test==\n" +
                "This is ''test''. Actually, we're testing if this text can be successfully parsed.\n\n" +
                "If this test should [[Failure|fail]] we will have to fix it. Look [http://www.google.com here] for more info.\n\n" +
                "What follows is an example:\n\n" +
                " This is a test\n\n" +
                ":Thank you for '''your attention'''.";
        Document doc = new MarkupParser(content).parseDocument();

        assertTrue(doc.hasContent());
        ContentIterator contentIterator = doc.getContentIterator();

        Heading heading = (Heading) contentIterator.next();
        Paragraph para1 = (Paragraph) contentIterator.next();
        Paragraph para2 = (Paragraph) contentIterator.next();
        Paragraph para3 = (Paragraph) contentIterator.next();
        Literal literal1 = (Literal) contentIterator.next();
        Indent indent = (Indent) contentIterator.next();

        assertFalse(contentIterator.hasNext());

        assertEquals(3, para1.getContent().length);
        assertEquals(5, para2.getContent().length);
        assertEquals(1, para3.getContent().length);
    }

    public void testNowiki() {
    	String content = "<nowiki>Test</nowiki>";

    	Document doc = new MarkupParser(content).parseDocument();

    	assertNotNull(doc.getContent());

    	ContentIterator contentIterator = doc.getContentIterator();

    	Paragraph para = (Paragraph) contentIterator.next();

    	assertEquals(1, para.getContent().length);
    	assertTrue(para.getContent()[0] instanceof Nowiki);
    	assertEquals("<nowiki>Test</nowiki>", para.getContent()[0].toString());
    	assertEquals("Test", ((Nowiki)para.getContent()[0]).getNowiki());

    	assertFalse(contentIterator.hasNext());
    }

    public void testNowikiInParagraph() {
    	String content = "This is a <nowiki>nowiki</nowiki> test.";

    	Document doc = new MarkupParser(content).parseDocument();

    	assertNotNull(doc.getContent());

    	ContentIterator contentIterator = doc.getContentIterator();

    	Paragraph para = (Paragraph) contentIterator.next();

    	assertEquals(3, para.getContent().length);
    	assertTrue(para.getContent()[1] instanceof Nowiki);
    	assertEquals("<nowiki>nowiki</nowiki>", para.getContent()[1].toString());
    	assertEquals("nowiki", ((Nowiki)para.getContent()[1]).getNowiki());

    	assertFalse(contentIterator.hasNext());

    }

    public void testMultilineNowiki() {
    	String content =
    		"This is a paragraph.\n" +
    		"\n" +
    		"<nowiki>\n" +
    		"# Wikipedia\n" +
    		"## Syntax\n" +
    		"* Examples\n" +
    		"** Here\n" +
    		"</nowiki>\n" +
    		"\n" +
    		"Another paragraph.";

    	Document doc = new MarkupParser(content).parseDocument();

    	assertNotNull(doc.getContent());

    	ContentIterator contentIterator = doc.getContentIterator();

    	Paragraph firstPara = (Paragraph) contentIterator.next();
    	Paragraph nowikiPara = (Paragraph) contentIterator.next();
    	Paragraph lastPara = (Paragraph) contentIterator.next();

    	assertFalse(contentIterator.hasNext());

    	assertEquals("This is a paragraph.", firstPara.toString());
    	assertEquals("Another paragraph.", lastPara.toString());
    	assertEquals(1, nowikiPara.getContent().length);
    	assertTrue(nowikiPara.getContent()[0] instanceof Nowiki);
    }

    public void testFakeNowiki1() throws Exception {
		String content =
			"This is <no wiki ;-)";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator = doc.getContentIterator();

		Paragraph para = (Paragraph) contentIterator.next();

		assertFalse(contentIterator.hasNext());

		assertEquals(1, para.getContent().length);

		Characters chars = (Characters) para.getContent()[0];

		assertEquals("This is <no wiki ;-)", chars.toString());
	}

    public void testPre() throws Exception {
		String content =
			"<pre><nowiki>This is no wiki</nowiki></pre>";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator = doc.getContentIterator();

		Paragraph para = (Paragraph) contentIterator.next();

		assertEquals(1, para.getContent().length);

		Pre pre = (Pre) para.getContent()[0];

		assertFalse(contentIterator.hasNext());

		assertNotNull(pre.getNowiki());

		assertEquals("This is no wiki", pre.getNowiki().getNowiki());
	}

    public void testPreWithParagraph() throws Exception {
		String content =
			"<pre><nowiki>This is no wiki</nowiki></pre>\nAnother para.";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator = doc.getContentIterator();

		Paragraph para = (Paragraph) contentIterator.next();

		assertEquals(1, para.getContent().length);

		Pre pre = (Pre) para.getContent()[0];

		Paragraph para2 = (Paragraph) contentIterator.next();

		assertFalse(contentIterator.hasNext());

		assertEquals("Another para.", para2.getContent()[0].toString());

		assertNotNull(pre.getNowiki());

		assertEquals("This is no wiki", pre.getNowiki().getNowiki());
	}


    public void testPreWithWhitespace() throws Exception {
		String content =
			"<pre> \n\t\n\n    <nowiki>This is no wiki</nowiki></pre>";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator = doc.getContentIterator();

		Paragraph para = (Paragraph) contentIterator.next();

		assertEquals(1, para.getContent().length);

		Pre pre = (Pre) para.getContent()[0];

		assertFalse(contentIterator.hasNext());

		assertNotNull(pre.getNowiki());

		assertEquals("This is no wiki", pre.getNowiki().getNowiki());
	}

    public void testPreWithTrialingWhitespace() throws Exception {
		String content =
			"<pre> \n\t\n\n    <nowiki>This is no wiki</nowiki>  \t  \n\n</pre>\nAnother para.";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator = doc.getContentIterator();

		Paragraph para1 = (Paragraph) contentIterator.next();

		assertEquals(1, para1.getContent().length);

		Pre pre = (Pre) para1.getContent()[0];

		Paragraph para2 = (Paragraph) contentIterator.next();

		assertFalse(contentIterator.hasNext());

		assertEquals(1, para2.getContent().length);
		assertEquals("Another para.", para2.getContent()[0].toString());

		assertNotNull(pre.getNowiki());

		assertEquals("This is no wiki", pre.getNowiki().getNowiki());
	}

    public void testSpecialConstruct1() throws Exception {
		String content = "This contains a {{xy{{a{{}}bc}}}}.";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator = doc.getContentIterator();

		Paragraph para = (Paragraph) contentIterator.next();

		assertFalse(contentIterator.hasNext());

		assertEquals(3, para.getContent().length);

		assertEquals("This contains a ", para.getContent()[0].toString());

		assertEquals(".", para.getContent()[2].toString());

		SpecialConstruct specialConstruct =
			(SpecialConstruct) para.getContent()[1];

		assertEquals('{', specialConstruct.getCharacter());
		assertEquals("xy{{a{{}}bc}}", specialConstruct.getContent());
	}

    public void testTableSpecialConstruct() throws Exception {
		String content =
			"{| border=\"1\" cellspacing=\"0\" \n" +
			"cellpadding=\"5\" align=\"center\"\n" +
			"! This\n" +
			"! is\n" +
			"|- \n" +
			"| a\n" +
			"| table\n" +
			"|-\n" +
			"|}";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator = doc.getContentIterator();

		Paragraph para = (Paragraph) contentIterator.next();

		assertFalse(contentIterator.hasNext());

		assertEquals(1, para.getContent().length);

		SpecialConstruct specialConstruct =
			(SpecialConstruct) para.getContent()[0];

		assertEquals('|', specialConstruct.getCharacter());
		assertEquals(" border=\"1\" cellspacing=\"0\" \n" +
			"cellpadding=\"5\" align=\"center\"\n" +
			"! This\n" +
			"! is\n" +
			"|- \n" +
			"| a\n" +
			"| table\n" +
			"|-\n", specialConstruct.getContent());
	}

    public void testTableSpecialConstructWithTrailingParagraph() throws Exception {
		String content =
			"{| border=\"1\" cellspacing=\"0\" \n" +
			"cellpadding=\"5\" align=\"center\"\n" +
			"! This\n" +
			"! is\n" +
			"|- \n" +
			"| a\n" +
			"| table\n" +
			"|-\n" +
			"|}This is another paragraph.";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator = doc.getContentIterator();

		Paragraph para1 = (Paragraph) contentIterator.next();

		assertTrue(contentIterator.hasNext());

		Paragraph para2 = (Paragraph) contentIterator.next();

		assertFalse(contentIterator.hasNext());

		assertEquals(1, para1.getContent().length);

		SpecialConstruct specialConstruct =
			(SpecialConstruct) para1.getContent()[0];

		assertEquals('|', specialConstruct.getCharacter());
		assertEquals(" border=\"1\" cellspacing=\"0\" \n" +
			"cellpadding=\"5\" align=\"center\"\n" +
			"! This\n" +
			"! is\n" +
			"|- \n" +
			"| a\n" +
			"| table\n" +
			"|-\n", specialConstruct.getContent());

		assertEquals(1, para2.getContent().length);
		assertEquals("This is another paragraph.", para2.getContent()[0].toString());
	}

    public void testNestedTablesSpecialConstruct1() throws Exception {
		String content =
			"{| border=\"1\" cellpadding=\"5\" cellspacing=\"0\" align=\"center\"\n" +
			"|+'''An example table'''\n" +
			"|-\n" +
			"! style=\"background:#efefef;\" | First header\n" +
			"! colspan=\"2\" style=\"background:#ffdead;\" | Second header\n" +
			"|-\n" +
			"| upper left\n" +
			"| &nbsp;\n" +
			"| rowspan=2 style=\"border-bottom:3px solid grey;\" valign=\"top\" |\n" +
			"right side\n" +
			"|-\n" +
			"| style=\"border-bottom:3px solid grey;\" | lower left\n" +
			"| style=\"border-bottom:3px solid grey;\" | lower middle\n" +
			"|-\n" +
			"| colspan=\"3\" align=\"center\" |\n" +
			"{| border=\"0\"\n" +
			"|+''A table in a table''\n" +
			"|-\n" +
			"| align=\"center\" width=\"150px\" | [[Image:Wiki.png]]\n" +
			"| align=\"center\" width=\"150px\" | [[Image:Wiki.png]]\n" +
			"|-\n" +
			"| align=\"center\" colspan=\"2\" style=\"border-top:1px solid red; border-right:1px\n" +
			"          solid red; border-bottom:2px solid red; border-left:1px solid red;\" |\n" +
			"Two Wikimedia logos\n" +
			"|}\n" +
			"|}";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator =
			doc.getContentIterator();

		Paragraph para = (Paragraph) contentIterator.next();

		assertFalse(contentIterator.hasNext());

		assertEquals(1, para.getContent().length);

		SpecialConstruct specialConstruct =
			(SpecialConstruct) para.getContent()[0];

		assertEquals('|', specialConstruct.getCharacter());
		assertEquals(" border=\"1\" cellpadding=\"5\" cellspacing=\"0\" align=\"center\"\n" +
			"|+'''An example table'''\n" +
			"|-\n" +
			"! style=\"background:#efefef;\" | First header\n" +
			"! colspan=\"2\" style=\"background:#ffdead;\" | Second header\n" +
			"|-\n" +
			"| upper left\n" +
			"| &nbsp;\n" +
			"| rowspan=2 style=\"border-bottom:3px solid grey;\" valign=\"top\" |\n" +
			"right side\n" +
			"|-\n" +
			"| style=\"border-bottom:3px solid grey;\" | lower left\n" +
			"| style=\"border-bottom:3px solid grey;\" | lower middle\n" +
			"|-\n" +
			"| colspan=\"3\" align=\"center\" |\n" +
			"{| border=\"0\"\n" +
			"|+''A table in a table''\n" +
			"|-\n" +
			"| align=\"center\" width=\"150px\" | [[Image:Wiki.png]]\n" +
			"| align=\"center\" width=\"150px\" | [[Image:Wiki.png]]\n" +
			"|-\n" +
			"| align=\"center\" colspan=\"2\" style=\"border-top:1px solid red; border-right:1px\n" +
			"          solid red; border-bottom:2px solid red; border-left:1px solid red;\" |\n" +
			"Two Wikimedia logos\n" +
			"|}\n", specialConstruct.getContent());
	}

    public void testNestedTablesSpecialConstruct2() throws Exception {
		String content =
			"{|border=\"1\" cellpadding=\"4\" cellspacing=\"2\"\n" +
			"|\n" +
			"{|cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
			"|align=\"right\" width=\"50%\"| 432 ||width=\"50%\"| .1\n" +
			"|}\n" +
			"|-\n" +
			"|\n" +
			"{|cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
			"|align=\"right\" width=\"50%\"| 43 ||width=\"50%\"| .21\n" +
			"|}\n" +
			"|-\n" +
			"|\n" +
			"{|cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
			"|align=\"right\" width=\"50%\"| 4 ||width=\"50%\"| .321\n" +
			"|}\n" +
			"|}";

		Document doc = new MarkupParser(content).parseDocument();

		assertNotNull(doc);

		ContentIterator contentIterator =
			doc.getContentIterator();

		Paragraph para = (Paragraph) contentIterator.next();

		assertFalse(contentIterator.hasNext());

		assertEquals(1, para.getContent().length);

		SpecialConstruct specialConstruct =
			(SpecialConstruct) para.getContent()[0];

		assertEquals('|', specialConstruct.getCharacter());
		assertEquals("border=\"1\" cellpadding=\"4\" cellspacing=\"2\"\n" +
			"|\n" +
			"{|cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
			"|align=\"right\" width=\"50%\"| 432 ||width=\"50%\"| .1\n" +
			"|}\n" +
			"|-\n" +
			"|\n" +
			"{|cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
			"|align=\"right\" width=\"50%\"| 43 ||width=\"50%\"| .21\n" +
			"|}\n" +
			"|-\n" +
			"|\n" +
			"{|cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
			"|align=\"right\" width=\"50%\"| 4 ||width=\"50%\"| .321\n" +
			"|}\n", specialConstruct.getContent());
	}

    public void testImproperlyNestedSpecialConstructs1() throws Exception {
		String content =
			"{{ab{{cd}}ef";

		try {
			new MarkupParser(content).parseDocument();
			fail();
		} catch (RuntimeException e) {}
	}

    public void testImproperlyNestedSpecialConstructs2() throws Exception {
		String content =
			"{{ab{{cdef}}";

		try {
			new MarkupParser(content).parseDocument();
			fail();
		} catch (RuntimeException e) {}
	}

}
