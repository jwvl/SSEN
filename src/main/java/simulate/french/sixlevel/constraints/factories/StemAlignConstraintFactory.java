package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.ImmutableList;
import forms.morphosyntax.MForm;
import gen.mapping.FormMapping;
import gen.rule.string.Side;
import ranking.constraints.Constraint;
import ranking.constraints.factories.FormConstraintFactory;
import ranking.constraints.helper.ConstraintArrayList;
import simulate.french.sixlevel.constraints.StemAlignConstraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by janwillem on 31/03/16.
 */
public class StemAlignConstraintFactory extends FormConstraintFactory<MForm> {
    private final StemAlignConstraint leftConstraint;
    private final StemAlignConstraint rightConstraint;
    private final Collection<Constraint> totalSet;

    public StemAlignConstraintFactory() {
        this.leftConstraint = new StemAlignConstraint(Side.LEFT);
        this.rightConstraint = new StemAlignConstraint(Side.RIGHT);
        totalSet = ImmutableList.of(leftConstraint, rightConstraint);
    }

    @Override
    public void addForm(MForm morphologicalWords) {
        // Not necessary
    }

    @Override
    public List<Constraint> getConstraintsForForm(MForm form) {
        List<Constraint> result = new ArrayList<>();
        for (int i = 0; i < leftConstraint.getNumViolations(form); i++) {
            result.add(leftConstraint);
        }
        for (int i = 0; i < rightConstraint.getNumViolations(form); i++) {
            result.add(rightConstraint);
        }
        return result;
    }

    @Override
    protected void addViolatorsForForm(MForm form, ConstraintArrayList list) {
        for (int i = 0; i < leftConstraint.getNumViolations(form); i++) {
            list.add(leftConstraint);
        }
        for (int i = 0; i < rightConstraint.getNumViolations(form); i++) {
            list.add(rightConstraint);
        }
    }

    @Override
    public void addFormMapping(FormMapping fm) {
        // Not necessary
    }

    @Override
    public Collection<Constraint> getAll() {
        return totalSet;
    }
}
