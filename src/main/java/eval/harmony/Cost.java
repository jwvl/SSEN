package eval.harmony;

/**
 * Created by janwillem on 29/03/16.
 */
public interface Cost<C extends Cost<C>> extends Comparable<C> {
    C mergeWith(C c);
}
