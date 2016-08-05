package constraints.hierarchy.violations.vectors;

import constraints.helper.ConstraintArrayList;
import constraints.hierarchy.reimpl.Hierarchy;

/**
 * Created by janwillem on 01/04/16.
 */
public interface ViolationVector extends Comparable<ViolationVector> {
    int leftmostNonZero();
    int size();
    int get(int index);
    void set(int index, int value);
    void add(int index, int value);
    void increase(int index);
    ViolationVector merge(ViolationVector other);
    ViolationVector copy();
    void addConstraints(ConstraintArrayList constraints, Hierarchy ranking);


}
