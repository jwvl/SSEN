package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import constraints.Constraint;
import constraints.factories.FormConstraintFactory;
import constraints.helper.ConstraintArrayList;
import forms.morphosyntax.*;
import gen.mapping.FormMapping;
import simulate.french.sixlevel.constraints.MorphOrderConstraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by janwillem on 01/07/2017.
 */
public class MorphOrderConstraintFactory extends FormConstraintFactory<MForm> {

    private Multimap<MorphologicalWord, Constraint> constraintCache;
    private final Constraint[][][] allPossibilities;
    private List<Constraint> allConstraints;

    public MorphOrderConstraintFactory() {
        allConstraints = new ArrayList<>();
        allPossibilities = createPossibilities();
        constraintCache = HashMultimap.create();
    }

    private Constraint[][][] createPossibilities() {
        SyntacticCategory[] syntacticCategories = SyntacticCategory.values();
        Attribute[] attributes = Attribute.values();
        int numSynCats = syntacticCategories.length;
        int numAttributes = attributes.length;
        Constraint[][][] result = new Constraint[numSynCats][numAttributes][numAttributes];
        for (int i = 0; i < numSynCats; i++) {
            SyntacticCategory syntacticCategory = syntacticCategories[i];
            for (int j=0; j < numAttributes; j++) {
                Attribute first = attributes[j];
                for (int k = 0; k < numAttributes; k++) {
                    Attribute second = attributes[k];
                    if (j != k) {
                        Constraint made = new MorphOrderConstraint(syntacticCategory,first,second);
                        System.out.println("Created constraint " + made);
                        result[i][j][k] = made;
                        allConstraints.add(made);
                    }
                }
            }
        }
        return result;
    }

    private Constraint getOffender(SyntacticCategory syntacticCategory, Attribute first, Attribute second) {
        return allPossibilities[syntacticCategory.ordinal()][first.ordinal()][second.ordinal()];
    }

    private List<Constraint> getOffendingConstraintsForMWord(MorphologicalWord morphologicalWord) {
        List<Constraint> result = new ArrayList<>();
        List<Morpheme> morphemes = morphologicalWord.elementsAsList();
        SyntacticCategory syntacticCategory = morphologicalWord.getCategory();
        for (int i=0; i < morphemes.size()-1; i++) {
            Morpheme firstMorph = morphemes.get(i);
            for (int j = i+1; j < morphemes.size(); j++) {
                Morpheme secondMorph = morphemes.get(j);
                result.addAll(getOffendersForMorphemes(syntacticCategory,firstMorph,secondMorph));
            }
        }
        return result;
    }

    private Collection<Constraint> getOffendersForMorphemes(SyntacticCategory synCat, Morpheme firstMorph, Morpheme secondMorph) {
        List<Constraint> result = Lists.newArrayListWithExpectedSize(6);
        for (MElement firstElement: firstMorph.elementsAsSet()) {
            for (MElement secondElement: secondMorph.elementsAsSet()) {
                Constraint found = getOffender(synCat, firstElement.getAttribute(),secondElement.getAttribute());
                result.add(found);
            }
        }
        return result;
    }

    @Override
    public void addFormMapping(FormMapping fm) {
addForm((MForm)fm.right());
    }

    @Override
    public Collection<Constraint> getAll() {
        return allConstraints;
    }

    @Override
    public void addForm(MForm morphologicalWords) {
        // Not necessary?
    }

    @Override
    public List<Constraint> getConstraintsForForm(MForm form) {
        List<Constraint> result = new ArrayList<>(form.size());
        for (MorphologicalWord mWord: form.elementsAsList()) {
            if (!constraintCache.containsKey(mWord)) {
                List<Constraint> toAddToCache = getOffendingConstraintsForMWord(mWord);
                constraintCache.putAll(mWord, toAddToCache);
            }
            result.addAll(constraintCache.get(mWord));
        }
        return result;
    }

    @Override
    protected void addViolatorsForForm(MForm right, ConstraintArrayList list) {
        list.addAll(getConstraintsForForm(right));
    }
}
