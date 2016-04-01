/**
 *
 */
package learn.stats;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class DataPoint {
    public final double x;
    public final double y;

    /**
     * @param x
     * @param y
     */
    public DataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param x
     * @param y
     */
    public DataPoint(int x, double y) {
        this.x = x;
        this.y = y;
    }

    public static DataPoint of(double x, double y) {
        return new DataPoint(x, y);
    }

}
