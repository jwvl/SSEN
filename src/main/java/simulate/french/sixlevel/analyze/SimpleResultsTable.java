package simulate.french.sixlevel.analyze;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by janwillem on 29/06/2017.
 */
public class SimpleResultsTable {
    private final Table<String,String,String> table;
    private final DecimalFormat decimalFormat;


    public SimpleResultsTable() {
        table = HashBasedTable.create();
        decimalFormat = new DecimalFormat("#.###");
    }

    public void addResult(String row, String column, String data) {
        table.put(row,column,data);
    }


    public void addResult(String row, String column, double data) {
        String dataString = decimalFormat.format(data);
        addResult(row,column,dataString);
    }

    public String printRows(boolean includeHeaders, String separator) {
        StringBuilder builder = new StringBuilder();
        List<String> columnKeys = Lists.newArrayList(table.columnKeySet());
        List<String> rowNames = Lists.newArrayList(table.rowKeySet());
        if (includeHeaders) {
            // print headers
            builder.append("Grammar");
            for (String column : columnKeys) {
                builder.append(separator).append(column);
            }
            builder.append("\n");
        }
        // print contents
        for (int i=0; i < rowNames.size(); i++) {
            for (String rowName: rowNames) {
                builder.append(rowName.toString());
                for (String column: columnKeys) {
                    String cell = table.get(rowName, column);
                    builder.append(separator).append(cell);
                }
                if (i < rowNames.size() - 1) {
                    builder.append("\n");
                }
            }
        }
        return builder.toString();
    }

    public String printRows(boolean includeHeaders) {
        return printRows(includeHeaders, "\t");
    }
}
