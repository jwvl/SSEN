/**
 *
 */
package forms.morphosyntax;

import com.google.common.base.Splitter;
import forms.LinearArrayForm;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

import java.util.List;

/**
 * @author jwvl
 * @date Dec 13, 2014
 */
public class SemSynForm extends LinearArrayForm<SyntacticWord> {
    SyntacticCategory headCategory;

    /**
     * @param contents
     */
    private SemSynForm(SyntacticWord[] contents) {
        super(contents);
        for (SyntacticWord l : contents) {
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

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (headCategory != null ? headCategory.hashCode() : 0);
        return result;
    }
}
