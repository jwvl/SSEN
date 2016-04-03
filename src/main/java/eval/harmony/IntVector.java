/**
 *
 */
package eval.harmony;

import java.util.Arrays;

/**
 * @author jwvl
 * @date Nov 29, 2014
 */
public class IntVector extends ViolationVector {
    final int[] contents;

    static ViolationVector createEmpty(int size) {
        return new IntVector(size);

    }

    @Override
    public String toString() {
        return Arrays.toString(contents);
    }

    /**
     * @param size
     */
    private IntVector(int size) {
        super(size);
        contents = new int[size];
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#compareAt(int)
     */
    @Override
    protected int compareFrom(int index, ViolationVector v) {
        IntVector o = (IntVector) v;
        int result = 0;

        while (result == 0 && index < size()) {
            result = contents[index] - o.contents[index];
            index++;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#copy()
     */
    @Override
    protected ViolationVector copy() {
        IntVector copy = new IntVector(size());
        System.arraycopy(contents, leftmostNonZero, copy.contents, leftmostNonZero, size() - leftmostNonZero);
        copy.leftmostNonZero = leftmostNonZero;
        return copy;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#doIncrement(int)
     */
    @Override
    protected void doIncrement(int index) {
        contents[index]++;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#mergeWith(eval.harmony.ViolationVector)
     */
    @Override
    protected void mergeWith(ViolationVector o) {
        int startAt = Math.min(leftmostNonZero, o.leftmostNonZero);
        leftmostNonZero = startAt;
        for (int i = startAt; i < contents.length; i++) {
            contents[i] += o.valueAsInt(i);
        }


    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#valueAsBoolean(int)
     */
    @Override
    protected boolean valueAsBoolean(int index) {
        return contents[index] != 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#valueAsInt(int)
     */
    @Override
    protected int valueAsInt(int index) {
        return contents[index];
    }

}
