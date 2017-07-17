/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.Lists;
import forms.LinearArrayForm;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import util.string.CollectionPrinter;

import java.util.Arrays;
import java.util.List;

/**
 * An MForm is a form on the Morphological level--
 * a series of Morphological Words, containing morphemes.
 *
 * @author jwvl
 * @date Dec 10, 2014
 */
public class MForm extends LinearArrayForm<MorphologicalWord> {
    private final int hashCode;

    /**
     * Constructor with M-structure and list of Morphemes.
     *
     * @param linearizedMorphemes Ordered Morphemes
     */
    MForm(MorphologicalWord[] linearizedMorphemes) {
        super(linearizedMorphemes);
        this.hashCode = Arrays.deepHashCode(linearizedMorphemes);
    }

    public MForm readFromString(String input) {
        String[] elements = input.split(" ");
        List<MorphologicalWord> toRead = Lists.newArrayList();
        for (String element: elements) {

        }
        return null;
    }

    public String toString() {
        return CollectionPrinter.collectionToString(elementsAsList(), " ");
    }

    /* (non-Javadoc)
     * @see forms.Form#getLevel()
     */
    @Override
    public Level getLevel() {
        return BiPhonSix.getMformLevel();
    }

    /**
     * @return
     */
    public List<Morpheme> getMorphemes() {
        List<Morpheme> allMorphemes = Lists.newArrayList();
        for (MorphologicalWord morphologicalWord : this) {
            for (Morpheme m: morphologicalWord) {
                allMorphemes.add(m);
            }
        }
        return allMorphemes;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MForm that = (MForm) o;
        return hashCode == that.hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * @return
     */
    public MorphemeCollection toMorphemeCollection() {
        return new MorphemeCollection(getMorphemes());
    }

    /* (non-Javadoc)
     * @see forms.Form#toBracketedString()
     */
    @Override
    public String toBracketedString() {
        return getLevel().getInfo().bracket(toString());
    }

}
