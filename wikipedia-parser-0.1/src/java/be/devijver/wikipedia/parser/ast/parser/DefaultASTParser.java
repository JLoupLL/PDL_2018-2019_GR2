package be.devijver.wikipedia.parser.ast.parser;

import be.devijver.wikipedia.Visitor;
import be.devijver.wikipedia.parser.ast.Content;
import be.devijver.wikipedia.parser.ast.Document;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 17:00:52
 * To change this template use File | Settings | File Templates.
 */
public class DefaultASTParser extends AbstractASTParser {
    public DefaultASTParser(Document doc) {
        super(doc);
    }

    protected void doInvokeParseMethod(Content content, Visitor visitor, Object context) {
        try {
            Method method = getClass().getMethod("parse", new Class[] { content.getClass(), Visitor.class, context.getClass() });
            method.invoke(this, new Object[] { content, visitor, context });
        } catch (NoSuchMethodException e) {
            throw new ASTParseException("Don't know how to handle AST class " + content.getClass().getName() + "!");
        } catch (IllegalAccessException e) {
            throw new ASTParseException("Error while handling AST object [" + content.toString() + "]: illegal acces!");
        } catch (InvocationTargetException e) {
            throw new ASTParseException("Error while handling AST object [" + content.toString() + "]: " + e.getTargetException().getMessage(), e.getTargetException());
        }

    }
}
