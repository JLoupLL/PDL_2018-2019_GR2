package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 12:51:50
 * To change this template use File | Settings | File Templates.
 */
public class OrderedListItem extends AbstractSingleContentHolder {

    public OrderedListItem(Content content) {
        super(content);
    }

    public String toString() {
        return "# " + getContent().toString();
    }

}
