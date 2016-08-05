package constraints.hierarchy.reimpl;

import constraints.Constraint;
import constraints.hierarchy.Con;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by janwillem on 05/08/16.
 */
public class SetCon implements Con {
    private final Set<Constraint> contents;

    private SetCon(Set<Constraint> contents) {
        this.contents = contents;
    }

    public SetCon() {
        this(Collections.synchronizedSet(new HashSet<>()));
    }

    @Override
    public boolean contains(Constraint constraint) {
        return contents.contains(constraint);
    }

    @Override
    public void add(Constraint constraint) {
        contents.add(constraint);
    }

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public Iterator<Constraint> iterator() {
        return contents.iterator();
    }
}
