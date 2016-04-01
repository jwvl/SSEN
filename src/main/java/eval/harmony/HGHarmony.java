/**
 *
 */
package eval.harmony;

import com.google.common.collect.Ordering;
import ranking.OldRankedConstraint;

/**
 * Implementation of Harmony as a summed set of weights (see Smolensky &
 * Legendre 1991)
 *
 * @author jwvl
 * @date Dec 3, 2014
 */
public class HGHarmony extends Harmony<HGHarmony> {

    private final static HGHarmony INFINITE = createInfinite();
    private final static HGHarmony NULL = createNull();

    int minStr = 0;
    int maxStr = 0;
    double[] summedWeightsPerStratum;
    ViolationVector violationVector;
    StratifiedWeight[] weights;

    private static Ordering<HGHarmony> hgOrdering = new Ordering<HGHarmony>() {

        @Override
        public int compare(HGHarmony arg0, HGHarmony arg1) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == NULL)
                return -1;
            else if (arg1 == NULL)
                return 1;

            if (arg0 == INFINITE)
                return 1;
            else if (arg1 == INFINITE)
                return -1;

            for (int i = Math.min(arg0.minStr, arg1.minStr); i <= Math.max(
                    arg0.maxStr, arg1.maxStr); i++) {
                double w1 = arg0.summedWeightAt(i);
                double w2 = arg1.summedWeightAt(i);
                if (w1 != w2)
                    return Double.compare(w1, w2);
            }
            return 0;
        }

    };

    protected HGHarmony() {
    }

    private static HGHarmony createNull() {
        HGHarmony result = new HGHarmony(0);
        result.weights = new StratifiedWeight[0];
        result.summedWeightsPerStratum[0] = 0d;
        return result;
    }

    /**
     * @return
     */
    private static HGHarmony createInfinite() {
        HGHarmony result = new HGHarmony(0);
        result.weights = new StratifiedWeight[0];
        result.summedWeightsPerStratum[0] = Double.POSITIVE_INFINITY;
        return result;
    }

    private HGHarmony(int size) {
        weights = new StratifiedWeight[size];
        violationVector = ViolationVector.createIntVector(size);
        summedWeightsPerStratum = new double[maxStr];

    }

    /*
     * (non-Javadoc)
     *
     * @see eval.Harmony#addViolation(con.RankedConstraint, int, int)
     */
    @Override
    public void addViolation(OldRankedConstraint rc, int numViolations) {
        int stratum = rc.getStratum();
        if (stratum < minStr)
            minStr = stratum;
        if (stratum > maxStr)
            maxStr = stratum;
        double addedWeight = rc.getDisharmony();
        increaseVectorValues(stratum, rc.getRankingIndex(), numViolations);
        increaseStratifiedWeight(stratum, rc.getRankingIndex(),
                rc.getDisharmony());
        increaseSummedWeight(stratum, addedWeight);

    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#getWorst()
     */
    @Override
    public HGHarmony getInfinite() {
        return INFINITE;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#getOrdering()
     */
    @Override
    public Ordering<HGHarmony> getOrdering() {
        return hgOrdering;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#setValues(int, int)
     */
    public void increaseVectorValues(int stratum, int constraintIndex,
                                     int numViolations) {
        for (int i = 0; i < numViolations; i++) {
            violationVector.incrementAt(constraintIndex);
        }
    }

    private void increaseSummedWeight(int stratum, double amount) {
        summedWeightsPerStratum[stratum] += amount;
    }

    private void mergeSummedWeights(HGHarmony other) {
        for (int i = minStr; i <= maxStr; i++) {
            double weightToPut = other.summedWeightAt(i);
            increaseSummedWeight(i, weightToPut);
        }
    }

    private void increaseStratifiedWeight(int stratum, int index, double weight) {
        if (weights[index] == null)
            weights[index] = new StratifiedWeight(weight, stratum);
        else
            weights[index] = weights[index].add(weight);
    }

    private void mergeStratifiedWeights(HGHarmony o) {
        int startAt = violationVector.leftmostNonZero;
        for (int i = startAt; i < weights.length; i++) {
            StratifiedWeight osw = o.weights[i];
            if (osw != null) {
                if (weights[i] == null)
                    weights[i] = new StratifiedWeight(o.weights[i]);
                else {
                    weights[i] = weights[i].add(osw.weight);
                }
            }

        }

    }

    private double summedWeightAt(int stratum) {
        return summedWeightsPerStratum[stratum];
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#copy()
     */
    @Override
    public HGHarmony copy() {
        HGHarmony result = new HGHarmony(violationVector.size());
        result.mergeHarmonies(this);
        return result;
    }

    @Override
    public void mergeHarmonies(HGHarmony o) {
        this.violationVector.mergeWith(o.violationVector);
        mergeSummedWeights(o);
        mergeStratifiedWeights(o);

    }

    @Override
    public HGHarmony getMin(HGHarmony other) {
        if (this == NULL) {
            if (other == NULL)
                return getNull();
            return other;
        } else if (other == NULL)
            return this;
        return hgOrdering.min(this, other);
    }

    public HGHarmony initialize(int size, Iterable<OldRankedConstraint> violators) {

        HGHarmony result = new HGHarmony(size);
        for (OldRankedConstraint rc : violators) {
            result.addViolation(rc, 1);
        }
        return result;

    }

    @Override
    public HGHarmony getNull() {
        return NULL;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#toString()
     */
    @Override
    public String toString() {
        if (this == NULL)
            return ("[0]");
        else if (this == INFINITE)
            return ("∞");

        StringBuffer result = new StringBuffer("[");
        int nElements = violationVector.size();
        for (int i = 0; i < nElements; i++) {
            result.append(weightStringAt(i));
            if (i + 1 < nElements)
                result.append(", ");
        }
        result.append("]");
        return result.toString();
    }

    private String weightStringAt(int i) {
        return weights[i] == null ? "0" : weights[i].getString();
    }

    private class StratifiedWeight {
        private final double weight;
        private final int stratum;

        private StratifiedWeight(double weight) {
            this.weight = weight;
            this.stratum = 0;
        }

        private StratifiedWeight(double weight, int stratum) {
            this.weight = weight;
            this.stratum = stratum;
        }

        private StratifiedWeight(StratifiedWeight o) {
            this.weight = o.weight;
            this.stratum = o.stratum;
        }

        private StratifiedWeight add(StratifiedWeight o) {
            double totalWeight = this.weight + o.weight;
            return new StratifiedWeight(totalWeight, stratum);
        }

        private StratifiedWeight add(double addedWeight) {
            double totalWeight = this.weight + addedWeight;
            return new StratifiedWeight(totalWeight, stratum);
        }

        private String getString() {
            return String.format("%.2f (%d)", weight, stratum);
        }

    }

}
