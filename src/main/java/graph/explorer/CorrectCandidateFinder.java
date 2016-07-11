package graph.explorer;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.typesafe.config.ConfigFactory;
import forms.Form;
import forms.FormPair;
import gen.mapping.PairMapping;
import grammar.dynamic.DynamicNetworkGrammar;
import graph.LayeredGraph;
import graph.dynamic.SimpleDynamicGraph;

import java.util.Collection;

/**
 * Created by janwillem on 29/03/16.
 * This class serves to pre-generate the candidate spaces for pairs [SemF,AudF] of learning data.
 * When finished, it will return the "green graph", the subgraph containing all nodes that may connect SemF to AudF.
 */
public class CorrectCandidateFinder {
    private final DynamicNetworkGrammar grammar;
    private final FormPair formsToSeek;
    private ListMultimap<Form, Form> greenGraph;

    public CorrectCandidateFinder(DynamicNetworkGrammar grammar, FormPair formsToSeek) {
        this.grammar = grammar;
        this.formsToSeek = formsToSeek;
    }

    public void run() {
        VisitationGraph graph = new VisitationGraph(grammar);
        Visitor visitor = new Visitor(graph, formsToSeek.right());
        visitor.init(formsToSeek.left());
        greenGraph = graph.getGreenGraph();
        System.out.println(greenGraph.size() + " forms found for " + formsToSeek);
    }

    public static ListMultimap<Form, Form> generateCandidateSpace(DynamicNetworkGrammar grammar, FormPair dataPair) {
        CorrectCandidateFinder finder = new CorrectCandidateFinder(grammar, dataPair);
        finder.run();
        ListMultimap<Form, Form> result = finder.getGreenGraph();
        if (ConfigFactory.load().getBoolean("stats.showCandidateGraphs")) {
            SimpleDynamicGraph.showResult(result);
        }
        return result;
    }

    private LayeredGraph createLayeredGraph(Multimap<Form, Form> greenGraph) {
        LayeredGraph result = LayeredGraph.createInstance(grammar);
        for (Form form : greenGraph.keySet()) {
            Collection<Form> outcomes = greenGraph.get(form);
            for (Form outcome : outcomes) {
                result.addFormMapping(PairMapping.createInstance(form, outcome));
            }
        }
        return result;
    }


    public ListMultimap<Form, Form> getGreenGraph() {
        return greenGraph;
    }
}
