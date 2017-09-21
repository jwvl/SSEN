/**
 *
 */
package simulate.french.sixlevel.constraints;

import constraints.MappingConstraint;
import forms.FormPair;
import forms.morphosyntax.MElement;
import forms.morphosyntax.MFeatureType;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.SemSynForm;
import forms.phon.LexicalMapping;
import forms.primitives.segment.PhoneSubForm;
import gen.mapping.FormMapping;
import gen.mapping.specific.MfUfMapping;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

/**
 * @author jwvl
 * @date Jul 28, 2015
 */
public class PositiveLexicalConstraint extends MappingConstraint<MfUfMapping> {

    private final LexicalMapping mapping;
    private final String conceptValue;
    private final static String NULL_CONCEPT = "NULL CONCEPT";

    /**
     * @param leftLevel
     * @param mapping
     */
    protected PositiveLexicalConstraint(Level leftLevel, LexicalMapping mapping, double initialBias) {
        super(leftLevel, initialBias);
        String eventualValue = "NULL CONCEPT";
        for (MElement element: mapping.left()) {
            if (element.getType() == MFeatureType.CONCEPT) {
                eventualValue = element.getFeatureValue();
                break;
                }
            }

        this.conceptValue = eventualValue;
        this.mapping = mapping;
    }

    public PositiveLexicalConstraint(LexicalMapping mapping) {
        this(BiPhonSix.getUnderlyingFormLevel(), mapping, 0.0);
    }

    public PositiveLexicalConstraint(LexicalMapping mapping, double initialBias) {
        this(BiPhonSix.getUnderlyingFormLevel(), mapping, initialBias);
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {
        return false; // Is computed dynamically
    }

    /**
     * @param morpheme
     * @param phoneSubForm
     * @return
     */
    public static PositiveLexicalConstraint createInstance(Morpheme morpheme, PhoneSubForm phoneSubForm) {
        return new PositiveLexicalConstraint(LexicalMapping.of(morpheme, phoneSubForm));
    }

    /**
     * @return
     */
    public static PositiveLexicalConstraint createInstance(LexicalMapping mapping) {
        return new PositiveLexicalConstraint(mapping);
    }

    /**
     * @return
     */
    public static PositiveLexicalConstraint createInstance(LexicalMapping mapping, double initialBias) {
        return new PositiveLexicalConstraint(mapping, initialBias);
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
        int result = 0;
        if (f instanceof MfUfMapping) {
            result = getNumViolations((MfUfMapping) f);
        } else {
            result = 0;
        }
        return result;
    }

    public int getNumViolations(MfUfMapping f) {
        int result = 0;
        for (LexicalMapping mapping : f.getLexicalMappings()) {
            if (this.mapping.left().equals(mapping.left())) {
                if (!this.mapping.right().equals(mapping.right())) {
                    result++;
                }
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
        result.append(mapping.toString());
        return result.toString();
    }

    @Override
    public boolean canViolatePair(FormPair pair) {
        if (pair.left() instanceof SemSynForm) {
            SemSynForm ssf = (SemSynForm) pair.left();
            return canViolateSsf(ssf);
        } else {
            return false;
        }
    }

    public boolean canViolateSsf(SemSynForm ssf) {
        for (String value: ssf.getConceptStrings()) {
            if (value.equals(conceptValue)) {
                return true;
            }
        }
        return false;
    }
}
