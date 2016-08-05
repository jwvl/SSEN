/**
 *
 */
package constraints.hierarchy.violations;

import eval.harmony.autosort.StratifiedDouble;
import constraints.Constraint;

/**
 * @author jwvl
 * @date Sep 8, 2015
 */
public class ConstraintViolation implements Comparable<ConstraintViolation> {
    private final Constraint constraint;
    private final StratifiedDouble disharmony;

    /**
     * @param constraint
     * @param disharmony
     */
    private ConstraintViolation(Constraint constraint, StratifiedDouble disharmony) {
        this.constraint = constraint;
        this.disharmony = disharmony;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ConstraintViolation cv) {
        return disharmony.compareTo(cv.disharmony);
    }

    /**
     * @param c
     * @param stratifiedDouble
     * @return
     */
    public static ConstraintViolation of(Constraint c, StratifiedDouble stratifiedDouble) {
        return new ConstraintViolation(c, stratifiedDouble);
    }

    /**
     * @return
     */
    public Constraint getConstraint() {
        return constraint;
    }

    /**
     * @return
     */
    public StratifiedDouble getDisharmony() {
        return disharmony;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());
        result = prime * result + ((disharmony == null) ? 0 : disharmony.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ConstraintViolation))
            return false;
        ConstraintViolation other = (ConstraintViolation) obj;
        if (constraint == null) {
            if (other.constraint != null)
                return false;
        } else if (!constraint.equals(other.constraint))
            return false;
        if (disharmony == null) {
            if (other.disharmony != null)
                return false;
        } else if (!disharmony.equals(other.disharmony))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s : %s", constraint, disharmony);
    }


}
