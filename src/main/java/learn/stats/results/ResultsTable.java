package learn.stats.results;

import io.MyStringTable;

import java.io.IOException;
import java.util.Map;

/**
 * Created by janwillem on 30/07/16.
 */
public class ResultsTable {
    private final MyStringTable table;

    public ResultsTable() {
        table = initializeStringTable();
    }

    private MyStringTable initializeStringTable() {
        String[] columns = {"name","uuid","numData","evaluationNoise","initPlasticity","plasticityDecay","numEpochs","updateAlgorithm","atDatum","errorRate","timeTaken"};
        return MyStringTable.fromColumnArray(columns);
    }

    public void appendDatum(String columnName, String data) {
        int rowNumber = table.getNumRows()-1;
        table.setString(rowNumber, columnName, data);
    }

    public void appendRow() {
        table.appendEmptyRow();
    }

    public void saveToFile(String s) {
        try {
            table.writeToFile(s,"\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendData(Map<String,String> data) {
        for (String s: data.keySet()) {
            appendDatum(s, data.get(s));
        }
    }
}
