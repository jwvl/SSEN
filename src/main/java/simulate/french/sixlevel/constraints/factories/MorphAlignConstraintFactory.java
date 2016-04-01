/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forms.morphosyntax.*;
import gen.mapping.FormMapping;
import gen.rule.string.Side;
import ranking.constraints.Constraint;
import ranking.constraints.factories.FormConstraintFactory;
import ranking.constraints.helper.ConstraintArrayList;
import simulate.french.sixlevel.constraints.MorphAlignConstraint;
import util.collections.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class MorphAlignConstraintFactory extends FormConstraintFactory<MForm> {
    private Map<Pair<SyntacticCategory, AttributeSet>, MorphAlignConstraint> rightConstraintCache;
    private Map<Pair<SyntacticCategory, AttributeSet>, MorphAlignConstraint> leftConstraintCache;

    public MorphAlignConstraintFactory() {
        leftConstraintCache = Maps.newHashMap();
        rightConstraintCache = Maps.newHashMap();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ranking.constraints.factories.ConstraintFactory#addTransgressors(java
     * .util.Collection)
     */
    @Override
    public void addForm(MForm mForm) {

        for (MorphologicalWord morphologicalWord : mForm) {
            Collection<Pair<SyntacticCategory, AttributeSet>> pairs = computeOffenders(morphologicalWord);
            for (Pair<SyntacticCategory, AttributeSet> pair : pairs) {
                if (!leftConstraintCache.containsKey(pair)) {
                    createConstraints(pair);
                }
            }
        }

    }

    /**
     * @param morphologicalWord
     */
    private List<Pair<SyntacticCategory, AttributeSet>> computeOffenders(
            MorphologicalWord morphologicalWord) {
        List<Pair<SyntacticCategory, AttributeSet>> result = new ArrayList<>(morphologicalWord.size());
        SyntacticCategory syntacticCategory = morphologicalWord.getCategory();
        for (Morpheme morpheme : morphologicalWord) {
            AttributeSet attributeSet = morpheme.getAttributes();
            result.add(Pair.of(syntacticCategory, attributeSet));
        }
        return result;
    }

    private void createConstraints(
            Pair<SyntacticCategory, AttributeSet> offendingPair) {

        MorphAlignConstraint alignLeftConstraint = new MorphAlignConstraint(
                offendingPair.getLeft(), offendingPair.getRight(), Side.LEFT);
        MorphAlignConstraint alignRightConstraint = new MorphAlignConstraint(
                offendingPair.getLeft(), offendingPair.getRight(), Side.RIGHT);
        leftConstraintCache.put(offendingPair, alignLeftConstraint);
        rightConstraintCache.put(offendingPair, alignRightConstraint);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ranking.constraints.factories.ConstraintFactory#computeSpecific(graph
     * .Transgressor)
     */
    @Override
    public List<Constraint> getConstraintsForForm(MForm transgressor) {
        List<MorphAlignConstraint> possibleOffenders = Lists.newArrayList();
        List<Constraint> result = Lists.newArrayList();

        for (MorphologicalWord morphologicalWord : transgressor) {
            Collection<Pair<SyntacticCategory, AttributeSet>> pairs = computeOffenders(morphologicalWord);
            for (Pair<SyntacticCategory, AttributeSet> pair : pairs) {
                if (!leftConstraintCache.containsKey(pair)) {
                    createConstraints(pair);
                }
                possibleOffenders.add(leftConstraintCache.get(pair));
                possibleOffenders.add(rightConstraintCache.get(pair));
            }
        }
        for (MorphAlignConstraint c : possibleOffenders) {
            for (int i = 0; i < c.getNumViolations(transgressor); i++) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    protected void addViolatorsForForm(MForm right, ConstraintArrayList list) {
        for (Constraint c : getConstraintsForForm(right)) {
            list.add(c);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.factories.ConstraintFactory#createAll()
     */
    @Override
    public Collection<Constraint> getAll() {
        Collection<Constraint> result = Sets.newHashSet();
        for (Pair<SyntacticCategory, AttributeSet> pair : leftConstraintCache
                .keySet()) {
            result.add(leftConstraintCache.get(pair));
            result.add(rightConstraintCache.get(pair));
        }
        return result;
    }

    /* (non-Javadoc)
     * @see ranking.constraints.factories.ConstraintFactory#addFormMapping(gen.mapping.FormMapping)
     */
    @Override
    public void addFormMapping(FormMapping fm) {
        addForm((MForm) fm.right());

    }
}
