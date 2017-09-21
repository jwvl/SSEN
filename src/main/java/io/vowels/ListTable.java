package io.vowels;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListTable {
    private final Map<String,List<String>> data;
    private final int numRows;
    private final String[] headers;

    public ListTable(Map<String, List<String>> data, String[] headers) {
        this.headers = headers;
        this.data = data;
        this.numRows = data.get(headers[0]).size();
    }

    public static ListTable readFromFile(String path, String separator) {
        List<String> asStringList = Lists.newArrayList();
        URL url = Resources.getResource(path);
        try {
            asStringList = Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] headers = asStringList.get(0).split(separator);
        Map<String,List<String>> data = new HashMap<>();
        for (String header: headers) {
            data.put(header,Lists.newArrayList());
        }
        for (int i=1; i < asStringList.size(); i++) {
            String[] splitLine = asStringList.get(i).split(separator);
            for (int j = 0; j < headers.length; j++) {
                String header = headers[j];
                data.get(header).add(splitLine[j]);
            }
        }
        return new ListTable(data, headers);
    }

    public static ListTable readFromFile(String path) {
        return readFromFile(path, "\t");
    }

    public List<String> getColumn(String header) {
        if (data.containsKey(header)) {
            return data.get(header);
        } else {
            System.err.println("No column " + header + " in data table.");
            return Collections.emptyList();
        }
    }

    public String getString(int row, String header) {
        return getColumn(header).get(row);
    }

    public double getDouble(int row, String header) {
        return Double.parseDouble(getString(row,header));
    }

    public int numRows() {
        return numRows;
    }
}
