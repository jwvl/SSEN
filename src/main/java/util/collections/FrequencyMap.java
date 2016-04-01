/**
 *
 */
package util.collections;

import com.google.common.collect.LinkedHashMultiset;

import java.util.Random;

/**
 * @author jwvl
 * @date Dec 17, 2014
 */
public class FrequencyMap<O extends Object> {
    LinkedHashMultiset<O> contents;
    private Random r;

    /**
     * Creates an empty frequency map.
     *
     * @param contents
     */
    protected FrequencyMap() {
        contents = LinkedHashMultiset.create();
    }

    public void addOne(O o) {
        contents.add(o);
    }

    public void add(O o, int num) {
        contents.add(o, num);
    }

    public void setCount(O o, int num) {
        contents.setCount(o, num);
    }

    public int getCount(O o) {
        return contents.count(o);
    }

    public O drawRandom() {
        int randomInt = r.nextInt(contents.size());
        int cumulativeProbability = 0;
        for (O o : contents) {
            cumulativeProbability += contents.count(o);
            if (randomInt < cumulativeProbability) {
                return o;
            }
        }
        return null;
    }

}
