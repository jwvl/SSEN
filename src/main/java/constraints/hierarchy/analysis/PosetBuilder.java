package constraints.hierarchy.analysis;

import constraints.Constraint;
import constraints.hierarchy.reimpl.Hierarchy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by janwillem on 20/08/16.
 */
public class PosetBuilder {
    private final Hierarchy constraints;
    private final ConstraintOrderMap map;

    private PosetBuilder(Hierarchy constraints, ConstraintOrderMap map) {
        this.constraints = constraints;
        this.map = map;
    }

    public static Poset<Constraint> buildPoset(Hierarchy constraints, ConstraintOrderMap map) {
        return new PosetBuilder(constraints,map).buildContents();
    }

    private Poset<Constraint> buildContents() {
        List<Constraint> tempList = new ArrayList<Constraint>(constraints.size());
        for (Constraint c: constraints) {
            tempList.add(c);
        }
        tempList.sort(map.getComparator());
        List<Set<Constraint>> listOfSets = new ArrayList<Set<Constraint>>();
        Set<Constraint> currentSet = new HashSet<Constraint>();
        Constraint current = tempList.get(0);
        currentSet.add(current);
        int compare;
        for (int i=1; i< tempList.size(); i++) {
            Constraint next = tempList.get(i);
            compare = map.getComparator().compare(current,next);
            if (compare != 0) {
                listOfSets.add(currentSet);
                currentSet = new HashSet<>();
            }
            current = next;
            currentSet.add(current);
        }
        return new Poset<>(listOfSets);
    }
}
