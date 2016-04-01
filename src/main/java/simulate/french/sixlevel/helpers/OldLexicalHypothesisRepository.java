/**
 *
 */
package simulate.french.sixlevel.helpers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import forms.morphosyntax.Morpheme;
import forms.phon.LexicalMapping;
import forms.primitives.segment.PhoneSubForm;
import gen.alignment.MorphemePhoneAlignment;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author jwvl
 * @date Jul 23, 2015
 */
public class OldLexicalHypothesisRepository implements Iterable<Morpheme> {
    private int MAX_AFFIX_LENGTH = 1;
    private Table<Morpheme, PhoneSubForm, LexicalMapping> repository = HashBasedTable
            .create();

    public void addAlignment(MorphemePhoneAlignment mpa) {
        System.out.println("From alignment:");
        System.out.println(mpa);
        Collection<LexicalMapping> subMappings = mpa.getLexicalSubmappings();
        for (LexicalMapping subMapping : subMappings) {
            Morpheme currentM = subMapping.left();
            PhoneSubForm currentP = subMapping.right();
            if (allowedAffixMapping(currentM, currentP)) {
                System.out.printf("Adding mapping: %s --> %s%n", currentM,
                        currentP);

                addMapping(currentM, currentP);
            }

        }
    }

    /**
     * @param currentM
     * @param currentP
     * @return
     */
    private boolean allowedAffixMapping(Morpheme currentM, PhoneSubForm currentP) {
        return (currentM.hasConceptFeature() || currentP.size() <= MAX_AFFIX_LENGTH);

    }

    private void addMapping(Morpheme m, PhoneSubForm psf) {
        if (repository.get(m, psf) == null) {
            repository.put(m, psf, LexicalMapping.of(m, psf));
        }
    }

    public Collection<PhoneSubForm> getCandidates(Morpheme morpheme) {
        return repository.row(morpheme).keySet();
    }

    public Collection<LexicalMapping> getSubmappings(Morpheme morpheme) {
        return repository.row(morpheme).values();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Morpheme> iterator() {
        return repository.rowKeySet().iterator();
    }

    /**
     *
     */
    public void printContents() {
        for (Morpheme m : this) {
            System.out.printf("** Lexical hypotheses for %s : **\n",
                    m.toString());
            Collection<PhoneSubForm> subforms = getCandidates(m);
            for (PhoneSubForm psf : subforms) {
                System.out.printf("  %s\n", psf);
            }
        }
    }

}
