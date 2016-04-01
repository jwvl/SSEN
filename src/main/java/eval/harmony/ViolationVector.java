/**
 *
 */
package eval.harmony;

import com.google.common.collect.Lists;
import util.collections.ComparableTester;

import java.util.List;

/**
 * @author jwvl
 * @date Nov 29, 2014
 */
public abstract class ViolationVector implements Comparable<ViolationVector> {
    private final int size;
    protected int leftmostNonZero;

    public static ViolationVector createBooleanVector(int size) {
        return BooleanVector.createEmpty(size);
    }

    public static ViolationVector createByteVector(int size) {
        return ByteVector.createEmpty(size);
    }

    public static ViolationVector createIntVector(int size) {
        return IntVector.createEmpty(size);
    }

    /**
     * @param size Size of vector
     */
    public ViolationVector(int size) {
        this.size = size;
        leftmostNonZero = size;
    }

    public int compareTo(ViolationVector o) {
        int result = o.leftmostNonZero - leftmostNonZero;
        return result == 0 ? compareFrom(leftmostNonZero, o) : result;

    }

    public void incrementAt(int index) {
        if (index < leftmostNonZero) {
            leftmostNonZero = index;
        }
        doIncrement(index);
    }

    public int size() {
        return size;
    }

    protected abstract int compareFrom(int index, ViolationVector o);

    protected abstract ViolationVector copy();

    protected abstract void doIncrement(int index);

    protected abstract void mergeWith(ViolationVector o);

    protected abstract boolean valueAsBoolean(int index);

    protected abstract int valueAsInt(int index);

    private static ViolationVector createRandomInt(int size) {
        ViolationVector result = createIntVector(size);
        for (int i = 0; i < size; i++) {
            int index = (int) (Math.random() * size);
            result.incrementAt(index);
        }
        return result;
    }

    private static ViolationVector createRandomBool(int size) {
        ViolationVector result = createBooleanVector(size);
        for (int i = 0; i < size / 2; i++) {
            int index = (int) (Math.random() * size);
            result.incrementAt(index);
        }
        return result;
    }

    public static void testInts(int numVectors, int size) {
        List<Comparable> vectors = Lists.newArrayList();
        for (int i = 0; i < numVectors; i++) {
            vectors.add(createRandomInt(size));
        }
        ComparableTester.printMaxFinder(vectors);

    }

    public static void testBools(int numVectors, int size) {
        List<Comparable> vectors = Lists.newArrayList();
        for (int i = 0; i < numVectors; i++) {
            vectors.add(createRandomBool(size));
        }
        ComparableTester.printMaxFinder(vectors);

    }
}
