/**
 *
 */
package simulate.french.sixlevel.helpers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.typesafe.config.ConfigFactory;
import forms.morphosyntax.Morpheme;
import forms.phon.LexicalMapping;
import forms.phon.Sonority;
import forms.primitives.segment.Phone;
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
        minimalMappings.clear();
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
        this.minimalMappings = Sets.newHashSet();
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
        if (minimalMappings.size() == 0) {
            minimalMappings = createMinimalMappings();
        }
        return minimalMappings;
    }

    /**
     * @return the minimalMappings
     */
    public Set<LexicalMapping> createMinimalMappings() {
        Set<LexicalMapping> result = Sets.newHashSet();
        for (Morpheme morpheme: this) {
            PhoneSubForm shortest = getCandidates(morpheme).stream().min((s1,s2) -> Integer.compare(s1.size(),s2.size())).orElse(null);
            if (shortest != null) {
                result.add(LexicalMapping.of(morpheme,shortest));
            }

        }
        return result;
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

    public void createAbstractForms(String nonZeroForm, String abstractPhone) {
        Set<LexicalMapping> toAdd = Sets.newHashSet();
        for (LexicalMapping mapping: getMinimalMappings()) {
            Morpheme morpheme = mapping.left();
            PhoneSubForm minimal = mapping.right();
            Set<PhoneSubForm> otherSubforms = repository.columnKeySet();
            String leftAdded = nonZeroForm+minimal.contentsAsString();
            String rightAdded = minimal.contentsAsString()+nonZeroForm;

            for (PhoneSubForm phoneSubForm: otherSubforms) {
                String asString = phoneSubForm.contentsAsString();
                if (asString.equals(leftAdded)) {
                    PhoneSubForm abstractLeft = PhoneSubForm.createFromString(abstractPhone+minimal.contentsAsString());
                    System.out.println("Adding " + morpheme +" -- " + abstractLeft);
                    toAdd.add(LexicalMapping.of(morpheme,abstractLeft));
                } else if (asString.equals(rightAdded)) {
                    PhoneSubForm abstractRight = PhoneSubForm.createFromString(minimal.contentsAsString()+abstractPhone);
                    toAdd.add(LexicalMapping.of(morpheme,abstractRight));
                    System.out.println("Adding " + morpheme +" -- " + abstractRight);
                }
            }
        }
        for (LexicalMapping mapping: toAdd) {
            addMapping(mapping.left(), mapping.right());
        }
    }

    public void appendPosteriorSchwas() {
        byte[] schwa = Phone.encode("ə");
        Set<LexicalMapping> toAdd = Sets.newHashSet();
        for (LexicalMapping subMapping : repository.values()) {
            Morpheme currentM = subMapping.left();
            PhoneSubForm currentP = subMapping.right();
            byte[] contents = currentP.getContents();
            if (currentP.getContents().length > 1) {
            Phone asPhone = Phone.getByCode(contents[contents.length-1]);
            if (asPhone.getSonority() != Sonority.V) {
                byte[] newContents = Arrays.copyOf(contents, contents.length + 1);
                newContents[newContents.length - 1] = schwa[0];
                PhoneSubForm newP = PhoneSubForm.createFromByteArray(newContents);
                toAdd.add(LexicalMapping.of(currentM,newP));
            }
            }
        }
        for (LexicalMapping lexicalMapping: toAdd) {
            repository.put(lexicalMapping.left(), lexicalMapping.right(), lexicalMapping);
        }
    }

}
