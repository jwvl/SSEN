package io.candidates;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import forms.Form;
import forms.FormPair;
import grammar.subgraph.CandidateGraph;
import grammar.subgraph.CandidateSpaces;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 04/03/2017.
 */
public class CandidateSpacesToTables {

    public static void writeToFile(CandidateSpaces candidateSpaces, String path) {
        List<String> nodeStrings = Lists.newArrayList();
        List<String> edgeStrings = Lists.newArrayList();
        int nodeCounter = 0;
        Map<Form,Integer> formsAndIndices = Maps.newHashMap();
        Collection<FormPair> formPairs = candidateSpaces.getPairs();
        nodeStrings.add("FormPair\tForm\tLabel\tLevel");
        edgeStrings.add("FormPair\tIn-id\tOut-id");
        for (FormPair formPair: formPairs) {
            CandidateGraph candidateGraph = candidateSpaces.getGraph(formPair);
            CandidateGraphWriter writer = new CandidateGraphWriter(candidateGraph);
            nodeStrings.addAll(writer.writeNodes(formPair.toString()));
            edgeStrings.addAll(writer.writeEdges(formPair.toString()));
        }
        writeLines(nodeStrings, (path+"-nodes.txt"));
        writeLines(edgeStrings, (path+"-edges.txt"));

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
                if (i < lines.size() - 1) {
                    bw.newLine();
                }

            }
            bw.close();
        } catch(IOException e) {
            System.err.println("Could not write to " +fileName);
        }
    }
}
