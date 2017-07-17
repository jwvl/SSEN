/**
 *
 */
package forms.morphosyntax;

import com.google.common.base.Splitter;
import forms.LinearArrayForm;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

import java.util.*;

/**
 * @author jwvl
 * @date Dec 11, 2014
 */
public class MStructure extends LinearArrayForm<SyntacticWord> {
    final Set<Agreement> agreements;

    private MStructure(SyntacticWord[] contents) {
        super(contents);
        agreements = new HashSet<Agreement>();
        findAgreements();
    }

    public static MStructure createInstance(List<SyntacticWord> contents) {
        MStructure result = new MStructure(contents.toArray(new SyntacticWord[contents.size()]));
        return result;
    }

    private void addAgreement(SyntacticWord a, SyntacticWord b, MElement aFeature,
                              MElement bFeature) {
        agreements.add(Agreement.between(aFeature, bFeature, a, b));

    }

    private void findAgreements() {
        for (int i = 0; i < getNumSubForms(); i++) {
            SyntacticWord iSyntacticWord = contents[i];
            Set<MElement> iSet = iSyntacticWord.getAllNonConceptFeatures();
            for (int j = i + 1; j < getNumSubForms(); j++) {
                SyntacticWord jSyntacticWord = contents[j];
                Set<MElement> jSet = jSyntacticWord.getAllNonConceptFeatures();
                for (MElement aFeature : iSet) {
                    for (MElement bFeature : jSet) {
                        if (aFeature.attributeEquals(bFeature)) {
                            if (aFeature.expressesValue()
                                    && bFeature.expressesValue()) {
                                addAgreement(iSyntacticWord, jSyntacticWord, aFeature,
                                        bFeature);
                            }
                        }
                    }
                }

            }
        }
    }

    public void printContents() {
        System.out.println("Lexemes in this structure:");
        System.out.println(Arrays.toString(contents));

        System.out.println("Agreements:");
        for (Agreement agr : agreements) {
            System.out.println(agr.toString());
        }
    }

    protected SyntacticWord[] getLexemes() {
        return contents;
    }

    public String toString() {
        return concatElementsToString(" ");
    }

    //@Override
    public static MStructure readFromString(String input) {
        List<String> lexemeStrings = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(input);
        SyntacticWord[] inputSyntacticWords = new SyntacticWord[lexemeStrings.size()];
        for (int i = 0; i < lexemeStrings.size(); i++) {
            inputSyntacticWords[i] = SyntacticWord.parseFromString(lexemeStrings.get(i));
        }
        return new MStructure(inputSyntacticWords);
    }


    /**
     * @return
     */
    public Collection<Agreement> getAgreements() {
        return agreements;
    }

    public Level getLevel() {
        return BiPhonSix.getMstructureLevel();
    }

    public String toBracketedString() {
        return getLevel().getInfo().bracket(toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MStructure that = (MStructure) o;

        if (getLevel() != that.getLevel() ) return false;
        return Arrays.deepEquals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        int result = 31 *  + Arrays.deepHashCode(contents);
        result = 31 * result + getLevel().myIndex();
        return result;
    }
}
