/**
 *
 */
package util.string.ngraph;

import util.collections.FrequencyMap;

import java.util.Collection;

/**
 * @author jwvl
 * @date Jun 6, 2015
 */
public class NGraphMap extends FrequencyMap<NGraph> {
    int n;


    private NGraphMap(int n) {
        super();
        this.n = n;
    }

    public static NGraphMap createInstance(int n) {
        return new NGraphMap(n);
    }

    public void addAll(CharSequence cs) {
        Collection<NGraph> all = NGraph.getAllNgraphs(n, cs);
        for (NGraph ng : all) {
            this.addOne(ng);
        }
    }


    public int countUnknowns(CharSequence toTest) {
        int result = 0;
        for (NGraph ng : NGraph.getAllNgraphs(n, toTest)) {
            if (getCount(ng) < 1) {
                result += 1;
            }
        }
        return result;
    }

    public boolean isLegal(CharSequence toCheck, int maxLongestUnknownSequence) {
        return (longestUnknownSequence(toCheck) <= maxLongestUnknownSequence);
    }

    public int longestUnknownSequence(CharSequence toTest) {
        int currLongest = 0, counter = 0;
        for (NGraph ng : NGraph.getAllNgraphs(n, toTest)) {
            if (getCount(ng) < 1) {
                counter++;
                if (counter > currLongest) {
                    currLongest = counter;
                }
            } else {
                counter = 0;
            }
        }
        return currLongest;
    }


}
