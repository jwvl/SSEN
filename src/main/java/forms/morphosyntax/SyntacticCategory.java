/**
 *
 */
package forms.morphosyntax;

/**
 * A wrapper for a String, representing a syntactic category.
 * Additionally keeps track of created objects.
 *
 * @author jwvl
 * @date Dec 15, 2014
 */
public enum SyntacticCategory {
    N, ADJ, DET;

    public static SyntacticCategory getInstance(String name) {
        return valueOf(name);
    }

}
