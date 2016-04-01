/**
 *
 */
package io.phoneticdata;

import io.MyStringTable;
import phonetics.FeatureScale;
import util.arrays.ArrayTricks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date 03/12/2015
 */
public class PhoneticDataReader {
    private final MyStringTable stringTable;
    private Map<String, FeatureScale> columnsToScales;

    /**
     * @param stringTable
     */
    private PhoneticDataReader(MyStringTable stringTable) {
        this.stringTable = stringTable;
        this.columnsToScales = new HashMap<String, FeatureScale>();
    }

    public void addScale(String columnName, FeatureScale scale) {
        if (stringTable.hasColumn(columnName)) {
            columnsToScales.put(columnName, scale);
        }
    }

    public void addScale(String columnName, int numSteps) {
        if (!stringTable.hasColumn(columnName)) {
            return; //TODO throw error
        }
        double[] asValues = parseContentsAsDoubleArray(columnName);
        double minValue = ArrayTricks.getMinimum(asValues);
        double maxValue = ArrayTricks.getMaximum(asValues);


    }

    /**
     * @param columnName
     * @return
     */
    private double[] parseContentsAsDoubleArray(String columnName) {
        int colNum = stringTable.findColumn(columnName);
        List<String> allData = stringTable.getColumnContents(colNum);
        double[] result = new double[allData.size()];
        for (int i = 0; i < allData.size(); i++) {
            String nextString = allData.get(i);
            try {
                double next = Double.parseDouble(nextString);
                result[i] = next;
            } catch (NumberFormatException e) {
                return new double[0];
            }
        }
        return result;
    }


}
