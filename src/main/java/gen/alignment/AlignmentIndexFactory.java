/**
 *
 */
package gen.alignment;

import util.combinatorics.Combinatorics;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates (noncrossing!) alignments from a pre-filled int array
 *
 * @author jwvl
 * @date Dec 3, 2014
 */
public class AlignmentIndexFactory {
    private int nGroups;
    private List<AlignmentIndex> results;

    private AlignmentIndexFactory() {

    }

    public static AlignmentIndexFactory createInstance() {
        return new AlignmentIndexFactory();
    }

    public List<AlignmentIndex> createAll(int[] pre, int numGroups) {
        nGroups = numGroups;
        results = new ArrayList<AlignmentIndex>();
        recursivelyAlign(pre, 0, 0);
        return results;
    }

    private void recursivelyAlign(int[] aligned, int atIndex, int min) {
        if (atIndex < aligned.length) {
            if (aligned[atIndex] >= 0) {
                recursivelyAlign(aligned, atIndex + 1, aligned[atIndex]);
            } else {
                int[] leftmostPositive = Combinatorics.getLeftmostNonNegative(
                        aligned, atIndex);
                int max = leftmostPositive[1];
                if (max == -1) {
                    max = nGroups - 1;
                }
                for (int i = min; i <= max; i++) {
                    int[] changed = aligned.clone();
                    changed[atIndex] = i;
                    recursivelyAlign(changed, atIndex + 1, i);
                }
            }
        } else {
            AlignmentIndex result = new AlignmentIndex(aligned, nGroups);
            System.out.println("Found alignment: "+ result);
            results.add(result);
        }

    }

}
