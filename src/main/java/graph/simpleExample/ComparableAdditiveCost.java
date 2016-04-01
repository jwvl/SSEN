/**
 *
 */
package graph.simpleExample;

/**
 * @author jwvl
 * @date Dec 3, 2014
 */
public interface ComparableAdditiveCost extends Comparable<ComparableAdditiveCost> {

    ComparableAdditiveCost addTo(ComparableAdditiveCost cac);

}
