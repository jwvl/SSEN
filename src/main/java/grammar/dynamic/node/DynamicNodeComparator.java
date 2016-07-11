package grammar.dynamic.node;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eval.harmony.CostFactory;
import gen.mapping.FormMapping;
import grammar.dynamic.DynamicNetworkGrammar;
import ranking.constraints.helper.ConstraintArrayList;
import util.arrays.DoubleArraySorter;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;

/**
 * Created by janwillem on 29/03/16.
 */
public class DynamicNodeComparator implements Comparator<DynamicCostNode> {
    private final DynamicNetworkGrammar grammar;
    private final CostFactory costFactory;
    private final LoadingCache<DynamicCostNode,double[]> cachedCosts;


    public DynamicNodeComparator(DynamicNetworkGrammar grammar, CostFactory costFactory) {
        this.grammar = grammar;
        this.costFactory = costFactory;
        CacheLoader<DynamicCostNode,double[]> candidateLoader = new CacheLoader<DynamicCostNode,double[]>() {
            @Override
            public double[] load(DynamicCostNode node) throws Exception {
                return computeCost(node);
            }
        };
        cachedCosts = CacheBuilder.newBuilder().maximumSize(50000).weakKeys().concurrencyLevel(1).build(candidateLoader);

    }

    public double[] computeCost(DynamicCostNode node) {
        return getCostRecursive(ConstraintArrayList.create(),node);
    }

    @Override
    public int compare(DynamicCostNode o1, DynamicCostNode o2) {
        int result;
        try {
            if (o1 == null || o2 == null) {
                System.err.println("Error!");
            }
            double[] firstCosts = cachedCosts.get(o1);
            double[] secondCosts = cachedCosts.get(o2);

            result = DoubleArraySorter.compare(firstCosts,secondCosts);
        } catch (ExecutionException e) {
            result = 0;
        }
        return result;
    }

    private double[] getCostRecursive(ConstraintArrayList violators, DynamicCostNode node) {
        if (node.isInitialNode()) {
            double[] mergeWith = costFactory.getDoubleArray(violators);
            return DoubleArraySorter.mergeWithSorted(new double[0],mergeWith);
        }
        double[] cachedCost = cachedCosts.getIfPresent(node);
        if (cachedCost != null) {
            double[] mergeWith = costFactory.getDoubleArray(violators);
            return DoubleArraySorter.mergeWithSorted(cachedCost,mergeWith);
        } else {
            FormMapping mapping = node.getFormMapping();
            violators.append(grammar.getViolators(mapping));
            //System.out.printf("Getting cost for parent of %s <-- %s%n:",node.getMappedForm(), node.getParent().getMappedForm());
            return getCostRecursive(violators,node.getParent());
        }
    }


//    @Override
//    public int compare(DynamicCostNode a, DynamicCostNode b) {
//        List<FormMapping> aList = a.getAllPredecessors();
//        List<FormMapping> bList = b.getAllPredecessors();
//        int aListSize = aList.getNumSteps();
//        int bListSize = bList.getNumSteps();
//        int overlapAt = Math.min(aListSize,bListSize);
//        for (int i = 1; i <= overlapAt; i++) {
//            if (aList.get(overlapAt - i).equals(bList.get(overlapAt - i))) {
//                aList.remove(overlapAt - i);
//                bList.remove(overlapAt - i);
//            }
//        }
//        ConstraintArrayList aViolators = grammar.getViolators(aList);
//        ConstraintArrayList bViolators = grammar.getViolators(bList);
//        double[] aDoubles = costFactory.getDoubleArray(aViolators);
//        double[] bDoubles = costFactory.getDoubleArray(bViolators);
//        int result = DoubleArraySorter.compare(aDoubles, bDoubles);
//        return result;
//    }
}
