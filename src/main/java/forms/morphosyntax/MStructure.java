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
public class MStructure extends LinearArrayForm<Lexeme> {
    final Set<Agreement> agreements;

    private MStructure(Lexeme[] contents) {
        super(contents);
        agreements = new HashSet<Agreement>();
        findAgreements();
    }

    public static MStructure createInstance(List<Lexeme> contents) {
        MStructure result = new MStructure(contents.toArray(new Lexeme[contents.size()]));
        return result;
    }

    private void addAgreement(Lexeme a, Lexeme b, MElement aFeature,
                              MElement bFeature) {
        agreements.add(Agreement.between(aFeature, bFeature, a, b));

    }

    private void findAgreements() {
        for (int i = 0; i < getNumSubForms(); i++) {
            Lexeme iLexeme = contents[i];
            Set<MElement> iSet = iLexeme.getAllNonConceptFeatures();
            for (int j = i + 1; j < getNumSubForms(); j++) {
                Lexeme jLexeme = contents[j];
                Set<MElement> jSet = jLexeme.getAllNonConceptFeatures();
                for (MElement aFeature : iSet) {
                    for (MElement bFeature : jSet) {
                        if (aFeature.attributeEquals(bFeature)) {
                            if (aFeature.expressesValue()
                                    && bFeature.expressesValue()) {
                                addAgreement(iLexeme, jLexeme, aFeature,
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
        Arrays.toString(contents);

        System.out.println("Agreements:");
        for (Agreement agr : agreements) {
            System.out.println(agr.toString());
        }
    }

    protected Lexeme[] getLexemes() {
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
