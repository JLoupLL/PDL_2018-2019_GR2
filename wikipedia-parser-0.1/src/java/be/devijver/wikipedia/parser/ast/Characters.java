package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 4-nov-2006
 * Time: 22:49:39
 * To change this template use File | Settings | File Templates.
 */
public class Characters implements Content {
    private final String characters;

    public Characters(String characters) {
        this.characters = characters;
    }

    public String toString() {
        return characters;
    }
}
