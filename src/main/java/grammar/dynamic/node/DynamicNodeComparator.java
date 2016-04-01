package grammar.dynamic.node;

import eval.harmony.CostFactory;
import gen.mapping.FormMapping;
import grammar.dynamic.DynamicNetworkGrammar;
import ranking.constraints.helper.ConstraintArrayList;
import util.arrays.DoubleArraySorter;

import java.util.Comparator;
import java.util.List;

/**
 * Created by janwillem on 29/03/16.
 */
public class DynamicNodeComparator implements Comparator<DynamicCostNode> {
    private final DynamicNetworkGrammar grammar;
    private final CostFactory costFactory;


    public DynamicNodeComparator(DynamicNetworkGrammar grammar, CostFactory costFactory) {
        this.grammar = grammar;
        this.costFactory = costFactory;
    }


    @Override
    public int compare(DynamicCostNode a, DynamicCostNode b) {
        List<FormMapping> aList = a.getAllPredecessors();
        List<FormMapping> bList = b.getAllPredecessors();
        int aListSize = aList.size();
        int bListSize = bList.size();
        for (int i = 1; i <= aListSize && i <= bListSize; i++) {
            if (aList.get(aListSize - i).equals(bList.get(bListSize - i))) {
                aList.remove(aListSize - i);
                bList.remove(bListSize - i);
            }
        }
        ConstraintArrayList aViolators = grammar.getViolators(aList);
        ConstraintArrayList bViolators = grammar.getViolators(bList);
        double[] aDoubles = costFactory.getDoubleArray(aViolators);
        double[] bDoubles = costFactory.getDoubleArray(bViolators);
        int result = DoubleArraySorter.compare(aDoubles, bDoubles);
        return result;
    }
}
