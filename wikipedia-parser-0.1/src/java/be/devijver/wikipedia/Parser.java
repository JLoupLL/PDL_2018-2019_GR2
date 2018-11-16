package be.devijver.wikipedia;

import java.io.StringWriter;
import java.io.Writer;

import be.devijver.wikipedia.html.HtmlVisitor;
import be.devijver.wikipedia.parser.ast.parser.DefaultASTParser;
import be.devijver.wikipedia.parser.wikitext.MarkupParser;

/**
 * <p>Convenience class to convert Wikipedia wiki notation to HTML.
 *
 * <p>The following
 *
 * @author Steven Devijver
 * @since 2007-10-21
 *
 */
public class Parser {
	public static void toHtml(String wikiNotation, SmartLinkResolver smartLinkResolver, Writer out) {
		toHtml(wikiNotation, smartLinkResolver, out, false);
	}

	public static void toHtml(String wikiNotation, SmartLinkResolver smartLinkResolver, Writer out, boolean flush) {
		Visitor visitor = new HtmlVisitor(out, smartLinkResolver, flush);
		new DefaultASTParser(new MarkupParser(wikiNotation).parseDocument()).parse(visitor);
	}

	public static String toHtml(String wikiNotation, SmartLinkResolver smartLinkResolver) {
		StringWriter sw = new StringWriter();
		Visitor visitor = new HtmlVisitor(sw, smartLinkResolver, true);
		new DefaultASTParser(new MarkupParser(wikiNotation).parseDocument()).parse(visitor);
		return sw.toString();
	}

	public void withVisitor(String wikiNotation, Visitor visitor) {
		new DefaultASTParser(new MarkupParser(wikiNotation).parseDocument()).parse(visitor);
	}
}
