package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 11:12:21
 * To change this template use File | Settings | File Templates.
 */
public class UnorderedListItem extends AbstractSingleContentHolder  {
    public UnorderedListItem(Content content) {
        super(content);
    }

    public String toString() {
        return "* " + getContent().toString();
    }
}
