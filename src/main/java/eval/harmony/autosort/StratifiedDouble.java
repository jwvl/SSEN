/**
 *
 */
package eval.harmony.autosort;

/**
 * @author jwvl
 * @date May 27, 2015
 */
public class StratifiedDouble implements Comparable<StratifiedDouble> {
    private final int stratum;
    private final double value;
    public static StratifiedDouble LOWEST_RANKING = new StratifiedDouble(
            Integer.MAX_VALUE, Double.MIN_VALUE);
    public static StratifiedDouble HIGHEST_RANKING = new StratifiedDouble(
            Integer.MIN_VALUE, Double.MAX_VALUE);

    /**
     * @param stratum
     * @param value
     */
    private StratifiedDouble(int stratum, double value) {
        this.stratum = stratum;
        this.value = value;
    }

    public static StratifiedDouble of(int stratum, double value) {
        return new StratifiedDouble(stratum, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(StratifiedDouble o) {
        int result = stratum - o.stratum;
        if (result == 0) {
            result = -Double.compare(value, o.value);
        }

        return result;
    }

    public int harmonyCompareTo(StratifiedDouble o) {
        int result = stratum - o.stratum;
        if (result == 0) {
            result = Double.compare(value, o.value);
        }
        return result;
    }

    public int getStratum() {
        return stratum;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return String.format("(%d) %.2f", stratum, value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + stratum;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof StratifiedDouble))
            return false;
        StratifiedDouble other = (StratifiedDouble) obj;
        if (stratum != other.stratum)
            return false;
        return Double.doubleToLongBits(value) == Double
                .doubleToLongBits(other.value);
    }


}
