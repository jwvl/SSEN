package io.candidates;

import com.google.common.base.Charsets;
import com.google.common.collect.*;
import com.google.common.io.Resources;
import forms.Form;
import forms.FormPair;
import grammar.dynamic.DynamicNetworkGrammar;
import grammar.subgraph.CandidateGraph;
import grammar.subgraph.CandidateSpaces;
import learn.data.PairDistribution;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

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
                maxLevels = Integer.parseInt(lineParts[2]);
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
        Set<String> formsAsStrings = formsPerLevel.get(formPairIndex,level);
        if (formsAsStrings == null) {
            System.err.println("Nothing found for formpair #"+formPairIndex +" at level " +level);
            return false;
        }
        return formsAsStrings.contains(form);
    }


    public boolean hasForm(String formPair, int level, String form) {
        Integer asInteger = formPairToInt.get(formPair);
        if (asInteger != null) {
            return hasForm(asInteger, level, form);
        }
        return false;
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

    public CandidateSpaces toCandidateSpaces(PairDistribution pairDistribution, DynamicNetworkGrammar grammar) {
        Map<FormPair,CandidateGraph> contents = Maps.newHashMap();
        for (FormPair formPair: pairDistribution.getKeys()) {
            CandidateGraph candidateGraph = getCandidateGraph(formPair, grammar);
            contents.put(formPair, candidateGraph);
        }
        return new CandidateSpaces(contents);
    }

    private CandidateGraph getCandidateGraph(FormPair formPair, DynamicNetworkGrammar grammar) {
        ListMultimap<Form, Form> mappings = ArrayListMultimap.create();
        int numLevels = grammar.getLevelSpace().getSize();
        String formPairAsString = formPair.toString();
        Set<Form> visited = Sets.newHashSet();
        Stack<Form> toExpand = new Stack<>();
        toExpand.add(formPair.left());
        while (!toExpand.isEmpty()) {
            Form next = toExpand.pop();
            List<Form> successors = grammar.getSuccessors(next);
            visited.add(next);
            for (Form successor : successors) {
                int level = successor.getLevelIndex();
                if (level < numLevels && !visited.contains(successor) && hasForm(formPairAsString, level, successor.toString())) {
                    mappings.put(next, successor);
                    toExpand.add(successor);
                }
            }
        }
    return new CandidateGraph(formPair,mappings);
    }
}
