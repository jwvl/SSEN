package ranking.constraints.helper;

import cern.colt.list.IntArrayList;
import com.typesafe.config.ConfigFactory;
import ranking.constraints.Constraint;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by janwillem on 31/03/16.
 */
public class ConstraintArrayList implements Iterable<Constraint> {
    private final IntArrayList list;
    private final static int DEFAULT_INITIAL_SIZE = ConfigFactory.load().getInt("implementation.standardConstraintListSize");
    public final static ConstraintArrayList EMPTY = create(0);

    private ConstraintArrayList(IntArrayList list) {
        this.list = list;
    }


    public static ConstraintArrayList create(int initialSize) {
        IntArrayList newList = new IntArrayList(initialSize);
        return new ConstraintArrayList(newList);
    }

    public static ConstraintArrayList create() {
        return create(DEFAULT_INITIAL_SIZE);
    }

    public Constraint get(int index) {
        int valueAt = list.getQuick(index);
        return Constraint.withIndex(valueAt);
    }

    public void add(Constraint constraint) {
        list.add(constraint.getId());
    }

    public void addMultiple(Constraint constraint, int numToAdd) {
        int id = constraint.getId();
        for (int i = 0; i < numToAdd; i++) {
            list.add(id);
        }
    }

    public void addAll(Collection<Constraint> constraintCollection) {
        for (Constraint constraint : constraintCollection) {
            add(constraint);
        }
    }

    public int size() {
        return list.size();
    }


    @Override
    public Iterator<Constraint> iterator() {

        Iterator<Constraint> it = new Iterator<Constraint>() {

            private int currentIndex = 0;
            private int currentSize = list.size();

            @Override
            public boolean hasNext() {
                return currentIndex < currentSize;
            }

            @Override
            public Constraint next() {
                return Constraint.withIndex(list.getQuick(currentIndex++));
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    public void append(ConstraintArrayList other) {
        list.addAllOf(other.list);
    }
}
