/**
 *
 */
package learn.genetic;

import constraints.hierarchy.reimpl.Hierarchy;
import learn.stats.ErrorCounter;

/**
 * @author jwvl
 * @date 30/09/2015
 */
public class ConHypothesis {
    private final Hierarchy con;
    private final int identifier;
    private static int ID_COUNTER;
    private ErrorCounter errorCounter;

    /**
     * @param con
     */
    private ConHypothesis(Hierarchy con) {
        this.con = con;
        this.identifier = ID_COUNTER++;
        errorCounter = new ErrorCounter();
    }


    public Hierarchy getCon() {
        return con;
    }

    public int getIdentifier() {
        return identifier;
    }

    /**
     * @param child
     * @return
     */
    public static ConHypothesis createInstance(Hierarchy child) {
        return new ConHypothesis(child);
    }


    public int getNumEvaluations() {
        return errorCounter.getTotal();
    }


    public int getSuccessfulEvaluations() {
        return errorCounter.getTotal() - errorCounter.getErrors();
    }


    public void addEvaluation(boolean success) {
        errorCounter.increaseCount(!success);
    }
}
