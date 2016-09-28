/**
 *
 */
package gen.alignment;

import java.util.Arrays;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public class NonCrossingAlignmentIndex implements IAlignmentIndex {

    public static NonCrossingAlignmentIndex NULL = new NonCrossingAlignmentIndex(new int[0]);
    final int[] lengthPerGroup;
    final int numBottom;


    /**
     */
    public NonCrossingAlignmentIndex(int[] lengths) {
        this.lengthPerGroup = lengths;
        int sum = 0;
        for (int length : lengthPerGroup) {
            sum += length;
        }
        numBottom = sum;
    }

    /* (non-Javadoc)
     * @see gen.alignment.IAlignmentIndex#getNumBottom()
     */
    @Override
    public int getNumBottom() {
        return numBottom;
    }

    /* (non-Javadoc)
     * @see gen.alignment.IAlignmentIndex#getNumTop()
     */
    @Override
    public int getNumTop() {
        return lengthPerGroup.length;
    }

    /* (non-Javadoc)
     * @see gen.alignment.IAlignmentIndex#toNonCrossingAlignmentIndex()
     */
    @Override
    public NonCrossingAlignmentIndex toNonCrossingAlignmentIndex() {
        return this;
    }

    /**
     * @return
     */
    public int[] getLengths() {
        return lengthPerGroup;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("NonCrossingAlignmentIndex{");
        sb.append("lengthPerGroup=").append(Arrays.toString(lengthPerGroup));
        sb.append(", numBottom=").append(numBottom);
        sb.append('}');
        return sb.toString();
    }
}
