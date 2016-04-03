package ranking.violations.vectors;

import cern.colt.list.MinMaxNumberList;
import com.typesafe.config.ConfigFactory;
import ranking.IndexedSampledHierarchy;
import ranking.constraints.Constraint;
import ranking.constraints.helper.ConstraintArrayList;

/**
 * Created by janwillem on 02/04/16.
 */
public class MinMaxViolationVector implements ViolationVector {
    private MinMaxNumberList list;
    private final int max;
    private final int length;
    private final static int maxExpectedSize = ConfigFactory.load().getInt("implementation.expectedNumConstraints");
    private final static long[] zeroes = new long[maxExpectedSize];
    private int leftmostNonZero;


    public static MinMaxViolationVector create(int max, int length) {
        MinMaxNumberList list = new MinMaxNumberList(0,max,maxExpectedSize);
        list.addAllOfFromTo(zeroes,0,maxExpectedSize-1);
        return new MinMaxViolationVector(list,max,length);
    }


    public MinMaxViolationVector(MinMaxNumberList list, int max, int length) {
        this.list = list;
        this.max = max;
        this.length = length;
    }

    @Override
    public int leftmostNonZero() {
        return leftmostNonZero;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public int get(int index) {
        return (int)list.getQuick(index);
    }

    @Override
    public void set(int index, int value) {
        list.setQuick(index,(long) value);
        if (value < leftmostNonZero) {
            leftmostNonZero = value;
        }
    }

    private void setLong(int index, long value) {
        list.setQuick(index,value);
        if (index < leftmostNonZero) {
            leftmostNonZero = index;
        }
    }

    @Override
    public void add(int index, int value) {
        list.setQuick(index,list.get(index)+value);
    }

    @Override
    public void increase(int index) {
        list.setQuick(index,list.get(index)+1L);
        if (index < leftmostNonZero) {
            leftmostNonZero = index;
        }
    }


    @Override
    public ViolationVector merge(ViolationVector other) {
        MinMaxViolationVector result = MinMaxViolationVector.create(max,length);
        for (int i=0; i < size(); i++) {
            result.add(i, get(i));
            result.add(i,other.get(i));
        }
        return result;
    }

    @Override
    public ViolationVector copy() {
        MinMaxViolationVector vector = MinMaxViolationVector.create(max, maxExpectedSize);
        for (int i=0; i < length; i++) {
            vector.setLong(i,list.getQuick(i));
        }
        return vector;
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
            return  firstResult;
        for (int i=0; i < size() && i < o.size(); i++) {
            int difference = get(i) - o.get(i);
            if (difference != 0) {
                return difference;
            }
        }
        return size()-o.size();
    }
}
