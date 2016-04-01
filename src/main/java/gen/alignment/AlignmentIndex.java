/**
 *
 */
package gen.alignment;

import java.util.Arrays;

/**
 * @author jwvl
 * @date Dec 3, 2014
 */
public class AlignmentIndex implements IAlignmentIndex {

    private int[] groupIndices;
    private int nGroups;

    public AlignmentIndex(int[] groupIndices, int nGroups) {
        this.groupIndices = groupIndices;
        this.nGroups = nGroups;
    }

    public int[] getGroupIndices() {
        return groupIndices;
    }

    @Override
    public int getNumBottom() {
        return groupIndices.length;
    }

    @Override
    public int getNumTop() {
        return nGroups;
    }

    @Override
    public String toString() {
        return "AlignmentIndex [groupIndices="
                + Arrays.toString(groupIndices) + ", nGroups=" + nGroups
                + "]";
    }

    /* (non-Javadoc)
     * @see gen.alignment.IAlignmentIndex#toNonCrossingAlignmentIndex()
     */
    @Override
    public NonCrossingAlignmentIndex toNonCrossingAlignmentIndex() {
        int[] result = new int[nGroups];
        int currentLowest = groupIndices[0];
        for (int index : groupIndices) {
            if (index >= 0) {
                if (index < currentLowest) {
                    System.err.println("Alignment index array is inconsistent!");
                    return NonCrossingAlignmentIndex.NULL;
                } else if (index > -1) {
                    result[index]++;
                    currentLowest = index;
                }
            }
        }
        return new NonCrossingAlignmentIndex(result);
    }


}
