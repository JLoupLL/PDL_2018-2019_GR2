package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 1:28:45
 * To change this template use File | Settings | File Templates.
 */
public class Bold extends AbstractContentHolder {
    public Bold(Content[] content) {
        super(content);
    }

    public String toString() {
        return "'''" + super.toString() + "'''";
    }
}
