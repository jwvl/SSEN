/**
 *
 */
package learn.genetic;

import java.util.*;


/**
 * @author jwvl
 * @date 30/09/2015
 */
public class RelativeFitnessMemberPicker implements MemberPicker {
    private static Random rnd = new Random();

    public List<ConHypothesis> pick(CandidatePool candidatePool, int numMembers, boolean fittest) {
        List<ConHypothesis> allMembers = candidatePool.getMembers();
        if (numMembers <= candidatePool.size()) {
            return allMembers;
        }
        List<ConHypothesis> result = new ArrayList<ConHypothesis>(numMembers);
        double summedFitness = 0;
        FitnessFunction fitfunc = candidatePool.getFitnessFunction();
        TreeMap<ConHypothesis, Double> scores = new TreeMap<ConHypothesis, Double>();
        for (ConHypothesis learningCon : allMembers) {
            double fitness = fitfunc.apply(learningCon);
            summedFitness += fitness;
            scores.put(learningCon, fitness);
        }
        for (int i = 0; i < numMembers; i++) {
            boolean found = false;
            double weightedRandomNumber = rnd.nextDouble() * summedFitness;
            double toPick = fittest ? weightedRandomNumber : summedFitness - weightedRandomNumber;
            Iterator<ConHypothesis> iterator = scores.keySet().iterator();
            while (!found) {
                ConHypothesis current = iterator.next();
                double currentScore = scores.get(current);
                if (currentScore > toPick) {
                    result.add(current);
                    scores.remove(current);
                    summedFitness -= currentScore;
                    found = true;
                }
            }
        }

        return result;

    }


}
