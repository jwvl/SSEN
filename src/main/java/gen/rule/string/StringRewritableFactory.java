/**
 *
 */
package gen.rule.string;


/**
 * @author jwvl
 * @date Jul 18, 2015
 */
public abstract class StringRewritableFactory<S extends StringRewritable> {
    public abstract S createFromString(String s);

    public abstract S getDummy();

}
