/**
 *
 */
package ranking.violations;

import com.google.common.base.Strings;
import ranking.constraints.Constraint;

/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public class NumericViolation implements Violation {
    private final Constraint c;
    private final int value;

    /**
     * @return
     */
    public static NumericViolation createInstance(Constraint c, int n) {
        return new NumericViolation(c, n);
    }

    /**
     * @param c
     * @param value
     */
    private NumericViolation(Constraint c, int value) {
        this.c = c;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Violation o) {
        NumericViolation iv = (NumericViolation) o;

        return Integer.valueOf(value).compareTo(Integer.valueOf(iv.value));
    }

    public Constraint getConstraint() {
        return c;
    }

    public int getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see con.violations.Violation#asArrayString()
     */
    @Override
    public String toString() {
        return Strings.repeat("*", value);
    }


}
