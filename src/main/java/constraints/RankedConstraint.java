package constraints;

import java.util.Objects;

/**
 * Created by janwillem on 31/03/16.
 */
public class RankedConstraint implements Comparable<RankedConstraint> {
    private final Constraint constraint;
    private final double ranking;
    private final boolean USE_STRATA = Constraint.STRATIFY;

    private RankedConstraint(Constraint constraint, double ranking) {
        this.constraint = constraint;
        this.ranking = ranking;
    }

    public static RankedConstraint of(Constraint constraint, double ranking) {
        return new RankedConstraint(constraint, ranking);
    }

    @Override
    public int compareTo(RankedConstraint o) {
        if (USE_STRATA && constraint.getStratum() != o.constraint.getStratum()) {
            return o.constraint.getStratum() - constraint.getStratum();}
        return Double.compare(o.ranking, ranking);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankedConstraint that = (RankedConstraint) o;
        return Double.compare(that.ranking, ranking) == 0 &&
                Objects.equals(constraint, that.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraint, ranking);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(constraint.toString());
        sb.append(" (").append(constraint.getStratum()).append(") ");
        sb.append(String.format("%.2f", ranking));
        return sb.toString();
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public double getRanking() {
        return ranking;
    }
}
