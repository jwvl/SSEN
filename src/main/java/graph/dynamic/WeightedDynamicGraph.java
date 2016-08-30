package graph.dynamic;

import forms.Form;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import simulate.analysis.CandidateMappingTable;

import java.util.Set;

/**
 * Created by janwillem on 20/08/16.
 */
public class WeightedDynamicGraph {
    private final CandidateMappingTable table;
    private final Graph graph;

    public WeightedDynamicGraph(CandidateMappingTable table) {
        this.table = table;
        this.graph = constructGraph("graph");
    }

    private Graph constructGraph(String name) {
        Graph result = new SingleGraph(name);
        Set<Form> outgoing = table.getFrequencyTable().getColumnSet();
        for (Form out: outgoing) {
            Set<Form> incoming = table.getFrequencyTable().getRowsForColumn(out);
            // TODO afmaken?
        }
        return null;

    }
}
