package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 15:48:29
 * To change this template use File | Settings | File Templates.
 */
public class Heading extends AbstractSingleContentHolder {

    public Heading(Content content) {
        super(content);
    }

    public String toString() {
        return "=" + getContent().toString() + "=";
    }
}
