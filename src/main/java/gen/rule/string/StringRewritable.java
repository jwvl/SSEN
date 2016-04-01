/**
 *
 */
package gen.rule.string;

/**
 * @author jwvl
 * @date Jul 11, 2015
 */
public interface StringRewritable {
    String toRewritableString();

    char getSubformConcatenator();

    char getLeftBoundarySymbol();

    char getRightBoundarySymbol();

    byte[] toByteArray();

}
