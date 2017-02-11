package graph.dynamic;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import forms.Form;
import grammar.levels.Level;
import graph.LevelwiseFormMap;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by janwillem on 30/03/16.
 */
public class SimpleDynamicGraph {
    private final Map<Form, Collection<Form>> map;
    private final Graph graph;
    private static int counter = 0;

    public SimpleDynamicGraph(Map<Form, Collection<Form>> map, boolean displayRightAway) {
        this.map = map;
        graph = new SingleGraph("Simple graph");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        if (displayRightAway) {
            Viewer viewer = graph.display();
            viewer.disableAutoLayout();
        }
        addNodes();
        FileSinkImages pic = new FileSinkImages(FileSinkImages.OutputType.PNG, FileSinkImages.Resolutions.SXGA);
        pic.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_ONCE_AT_NEW_IMAGE);
        pic.setStyleSheet(
                "graph { padding: 50px; fill-color: white; }" +
                        "node { fill-color: #3d5689; }" +
                        "edge { fill-color: black; }");
        try {
            pic.writeAll(graph, "outputs/graph-"+(counter++)+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SimpleDynamicGraph(ListMultimap<Form, Form> map, boolean b) {
        this(map.asMap(), b);
    }

    private void addNodes() {
        LevelwiseFormMap levelwiseFormMap = LevelwiseFormMap.createFromMultimap(map);
        ArrayListMultimap<Level,Form> asArrayMultimap = levelwiseFormMap.toArrayMultimap();
        for (Form a : map.keySet()) {
            double[] coords = getNodeCoordinates(a,asArrayMultimap);
            String aString = a.toString();
            Node aNode = graph.addNode(aString);
            aNode.setAttribute("x", coords[0]);
            aNode.setAttribute("ui.label", aString);
            int index = asArrayMultimap.get(a.getLevel()).indexOf(a);
            aNode.setAttribute("y",coords[1]);

            for (Form b : map.get(a)) {
                coords = getNodeCoordinates(b,asArrayMultimap);
                String bString = b.toString();
                Node bNode = graph.addNode(bString);
                bNode.setAttribute("x", coords[0]);
                bNode.setAttribute("ui.label", bString);
                index = asArrayMultimap.get(b.getLevel()).indexOf(b);
                bNode.setAttribute("y",coords[1]);
                String edgeString = aString + "->" + bString;
                Edge edge = graph.addEdge(edgeString, aString, bString);
                edge.setAttribute("stroke-mode","plain");
                edge.addAttribute("ui.style","stroke-width: 5;");
            }
        }
//        for (Form a : map.keySet()) {
//            String aString = a.toString();
//            Node aNode = graph.getNode(aString);
//            aNode.setAttribute("x", a.getLevel().myIndex());
//        }
    }

    public static void showResult(ListMultimap<Form, Form> map) {
        SimpleDynamicGraph sdGraph = new SimpleDynamicGraph(map, true);
    }

    public double[] getNodeCoordinates(Form form, ArrayListMultimap<Level,Form> multimap) {
        int width = multimap.keySet().size();
        int height = multimap.get(form.getLevel()).size();
        int xIndex = form.getLevelIndex();
        int yIndex = multimap.get(form.getLevel()).indexOf(form);
        double x = ((double) (xIndex+1)) / (width-1);
        double y = ((double) (yIndex+1)) / (height+1);
        return new double[]{x,y};
    }
}
