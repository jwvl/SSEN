package grammar.dynamic.node;

import grammar.dynamic.persistent.LinkedNode;

import java.util.Comparator;

/**
 * Created by janwillem on 05/08/16.
 * Some explanation: the short[] ints act as a pair (ranking of leftmost smaller constraint, compare
 */
public class OldLinkedNodeComparator implements Comparator<LinkedNode> {

    public OldLinkedNodeComparator() {
    }

    @Override
    public int compare(LinkedNode o1, LinkedNode o2) {
        System.out.println("Comparing:");
        System.out.println(o1);
        System.out.println(o2);
        short[] currentLeftmost = compareRecursively(o1,o2, new short[]{Short.MAX_VALUE,0});
        System.out.println("Winner: "+currentLeftmost[1]);
        return currentLeftmost[1];

    }

    private short[] compareRecursively(LinkedNode o1, LinkedNode o2, short[] currentLeftmostNonZero) {


        if (o1 == o2) {
          //  System.out.println("These two are equal!");
            return currentLeftmostNonZero;
        }
        int o1Depth = o1.getDepth();
        if (o2 == null) {
            System.out.println("Huh?");
        }
        int o2Depth = o2.getDepth();

        if (o1Depth > o2Depth) {
                currentLeftmostNonZero = compareAndKeepLeftmost(o1,o2, currentLeftmostNonZero);
                return compareRecursively(o1.getParent(),o2, currentLeftmostNonZero);

        } else if (o1Depth < o2Depth) {
            currentLeftmostNonZero = compareAndKeepLeftmost(o1,o2, currentLeftmostNonZero);
            return compareRecursively(o1,o2.getParent(), currentLeftmostNonZero);
        } else {
            currentLeftmostNonZero = compareAndKeepLeftmost(o1,o2,currentLeftmostNonZero);
            return compareRecursively(o1.getParent(),o2.getParent(), currentLeftmostNonZero);
        }

    }

    private short[] compareAndKeepLeftmost(LinkedNode o1, LinkedNode o2, short[] currentLeftmost) {

        short[] thisComparison = o1.getLeftmostDifference(o2);
        if (thisComparison[0] < currentLeftmost[0]) {
            System.out.println("Leftmost difference is " +thisComparison[0] +" at " + thisComparison[1]);
            return thisComparison;
        } else {

            System.out.println("Leftmost difference is " +currentLeftmost[0] +" at " + currentLeftmost[1]);
            return currentLeftmost;
        }
    }
}
