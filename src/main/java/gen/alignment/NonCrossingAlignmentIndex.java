/**
 *
 */
package gen.alignment;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public class NonCrossingAlignmentIndex implements IAlignmentIndex {

    public static NonCrossingAlignmentIndex NULL = new NonCrossingAlignmentIndex(new int[0]);
    final int[] lengthPerGroup;
    final int numBottom;


    /**
     * @param result
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


}
