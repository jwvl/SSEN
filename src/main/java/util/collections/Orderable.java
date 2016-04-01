/**
 *
 */
package util.collections;

import com.google.common.collect.Ordering;

/**
 * @author jwvl
 * @date Dec 4, 2014
 */
public interface Orderable<O extends Object> {
    Ordering<O> getOrdering();

}
