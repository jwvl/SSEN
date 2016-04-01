/**
 *
 */
package forms;

/**
 * @author jwvl
 * @date May 9, 2015
 */
public class GenericFormPair<L extends Form, R extends Form> {
    private final L l;
    private final R r;
    private FormPair myPair;

    public GenericFormPair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public L getLeft() {
        return l;
    }

    public R getRight() {
        return r;
    }

    public FormPair getUnspecifiedPair() {
        if (myPair == null) {
            myPair = FormPair.of(l, r);
        }
        return myPair;
    }

    @Override
    public String toString() {
        return myPair == null ? getUnspecifiedPair().toString() : myPair.toString();
    }

}
