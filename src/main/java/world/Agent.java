/**
 *
 */
package world;

import grammar.Grammar;

import java.util.List;

/**
 * @author jwvl
 * @date 18/10/2014
 * An agent represents a learner, person etc. in a simulation World, and is
 * the containing object for one or more Grammars.
 */
public class Agent {
    private List<Grammar> grammars;
    private AgentInfo info;

}
