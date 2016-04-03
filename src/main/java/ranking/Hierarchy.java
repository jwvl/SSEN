package ranking;

import com.typesafe.config.ConfigFactory;
import ranking.constraints.Constraint;
import ranking.constraints.RankedConstraint;

import java.util.*;

/**
 * Created by janwillem on 29/03/16.
 */
public abstract class Hierarchy implements Con {
    private double[] map;

    public abstract double getRanking(Constraint c);

    public void addConstraint(Constraint c, double value) {
        put(c,value);
    }

    public Hierarchy(double[] contents) {
        map = contents;
    }

    public Hierarchy(int expectedSize) {
        map = new double[expectedSize];
        Arrays.fill(map,Double.NEGATIVE_INFINITY);
    }

    public Hierarchy() {
        this(ConfigFactory.load().getInt("implementation.expectedNumConstraints"));
    }



    /* (non-Javadoc)
     * @see ranking.Con#contains(ranking.constraints.Constraint)
     */
    public boolean contains(Constraint constraint) {
        if (map == null) {
            System.err.println("Map is null!!");
        } else if (constraint == null) {
            System.err.println("Constraint is null!");
        }
        return map[constraint.getId()] != Double.NEGATIVE_INFINITY;
    }

    public void put(Constraint constraint, double value) {
        map[constraint.getId()] = value;
    }

    public abstract int size();

    /**
     *
     */
    public void printContents() {
        for (RankedConstraint rc : toRankedConstraintList()) {
            System.out.println(rc);
        }
    }

    public abstract List<RankedConstraint> toRankedConstraintList();


    protected double getRankingValue(Constraint c) {
        return map[c.getId()];
    }


}
