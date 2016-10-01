package io.riverplot;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import forms.Form;
import simulate.analysis.CandidateMappingTable;
import util.collections.FrequencyTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by janwillem on 29/09/16.
 */
public class RiverPlotOutput {
    private final Form inputForm;
    private final FrequencyTable<Form,Form> mappings;
    private final Map<Form,Integer> formsWithIDs;
    private final List<List<WeightedForm>> weightedFormsList;
    private final int numLevels = 6;

    public RiverPlotOutput(Form inputForm, CandidateMappingTable candidateMappingTable, double cutoff) {
        this.inputForm = inputForm;
        this.mappings = candidateMappingTable.getFrequencyTableAbove(cutoff);
        weightedFormsList = Lists.newArrayList();
        for (int i=0; i < numLevels; i++) {
            weightedFormsList.add(Lists.newArrayList());
        }
        formsWithIDs = createFormsWithIDs(mappings);
    }

    public void writeToFile(String prefix) {
        try {
            writeNodes(prefix);
            writeEdges(prefix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<Form, Integer> createFormsWithIDs(FrequencyTable<Form,Form> frequencyTable) {
        Map<Form,Integer> result = Maps.newHashMap();
        Set<Form> fromForms = frequencyTable.getColumnSet();
        int idCounter = 1;
        for (Form from: fromForms) {
            if (!result.containsKey(from)) {
                result.put(from, idCounter++);
            }
            Set<Form> toForms = frequencyTable.getRowsForColumn(from);
            int toFrequencies = 0;
            for (Form to: toForms) {
                if (!result.containsKey(to)) {
                    result.put(to, idCounter++);
                }
                toFrequencies += frequencyTable.getCount(from,to);
            }
            WeightedForm weightedForm = new WeightedForm(from, toFrequencies);
            weightedFormsList.get(from.getLevelIndex()).add(weightedForm);
        }
        return result;
    }

    private void writeNodes(String filePrefix) throws IOException {
        File outputFile = new File(filePrefix+"-nodes.txt");
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        for (List<WeightedForm> weightedFormList: weightedFormsList) {
            Collections.sort(weightedFormList);
            //Collections.reverse(weightedFormList);
        }

        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("ID\tx\ty\tlabels");
        for (Form f: formsWithIDs.keySet()) {
            bw.write("\n"+getNodeLine(f));
        }
        bw.close();
    }

    private String getNodeLine(Form f) {
        int id = formsWithIDs.get(f);
        int level = f.getLevelIndex();
        double yPos = getYCoordinate(f);
        String label = RFriendly.makeRFriendly(f.toBracketedString());
        return id+"\t"+level+"\t"+yPos+"\t"+label;
    }

    private void writeEdges(String filePrefix) throws IOException {
        File outputFile = new File(filePrefix+"-edges.txt");
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("N1\tN2\tValue");

        Set<Form> fromForms = mappings.getColumnSet();
        for (Form from: fromForms) {
            Set<Form> toForms = mappings.getRowsForColumn(from);
            for (Form to: toForms) {
                bw.write("\n"+getEdgeLineForForms(from,to));
            }
        }
        bw.close();
    }

    private String getEdgeLineForForms(Form from, Form to) {
        int id1 = formsWithIDs.get(from);
        int id2 = formsWithIDs.get(to);
        int value = mappings.getCount(from,to);
        return id1+"\t"+id2+"\t"+value;
    }

    private double getYCoordinate(Form x) {
        int level = x.getLevelIndex();
        double size = weightedFormsList.get(level).size();
        if (size < 1) {
            size = 1.0;
        }
        return (1+indexOfWeightedForm(x)) / (size+1);
    }

    private int indexOfWeightedForm(Form x) {
        List<WeightedForm> weightedForms = weightedFormsList.get(x.getLevelIndex());
        for (int i=0; i < weightedForms.size(); i++) {
            WeightedForm weightedForm = weightedForms.get(i);
            if (weightedForm.getForm().equals(x)) {
                return i;
            }
        }
        return 0;
    }

    public static void writeNodeAndEdgeFiles(String prefix, Collection<RiverPlotOutput> riverPlotOutputs) {
        try {
            RiverPlotOutput.writeNodeFile(prefix+"-nodes.txt", riverPlotOutputs);
            RiverPlotOutput.writeEdgeFile(prefix+"-edges.txt", riverPlotOutputs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNodeFile(String fileName, Collection<RiverPlotOutput> riverPlotOutputs) throws IOException {
        File outputFile = new File(fileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Input\tID\tx\ty\tlabels");
        for (RiverPlotOutput output: riverPlotOutputs) {
            String inputString = RFriendly.makeRFriendly(output.inputForm.toBracketedString());
            for (Form f: output.formsWithIDs.keySet()) {
                bw.write("\n"+inputString+"\t"+output.getNodeLine(f));
            }
        }
        bw.close();
    }

    public static void writeEdgeFile(String fileName, Collection<RiverPlotOutput> riverPlotOutputs) throws IOException {
        File outputFile = new File(fileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Input\tN1\tN2\tValue");
        for (RiverPlotOutput output: riverPlotOutputs) {
            String inputString = RFriendly.makeRFriendly(output.inputForm.toBracketedString());
            Set<Form> fromForms = output.mappings.getColumnSet();
            for (Form from: fromForms) {
                Set<Form> toForms = output.mappings.getRowsForColumn(from);
                for (Form to: toForms) {
                    bw.write("\n"+inputString+"\t"+output.getEdgeLineForForms(from,to));
                }
            }
        }
        bw.close();
    }
}
