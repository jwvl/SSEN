package simulate.analysis.statistics;

import candidates.Candidate;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import constraints.hierarchy.reimpl.Hierarchy;
import forms.Form;
import forms.FormPair;
import grammar.Grammar;
import graph.Direction;
import learn.PairDistribution;
import learn.ViolatedCandidate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by janwillem on 23/09/16.
 */
public class HierarchyComparer {
    private final double[][] distances;
    private final Table<UUID, Form, Candidate> table;
    private final List<UUID> uuids;

    public HierarchyComparer(double[][] distances, Table<UUID, Form, Candidate> table, List<UUID> uuids) {
        this.distances = distances;
        this.table = table;
        this.uuids = uuids;
    }

    public static HierarchyComparer build(Map<UUID, Hierarchy> hierarchies, Grammar grammar, PairDistribution pairDistribution, Direction direction) {
        List<UUID> uuids = Lists.newArrayList(hierarchies.keySet());
        Table<UUID, Form, Candidate> table = makeTable(grammar, hierarchies,pairDistribution,direction);
        double[][] distances = calculateDistances(uuids,table);
        return new HierarchyComparer(distances,table,uuids);

    }

    private static Table<UUID,Form,Candidate> makeTable(Grammar grammar, Map<UUID,Hierarchy> hierarchies, PairDistribution pairDistribution, Direction direction) {
        Table<UUID,Form,Candidate> result = HashBasedTable.create();
        for (UUID uuid: hierarchies.keySet()) {
            Hierarchy hierarchy = hierarchies.get(uuid);
            grammar.setHierarchy(hierarchy);
            Map<Form,Candidate> winningCandidates = getWinningCandidates(grammar,pairDistribution,direction);
            for (Form form: winningCandidates.keySet()) {
                result.put(uuid,form,winningCandidates.get(form));
            }
        }
        return result;
    }

    private static Map<Form, Candidate> getWinningCandidates(Grammar grammar, PairDistribution pairDistribution, Direction direction) {
        Map<Form,Candidate> result = new HashMap<Form,Candidate>();
        for (FormPair formPair: pairDistribution.getKeys()) {
            FormPair unlabeled = formPair.getUnlabeled(direction);
            Form input = formPair.left();
            ViolatedCandidate violatedCandidate = grammar.getWinner(unlabeled,true,0.0);
            result.put(input,violatedCandidate.getCandidate());
        }
        return result;
    }

    private static double[][] calculateDistances(List<UUID> uuids, Table<UUID, Form, Candidate> table) {
        double[][] result = new double[uuids.size()][uuids.size()];
        for (int i=0; i < uuids.size(); i++) {
            UUID a = uuids.get(i);
            for (int j = i; j < uuids.size(); j++) {
                UUID b = uuids.get(j);
                double distance;;
                if (i == j) {
                    distance = 0;
                } else {
                    distance = getTotalDistance(a,b, table);
                }
                result[i][j] = distance;
                result[j][i] = distance;
            }
        }
        return result;
    }

    private static double getTotalDistance(UUID a, UUID b, Table<UUID, Form, Candidate> table) {
        double summedDistance = 0.0;
        if (a.equals(b)) {
            return 0.0;
        }
        Map<Form,Candidate> aCandidates = table.row(a);
        Map<Form,Candidate> bCandidates = table.row(b);
        for (Form aInput: aCandidates.keySet()) {
            Candidate aCandidate = aCandidates.get(aInput);
            Candidate bCandidate = bCandidates.get(aInput);
            summedDistance += CandidateDistance.getDistance(aCandidate,bCandidate);
        }
        return summedDistance;
    }

    public void writeToFile(String path) {
        File outputFile = new File(path);
        try {
            if (!outputFile.exists()) {

                outputFile.createNewFile();

            }

            FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            // First row
            bw.append("Distances");
            for (int i = 0; i < uuids.size(); i++) {
                bw.append("\t").append(uuids.get(i).toString());
            }
            for (int i = 0; i < uuids.size(); i++) {
                bw.newLine();
                bw.append(uuids.get(i).toString());
                for (int j=0; j < uuids.size(); j++) {
                    bw.append("\t").append(String.valueOf(distances[i][j]));
                }
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("Couldn't write to file!");
        }
    }
}
