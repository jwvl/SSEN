/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.phon.PhoneTransform;
import gen.mapping.FormMapping;
import gen.mapping.specific.UfSfMapping;
import grammar.levels.predefined.BiPhonSix;
import constraints.MappingConstraint;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class FaithfulnessConstraint extends MappingConstraint<UfSfMapping> {

    private final PhoneTransform forbidden;

    /**
     * Creates a faithfulness constraint forbidding a single transform.
     *
     * @param transform
     */
    private FaithfulnessConstraint(PhoneTransform transform) {
        super(BiPhonSix.getSurfaceFormLevel());
        this.forbidden = transform;
        if (transform.left().toString().equals("+") && transform.right().toString().equals("∅")) {
            System.err.println(this + "!!!");
        }
    }

    /**
     * @param transform
     * @return
     */
    public static FaithfulnessConstraint createInstance(PhoneTransform transform) {
        return new FaithfulnessConstraint(transform);
    }

    public int getNumViolations(UfSfMapping f) {
        int result = 0;
        for (PhoneTransform rewrite : f.getRewrites()) {
            if (rewrite.equals(forbidden)) {
                result += 1;
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("*");
        result.append(forbidden.toString());
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * constraints.MappingConstraint#getNumViolations(gen.mapping.FormMapping
     * )
     */
    @Override
    public int getNumViolations(FormMapping f) {
        if (f instanceof UfSfMapping) {
            return getNumViolations((UfSfMapping) f);
        } else {
            return 0;
        }
    }

}
