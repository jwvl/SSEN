/**
 *
 */
package util.arrays;

/**
 * @author jwvl
 * @date 16/03/2016
 */
public class DoubleArraySorter {

    public static int compare(double[] values, double[] walues) {
        for (int i = 0; i < values.length && i < walues.length; i++) {
            int result = Double.compare(values[i], walues[i]);
            if (result != 0) {
                return result;
            }
        }
        return values.length - walues.length;
    }


    public static double[] mergeWithSorted(double[] values, double[] walues) {
        double[] resultArray = new double[values.length + walues.length];
        int i = 0, j = 0, k = 0;

        while (i < values.length && j < walues.length) {
            if (values[i] > walues[j])
                resultArray[k++] = values[i++];

            else
                resultArray[k++] = walues[j++];
        }

        while (i < values.length)
            resultArray[k++] = values[i++];


        while (j < walues.length)
            resultArray[k++] = walues[j++];

        return resultArray;
    }

    public static String printDoubleArray(double[] array) {
        StringBuilder result = new StringBuilder("[ ");
        for (double d : array) {
            result.append(String.format("%.2f ", d));
        }
        return result.append(" ]").toString();
    }


}
