/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.phon.PhoneTransform;
import gen.mapping.FormMapping;
import gen.mapping.specific.SfPfMapping;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.MappingConstraint;

/**
 * @author jwvl
 * @date Jul 28, 2015
 */
public class CueConstraint extends MappingConstraint<SfPfMapping> {

    private final PhoneTransform forbidden;

    /**
     * Creates a faithfulness constraint forbidding a single transform.
     *
     * @param transform
     */
    private CueConstraint(PhoneTransform transform) {
        super(BiPhonSix.getPhoneticLevel());
        this.forbidden = transform;
    }

    /**
     * @param transform
     * @return
     */
    public static CueConstraint createInstance(PhoneTransform transform) {
        return new CueConstraint(transform);
    }

    public int getNumViolations(SfPfMapping f) {
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
     * @see ranking.constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ranking.constraints.MappingConstraint#getNumViolations(gen.mapping.FormMapping
     * )
     */
    @Override
    public int getNumViolations(FormMapping f) {
        if (f instanceof SfPfMapping) {
            return getNumViolations((SfPfMapping) f);
        } else {
            return 0;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("*");
        result.append(forbidden.toString());
        return result.toString();
    }


}
