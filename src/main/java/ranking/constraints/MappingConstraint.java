/**
 *
 */
package ranking.constraints;

import gen.mapping.FormMapping;
import grammar.levels.Level;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public abstract class MappingConstraint<F extends FormMapping> extends Constraint {

    /**
     * @param leftLevel
     */
    protected MappingConstraint(Level leftLevel) {
        super(leftLevel);
    }

    /**
     * @param leftLevel
     * @param initialBias
     */
    public MappingConstraint(Level leftLevel, double initialBias) {
        super(leftLevel, initialBias);
    }

    @Override
    public abstract int getNumViolations(FormMapping f);


}
