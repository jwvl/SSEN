/**
 *
 */
package forms.primitives.feature;

/**
 * @author jwvl
 * @date Dec 14, 2014 A privative value simply means that a given feature is
 * present. Therefore this is a singleton class (in a way, the opposite of
 * the NULL value).
 */
public class PrivativeValue extends Value<PrivativeValue> {
    protected final boolean val;

    private PrivativeValue INSTANCE = new PrivativeValue(true);
    private PrivativeValue NULL = new PrivativeValue(false);

    public PrivativeValue getInstance() {
        return INSTANCE;
    }

    /**
     * @param val
     */
    private PrivativeValue(boolean value) {
        this.val = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see representations.feature.Value#toString()
     */
    @Override
    public String toString() {
        return "+";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (val ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PrivativeValue))
            return false;
        PrivativeValue other = (PrivativeValue) obj;
        return val == other.val;
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Value#isNull()
     */
    @Override
    public boolean isNull() {
        return (this == NULL);
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Value#getNull()
     */
    @Override
    public PrivativeValue getNull() {
        return NULL;
    }

}
