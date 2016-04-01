/**
 *
 */
package ranking;


import ranking.constraints.Constraint;

/**
 * CON is the Optimality-theoretic set of constraints,
 * here implemented as a HashSet of constraints.
 * In SSEN we may often want to iterate over a subset of CON.
 *
 * @author jwvl
 * @date Nov 17, 2014
 */
public interface Con extends Iterable<Constraint> {
    boolean contains(Constraint constraint);


    int size();
}
