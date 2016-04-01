/**
 *
 */
package util.collections;

import java.util.TreeSet;

/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public class SortedDoubleList extends TreeSet<Double> {

    /**
     *
     */
    private static final long serialVersionUID = -7520119655085020830L;

    public void add(double d, int numTimes) {
        add(d);
        for (int i = 1; i < numTimes; i++) {
            add(Math.nextAfter(d, Double.MAX_VALUE));
        }
    }

    public SortedDoubleList join(SortedDoubleList b) {
        SortedDoubleList result = (SortedDoubleList) this.clone();
        result.addAll(b);
        return result;
    }


}
