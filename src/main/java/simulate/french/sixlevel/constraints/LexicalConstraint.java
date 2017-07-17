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
public class LexicalConstraint extends MappingConstraint<MfUfMapping> {

    private final LexicalMapping mapping;
    private final String conceptValue;
    private final static String NULL_CONCEPT = "NULL CONCEPT";

    /**
     * @param leftLevel
     * @param mapping
     */
    protected LexicalConstraint(Level leftLevel, LexicalMapping mapping, double initialBias) {
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

    public LexicalConstraint(LexicalMapping mapping) {
        this(BiPhonSix.getUnderlyingFormLevel(), mapping, 0.0);
    }

    public LexicalConstraint(LexicalMapping mapping, double initialBias) {
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
    public static LexicalConstraint createInstance(Morpheme morpheme, PhoneSubForm phoneSubForm) {
        return new LexicalConstraint(LexicalMapping.of(morpheme, phoneSubForm));
    }

    /**
     * @return
     */
    public static LexicalConstraint createInstance(LexicalMapping mapping) {
        return new LexicalConstraint(mapping);
    }

    /**
     * @return
     */
    public static LexicalConstraint createInstance(LexicalMapping mapping, double initialBias) {
        return new LexicalConstraint(mapping, initialBias);
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
            if (this.mapping.equals(mapping)) {
                result++;
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
