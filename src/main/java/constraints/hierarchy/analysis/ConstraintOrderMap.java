package constraints.hierarchy.analysis;

import constraints.Constraint;
import constraints.hierarchy.reimpl.Hierarchy;

import java.util.Comparator;
import java.util.List;

/**
 * Created by janwillem on 20/08/16.
 */
public class ConstraintOrderMap {
    private List<Hierarchy> hierarchies;
    private int[][] sortingTable;
    private final Comparator<Constraint> constraintComparator;

    public ConstraintOrderMap(List<Hierarchy> hierarchies) {
        this.hierarchies = hierarchies;
        int totalNumConstraints = Constraint.getNumberCreated();
        this.sortingTable = new int[totalNumConstraints][totalNumConstraints];
        fillTable(totalNumConstraints);
        constraintComparator = new Comparator<Constraint>() {
            @Override
            public int compare(Constraint o1, Constraint o2) {
                if (o1.getId() < o2.getId()) {
                    return sortingTable[o1.getId()][o2.getId()];
                } else if (o1.getId() > o2.getId()) {
                    return -sortingTable[o2.getId()][o1.getId()];
                } else {
                    return 0;
                }
            }
        };
    }

    private void fillTable(int totalNumConstraints) {
        for (int i = 0; i < totalNumConstraints - 1; i++) {
            for (int j = i + 1; j < totalNumConstraints; j++) {
                Hierarchy first = hierarchies.get(0);
                int resultSign = getOrder(i, j, first);
                for (int h = 1; h < hierarchies.size(); h++) {
                    int testSymbol = getOrder(i, j, hierarchies.get(h));
                    if (resultSign != testSymbol) {
                        resultSign = 0;
                        break;
                    }
                }
                if (resultSign != 0) {
                    sortingTable[i][j] = resultSign;
                }
            }
        }
    }

    private int getOrder(int constraintA, int constraintB, Hierarchy hierarchy) {
        short ordinalA = hierarchy.getRankingIndex(Constraint.withIndex(constraintA));
        short ordinalB = hierarchy.getRankingIndex(Constraint.withIndex(constraintB));
        if (ordinalA < ordinalB) {
            return -1;
        } else if (ordinalA > ordinalB) {
            return 1;
        } else {
            return 0;
        }
    }

    public Comparator<Constraint> getComparator() {
        return constraintComparator;
    }
}
