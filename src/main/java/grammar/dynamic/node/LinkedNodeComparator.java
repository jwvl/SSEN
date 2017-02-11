package grammar.dynamic.node;

import constraints.SparseViolationProfile;

import java.util.Comparator;

/**
 * Created by janwillem on 05/08/16.
 * Some explanation: the short[] ints act as a pair (ranking of leftmost smaller constraint, compare
 */
public class LinkedNodeComparator implements Comparator<LinkedNode> {
    private static short[] EMPTY_ARRAY = new short[0];

    public LinkedNodeComparator() {
    }

    @Override
    public int compare(LinkedNode o1, LinkedNode o2) {
        int result;
        if (o1.getWorstViolator() == o2.getWorstViolator()) {
            result = compareRecursively(o1, o2, EMPTY_ARRAY, EMPTY_ARRAY);
        } else {
            result = o2.getWorstViolator() - o1.getWorstViolator();
        }
//        System.out.println("Comparing:");
//        System.out.println(o1.toStringRecursive());
//        System.out.println(o2.toStringRecursive());
//        System.out.println("Result: " + result);
        return result;

    }

    private int compareRecursively(LinkedNode o1, LinkedNode o2, short[] o1Profile, short[] o2Profile) {

        if (o1 == o2) {
            return SparseViolationProfile.compareProfiles(o1Profile,o2Profile);
        }

        if (o1.depth > o2.depth) {
                o1Profile = SparseViolationProfile.mergeSorted(o1.getViolationProfile(),o1Profile);
                return compareRecursively(o1.getParent(),o2, o1Profile,o2Profile);

        } else if (o1.depth < o2.depth) {
            o2Profile = SparseViolationProfile.mergeSorted(o2.getViolationProfile(),o2Profile);;
            return compareRecursively(o1,o2.getParent(),o1Profile,o2Profile);
        } else {
            o1Profile = SparseViolationProfile.mergeSorted(o1.getViolationProfile(),o1Profile);
            o2Profile = SparseViolationProfile.mergeSorted(o2.getViolationProfile(),o2Profile);;
            return compareRecursively(o1.getParent(),o2.getParent(), o1Profile, o2Profile);
        }

    }
}
