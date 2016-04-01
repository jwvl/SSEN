/**
 *
 */
package phonetics;

/**
 * @author jwvl
 * @date 04/10/2015
 */
public class Measure {
    private final String scaleName;
    private final String unitName;

    /**
     * @param scaleName
     * @param unitName
     */
    public Measure(String scaleName, String unitName) {
        this.scaleName = scaleName;
        this.unitName = unitName;
    }

    public String getScaleName() {
        return scaleName;
    }

    public String getUnitName() {
        return unitName;
    }

    public DiscretizedScale toScale(int numSteps, double minValue, double stepSize) {
        return new DiscretizedScale(this, minValue, stepSize, numSteps);
    }

    public DiscretizedScale toScale(double minValue, double maxValue, double stepSize) {
        int numSteps = (int) (1 + Math.floor((maxValue - minValue) / stepSize));
        return this.toScale(minValue, stepSize, numSteps);
    }

    /**
     * @return
     */
    public String toString() {
        return String.format("%s (%s)", scaleName, unitName);
    }

}
