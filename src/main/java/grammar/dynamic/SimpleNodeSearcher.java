package grammar.dynamic;

import eval.harmony.CostFactory;
import forms.Form;
import gen.mapping.FormMapping;
import grammar.dynamic.node.SimpleCostNode;
import constraints.helper.ConstraintArrayList;
import util.arrays.DoubleArraySorter;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 29/03/16.
 */
public class SimpleNodeSearcher extends AbstractNodeSearcher<SimpleCostNode> {
    private CostFactory costFactory;

    public SimpleNodeSearcher(CostFactory costFactory) {
        super(new PriorityQueue<>());
        this.costFactory = costFactory;
    }

    public SimpleCostNode getInitial(Form initialForm) {
        return SimpleCostNode.createNew(initialForm);
    }

    public SimpleCostNode getSuccessor(SimpleCostNode parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        double[] cost = costFactory.getDoubleArray(constraintList);
        double[] mergedCost = DoubleArraySorter.mergeWithSorted(parent.getCost(), cost);
        return new SimpleCostNode(parent, mergedCost, formMapping);
    }


}
