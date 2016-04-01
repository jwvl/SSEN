/**
 *
 */
package forms.primitives.boundary;

import util.string.Regex;

/**
 * @author jwvl
 * @date 16/02/2016
 */
public enum Edge {

    WORD('#'), MORPHEME('+'), SYLLABLE('.');

    private final char symbol;

    Edge(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static boolean isEdgeSymbol(char c) {
        for (Edge edge : values()) {
            if (edge.symbol == c)
                return true;
        }
        return false;
    }

    public static Edge fromChar(char c) {
        for (Edge edge : values()) {
            if (edge.symbol == c)
                return edge;
        }
        return null;
    }

    public static String cleanFromString(String string) {
        for (Edge edge : values()) {
            char symbol = edge.symbol;
            string = string.replaceAll(Regex.charToEscapeString(symbol), "");
        }
        return string;
    }

    public String toString() {
        return "" + symbol;
    }


}
