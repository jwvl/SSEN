/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.Lists;
import forms.LinearArrayForm;
import forms.MorphemeCollection;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import util.string.CollectionPrinter;

import java.util.List;

/**
 * An MForm is a form on the Morphological level--
 * a series of Morphological Words, containing morphemes.
 *
 * @author jwvl
 * @date Dec 10, 2014
 */
public class MForm extends LinearArrayForm<MorphologicalWord> {

    /**
     * Constructor with M-structure and list of Morphemes.
     *
     * @param linearizedMorphemes Ordered Morphemes
     */
    MForm(MorphologicalWord[] linearizedMorphemes) {
        super(linearizedMorphemes);
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
            allMorphemes.addAll(morphologicalWord.elementsAsList());
        }
        return allMorphemes;

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
