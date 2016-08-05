package constraints;

import candidates.Candidate;
import com.google.common.collect.Ordering;
import com.typesafe.config.ConfigFactory;
import gen.mapping.FormMapping;
import grammar.levels.Level;
import constraints.helper.ConstraintArrayList;

/**
 * A Constraint can be seen as a function mapping a Transgressor type object
 * (i.e. an entity capable of incurring violations) to an integer (being the
 * number of violations inflicted on that transgressor by this Constraint).
 * Additionally they may get one or more labels, to distinguish between
 * different constraint "families".
 *
 * @author Jan-Willem van Leussen, Jan 15, 2015
 */
public abstract class Constraint {
    private final short id;
    protected static short idCounter = 0;
    private static int DEFAULT_STRATUM = 0;
    private int stratum;
    private static Constraint[] map = new Constraint[ConfigFactory.load().getInt("implementation.expectedNumConstraints")];
    private final double initialBias;
    protected final Level rightLevel;

    private static Ordering<Constraint> levelOrdering = new Ordering<Constraint>() {

        @Override
        public int compare(Constraint c1, Constraint c2) {
            if (c1.rightLevel != c2.rightLevel) {
                return c1.rightLevel.compareTo(c2.rightLevel);
            } else
                return (c1.toString().compareTo(c2.toString()));
        }

    };

    protected Constraint(Level rightLevel, double initialBias) {
        this.stratum = DEFAULT_STRATUM;
        this.initialBias = initialBias;
        this.id = idCounter++;
        this.rightLevel = rightLevel;
        map[this.id] = this;
        System.out.println("Created constraint #" + id);
    }

    protected Constraint(Level rightLevel) {
        this(rightLevel, 0.0);
    }

    public static int getNumberCreated() {
        return idCounter;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Constraint))
            return false;
        Constraint other = (Constraint) obj;
        return getId() == other.getId();
    }

    // TODO Maybe do this differently, over the list of transgressors?
    public int getNumViolations(Candidate can) {
        int count = 0;

        for (FormMapping fm : can.getMappings()) {
            count += getNumViolations(fm);
        }
        return count;
    }

    public abstract int getNumViolations(FormMapping fm);

    public void appendViolations(FormMapping fm, ConstraintArrayList list) {
        list.addMultiple(this, getNumViolations(fm));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId();
        return result;
    }


    public abstract String toString();

    /**
     * @return the id
     */
    public short getId() {
        return id;
    }

    /**
     * @return the levelOrdering
     */
    public static Ordering<Constraint> getLevelOrdering() {
        return levelOrdering;
    }

    public abstract boolean caches();

    /**
     * @return
     */
    public double getInitialBias() {
        return initialBias;
    }


    public void setStratum(int stratum) {
        this.stratum = stratum;
    }

    public int getStratum() {
        return stratum;
    }

    public static Constraint withIndex(int index) {
        return map[index];
    }
}
