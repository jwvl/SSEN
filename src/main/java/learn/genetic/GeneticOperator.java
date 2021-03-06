/**
 *
 */
package learn.genetic;

import constraints.hierarchy.reimpl.Hierarchy;
import eval.sample.AbstractSampler;

import java.util.Random;

/**
 * @author jwvl
 * @date 30/09/2015
 */
public class GeneticOperator {
    private final static Random rnd = new Random();
    private final AbstractSampler sampler;
    private final CrossoverMethod crossoverMethod;

    /**
     * @param sampler
     */
    private GeneticOperator(AbstractSampler sampler, CrossoverMethod crossoverMethod) {
        this.sampler = sampler;
        this.crossoverMethod = crossoverMethod;
    }

    public static GeneticOperator createInstance(AbstractSampler sampler, CrossoverMethod crossoverMethod) {
        return new GeneticOperator(sampler, crossoverMethod);
    }

    public ConHypothesis crossOver(ConHypothesis a, ConHypothesis b, double aWeight, double noise) {
        Hierarchy child = crossover(a.getCon(), b.getCon(), aWeight);
        addNoise(child, noise);
        ConHypothesis newCon = ConHypothesis.createInstance(child);
        return newCon;
    }

    private void addNoise(Hierarchy unNoised, double noise) {
        // TODO ever fix?
    }

    public ConHypothesis mutate(ConHypothesis original, double noise) {
        Hierarchy newMap = original.getCon();
        addNoise(newMap, noise);
        return ConHypothesis.createInstance(newMap);
    }

    private Hierarchy crossover(Hierarchy aCon, Hierarchy bCon, double aWeight) {
        double bWeight = 1 - aWeight;
        Hierarchy result = Hierarchy.createNew();
//        Map<Constraint, Double> aMap = aCon.getMap();
//        Map<Constraint, Double> bMap = bCon.getMap();
//        for (Constraint constraint : aMap.keySet()) {
//            double aRanking = aMap.get(constraint);
//            double bRanking;
//            if (bMap.containsKey(constraint)) {
//                bRanking = bMap.get(constraint);
//            } else {
//                bRanking = aRanking;
//            }
//            double newRanking = 0;
//            if (crossoverMethod == CrossoverMethod.AVERAGE) {
//                newRanking = (aWeight * aRanking) + (bWeight * bRanking);
//            } else if (crossoverMethod == CrossoverMethod.PICK_ONE) {
//                double randomUniform = rnd.nextDouble();
//                if (randomUniform <= aWeight) {
//                    newRanking = aRanking;
//                } else {
//                    newRanking = bRanking;
//                }
//            }
//            result.addConstraint(constraint, newRanking);
//        }
//
//        // If the constraint wasn't in A but is in B, we just give it B's value
//        for (Constraint constraint : bMap.keySet()) {
//            if (!result.contains(constraint)) {
//                result.addConstraint(constraint, bMap.get(constraint));
//            }
//        }
        return result;
    }

}
