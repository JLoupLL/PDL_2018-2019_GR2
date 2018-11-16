package be.devijver.wikipedia;

import be.devijver.wikipedia.parser.ast.AttributeList;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 16:39:21
 * To change this template use File | Settings | File Templates.
 */
public interface Visitor {
    void startHeading1();

    void startHeading2();

    void startHeading3();

    void startHeading4();

    void startHeading5();

    void startHeading6();

    void endHeading1();

    void endHeading2();

    void endHeading3();

    void endHeading4();

    void endHeading5();

    void endHeading6();

    void startLiteral();

    void endLiteral();

    void handleString(String s);

    void startParagraph();

    void endParagraph();

    void startUnorderedList();

    void startUnorderedListItem();

    void endUnorderedListItem();

    void endUnorderedList();

    void startOrderedList();

    void startOrderedListItem();

    void endOrderedListItem();

    void endOrderedList();

    void startDocument();

    void endDocument();

    void startBold();

    void endBold();

    void startItalics();

    void endItalics();

    void startIndent();

    void endIndent();

    void startNormalLinkWithCaption(String s);

    void endNormalLinkWithCaption();

    void startSmartLinkWithCaption(String s);

    void endSmartLinkWithCaption();

	void handleNowiki(String nowiki);

	void handleNormalLinkWithoutCaption(String string);

	void handleSmartLinkWithoutCaption(String string);

	void startPre();

	void endPre();

	void startTable(AttributeList tableOptions);

	void endTable();

	void startCaption(AttributeList captionOptions);

	void endCaption();

	void startTableRecord(AttributeList rowOptions);

	void endTableRecord();

	void startTableData(AttributeList options);

	void endTableData();

	void startTableHeader(AttributeList list);

	void endTableHeader();
}
