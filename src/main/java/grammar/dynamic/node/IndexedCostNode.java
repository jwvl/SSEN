package grammar.dynamic.node;

import gen.mapping.FormMapping;
import ranking.violations.vectors.ViolationVector;

/**
 * Created by janwillem on 02/04/16.
 */
public class IndexedCostNode extends AbstractCostNode<IndexedCostNode> implements Comparable<IndexedCostNode> {
    ViolationVector cost;

    public IndexedCostNode(IndexedCostNode parent, FormMapping formMapping, ViolationVector cost) {
        super(parent, formMapping);
        this.cost = cost;
    }

    @Override
    public int compareTo(IndexedCostNode o) {
        return cost.compareTo(o.cost);
    }

    public ViolationVector getCost() {
        return cost;
    }
}
