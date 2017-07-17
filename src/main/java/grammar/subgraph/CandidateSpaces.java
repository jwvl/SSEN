package grammar.subgraph;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import forms.Form;
import forms.FormPair;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import grammar.dynamic.DynamicNetworkGrammar;
import graph.explorer.CorrectCandidateFinder;
import io.candidates.CandidateGraphWriter;
import io.candidates.CandidateSpacesToTables;
import learn.data.PairDistribution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by janwillem on 30/03/16.
 */
public class CandidateSpaces {
    private static int stopAt = Integer.MAX_VALUE;
    private final Map<FormPair, CandidateGraph> spaces;

    public CandidateSpaces(Map<FormPair, CandidateGraph> spaces) {
        this.spaces = spaces;
    }

    public CandidateSpaces() {
        this(new HashMap<>());
    }

    public void add(CandidateGraph space) {
        spaces.put(space.getFormPair(), space);
        int id = spaces.size();
        if (id % 100 == 0) {
            CandidateGraphWriter writer = new CandidateGraphWriter(space);
            CandidateSpacesToTables.writeToFile(this,"graph-"+id);
        }
    }

    public List<FormMapping> filter(FormPair formPair, Collection<FormMapping> toFilter) {
        return spaces.get(formPair).filter(toFilter);
    }

    public SubCandidateSet filterSet(FormPair formPair, SubCandidateSet toFilter) {
        CandidateGraph candidateGraph = spaces.get(formPair);
        List<FormMapping> result = candidateGraph.filter(toFilter.getContents());
        return SubCandidateSet.of(result);
    }

    public static CandidateSpaces fromDistribution(PairDistribution pairDistribution, DynamicNetworkGrammar grammar) {

        Collection<FormPair> allPairs = pairDistribution.getKeys();
        Map<FormPair, CandidateGraph> map = new HashMap<>();
        int numToCreate = stopAt < 0 ? allPairs.size() : stopAt;
        int count = 0;
        Iterator<FormPair> pairIterator = allPairs.iterator();
        while (pairIterator.hasNext() && count < numToCreate) {
            FormPair fp = pairIterator.next();
            ListMultimap<Form, Form> greenMap = CorrectCandidateFinder.generateCandidateSpace(grammar, fp);
            CandidateGraph candidateGraph = new CandidateGraph(fp, greenMap);
            map.put(fp, candidateGraph);
            count++;
        }
        return new CandidateSpaces(map);
    }

    public Collection<FormPair> getPairs() {
        return spaces.keySet();
    }

    public CandidateGraph getGraph(FormPair formPair) {
        return spaces.get(formPair);
    }

    public void writeAllToFile(String path) {
        List<CandidateGraph> graphsList = Lists.newArrayList(spaces.values());
        for (int i=0; i < graphsList.size(); i++) {
            String outputPath = path+"/graf-"+i+"";
            CandidateGraph graph = graphsList.get(i);
            CandidateGraphWriter writer = new CandidateGraphWriter(graph);
            List<String> nodes = writer.writeNodes(outputPath);
            List<String> edges = writer.writeEdges(outputPath);
        }

    }

    private static void linesToFile(List<String> lines, String outputPath) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {


            fw = new FileWriter(outputPath);
            bw = new BufferedWriter(fw);
            for (int i =0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                if (i < lines.size()-1) {
                    bw.newLine();
                }
            }


        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }
}
