package grammar.dynamic.node.hg;

import constraints.Constraint;
import constraints.helper.ConstraintArrayList;
import constraints.hierarchy.reimpl.Hierarchy;
import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import grammar.dynamic.AbstractNodeSearcher;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 28/09/16.
 */
public class HarmonicNodeSearcher extends AbstractNodeSearcher<HarmonicCostNode> {
    private final Hierarchy hierarchy;

    public HarmonicNodeSearcher(Hierarchy hierarchy) {
        super(new PriorityQueue<>(new HarmonicNodeComparator()));
        this.hierarchy = hierarchy;
    }

    @Override
    protected HarmonicCostNode getInitial(Form initialForm) {
        PairMapping pairMapping = PairMapping.createInstance(null, initialForm);
        return new HarmonicCostNode(null, pairMapping, 0.0);
    }

    @Override
    public HarmonicCostNode getSuccessor(HarmonicCostNode parent, FormMapping formMapping, ConstraintArrayList constraintArrayList) {
        double newDisharmony = 0.0;
        for (Constraint c: constraintArrayList) {
            newDisharmony += hierarchy.getRanking(c);
        }
        return new HarmonicCostNode(parent, formMapping, newDisharmony);
    }
}
