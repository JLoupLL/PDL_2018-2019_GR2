package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 11:13:21
 * To change this template use File | Settings | File Templates.
 */
public class AbstractSingleContentHolder implements SingleContentHolder {
    private final Content content;

    public AbstractSingleContentHolder(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }
}
