package eval.harmony;

import com.typesafe.config.ConfigFactory;
import eval.harmony.autosort.SimplestDoubleArray;
import constraints.hierarchy.reimpl.Hierarchy;
import constraints.Constraint;
import constraints.helper.ConstraintArrayList;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by janwillem on 29/03/16.
 */
public class CostFactory {
    private final Hierarchy ranking;
    private final CostType costType;
    private final static double stratumMultiplier = ConfigFactory.load().getDouble("system.stratumMultiplier");

    public CostFactory(Hierarchy ranking, CostType costType) {
        this.ranking = ranking;
        this.costType = costType;
    }

    public Cost getCost(Collection<Constraint> constraints) {
        switch (costType) {
            case SIMPLE_DOUBLES_OT:
                return getSimpleDoubles(constraints);
            case SIMPLE_DOUBLES_HG:
                return null;
        }
        return null;
    }

    public Cost getEmpty() {
        switch (costType) {
            case SIMPLE_DOUBLES_OT:
                return new SimplestDoubleArray(new double[0]);
            case SIMPLE_DOUBLES_HG:
                return null;
        }
        return null;
    }

    private Cost getSimpleDoubles(Collection<Constraint> constraints) {
        double[] values = new double[constraints.size()];
        int i = 0;
        for (Constraint c : constraints) {
            double rankingValue = ranking.getRanking(c);
            int stratum = c.getStratum();
            double warpedRankingValue = rankingValue + (stratumMultiplier * stratum);
            values[i++] = warpedRankingValue;
        }
        return new SimplestDoubleArray(values);
    }

    public double[] getDoubleArray(ConstraintArrayList constraints) {
        double[] values = new double[constraints.size()];
        int nConstraints = constraints.size();
        int i = 0;
        for (Constraint c : constraints) {
            double rankingValue = ranking.getRanking(c);
            int stratum = c.getStratum();
            double warpedRankingValue = rankingValue + (stratumMultiplier * stratum);
            values[i++] = warpedRankingValue;
        }
        Arrays.sort(values);
        double[] reversedValues = new double[nConstraints];
        for (int j = 0; j < nConstraints; j++) {
            reversedValues[nConstraints - (j + 1)] = values[j];
        }
        return reversedValues;
    }
}
