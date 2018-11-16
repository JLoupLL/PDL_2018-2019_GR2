package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 13:58:29
 * To change this template use File | Settings | File Templates.
 */
public class Literal extends AbstractContentHolder {

    public Literal(Content[] content) {
        super(content);
    }

    public String toString() {
        return " " + super.toString();
    }
}
