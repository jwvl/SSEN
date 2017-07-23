/**
 *
 */
package forms.morphosyntax;

import com.google.common.base.Splitter;
import forms.LinearArrayForm;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author jwvl
 * @date Dec 13, 2014
 */
public class SemSynForm extends LinearArrayForm<SyntacticWord> {
    SyntacticCategory headCategory;
    private String[] conceptStrings;

    /**
     * @param contents
     */
    private SemSynForm(SyntacticWord[] contents) {
        super(contents);
        conceptStrings = new String[contents.length];

        for (int i=0; i < contents.length; i++) {
            SyntacticWord l = contents[i];
            conceptStrings[i] = l.getConcept().getFeatureValue();
            if (l.isHead()) {
                headCategory = l.getSyntacticCategory();
            }
        }
    }

    public static SemSynForm createFromString(String input, Level myLevel) {
        System.out.println("Creating from input: " + input);
        List<String> lexemeStrings = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(input);
        SyntacticWord[] inputSyntacticWords = new SyntacticWord[lexemeStrings.size()];
        for (int i = 0; i < lexemeStrings.size(); i++) {
            inputSyntacticWords[i] = SyntacticWord.parseFromString(lexemeStrings.get(i));
        }
        return new SemSynForm(inputSyntacticWords);
    }

    public static SemSynForm readFromString(String input) {
        Level myLevel = BiPhonSix.getSemSynFormLevel();
        SemSynForm result = createFromString(input, myLevel);
        return result;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (SyntacticWord l : contents) {
            result.append(l.toString());
            result.append(' ');
        }
        return result.toString();
    }

    /* (non-Javadoc)
     * @see forms.Form#getLevel()
     */
    @Override
    public Level getLevel() {
        return BiPhonSix.getSemSynFormLevel();
    }

    /* (non-Javadoc)
     * @see forms.Form#toBracketedString()
     */
    @Override
    public String toBracketedString() {
        return getLevel().getInfo().bracket(toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SemSynForm lexemes = (SemSynForm) o;
        return headCategory == lexemes.headCategory;
    }

    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(contents), headCategory);
    }

    public String[] getConceptStrings() {
        return conceptStrings;
    }
}
