/**
 *
 */
package eval.harmony;


/**
 * @author jwvl
 * @date Nov 29, 2014
 */
public class BooleanVector extends ViolationVector {
    final boolean[] contents;

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#createEmpty(int)
     */
    static BooleanVector createEmpty(int size) {
        return new BooleanVector(size);

    }

    /**
     * Private constructor, only called by other constructor methods.
     *
     * @param size Size of contents array.
     */
    private BooleanVector(int size) {
        super(size);
        contents = new boolean[size];
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#compareFrom(int,
     * eval.harmony.ViolationVector)
     */
    @Override
    protected int compareFrom(int index, ViolationVector o) {

        BooleanVector v = (BooleanVector) o;
        int result = 0;

        while (result == 0 && index < size()) {
            result = Boolean.valueOf(contents[index]).compareTo(
                    v.contents[index]);
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
        BooleanVector copy = new BooleanVector(size());
        for (int i = leftmostNonZero; i < size(); i++) {
            copy.contents[i] = contents[i];
        }
        return copy;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.ViolationVector#doIncrement(int)
     */
    @Override
    protected void doIncrement(int index) {
        contents[index] = true;
    }

    /* (non-Javadoc)
     * @see eval.harmony.ViolationVector#mergeWith(eval.harmony.ViolationVector)
     */
    @Override
    protected void mergeWith(ViolationVector o) {
        int startAt = Math.min(leftmostNonZero, o.leftmostNonZero);
        leftmostNonZero = startAt;
        for (int i = startAt; i < contents.length; i++) {
            contents[i] = (contents[i] || o.valueAsBoolean(i));
        }

    }

    /* (non-Javadoc)
     * @see eval.harmony.ViolationVector#valueAsBoolean(int)
     */
    @Override
    protected boolean valueAsBoolean(int index) {
        return contents[index];
    }

    /* (non-Javadoc)
     * @see eval.harmony.ViolationVector#valueAsInt(int)
     */
    @Override
    protected int valueAsInt(int index) {
        return contents[index] == false ? 0 : 1;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer("[");
        for (int i = 0; i < size(); i++) {
            char c = contents[i] ? '1' : '0';
            result.append(c);
        }
        result.append(']');
        return result.toString();
    }

}
