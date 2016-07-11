package grammar.dynamic.node;

import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import util.arrays.DoubleArraySorter;

/**
 * Created by janwillem on 29/03/16.
 */
public class SimpleCostNode extends AbstractCostNode<SimpleCostNode> implements Comparable<SimpleCostNode> {
    private double[] cost;

    public SimpleCostNode(SimpleCostNode parent, double[] cost, FormMapping formMapping) {
        super(parent, formMapping);
        this.cost = cost;
    }

    public SimpleCostNode createSuccessor(FormMapping mapping, double[] newCost) {
        double[] mergedCost = DoubleArraySorter.mergeWithSorted(cost, newCost);
        SimpleCostNode successor = new SimpleCostNode(this, mergedCost, mapping);
        cost = null;
        return successor;
    }

    public static SimpleCostNode createNew(Form initialForm) {
        PairMapping pairMapping = PairMapping.createInstance(null, initialForm);
        return new SimpleCostNode(null, new double[0], pairMapping);
    }


    @Override
    public int compareTo(SimpleCostNode o) {
        int result = DoubleArraySorter.compare(cost, o.cost);
        return result;
    }

    public double[] getCost() {
        return cost;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SimpleCostNode{");
        sb.append(getFormMapping());
        sb.append(", ");
        if (cost == null) sb.append("null");
        else {
            sb.append('[');
            for (int i = 0; i < cost.length; ++i)
                sb.append(i == 0 ? "" : ", ").append(String.format("%.2f", cost[i]));
            sb.append(']');
        }
        sb.append('}');
        return sb.toString();
    }


}
