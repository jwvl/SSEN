/**
 *
 */
package eval.harmony.autosort;

import eval.harmony.Cost;

/**
 * @author jwvl
 * @date 16/03/2016
 */
public class SimplestDoubleArray implements Cost<SimplestDoubleArray> {
    final double[] values;

    public SimplestDoubleArray(double[] values) {
        this.values = values;
    }

    public int compareTo(SimplestDoubleArray o) {

        for (int i = 0; i < values.length && i < o.values.length; i++) {
            int result = Double.compare(values[i], o.values[i]);
            if (result != 0) {
                return result;
            }
        }
        return size() - o.size();
    }


    public SimplestDoubleArray mergeWith(SimplestDoubleArray s) {
        double[] resultArray = new double[values.length + s.values.length];
        int i = 0, j = 0, k = 0;

        while (i < values.length && j < s.values.length) {
            if (values[i] < s.values[j])
                resultArray[k++] = values[i++];

            else
                resultArray[k++] = s.values[j++];
        }

        while (i < values.length)
            resultArray[k++] = values[i++];


        while (j < s.values.length)
            resultArray[k++] = s.values[j++];

        return new SimplestDoubleArray(resultArray);
    }

    public double getLeftmostValue() {
        return values.length > 0 ? values[0] : Double.MIN_VALUE;
    }

    public int size() {
        return values.length;
    }

    public boolean isEmpty() {
        return values.length == 0;
    }

}
