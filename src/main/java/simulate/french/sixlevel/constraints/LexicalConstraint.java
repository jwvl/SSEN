/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.morphosyntax.Morpheme;
import forms.phon.LexicalMapping;
import forms.primitives.segment.PhoneSubForm;
import gen.mapping.FormMapping;
import gen.mapping.specific.MfUfMapping;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.MappingConstraint;

/**
 * @author jwvl
 * @date Jul 28, 2015
 */
public class LexicalConstraint extends MappingConstraint<MfUfMapping> {

    private final LexicalMapping mapping;

    /**
     * @param leftLevel
     * @param mapping
     */
    protected LexicalConstraint(Level leftLevel, LexicalMapping mapping, double initialBias) {
        super(leftLevel, initialBias);
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
     * @see ranking.constraints.Constraint#caches()
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
     * @param morpheme
     * @param phoneSubForm
     * @return
     */
    public static LexicalConstraint createInstance(LexicalMapping mapping) {
        return new LexicalConstraint(mapping);
    }

    /**
     * @param morpheme
     * @param phoneSubForm
     * @return
     */
    public static LexicalConstraint createInstance(LexicalMapping mapping, double initialBias) {
        return new LexicalConstraint(mapping, initialBias);
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
     * @see ranking.constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("*");
        result.append(mapping.toString());
        return result.toString();
    }

}
