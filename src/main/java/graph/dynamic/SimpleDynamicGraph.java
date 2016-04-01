package graph.dynamic;

import com.google.common.collect.ListMultimap;
import forms.Form;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Collection;
import java.util.Map;

/**
 * Created by janwillem on 30/03/16.
 */
public class SimpleDynamicGraph {
    private final Map<Form, Collection<Form>> map;
    private final Graph graph;

    public SimpleDynamicGraph(Map<Form, Collection<Form>> map, boolean displayRightAway) {
        this.map = map;
        graph = new SingleGraph("Simple graph");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        if (displayRightAway)
            graph.display();
        addNodes();
    }

    public SimpleDynamicGraph(ListMultimap<Form, Form> map, boolean b) {
        this(map.asMap(), b);
    }

    private void addNodes() {
        for (Form a : map.keySet()) {
            String aString = a.toString();
            Node aNode = graph.addNode(aString);
            aNode.setAttribute("x", a.getLevel().myIndex());
            aNode.setAttribute("ui.label", aString);
            for (Form b : map.get(a)) {
                String bString = b.toString();
                Node bNode = graph.addNode(bString);
                bNode.setAttribute("x", b.getLevel().myIndex());
                bNode.setAttribute("ui.label", bString);
                String edgeString = aString + "->" + bString;
                graph.addEdge(edgeString, aString, bString);
            }
        }
        for (Form a : map.keySet()) {
            String aString = a.toString();
            Node aNode = graph.getNode(aString);
            aNode.setAttribute("x", a.getLevel().myIndex());
        }
    }

    public static void showResult(ListMultimap<Form, Form> map) {
        SimpleDynamicGraph sdGraph = new SimpleDynamicGraph(map, true);
    }
}
