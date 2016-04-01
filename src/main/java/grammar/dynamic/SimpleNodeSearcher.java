package grammar.dynamic;

import eval.harmony.CostFactory;
import forms.Form;
import gen.mapping.FormMapping;
import grammar.dynamic.node.SimpleCostNode;
import ranking.constraints.helper.ConstraintArrayList;
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

    public void init(Form initialForm) {
        getQueue().add(SimpleCostNode.createNew(initialForm));
    }

    public void addSuccessor(SimpleCostNode parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        double[] cost = costFactory.getDoubleArray(constraintList);
        double[] mergedCost = DoubleArraySorter.mergeWithSorted(parent.getCost(), cost);
        SimpleCostNode successor = new SimpleCostNode(parent, mergedCost, formMapping);
        getQueue().add(successor);
    }


}
