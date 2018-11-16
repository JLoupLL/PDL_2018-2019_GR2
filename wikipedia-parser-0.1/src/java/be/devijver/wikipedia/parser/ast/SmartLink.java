package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 0:26:18
 * To change this template use File | Settings | File Templates.
 */
public class SmartLink implements Content {
    private final String destination;
    private final String caption;

    public SmartLink(String destination) {
        this.destination = destination;
        this.caption = null;
    }

    public SmartLink(String destination, String caption) {
        this.destination = destination;
        this.caption = caption;
    }

    public String getDestination() {
        return destination;
    }

    public String getCaption() {
        return caption;
    }

    public String toString() {
        if (caption == null) {
            return "[[" + destination + "]]";
        } else {
            return "[[" + destination + "|" + caption + "]]";
        }

    }
}
