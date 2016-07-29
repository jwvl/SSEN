/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forms.morphosyntax.*;
import gen.mapping.FormMapping;
import ranking.constraints.Constraint;
import ranking.constraints.factories.FormConstraintFactory;
import ranking.constraints.helper.ConstraintArrayList;
import simulate.french.sixlevel.constraints.MorphAlignConstraint;
import simulate.french.sixlevel.constraints.MorphemeConstraint;
import util.collections.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class MorphemeConstraintFactory extends FormConstraintFactory<MForm> {
    private Map<Morpheme, MorphemeConstraint> constraintCache;

    public MorphemeConstraintFactory() {
        constraintCache = Maps.newHashMap();
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
            for (Morpheme morpheme: morphologicalWord) {
                if (!constraintCache.containsKey(morpheme)) {
                    createConstraint(morpheme);
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

    private MorphemeConstraint createConstraint(
            Morpheme offendingPair) {
        MorphemeConstraint morphemeConstraint = new MorphemeConstraint(offendingPair);
        constraintCache.put(offendingPair, morphemeConstraint);
        return morphemeConstraint;
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
            for (Morpheme morpheme: morphologicalWord) {
                MorphemeConstraint morphemeConstraint = constraintCache.get(morpheme);
                if (morphemeConstraint == null) {
                    morphemeConstraint = createConstraint(morpheme);
                }
                result.add(morphemeConstraint);
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
        for (MorphemeConstraint morphemeConstraint: constraintCache.values()) {
            result.add(morphemeConstraint);
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