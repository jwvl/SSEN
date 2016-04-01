/**
 *
 */
package eval.harmony.autosort;

import eval.harmony.HarmonyCost;
import ranking.violations.RankedViolation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public class LazySortedViolations implements HarmonyCost<LazySortedViolations> {
    private boolean sorted;
    private List<RankedViolation> list;
    private RankedViolation leftmost;


    public void addViolations(Collection<RankedViolation> violation) {
        list.addAll(violation);
        sorted = false;
    }


    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(LazySortedViolations other) {
        int result = leftmost.compareTo(other.leftmost);
        if (result == 0) {
            result = sortAndCompare(other);
        }
        return result;
    }


    /**
     * @param other
     * @return
     */
    private int sortAndCompare(LazySortedViolations other) {
        sortIfNecessary();
        other.sortIfNecessary();
        int stopAt = Math.min(size(), other.size());
        int index = 1, result = 0;
        while (index < stopAt) {
            RankedViolation thisRv = list.get(index);
            RankedViolation thatRv = other.list.get(index);
            result = thisRv.compareTo(thatRv);
            if (result != 0) {
                return result;
            }
        }
        return size() - other.size();
    }


    /* (non-Javadoc)
     * @see eval.harmony.HarmonyCost#mergeCost(eval.harmony.HarmonyCost)
     */
    @Override
    public void mergeCost(LazySortedViolations other) {
        if (leftmost.compareTo(other.leftmost) < 0)
            this.leftmost = other.leftmost;
        addViolations(other.list);
    }


    private void sortIfNecessary() {
        if (!sorted) {
            Collections.sort(list);
        }
        sorted = true;
    }

    public int size() {
        return list.size();
    }


    public List<RankedViolation> getList() {
        return list;
    }


}
