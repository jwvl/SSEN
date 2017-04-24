package io.candidates;

import com.google.common.base.Charsets;
import com.google.common.collect.*;
import com.google.common.io.Resources;
import grammar.Grammar;
import grammar.subgraph.CandidateSpaces;
import learn.data.PairDistribution;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by janwillem on 21/04/2017.
 */
public class CandidateSpacesNodeList {
    private Map<String,Integer> formPairToInt;
    Table<Integer,Integer,Set<String>> formsPerLevel;

    public CandidateSpacesNodeList(List<String> lines) {
        this.formPairToInt = Maps.newHashMap();
        formsPerLevel = HashBasedTable.create();
        int maxLevels = Integer.parseInt(lines.get(0).split("\t")[2]);
        String line = lines.get(0);
        int lineNumber = 0;
        boolean stillInHeader = true;
        int atFormPair = 0;
        while (stillInHeader) {
            String[] lineParts = line.split("\t");
            try {
                atFormPair = Integer.parseInt(lineParts[1]);
                String formPairName = lineParts[0];
                for (int level=0; level < maxLevels; level++) {
                    formsPerLevel.put(atFormPair,level, Sets.newHashSet());
                }
                formPairToInt.put(formPairName,atFormPair);
                lineNumber++;
                line = lines.get(lineNumber);
            } catch (NumberFormatException e) {
                stillInHeader = false;
            }

        }

        for (int i = lineNumber; i < lines.size(); i++) {
            if (!line.isEmpty()) {
                String[] fields = lines.get(i).split("\t");
                int formPairIndex = Integer.parseInt(fields[0]);
                int formLevel = Integer.parseInt(fields[1]);
                String formString = fields[2];
                formsPerLevel.get(formPairIndex,formLevel).add(formString);
            }
        }
    }

    public boolean hasForm(int formPairIndex, int level, String form) {
        return formsPerLevel.get(formPairIndex,level).contains(form);
    }

    public boolean hasForm(String formPair, int level, String form) {
        return hasForm(formPairToInt.get(formPair),level,form);
    }

    public static CandidateSpacesNodeList readFromFile(String path) {
        List<String> result = Lists.newArrayList();
        URL url = Resources.getResource(path);
        try {
            result = Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CandidateSpacesNodeList(result);
    }

    public CandidateSpaces toCandidateSpaces(PairDistribution pairDistribution, Grammar grammar) {

    }
}
