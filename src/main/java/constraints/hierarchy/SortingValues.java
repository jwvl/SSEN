package constraints.hierarchy;

import com.google.common.collect.Ordering;
import util.collections.Orderable;

/**
 * This objects contains the relevant fields by which a RankedConstraint is
 * sorted (ranked).
 *
 * @author Jan-Willem van Leussen, Nov 28, 2014
 */
public class SortingValues implements Orderable<SortingValues> {
    double disharmony;
    double rankingValue;
    int stratum;

    public static final SortingValues INFINITY = new SortingValues(0,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    private static final SortingValues dummy = new SortingValues(0, 0d, 0d);

    private static final Ordering<SortingValues> defaultOrdering = new Ordering<SortingValues>() {

        @Override
        public int compare(SortingValues a, SortingValues b) {
            if (a.stratum != b.stratum)
                return b.stratum - a.stratum;
            if (b.disharmony < a.disharmony) // If A outranks B, its disharmony
                // is greater
                return 1;
            else if (b.disharmony > a.disharmony)
                return -1;
            else {
                if (b.rankingValue < a.rankingValue)
                    return 1;
                else if (b.rankingValue > a.rankingValue)
                    return -1;
            }
            return 0;
        }

    };

    private static final Ordering<SortingValues> rankingOrdering = new Ordering<SortingValues>() {

        @Override
        public int compare(SortingValues a, SortingValues b) {
            if (a.stratum != b.stratum)
                return b.stratum - a.stratum;
            if (b.rankingValue < a.rankingValue)
                return 1;
            else if (b.rankingValue > a.rankingValue)
                return -1;
            else {
                if (b.disharmony < a.disharmony)
                    return 1;
                else if (b.disharmony > a.disharmony)
                    return -1;
            }
            return 0;

        }

    };

    public static SortingValues createInstance(int stratum, double rankingValue) {
        return new SortingValues(stratum, rankingValue, rankingValue);
    }

    public static Ordering<SortingValues> getOrdStatic() {
        return dummy.getOrdering();
    }

    private SortingValues(int stratum, double rankingValue, double disharmony) {
        super();
        this.stratum = stratum;
        this.rankingValue = rankingValue;
        this.disharmony = disharmony;
    }

    public SortingValues copy() {
        return new SortingValues(stratum, rankingValue, disharmony);
    }

    public SortingValues copyTinyIncrement() {
        return new SortingValues(stratum, rankingValue, Math.nextAfter(
                disharmony, Double.MAX_VALUE));
    }

    /**
     * @return the disharmony
     */
    public double getDisharmony() {
        return disharmony;
    }

    /*
     * (non-Javadoc)
     *
     * @see util.collections.Orderable#getOrdering()
     */
    @Override
    public Ordering<SortingValues> getOrdering() {
        return defaultOrdering;
    }

    public Ordering<SortingValues> getRankingOrdering() {
        return rankingOrdering;
    }

    /**
     * @return the rankingValue
     */
    public double getRankingValue() {
        return rankingValue;
    }

    /**
     * @return the stratum
     */
    public int getStratum() {
        return stratum;
    }

    public void resetDisharmony() {
        disharmony = rankingValue;
    }

    @Override
    public String toString() {
        return String.format("(%d) %.2f", getStratum(), getDisharmony());
    }

}
