package learn.genetic.two;

import constraints.hierarchy.reimpl.Hierarchy;
import learn.stats.ErrorCounter;

import java.util.Map;

/**
 * Created by janwillem on 29/09/16.
 */
public class CandidatePopulation {
    private static int idCounter = 1;
    private final int id;
    private final Map<Hierarchy,ErrorCounter> population;

    public CandidatePopulation(int id, Map<Hierarchy, ErrorCounter> population) {
        this.id = id;
        this.population = population;
    }
}
