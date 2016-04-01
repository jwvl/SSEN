/**
 *
 */
package forms.primitives;


/**
 * @author jwvl
 * @date Nov 16, 2014
 */
public interface ISubmapping<S extends Subform, T extends Subform> {

    S left();

    T right();
}
