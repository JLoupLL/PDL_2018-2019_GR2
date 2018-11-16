package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 4-nov-2006
 * Time: 23:16:57
 * To change this template use File | Settings | File Templates.
 */
public class NormalLink implements Content {
    private final String url;
    private final String caption;

    public NormalLink(String url) {
        this.url = url;
        this.caption = null;
    }

    public NormalLink(String url, String caption) {
        this.url = url;
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public String getCaption() {
        return caption;
    }

    public String toString() {
        if (caption == null) {
            return "[" + url + "]";
        } else {
            return "[" + url + " " + caption + "]";
        }
    }
}
