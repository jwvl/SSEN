package grammar.dynamic;

import eval.harmony.CostFactory;
import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import grammar.dynamic.node.DynamicCostNode;
import grammar.dynamic.node.DynamicNodeComparator;
import constraints.helper.ConstraintArrayList;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 29/03/16.
 */
public class DynamicNodeSearcher extends AbstractNodeSearcher<DynamicCostNode> {

    public DynamicNodeSearcher(DynamicNetworkGrammar grammar, CostFactory costFactory) {
        super(new PriorityQueue<>(new DynamicNodeComparator(grammar, costFactory)));
    }

    @Override
    public DynamicCostNode getInitial(Form initialForm) {
        return new DynamicCostNode(null, PairMapping.createInstance(null, initialForm));
    }


    @Override
    public DynamicCostNode getSuccessor(DynamicCostNode parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        return new DynamicCostNode(parent, formMapping);
    }
}
