package graph.explorer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import forms.Form;
import grammar.dynamic.DynamicNetworkGrammar;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 29/03/16.
 */
public class VisitationGraph {
    private final ListMultimap<Form, Form> nodes;
    private final ListMultimap<Form, Form> greenNodes;
    private final Map<Form, VisitState> visited;
    private final DynamicNetworkGrammar grammar;

    public VisitationGraph(DynamicNetworkGrammar grammar) {
        this.grammar = grammar;
        nodes = ArrayListMultimap.create();
        greenNodes = ArrayListMultimap.create();
        visited = new HashMap<>();
    }


    public boolean wasVisited(Form form) {
        VisitState visitState = visited.get(form);
        return (visitState != VisitState.UNVISITED);
    }

    public List<Form> getSuccessors(Form parent) {
        Map<Form, Collection<Form>> asMap = nodes.asMap();
        List<Form> result = (List<Form>) asMap.get(parent);
        if (result == null || result.isEmpty()) {
            result = expand(parent);
        }
        return result;
    }

    private List<Form> expand(Form parent) {
        List<Form> result = grammar.getSuccessors(parent);
        nodes.putAll(parent, result);
        for (Form f : result) {
            visited.put(f, VisitState.UNVISITED);
        }
        return result;
    }

    public void markRed(Form backtrackingFrom) {
        visited.put(backtrackingFrom, VisitState.VISITED_RED);
    }

    public void markGreen(Form backtrackingFrom, Form backtrackingTo) {
        visited.put(backtrackingFrom, VisitState.VISITED_GREEN);
        greenNodes.put(backtrackingTo, backtrackingFrom);
    }

    public VisitState getState(Form f) {
        return visited.get(f);
    }

    public ListMultimap<Form, Form> getGreenGraph() {
        return greenNodes;
    }
}
