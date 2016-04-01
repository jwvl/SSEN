/**
 *
 */
package eval.harmony.autosort;

/**
 * @author jwvl
 * @date 16/03/2016
 */
public class SimplestDoubleArrayBuilder {
    private double[] values;
    private int backingArraySize;
    private int index;

    public SimplestDoubleArrayBuilder(int initialSize) {
        this.values = new double[initialSize];
        this.backingArraySize = initialSize;
        this.index = 0;
    }

    public SimplestDoubleArray build() {
        double[] shorterCopy = new double[index];
        System.arraycopy(values, 0, shorterCopy, 0, index);
        return new SimplestDoubleArray(shorterCopy);
    }

    public SimplestDoubleArrayBuilder add(double... ds) {
        for (double d : ds) {
            values[index++] = d;
            if (index > backingArraySize) {
                growBackingArray();
            }
        }
        return this;
    }

    /**
     *
     */
    private void growBackingArray() {
        int newSize = backingArraySize * 2;
        double[] copy = new double[newSize];
        System.arraycopy(values, 0, copy, 0, backingArraySize);
        values = copy;
        backingArraySize = newSize;
    }

}
