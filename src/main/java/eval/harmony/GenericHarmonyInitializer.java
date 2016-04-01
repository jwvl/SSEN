/**
 *
 */
package eval.harmony;

import com.google.common.base.Function;
import ranking.OldRankedConstraint;

import java.util.Collection;

/**
 * @author jwvl
 * @date 02/11/2014
 */
public class GenericHarmonyInitializer<H extends Harmony<H>> implements
        Function<Collection<OldRankedConstraint>, H> {

    Class<H> myClass;
    H infinite;
    H dummy;
    H NULL;
    int size;

    public GenericHarmonyInitializer(Class<H> h, int vectorSize) {
        myClass = h;
        this.size = vectorSize;

        try {
            dummy = myClass.newInstance();
            infinite = myClass.newInstance().getInfinite();
            NULL = dummy.getNull();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.google.common.base.Function#apply(java.lang.Object)
     */
    @Override
    public H apply(Collection<OldRankedConstraint> rcs) {
        if (rcs.size() == 0)
            return NULL;
        H result = dummy.initialize(size, rcs);
        return result;

    }

    public H getInfinite() {
        return infinite;
    }

}
