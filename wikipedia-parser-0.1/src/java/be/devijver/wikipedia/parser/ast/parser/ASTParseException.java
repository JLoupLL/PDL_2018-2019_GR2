package be.devijver.wikipedia.parser.ast.parser;


/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 16:53:11
 * To change this template use File | Settings | File Templates.
 */
public class ASTParseException extends RuntimeException {
    public ASTParseException(String s) {
        super(s);
    }

    public ASTParseException(String s, Throwable e) {
        super(s, e);
    }
}
