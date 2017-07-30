package io.pfc.app;

import com.google.common.base.Charsets;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 28/07/2017.
 */
public class RemoveInterviewerItems {
    private static final String inputDataFile = "data/inputs/pfc/PFC_version04.txt";
    private static final String toRemoveFile = "data/inputs/pfc/interviewer_items.txt";
    private static Table<String,Boolean,Integer> totalCounts;

    public static void main(String[] args) {
        totalCounts = HashBasedTable.create();
        List<String> inputLines = getLines(inputDataFile);
        List<String> toRemoveLines = getLines(toRemoveFile);
        for (String line: inputLines) {
            String[] parts = line.split("\t");
            if (parts.length > 3) {
                String tabbedString = parts[0]+"\t"+parts[1];
                int count = Integer.parseInt(parts[2]);
                int liaisonInt = Integer.parseInt(parts[3]);
                boolean liaison = liaisonInt > 0;
                addCount(tabbedString,count,liaison);
            }
        }
        for (String line: toRemoveLines) {
            String[] parts = line.split("\t");
            if (parts.length > 3) {
                String tabbedString = parts[0]+"\t"+parts[1];
                int count = Integer.parseInt(parts[2]);
                int liaisonInt = Integer.parseInt(parts[3]);
                boolean liaison = liaisonInt > 0;
                removeCount(tabbedString,count,liaison);
            }
        }
        printCounts();
    }

    private static void printCounts() {
        for (String tabbedString: totalCounts.rowKeySet()) {
            Map<Boolean,Integer> rowCounts = totalCounts.row(tabbedString);
            for (Boolean b: rowCounts.keySet()) {
                int toPrint = b.booleanValue() ? 1 : 0;
                int count = totalCounts.get(tabbedString,b);
                if (count > 0 && tabbedString.contains(",")) {
                    System.out.println(tabbedString + "\t" + count + "\t" + toPrint);
                }
            }
        }
        for (String tabbedString: totalCounts.rowKeySet()) {
            Map<Boolean,Integer> rowCounts = totalCounts.row(tabbedString);
            for (Boolean b: rowCounts.keySet()) {
                int toPrint = b.booleanValue() ? 1 : 0;
                int count = totalCounts.get(tabbedString,b);
                if (count > 0 && !tabbedString.contains(",")) {
                    System.out.println(tabbedString + "\t" + count + "\t" + toPrint);
                }
            }
        }
    }

    private static void addCount(String tabbedString, int count, boolean liaison) {
        int currentCount = !totalCounts.contains(tabbedString,liaison) ? 0 : totalCounts.get(tabbedString,liaison);
        int newCount = currentCount + count;
        totalCounts.put(tabbedString,liaison,newCount);
    }

    private static void removeCount(String tabbedString, int count, boolean liaison) {
        if (totalCounts.contains(tabbedString,liaison)) {
            int oldCount = totalCounts.get(tabbedString,liaison);
            int newCount = oldCount - count;
            totalCounts.put(tabbedString,liaison,newCount);
        }
    }

    private static List<String> getLines(String resource) {
        List<String> result = Lists.newArrayList();
        URL url = Resources.getResource(resource);
        try {
            result = Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
