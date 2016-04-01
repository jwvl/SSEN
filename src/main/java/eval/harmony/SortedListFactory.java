/**
 *
 */
package eval.harmony;

import eval.harmony.autosort.SortedValuesArray;
import eval.harmony.autosort.SortedValuesList;
import eval.harmony.autosort.SortedValuesMultiSet;
import ranking.SortingValues;
import util.collections.AutoSortedList;

import java.util.Collections;
import java.util.List;

/**
 * @author jwvl
 * @date Dec 4, 2014
 */
public class SortedListFactory {

    public static SortedValuesArray createArray(List<SortingValues> input) {
        return new SortedValuesArray(input);
    }

    /**
     * @return
     */
    public static AutoSortedList<SortingValues> createInfinite() {
        List<SortingValues> singleInf = Collections.singletonList(SortingValues.INFINITY);
        return new SortedValuesArray(singleInf);
    }

    public static SortedValuesList createList(List<SortingValues> input) {
        return new SortedValuesList(input);
    }

    public static SortedValuesMultiSet createMultiset(List<SortingValues> input) {
        return SortedValuesMultiSet.fromValues(input);
    }

}
