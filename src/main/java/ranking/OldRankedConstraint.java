package ranking;

import candidates.Candidate;
import com.google.common.collect.Ordering;
import gen.mapping.FormMapping;
import ranking.constraints.Constraint;

import java.text.DecimalFormat;

/**
 * A RankedConstraint is a composition class that is used to put a Constraint in
 * a real-valued stochastic Hierarchy, as proposed by Boersma (1998 et seq).
 * RankedConstraints have a "ranking value" and "disharmony". They are sorted on
 * the latter, which is the former plus some Gaussian noise.
 *
 * @author jwvl
 * @date May 3, 2014
 */
public class OldRankedConstraint {
    private final Constraint constraint;
    protected SortingValues sortingValues;
    private long lastSamplingId;

    /**
     * Custom comparator. Compares by stratum, then disharmony. Defined in
     * SortingValues. This would be the usual way of comparing in a Hierarchy.
     */
    public static Ordering<OldRankedConstraint> DisharmonyComparator = new Ordering<OldRankedConstraint>() {

        @Override
        public int compare(OldRankedConstraint arg0, OldRankedConstraint arg1) {
            SortingValues s0 = arg0.getSortingValues();
            SortingValues s1 = arg1.getSortingValues();
            return -s0.getOrdering().compare(s0, s1);
        }

    };
    /**
     * Custom comparator. Compares by stratum, then disharmony. This would be
     * the usual way of comparing in a Hierarchy.
     */
    public static Ordering<OldRankedConstraint> idComparator = new Ordering<OldRankedConstraint>() {
        public int compare(OldRankedConstraint a, OldRankedConstraint b) {
            int aId = a.getConstraint().getId();
            int bId = b.getConstraint().getId();
            return aId - bId;
        }
    };
    /**
     * Compares RankedConstraints first by stratum, then by ranking values.
     */
    public static Ordering<OldRankedConstraint> RankingComparator = new Ordering<OldRankedConstraint>() {

        @Override
        public int compare(OldRankedConstraint arg0, OldRankedConstraint arg1) {
            SortingValues s0 = arg0.getSortingValues();
            SortingValues s1 = arg1.getSortingValues();
            return s0.getRankingOrdering().compare(s0, s1);
        }

    };

    /**
     * Compares RankedConstraints first by stratum, then by ranking values.
     */
    public static Ordering<OldRankedConstraint> levelOrdering = new Ordering<OldRankedConstraint>() {

        @Override
        public int compare(OldRankedConstraint a, OldRankedConstraint b) {
            return Constraint.getLevelOrdering().compare(a.constraint, b.constraint);
        }

    };

    protected static double defaultRankingValue = 100.0;

    protected static int defaultStratum = 0;

    protected static final DecimalFormat df = new DecimalFormat("#.##");

    protected static OldRankedConstraint createDefault(Constraint c) {
        return new OldRankedConstraint(c);
    }

    /**
     * @param defaultRankingValue the defaultRankingValue to set
     */
    protected static void setDefaultRankingValue(double newDefaultRankingValue) {
        defaultRankingValue = newDefaultRankingValue;
    }

    /**
     * Public constructor. Ranking value is set to default.
     *
     * @param c Constraint that will be contained by this object.
     */
    protected OldRankedConstraint(Constraint c) {
        constraint = c;
        sortingValues = SortingValues.createInstance(defaultStratum,
                defaultRankingValue);
        lastSamplingId = -1;
    }

    /**
     * This method makes a "half-deep" copy of the RankedConstraint: the
     * Constraint is not copied, the values are.
     *
     * @return A copy of this ranked constraint
     */
    public OldRankedConstraint copy() {
        OldRankedConstraint result = new OldRankedConstraint(constraint);
        result.sortingValues = sortingValues.copy();
        return result;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    /**
     * @return the defaultRankingValue
     */
    public double getDefaultRankingValue() {
        return defaultRankingValue;
    }

    public double getDisharmony() {
        return sortingValues.disharmony;
    }

    /**
     * @return Disharmony, formatted with two decimal places
     */
    public String getFormattedDisharmony() {
        return df.format(sortingValues.disharmony);
    }

    /**
     * @return Ranking value, formatted with two decimal places
     */
    public String getFormattedRanking() {
        return df.format(sortingValues.rankingValue);
    }

    /**
     * Convenience method to prevent so many calls to getConstraint().
     *
     * @param t The Transgressor object that may be responsible for a
     *          violation.
     * @return The number of violations incurred by this transgressor.
     */
    public int getNumViolations(FormMapping t) {
        return constraint.getNumViolations(t);
    }

    public int getNumViolations(Candidate candidate) {
        return constraint.getNumViolations(candidate);
    }

    public double getRankingValue() {
        return sortingValues.rankingValue;
    }

    public SortingValues getSortingValues() {
        return sortingValues;
    }

    public int getStratum() {
        return sortingValues.stratum;
    }

    public void resetRankingValue() {
        sortingValues.rankingValue = defaultRankingValue;

    }

    public void setDisharmony(double disharmony) {
        sortingValues.disharmony = disharmony;
    }

    public void setRankingValue(double rankingValue) {
        sortingValues.rankingValue = rankingValue;
    }

    public void setStratum(int stratum) {
        sortingValues.stratum = stratum;
    }

    public String toString() {
        return constraint.toString();
    }

    /**
     * @return A String of the constraint with the ranking value, separated by a
     * tab
     */
    public String toStringWithRanking() {
        return constraint.toString() + "\t" + getFormattedRanking();
    }

    /**
     * @return A String of the constraint with the ranking value and disharmony,
     * separated by a tab
     */
    public String toStringWithRankings() {
        return constraint.toString() + "\t" + getFormattedRanking() + "\t"
                + getFormattedDisharmony();
    }

    /**
     * Adds a value delta (which can be positive or negative) to this
     * RankedConstraint's ranking value.
     *
     * @param delta Value to add
     */
    public void updateRankingValue(double delta) {
        sortingValues.rankingValue += delta;
    }

    /**
     * @param noise
     * @param samplingInt
     * @param lazy
     */
    public void conflateWithNoise(double noise, long samplingInt, boolean lazy) {
        if (lazy && lastSamplingId >= samplingInt) {
            // do nothing :-)

        } else {
            sortingValues.disharmony = sortingValues.rankingValue + noise;
        }

        this.lastSamplingId = samplingInt;
    }

    public long getLastSamplingId() {
        return lastSamplingId;
    }

    public void setLastSamplingId(long lastSamplingId) {
        this.lastSamplingId = lastSamplingId;
    }

    public int getRankingIndex() {
        System.err.println("This does not work yet!");
        return Integer.MIN_VALUE;
    }
}
