package constraints.hierarchy.reimpl;

import com.google.common.collect.Iterators;
import constraints.Constraint;
import constraints.hierarchy.Con;

import java.util.Iterator;

/**
 * Created by janwillem on 05/08/16.
 */
public class IntCon implements Con {
    private final Constraint[] contents;
    private int sizeCounter = 0;

    private IntCon(Constraint[] contents) {
        this.contents = contents;
    }

    public IntCon(int expectedSize) {
        this.contents = new Constraint[expectedSize];
    }

    @Override
    public boolean contains(Constraint constraint) {
        return contents[constraint.getId()] != null;
    }

    @Override
    public void add(Constraint constraint) {
        contents[constraint.getId()] = constraint;
        sizeCounter++;
    }

    @Override
    public int size() {
        return sizeCounter;
    }

    @Override
    public Iterator<Constraint> iterator() {
        return Iterators.forArray(contents);
    }
}
