package ranking.violations.vectors;

import cern.colt.bitvector.BitVector;
import com.typesafe.config.ConfigFactory;
import ranking.IndexedSampledHierarchy;
import ranking.constraints.Constraint;
import ranking.constraints.helper.ConstraintArrayList;

/**
 * Created by janwillem on 02/04/16.
 */
public class BitViolationVector implements ViolationVector {

    private final static int MAX_NUM_VIOLATIONS = ConfigFactory.load().getInt("implementation.expectedMaxViolations");
    private final BitVector vector;
    private int leftmostNonZero;

    public BitViolationVector(int size, int maxConstraintViolations) {
        this.vector = new BitVector(size*maxConstraintViolations);
        leftmostNonZero = size;
    }

    public BitViolationVector(BitVector vector) {
        this.vector = vector;
    }

    @Override
    public int leftmostNonZero() {
        return leftmostNonZero;
    }

    @Override
    public int size() {
        return vector.size()/MAX_NUM_VIOLATIONS;
    }

    @Override
    public int get(int index) {
        int startAt = index*MAX_NUM_VIOLATIONS;
        int result = 0;
        for (int i=0; i < MAX_NUM_VIOLATIONS; i++) {
            if (!vector.getQuick(startAt+i)) {
                return result;
            }
            result++;
        }
        return result;
    }

    @Override
    public void set(int index, int value) {
        int startAt = index*MAX_NUM_VIOLATIONS;
        for (int i=0; i < value; i++) {
            vector.set(startAt+i);
        }
        if (index < leftmostNonZero) {
            leftmostNonZero = index;
        }
    }

    @Override
    public void add(int index, int value) {
        set(index,get(index)+value);
    }

    @Override
    public void increase(int index) {
        int startAt = index*MAX_NUM_VIOLATIONS;
        for (int i=0; i < MAX_NUM_VIOLATIONS; i++) {
            if (!vector.getQuick(startAt+i)) {
                vector.set(startAt+i);
                return;
            }
        }
        if (index < leftmostNonZero) {
            leftmostNonZero = index;
        }
    }

    @Override
    public ViolationVector merge(ViolationVector other) {
        if (other instanceof BitViolationVector) {
            return mergeBitVectors((BitViolationVector) other);
        }
        else {
            ViolationVector copy = copy();
            for (int i=0; i < other.size(); i++) {
                copy.add(i,other.get(i));
            }
            return copy;
        }

    }

    private ViolationVector mergeBitVectors(BitViolationVector other) {
        BitVector oVector = other.vector;
        BitVector copy = vector.copy();
        copy.or(oVector);
        BitViolationVector result = new BitViolationVector(copy);
        result.leftmostNonZero = Math.min(leftmostNonZero,other.leftmostNonZero);
        return new BitViolationVector(copy);
    }

    @Override
    public ViolationVector copy() {
        BitViolationVector result = new BitViolationVector(vector.copy());
        result.leftmostNonZero = leftmostNonZero;
        return result;
    }

    @Override
    public void addConstraints(ConstraintArrayList constraints, IndexedSampledHierarchy ranking) {
        for (Constraint constraint: constraints) {
            int rankingIndex = ranking.getIndex(constraint);
            increase(rankingIndex);
        }
    }

    @Override
    public int compareTo(ViolationVector o) {
        int firstResult = o.leftmostNonZero()-leftmostNonZero;
        if (firstResult!=0)
            return firstResult;
        for (int i=leftmostNonZero; i < size(); i++) {
            int result = get(i) - o.get(i);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    public boolean bitSetAt(int index) {
        return vector.get(index);
    }

    public int sizeInBits() {
        return vector.size();
    }
}
