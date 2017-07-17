package grammar;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import constraints.Constraint;
import constraints.helper.ConstraintArrayList;
import forms.morphosyntax.SemSynForm;
import simulate.french.sixlevel.constraints.LexicalConstraint;

import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 28/06/2017.
 */
public class ViolatingConstraintCache {
    private int lastConstraintAdded;
    private ConstraintArrayList normalViolators;
    private List<LexicalConstraint> lexicalConstraints;
    private Map<SemSynForm,ConstraintArrayList> conceptViolators;

    public ViolatingConstraintCache() {
        lastConstraintAdded = -1;
        normalViolators = ConstraintArrayList.create();
        lexicalConstraints = Lists.newArrayList();
        conceptViolators = Maps.newHashMap();
    }

    public boolean isUpToDate() {
        return lastConstraintAdded == Constraint.getNumberCreated()-1;
    }

    public ConstraintArrayList getNormalViolators() {
        if (!isUpToDate()) {
            update();
        }
        return normalViolators;
    }

    public ConstraintArrayList getForSsf(SemSynForm ssf) {
        ConstraintArrayList result = conceptViolators.get(ssf);
        if (result == null) {
            update();
            result = ConstraintArrayList.create();
            for (LexicalConstraint c: lexicalConstraints) {
                if (c.canViolateSsf(ssf)) {
                    result.add(c);
                }
            }
            conceptViolators.put(ssf,result);
        }
        return result;
    }

    private void update() {
        for (int i = lastConstraintAdded+1; i < Constraint.getNumberCreated(); i++) {
            Constraint constraint = Constraint.withIndex(i);
            if (constraint instanceof LexicalConstraint) {
                LexicalConstraint lexicalConstraint = (LexicalConstraint) constraint;
                lexicalConstraints.add(lexicalConstraint);
            } else {
                normalViolators.add(constraint);
            }
        }
        lastConstraintAdded = Constraint.getNumberCreated()-1;
    }
}
