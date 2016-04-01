/**
 *
 */
package learn.genetic;

import learn.stats.ErrorCounter;
import ranking.GrammarHierarchy;

/**
 * @author jwvl
 * @date 30/09/2015
 */
public class ConHypothesis {
    private final GrammarHierarchy con;
    private final int identifier;
    private static int ID_COUNTER;
    private ErrorCounter errorCounter;

    /**
     * @param con
     */
    private ConHypothesis(GrammarHierarchy con) {
        this.con = con;
        this.identifier = ID_COUNTER++;
        errorCounter = new ErrorCounter();
    }


    public GrammarHierarchy getCon() {
        return con;
    }

    public int getIdentifier() {
        return identifier;
    }

    /**
     * @param child
     * @return
     */
    public static ConHypothesis createInstance(GrammarHierarchy child) {
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
