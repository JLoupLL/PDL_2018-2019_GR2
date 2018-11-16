package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 13:47:33
 * To change this template use File | Settings | File Templates.
 */
public class Indent extends AbstractSingleContentHolder {

    public Indent(Content content) {
        super(content);
    }

    public String toString() {
        return ": " + getContent().toString();
    }
}
