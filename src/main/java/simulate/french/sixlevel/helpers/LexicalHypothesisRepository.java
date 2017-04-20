/**
 *
 */
package simulate.french.sixlevel.helpers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.typesafe.config.ConfigFactory;
import forms.morphosyntax.Morpheme;
import forms.phon.LexicalMapping;
import forms.primitives.segment.PhoneSubForm;
import gen.alignment.MorphemePhoneAlignment;
import gen.alignment.SubstringDatabank;
import util.collections.StringMultimap;

import java.util.*;

/**
 * @author jwvl
 * @date Jul 23, 2015
 */
public class LexicalHypothesisRepository implements Iterable<Morpheme> {
    private final SubstringDatabank longestCommonSubstrings;
   // private int MAX_AFFIX_LENGTH = 1;
    private int MIN_CONCEPT_LENGTH = 1;
    private int MAX_LCS_DIFFERENCE = ConfigFactory.load().getInt("lexicon.maxLcsDifference");
    private Table<Morpheme, PhoneSubForm, LexicalMapping> repository = HashBasedTable
            .create();
    private Set<LexicalMapping> minimalMappings;

    public void addAlignment(MorphemePhoneAlignment mpa) {
        if (isIllegalAlignment(mpa)){
            return;
        }
       // System.out.println("From alignment:");
       // System.out.println(mpa);
        Collection<LexicalMapping> subMappings = mpa.getLexicalSubmappings();
        for (LexicalMapping subMapping : subMappings) {
            Morpheme currentM = subMapping.left();
            PhoneSubForm currentP = subMapping.right();
            if (allowedAffixMapping(currentM, currentP)) {
//                System.out.printf("Adding mapping: %s --> %s%n", currentM,
//                        currentP);

                addMapping(currentM, currentP);
            }
        }
    }

    public boolean isIllegalAlignment(MorphemePhoneAlignment mpa) {
        for (LexicalMapping lm: mpa.getLexicalSubmappings()) {
            if (lm.left().hasConceptFeature() & lm.right().size() < MIN_CONCEPT_LENGTH) {
                return true;
            }
        }
        return false;
    }

    public void addFromString(Morpheme morpheme, String pString) {
        PhoneSubForm psf = PhoneSubForm.createFromString(pString);
        addMapping(morpheme,psf);
    }


    /**
     * @param longestCommonSubstrings
     */
    public LexicalHypothesisRepository(SubstringDatabank longestCommonSubstrings) {
        this.longestCommonSubstrings = longestCommonSubstrings;
        this.minimalMappings = new HashSet<LexicalMapping>();
    }


    /**
     * @param currentM
     * @param currentP
     * @return
     */
    private boolean allowedAffixMapping(Morpheme currentM, PhoneSubForm currentP) {
        boolean isAllowed = true;
        int lcsLength = 0;
        List<String> substringList = longestCommonSubstrings.getLongestSubstrings(currentM);
        if (!substringList.isEmpty()) {
            lcsLength = substringList.get(0).length();
        }
        if (currentP.size() -lcsLength > MAX_LCS_DIFFERENCE) {
            isAllowed= false;
        }
        return isAllowed;
        //return (currentM.hasConceptFeature() || currentP.size() <= MAX_AFFIX_LENGTH);

    }

    private void addMapping(Morpheme m, PhoneSubForm psf) {
        if (repository.get(m, psf) == null) {
            LexicalMapping toCreate = LexicalMapping.of(m, psf);
            repository.put(m, psf, toCreate);
            List<String> substringList = longestCommonSubstrings.getLongestSubstrings(m);
            for (String s : substringList) {
                if (psf.contentsAsString().equals(s)) {
                    getMinimalMappings().add(toCreate);
                }
            }
            if (substringList.isEmpty()) {
                getMinimalMappings().add(LexicalMapping.of(m, PhoneSubForm.NULL_FORM));
            }
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


    /**
     * @return the longestCommonSubstrings
     */
    public SubstringDatabank getLongestCommonSubstrings() {
        return longestCommonSubstrings;
    }


    /**
     * @return the minimalMappings
     */
    public Set<LexicalMapping> getMinimalMappings() {
        return minimalMappings;
    }

    public StringMultimap toStringMultimap() {
        StringMultimap stringMultimap = StringMultimap.createNew();
        for (Morpheme m: this) {
            String mString = m.toString();
            for (PhoneSubForm psf: getCandidates(m)) {
                String pString = psf.toString();
                stringMultimap.put(mString, pString);
            }
        }
        return stringMultimap;
    }

    public void createAbstractForms() {
        for (LexicalMapping mapping: minimalMappings) {
            Morpheme morpheme = mapping.left();
            PhoneSubForm minimal = mapping.right();
            Set<PhoneSubForm> otherSubforms = repository.columnKeySet();
            // TODO finish this
        }
    }

}
