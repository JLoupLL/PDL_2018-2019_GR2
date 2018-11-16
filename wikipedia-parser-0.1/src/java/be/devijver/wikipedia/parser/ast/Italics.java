package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 1:30:50
 * To change this template use File | Settings | File Templates.
 */
public class Italics extends AbstractContentHolder {
    public Italics(Content[] content) {
        super(content);
    }

    public String toString() {
        return "''" + super.toString() + "''";
    }
}
