/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import forms.morphosyntax.*;
import gen.mapping.FormMapping;
import constraints.Constraint;
import constraints.factories.FormConstraintFactory;
import constraints.helper.ConstraintArrayList;
import simulate.french.sixlevel.constraints.TypedAnalyzeConstraint;
import util.collections.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class TypedAnalyzeConstraintFactory extends FormConstraintFactory<MForm> {
    private Map<AffixType, TypedAnalyzeConstraint> constraintCache;

    public TypedAnalyzeConstraintFactory() {
        constraintCache = Maps.newHashMap();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * constraints.factories.ConstraintFactory#addTransgressors(java
     * .util.Collection)
     */
    @Override
    public void addForm(MForm mForm) {

        for (MorphologicalWord morphologicalWord : mForm) {
            SyntacticCategory syntacticCategory = morphologicalWord.getCategory();
            for (Morpheme morpheme: morphologicalWord) {
                for (MElement mElement: morpheme) {
                    if (!mElement.isConcept()) {
                        Attribute attribute = mElement.getFeature().attribute;
                        AffixType affixType = AffixType.createInstance(syntacticCategory,attribute);
                        if (!constraintCache.containsKey(affixType)) { //TODO dit kan niet
                            createConstraint(affixType);
                        }
                    }
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

    private TypedAnalyzeConstraint createConstraint(
            AffixType offendingAffix) {
        TypedAnalyzeConstraint constraint = new TypedAnalyzeConstraint(offendingAffix);
        constraintCache.put(offendingAffix, constraint);
        return constraint;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * constraints.factories.ConstraintFactory#computeSpecific(graph
     * .Transgressor)
     */
    @Override
    public List<Constraint> getConstraintsForForm(MForm transgressor) {
        List<Constraint> result = Lists.newArrayList();

        for (MorphologicalWord morphologicalWord : transgressor) {
            for (Morpheme morpheme: morphologicalWord) {
                if (morpheme.size() > 1) {
                    for (AffixType affixType : morpheme.getAffixTypes()) {
                        TypedAnalyzeConstraint morphemeConstraint = constraintCache.get(affixType);
                        if (morphemeConstraint == null) {
                            morphemeConstraint = createConstraint(affixType);
                        }
                        result.add(morphemeConstraint);
                    }
                }
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
     * @see constraints.factories.ConstraintFactory#createAll()
     */
    @Override
    public Collection<Constraint> getAll() {
        Collection<Constraint> result = Sets.newHashSet();
        for (TypedAnalyzeConstraint morphemeConstraint: constraintCache.values()) {
            result.add(morphemeConstraint);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see constraints.factories.ConstraintFactory#addFormMapping(gen.mapping.FormMapping)
     */
    @Override
    public void addFormMapping(FormMapping fm) {
        addForm((MForm) fm.right());

    }
}
