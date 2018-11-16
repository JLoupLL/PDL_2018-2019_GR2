package be.devijver.wikipedia.html;

import java.io.StringWriter;

import be.devijver.wikipedia.SmartLinkResolver;
import be.devijver.wikipedia.Visitor;
import be.devijver.wikipedia.parser.ast.parser.DefaultASTParser;
import be.devijver.wikipedia.parser.wikitext.MarkupParser;
import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class HtmlVisitorTests extends TestCase {

	private String parse(String s) {
		return parse(s, null);
	}

	private String parse(String s, SmartLinkResolver smartLinkResolver) {
		StringWriter sw = new StringWriter();
		Visitor visitor = new HtmlVisitor(sw, smartLinkResolver, true);
		new DefaultASTParser(new MarkupParser(s).parseDocument()).parse(visitor);
		return sw.toString();
	}

	public void testParagraph() {
		String html = parse("This is a test");

		assertEquals("<div><p>This is a test\n</p></div>", html);
	}

	public void testTwoParagraphs() {
		String html = parse("This is a paragraph.\n\nThis is another paragraph.");

		assertEquals("<div><p>This is a paragraph.\n</p><p>This is another paragraph.\n</p></div>", html);
	}

	public void testItalics() {
		String html = parse("This ''is in'' italics.");

		assertEquals("<div><p>This \n<i>is in\n</i> italics.\n</p></div>", html);
	}

	public void testBold() {
		String html = parse("This '''is in''' bold.");

		assertEquals("<div><p>This \n<b>is in\n</b> bold.\n</p></div>", html);
	}

	public void testBoldAndItalics() {
		String html = parse("This '''''is in'' bold and italics'''.");

		assertEquals("<div><p>This \n<b><i>is in\n</i> bold and italics\n</b>.\n</p></div>", html);
	}

	public void testHeading1() {
		String html = parse("==Heading 1==");

		assertEquals("<div><h1>Heading 1\n</h1></div>", html);
	}

	public void testHeading2() {
		String html = parse("===Heading 2===");

		assertEquals("<div><h2>Heading 2\n</h2></div>", html);
	}

	public void testHeading3() {
		String html = parse("====Heading 3====");

		assertEquals("<div><h3>Heading 3\n</h3></div>", html);
	}

	public void testHeading4() {
		String html = parse("=====Heading 4=====");

		assertEquals("<div><h4>Heading 4\n</h4></div>", html);
	}

	public void testHeading5() {
		String html = parse("======Heading 5======");

		assertEquals("<div><h5>Heading 5\n</h5></div>", html);
	}

	public void testHeading6() {
		String html = parse("=======Heading 6=======");

		assertEquals("<div><h6>Heading 6\n</h6></div>", html);
	}

	public void testOrderedListWithOneItem() {
		String html = parse("# Test");

		assertEquals("<div><ol><li>Test\n</li></ol></div>", html);
	}

	public void testOrderedListWithTwoItems() {
		String html = parse("# Test1\n#Test2");

		assertEquals("<div><ol><li>Test1\n</li><li>Test2\n</li></ol></div>", html);
	}

	public void testUnorderedListWithOneItem() {
		String html = parse("* Test");

		assertEquals("<div><ul><li>Test\n</li></ul></div>", html);
	}

	public void testUnorderedListWithTwoItems() {
		String html = parse("* Test1\n* Test2");

		assertEquals("<div><ul><li>Test1\n</li><li>Test2\n</li></ul></div>", html);
	}

	public void testOrderedListWithNestedOrderedList() {
		String html = parse("# Test1\n## Test2");

		assertEquals("<div><ol><li>Test1\n<ol><li>Test2\n</li></ol></li></ol></div>", html);
	}

	public void testUnorderedListWithNestUnorderedList() {
		String html = parse("* Test1\n** Test2");

		assertEquals("<div><ul><li>Test1\n<ul><li>Test2\n</li></ul></li></ul></div>", html);
	}

	public void testOrderedListWithNestedUnorderedList() {
		String html = parse("# Test1\n#* Test2");

		assertEquals("<div><ol><li>Test1\n<ul><li>Test2\n</li></ul></li></ol></div>", html);
	}

	public void testUnorderedListWithNestedOrderedList() {
		String html = parse("* Test1\n*# Test2");

		assertEquals("<div><ul><li>Test1\n<ol><li>Test2\n</li></ol></li></ul></div>", html);
	}

	public void testOneIndent() {
		String html = parse(":One indent");

		assertEquals("<div><blockquote>One indent\n</blockquote></div>", html);
	}

	public void testTwoIndents() {
		String html = parse("::Two indents");

		assertEquals("<div><blockquote><blockquote>Two indents\n</blockquote></blockquote></div>", html);
	}

	public void testOneIndentAndTwoIndents() {
		String html = parse(":One indent\n::Two indents");

		assertEquals("<div><blockquote>One indent\n</blockquote><blockquote><blockquote>Two indents\n</blockquote></blockquote></div>", html);
	}

	public void testOneLineLiteral() {
		String html = parse(" public class Test");

		assertEquals("<div><pre>\npublic class Test\n</pre></div>", html);
	}

	public void testTwoLineLiteral() {
		String html = parse(" public class Test\n {}");

		assertEquals("<div><pre>\npublic class Test\n{}\n</pre></div>", html);
	}

	public void testNormalLink() {
		String html = parse("[http://www.google.com Google]");

		assertEquals("<div><p><a href=\"http://www.google.com\">Google\n</a></p></div>", html);
	}

	public void testSmartLinkWithoutSmartLinkResolver() {
		try {
			String html = parse("[[Smart Link|smart link]]");
			fail("RuntimeException expected: " + html);
		} catch (RuntimeException expected) {}
	}

	public void testSmartLinkWithSmartLinkResolver() {
		SmartLinkResolver smartLinkResolver =
			createStrictMock(SmartLinkResolver.class);

		smartLinkResolver.resolve("Smart Link");
		expectLastCall().andReturn("http://en.wikipedia.org");

		replay(smartLinkResolver);

		String html = parse("[[Smart Link|smart link]]", smartLinkResolver);

		verify(smartLinkResolver);

		assertEquals("<div><p><a href=\"http://en.wikipedia.org\">smart link\n</a></p></div>", html);
	}

	public void testParagraphWithCharactersToBeEscaped() {
		String html = parse("& < > é à è");

		assertEquals("<div><p>&amp; &lt; &gt; &eacute; &agrave; &egrave;\n</p></div>", html);
	}

	public void testNormalLinkWithoutCaption() throws Exception {
		String html = parse("[http://www.google.com]");

		assertEquals("<div><p><a href=\"http://www.google.com\">http://www.google.com</a></p></div>", html);
	}

	public void testSmartLinkWithoutCaption() throws Exception {
		SmartLinkResolver smartLinkResolver =
			createStrictMock(SmartLinkResolver.class);

		smartLinkResolver.resolve("Smart Link");
		expectLastCall().andReturn("http://en.wikipedia.org");

		replay(smartLinkResolver);

		String html = parse("[[Smart Link]]", smartLinkResolver);

		verify(smartLinkResolver);

		assertEquals("<div><p><a href=\"http://en.wikipedia.org\">Smart Link</a></p></div>", html);
	}

	public void testNowiki() throws Exception {
		String html = parse("This is a paragraph with <nowiki>''nowiki''\n'''nowiki'''</nowiki>..");

		assertEquals("<div><p>This is a paragraph with \n''nowiki''\n'''nowiki'''..\n</p></div>", html);
	}

	public void testPre() throws Exception {
		String html = parse("<pre><nowiki>This is no wiki</nowiki></pre>");

		assertEquals("<div><p><pre>\nThis is no wiki\n</pre></p></div>", html);
	}

	public void testTable1() throws Exception {
		String html = parse("{| |}");

		assertEquals("<div><p><table></table></p></div>", html);
	}

	public void testTable2() throws Exception {
		String html = parse(
				"{|\n" +
				"|+ This is a caption\n" +
				"|}"
			);

		assertEquals("<div><p><table><caption> This is a caption\n</caption></table></p></div>", html);
	}

	public void testTable3() throws Exception {
		String html = parse(
				"{|\n" +
				"|-\n" +
				"|cell1\n" +
				"|}"
			);

		assertEquals("<div><p><table><tr><td>cell1\n</td></tr></table></p></div>", html);
	}

	public void testTable4() throws Exception {
		String html = parse(
				"{|\n" +
				"|-\n" +
				"!cell1\n" +
				"|}"
			);

		assertEquals("<div><p><table><tr><th>cell1\n</th></tr></table></p></div>", html);
	}

	public void testTable5() throws Exception {
		String html = parse("{| cellpadding=\"5\" |}");

		assertEquals("<div><p><table cellpadding=\"5\"></table></p></div>", html);
	}

	public void testTable6() throws Exception {
		String html = parse(
				"{|\n" +
				"|+ ''This is italics''\n" +
				"|}"
			);

		assertEquals("<div><p><table><caption> \n<i>This is italics\n</i></caption></table></p></div>", html);
	}

	public void testTable7() throws Exception {
		String html = parse(
				"{|\n" +
				"|-\n" +
				"|[http://www.google.com ''Google'']\n" +
				"|}"
			);

		assertEquals("<div><p><table><tr><td><a href=\"http://www.google.com\"><i>Google\n</i></a></td></tr></table></p></div>", html);
	}
}
