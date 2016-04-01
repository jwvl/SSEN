/**
 *
 */
package ranking.violations;

import ranking.constraints.Constraint;


/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public interface Violation {

    String toString();

    Constraint getConstraint();

    int getValue();

}
