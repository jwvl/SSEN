/**
 *
 */
package forms.morphosyntax;

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
        boolean superEquals = super.equals(o);
//        if (!superEquals) {
//            System.out.println(this + " does not equal " + o + "!");
//        }
        if (!superEquals) return false;

        MStructure lexemes = (MStructure) o;

        boolean result = agreements != null ? agreements.equals(lexemes.agreements) : lexemes.agreements == null;
//        if (!result) {
//            System.out.println(this + " equals " + lexemes + "? " + result);
//        }
        return result;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (agreements != null ? agreements.hashCode() : 0);
        return result;
    }
}
