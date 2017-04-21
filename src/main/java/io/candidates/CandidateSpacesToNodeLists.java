package io.candidates;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import forms.Form;
import forms.FormPair;
import grammar.Grammar;
import grammar.subgraph.CandidateGraph;
import grammar.subgraph.CandidateSpaces;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by janwillem on 04/03/2017.
 */
public class CandidateSpacesToNodeLists {

    public static void writeToFile(Grammar grammar, CandidateSpaces candidateSpaces, String path) {
        List<String> linesToWrite = Lists.newArrayList();
        List<String> nodeStrings = Lists.newArrayList();
        List<FormPair> formPairs = Lists.newArrayList(candidateSpaces.getPairs());
        for (int i= 0; i < formPairs.size(); i++) {
            FormPair formPair = formPairs.get(i);
            linesToWrite.add(formPair.toString()+"\t"+i+"\t"+grammar.getLevelSpace().getSize());
            fillNodeStrings(nodeStrings, i,candidateSpaces.getGraph(formPair));
        }
        linesToWrite.addAll(nodeStrings);
        writeLines(linesToWrite, path);

    }

    private static void fillNodeStrings(List<String> nodeStrings, int i, CandidateGraph graph) {
        Map<Form,List<Form>> formListMap = graph.getMap();
        Set<Form> allForms = Sets.newHashSet(formListMap.keySet());
        for (Form form:formListMap.keySet()) {
            allForms.addAll(formListMap.get(form));
        }
        for (Form form: allForms) {
            nodeStrings.add(i +"\t"+form.getLevelIndex()+"\t"+form.toString());
        }
    }

    public static void writeLines(List<String> lines, String fileName) {
        File outputFile = new File(fileName);
        try {
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                bw.newLine();

            }
            bw.close();
        } catch(IOException e) {
            System.err.println("Could not write to " +fileName);
        }
    }
}
