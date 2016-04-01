/**
 *
 */
package gen.rule.string;

/**
 * @author jwvl
 * @date Jul 19, 2015
 */
public enum Side {
    LEFT, RIGHT, EITHER, NEITHER;
    public String abbreviation;

    static {
        LEFT.abbreviation = "L";
        RIGHT.abbreviation = "R";
        EITHER.abbreviation = "LR";
        NEITHER.abbreviation = "0";
    }
}
