/**
 *
 */
package learn.genetic;

import com.google.common.collect.Ordering;
import ranking.Hierarchy;
import util.collections.Distribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author jwvl
 * @date 30/09/2015
 * A first attempt at doing something like 'genetic OT'.
 */
public class CandidatePool {
    private final int maxPopulationSize;
    private List<ConHypothesis> population;
    private FitnessFunction fitnessFunction;
    private Ordering<ConHypothesis> populationOrdering;
    private double mutationNoise = 0.5;
    private final Random random = new Random();

    /**
     * @param maxPopulationSize
     * @param population
     */
    private CandidatePool(int maxPopulationSize, List<ConHypothesis> population) {
        this.maxPopulationSize = maxPopulationSize;
        this.population = population;
        changeFitnessFunction(0, 0.0);
    }

    public static CandidatePool createNew(int maxPopulationSize) {
        List<ConHypothesis> newList = new ArrayList<ConHypothesis>(maxPopulationSize);
        return new CandidatePool(maxPopulationSize, newList);
    }

    public static CandidatePool createFromList(int maxPopulationSize, List<ConHypothesis> population) {
        List<ConHypothesis> newList = new ArrayList<ConHypothesis>(maxPopulationSize);
        newList.addAll(population);
        return new CandidatePool(maxPopulationSize, newList);
    }

    public void changeFitnessFunction(int initialBias, double initialSuccess) {
        this.fitnessFunction = new FitnessFunction(initialBias, initialSuccess);
        populationOrdering = Ordering.natural().onResultOf(getFitnessFunction());
    }

    public void mutate(GeneticOperator operator, MemberPicker pairPicker) {
        if (size() >= maxPopulationSize) {
            List<ConHypothesis> toRemove = pairPicker.pick(this, 1, false);
            population.removeAll(toRemove);
        }
        List<ConHypothesis> memberToMutate = pairPicker.pick(this, 1, true);
        ConHypothesis newMember = operator.mutate(memberToMutate.get(0), mutationNoise);
        population.add(newMember);
    }

    public void crossMembers(GeneticOperator operator, MemberPicker pairPicker) {
        if (size() >= maxPopulationSize) {
            List<ConHypothesis> toRemove = pairPicker.pick(this, 1, false);
            population.removeAll(toRemove);
        }

        List<ConHypothesis> membersToCross = pairPicker.pick(this, 2, true);
        ConHypothesis newMember = operator.crossOver(membersToCross.get(0), membersToCross.get(1), 0.5, mutationNoise);
        population.add(newMember);

    }

    public int size() {
        return population.size();
    }

    private void sortMembers() {
        Collections.sort(population, populationOrdering);
    }

    /**
     * @return
     */
    public List<ConHypothesis> getMembers() {
        sortMembers();
        return population;
    }

    /**
     * @return the fitnessFunction
     */
    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }


    public ConHypothesis pickRandom() {
        int randomInt = random.nextInt(population.size());
        return population.get(randomInt);
    }

    public void toNextGeneration(Hierarchy ranking) {
        Distribution<ConHypothesis> points = new Distribution<ConHypothesis>("fitness");
        for (ConHypothesis con : population) {
            int percentagePoints = (int) (100 * fitnessFunction.apply(con));
            points.add(con, percentagePoints);
        }
    }
}
