/**
 *
 */
package ranking.violations;

import ranking.constraints.Constraint;


/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public class IndexedViolation implements Violation, Comparable<IndexedViolation> {
    private int index;
    private int value;

    /**
     * @param c2
     * @param numViolations
     * @return
     */
    public static IndexedViolation createInstance(int i, int n) {
        return new IndexedViolation(i, n);
    }

    /**
     * @param c
     * @param value
     */
    private IndexedViolation(int index, int value) {
        this.index = index;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(IndexedViolation iv) {
        return Integer.valueOf(index).compareTo(Integer.valueOf(iv.index));
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see con.violations.Violation#asArrayString()
     */
    @Override
    public String toString() {
        String ind = String.valueOf(index);
        StringBuffer result = new StringBuffer(String.valueOf(ind));
        for (int i = 1; i < value; i++) {
            result.append(", ");
            result.append(ind);
        }
        return result.toString();
    }

    /* (non-Javadoc)
     * @see ranking.violations.Violation#getConstraint()
     */
    @Override
    public Constraint getConstraint() {
        // TODO Auto-generated method stub
        return null;
    }


}
