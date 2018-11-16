package be.devijver.wikipedia.parser.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 4-nov-2006
 * Time: 18:02:53
 * To change this template use File | Settings | File Templates.
 */
public class Document implements ContentHolder {
    private final List contentList;
    private boolean hasContent = false;

    public Document() {
        this.contentList = new ArrayList();
    }

    public boolean hasContent() {
        return hasContent;
    }

    public int getParagraphCount() {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    public Paragraph getParagraph(int i) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public ContentIterator getContentIterator() {
        return new ContentIterator() {

            private final Iterator iter = contentList.iterator();

            public boolean hasNext() {
                return iter.hasNext();
            }

            public Content next() {
                return (Content) iter.next();
            }
        };
    }

    public void addContent(Content content) {
        if (content != null) {
            hasContent = true;
            this.contentList.add(content);
        }
    }

    public Content[] getContent() {
        return (Content[]) contentList.toArray(new Content[contentList.size()]);
    }
}
