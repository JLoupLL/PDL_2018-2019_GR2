package be.devijver.wikipedia.parser.javacc.markup;

import java.io.ByteArrayInputStream;

import be.devijver.wikipedia.parser.ast.Bold;
import be.devijver.wikipedia.parser.ast.Characters;
import be.devijver.wikipedia.parser.ast.ContentIterator;
import be.devijver.wikipedia.parser.ast.Document;
import be.devijver.wikipedia.parser.ast.Italics;
import be.devijver.wikipedia.parser.ast.Paragraph;
import be.devijver.wikipedia.parser.javacc.markup.JavaCCParser;
import be.devijver.wikipedia.parser.javacc.markup.ParseException;
import junit.framework.TestCase;

public class JavaCCParserTests extends TestCase {

	private Document parse(String content) {
		try {
			return new JavaCCParser(new ByteArrayInputStream(content.getBytes())).document();
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

//	public void testParagraphWithItalics() throws Exception {
//		Document doc = parse("''This\tis italics''");
//
//		assertNotNull(doc);
//
//		ContentIterator contentIterator = doc.getContentIterator();
//
//		Paragraph para = (Paragraph) contentIterator.next();
//
//		assertFalse(contentIterator.hasNext());
//
//		assertEquals(1, para.getContent().length);
//
//		Italics italics = (Italics) para.getContent()[0];
//
//		assertEquals(1, italics.getContent().length);
//
//		assertEquals("This\tis italics", italics.getContent()[0].toString());
//	}
//
//	/**
//	 * @throws Exception
//	 */
//	/**
//	 * @throws Exception
//	 */
//	public void testParagraphWithBold() throws Exception {
//		Document doc = parse("'''This\tis bold'''");
//
//		assertNotNull(doc);
//
//		ContentIterator contentIterator = doc.getContentIterator();
//
//		Paragraph para = (Paragraph) contentIterator.next();
//
//		assertFalse(contentIterator.hasNext());
//
//		assertEquals(1, para.getContent().length);
//
//		Bold bold = (Bold) para.getContent()[0];
//
//		assertEquals(1, bold.getContent().length);
//
//		assertEquals("This\tis bold", bold.getContent()[0].toString());
//	}
//
//	public void testParagraphWithBoldAndItalics() throws Exception {
//		Document doc = parse("'''This\tis bold''' and ''this is italics''");
//
//		assertNotNull(doc);
//
//		ContentIterator contentIterator = doc.getContentIterator();
//
//		Paragraph para = (Paragraph) contentIterator.next();
//
//		assertFalse(contentIterator.hasNext());
//
//		assertEquals(3, para.getContent().length);
//
//		Bold bold = (Bold) para.getContent()[0];
//
//		assertEquals(1, bold.getContent().length);
//
//		assertEquals("This\tis bold", bold.getContent()[0].toString());
//
//		Characters characters = (Characters) para.getContent()[1];
//
//		assertEquals(" and ", characters.toString());
//
//		Italics italics = (Italics) para.getContent()[2];
//
//		assertEquals(1, italics.getContent().length);
//
//		assertEquals("this is italics", italics.getContent()[0].toString());
//	}
//
//	public void testParagraphWithNestedBoldAndItalics1() throws Exception {
//		Document doc = parse("'''''Bold and italics'''''");
//
//		assertNotNull(doc);
//
//		ContentIterator contentIterator = doc.getContentIterator();
//
//		Paragraph para = (Paragraph) contentIterator.next();
//
//		assertFalse(contentIterator.hasNext());
//
//		assertEquals(1, para.getContent().length);
//
//		Bold bold = (Bold) para.getContent()[0];
//
//		assertEquals(1, bold.getContent().length);
//
//		Italics italics = (Italics) bold.getContent()[0];
//
//		assertEquals(1, italics.getContent().length);
//
//		assertEquals("Bold and italics", italics.getContent()[0].toString());
//
//	}
//
//	public void testParagraphWithNestedBoldAndItalics2() throws Exception {
//		Document doc = parse("'''''Bold''' and italics''");
//
//		assertNotNull(doc);
//
//		ContentIterator contentIterator = doc.getContentIterator();
//
//		Paragraph para = (Paragraph) contentIterator.next();
//
//		assertFalse(contentIterator.hasNext());
//
//		assertEquals(1, para.getContent().length);
//
//		Italics italics = (Italics) para.getContent()[0];
//
//		assertEquals(2, italics.getContent().length);
//
//		Bold bold = (Bold) italics.getContent()[0];
//
//		assertEquals(1, bold.getContent().length);
//
//		assertEquals("Bold", bold.getContent()[0].toString());
//
//		Characters characters = (Characters) italics.getContent()[1];
//
//		assertEquals(" and italics", characters.toString());
//	}
//
//	public void testParagraphWithNestedBoldAndItalics3() throws Exception {
//		Document doc = parse("'''''Italics'' and bold'''");
//
//		assertNotNull(doc);
//
//		ContentIterator contentIterator = doc.getContentIterator();
//
//		Paragraph para = (Paragraph) contentIterator.next();
//
//		assertFalse(contentIterator.hasNext());
//
//		assertEquals(1, para.getContent().length);
//
//		Bold bold = (Bold) para.getContent()[0];
//
//		assertEquals(2, bold.getContent().length);
//
//		Italics italics = (Italics) bold.getContent()[0];
//
//		assertEquals(1, italics.getContent().length);
//
//		assertEquals("Italics", italics.getContent()[0].toString());
//
//		Characters characters = (Characters) bold.getContent()[1];
//
//		assertEquals(" and bold", characters.toString());
//	}

	public void testDummy() throws Exception {

	}

}
