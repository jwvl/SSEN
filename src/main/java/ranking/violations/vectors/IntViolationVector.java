package ranking.violations.vectors;

import com.typesafe.config.ConfigFactory;
import ranking.IndexedSampledHierarchy;
import ranking.constraints.Constraint;
import ranking.constraints.helper.ConstraintArrayList;

/**
 * Created by janwillem on 02/04/16.
 */
public class IntViolationVector implements ViolationVector {

    private final int[] contents;
    private int leftmostNonZero;

    public IntViolationVector(int size) {
        contents = new int[size];
    }

    public IntViolationVector(int[] contents) {
        this.contents = new int[contents.length];
        System.arraycopy(contents,0,this.contents,0,contents.length);
    }

    public static ViolationVector create() {
        return new IntViolationVector(ConfigFactory.load().getInt("implementation.expectedNumConstraints"));
    }


    @Override
    public int leftmostNonZero() {
        return leftmostNonZero;
    }

    @Override
    public int size() {
        return contents.length;
    }

    @Override
    public int get(int index) {
        return contents[index];
    }

    @Override
    public void set(int index, int value) {
        contents[index] = value;
        if (index < leftmostNonZero) {
            leftmostNonZero = index;
        }
    }

    @Override
    public void add(int index, int value) {
        contents[index] +=value;
        if (index < leftmostNonZero) {
            leftmostNonZero = index;
        }
    }

    @Override
    public void increase(int index) {
        contents[index]++;
        if (index < leftmostNonZero) {
            leftmostNonZero = index;
        }
    }

    @Override
    public ViolationVector merge(ViolationVector other) {
        ViolationVector result = copy();
        for (int i=0; i < other.size(); i++) {
            result.add(i,other.get(i));
        }
        return result;
    }

    @Override
    public ViolationVector copy() {
        return new IntViolationVector(contents);
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
        int compareLeftmost = o.leftmostNonZero() - leftmostNonZero;
        if (compareLeftmost != 0) {
            return compareLeftmost;
        }
        for (int i=0; i < size() && i < o.size(); i++) {
            int difference = contents[i] - o.get(i);
            if (difference != 0) {
                return difference;
            }
        }
        return size()-o.size();
    }


}
