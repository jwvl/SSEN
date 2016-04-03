/**
 *
 */
package forms.primitives.feature;

/**
 * @author jwvl
 * @date Dec 14, 2014
 */
public class BinaryValue extends Value<BinaryValue> {

    private final boolean val;
    public final static BinaryValue MINUS = new BinaryValue(false);
    public final static BinaryValue PLUS = new BinaryValue(true);
    private final static BinaryValue NULL = new BinaryValue(false);

    private BinaryValue(boolean val) {
        this.val = val;
    }


    /* (non-Javadoc)
     * @see representations.feature.Value#toString()
     */
    @Override
    public String toString() {
        if (this == NULL) {
            return "0";
        } else if (val) {
            return "+";
        } else {
            return "-";
        }
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
        if (!(obj instanceof BinaryValue))
            return false;
        BinaryValue other = (BinaryValue) obj;
        if (val != other.val)
            return false;
        return this.getNull() == other.getNull();
    }


    /* (non-Javadoc)
     * @see forms.primitives.feature.Value#isNull()
     */
    @Override
    public boolean isNull() {
        // TODO Auto-generated method stub
        return false;
    }


    /* (non-Javadoc)
     * @see forms.primitives.feature.Value#getNull()
     */
    @Override
    public BinaryValue getNull() {
        // TODO Auto-generated method stub
        return null;
    }

}
