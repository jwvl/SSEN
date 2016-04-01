/**
 *
 */
package learn.learner;

import grammar.Grammar;
import learn.update.UpdateAlgorithm;

/**
 * @author jwvl
 * @date 22/01/2016
 */
public abstract class Learner {
    private final String name;
    private Grammar grammar;
    private UpdateAlgorithm updateAlgorithm;

    public Learner(String name) {
        this.name = name;
    }

    /**
     * @return the Learner's name
     */
    public String getName() {
        return name;
    }

}
