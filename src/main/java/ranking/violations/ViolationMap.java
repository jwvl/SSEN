/**
 *
 */
package ranking.violations;

import gen.mapping.FormMapping;
import ranking.OldHierarchy;
import util.collections.ListMap;

import java.util.List;

/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public abstract class ViolationMap<V extends Violation> extends ListMap<FormMapping, V> {


    protected ViolationMap(List<FormMapping> tList, OldHierarchy h) {
        fill(tList, h);
        //this.printAll();
    }

    /**
     * @param h
     * @return
     */
    public abstract void fill(List<FormMapping> tList, OldHierarchy h);

    public void sortEntries() {
        for (FormMapping t : this) {
            // TODO fix this? Does nothing for now!
            // Collections.sort(get(t));
        }
    }


}
