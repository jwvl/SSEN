package graph;

import com.google.common.collect.Multimap;
import edu.uci.ics.jung.graph.Graph;
import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import grammar.Grammar;
import graph.visual.SimpleGraphVisualizer;
import graph.visual.SimpleGraphWindow;

import java.util.Collection;
import java.util.Map;

/**
 * Created by janwillem on 30/03/16.
 */
public class SimpleLayeredGraph {

    private final LayeredGraph layeredGraph;

    private SimpleLayeredGraph(Grammar grammar) {
        layeredGraph = new LayeredGraph(grammar.getLevelSpace());
    }

    public static SimpleLayeredGraph createInstance(Grammar grammar, Multimap<Form, Form> multimap) {
        return createInstance(grammar, multimap.asMap());
    }

    public static SimpleLayeredGraph createInstance(Grammar grammar, Map<Form, Collection<Form>> formMappings) {
        SimpleLayeredGraph result = new SimpleLayeredGraph(grammar);
        for (Form a : formMappings.keySet()) {
            for (Form b : formMappings.get(a)) {
                FormMapping formMapping = PairMapping.createInstance(a, b);
                result.layeredGraph.addFormMapping(formMapping);
            }
        }
        return result;
    }

    public Graph<Form, FormMapping> getGraphObject() {
        return layeredGraph.getGraph();
    }

    public static void createWindow(Grammar grammar, Multimap<Form, Form> multimap) {
        SimpleLayeredGraph graph = SimpleLayeredGraph.createInstance(grammar, multimap);
        SimpleGraphVisualizer<Form, FormMapping> visualizer = new SimpleGraphVisualizer<Form, FormMapping>(graph.getGraphObject());
        SimpleGraphWindow<Form, FormMapping> window = new SimpleGraphWindow<>(visualizer);
    }
}
