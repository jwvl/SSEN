/**
 *
 */
package ranking.constraints.finder;

import gen.mapping.FormMapping;
import ranking.constraints.Constraint;

/**
 * @author jwvl
 * @date 24/02/2016
 */
public interface ConstraintCache {
    Constraint retrieve(FormMapping formMapping);

    boolean contains(Constraint constraint);
}
