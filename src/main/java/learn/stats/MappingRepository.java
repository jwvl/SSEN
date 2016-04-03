package learn.stats;

import com.google.common.collect.Multiset;
import forms.Form;
import gen.mapping.FormMapping;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Created by janwillem on 01/04/16.
 */
public class MappingRepository {
    private final Multiset<FormMapping> mappings;

    public MappingRepository(Multiset<FormMapping> mappings) {
        this.mappings = mappings;
    }

    public void buildGraph() {
        Graph graph = new SingleGraph("Simple graph");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        for (FormMapping mapping: mappings.elementSet()) {
            int count = mappings.count(mapping);
            Form left = mapping.left();
            Form right = mapping.right();
            Edge edge = graph.addEdge(mapping.toString(),left.toString(),right.toString());
            edge.setAttribute("ui.stroke-width");
            edge.getAttributeKeySet();
        }
    }
}
