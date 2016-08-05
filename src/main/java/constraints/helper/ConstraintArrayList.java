package constraints.helper;

import cern.colt.list.ShortArrayList;
import com.typesafe.config.ConfigFactory;
import constraints.Constraint;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by janwillem on 31/03/16.
 */
public class ConstraintArrayList implements Iterable<Constraint> {
    private final ShortArrayList list;
    private final static int DEFAULT_INITIAL_SIZE = ConfigFactory.load().getInt("implementation.standardConstraintListSize");
    public final static ConstraintArrayList EMPTY = create(0);

    private ConstraintArrayList(ShortArrayList list) {
        this.list = list;
    }


    public static ConstraintArrayList create(int initialSize) {
        ShortArrayList newList = new ShortArrayList(initialSize);
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
        short id = constraint.getId();
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
        list.beforeInsertAllOfFromTo(list.size(),other.list,0,other.list.size()-1);
    }

    public short[] getContentsAsShortArray() {
        short[] result = new short[size()];
        for (int i=0; i < size(); i++) {
            result[i] = get(i).getId();
        }
        return result;
    }

    public void trimToSize() {
        list.trimToSize();
    }
}
