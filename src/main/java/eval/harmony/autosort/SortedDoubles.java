/**
 *
 */
package eval.harmony.autosort;

/**
 * @author jwvl
 * @date May 27, 2015
 */
public interface SortedDoubles extends Comparable<SortedDoubles> {
    SortedDoubles getMax(SortedDoubles a, SortedDoubles b);

    SortedDoubles mergeWith(SortedDoubles s);

    StratifiedDouble getLeftmostValue();

    int size();

    boolean isEmpty();
}
