/**
 *
 */
package graph;

import candidates.Candidate;
import candidates.FormInput;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import forms.Form;
import forms.FormChain;
import forms.FormPair;
import forms.GraphForm;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import grammar.Grammar;
import grammar.levels.Level;
import grammar.levels.LevelSpace;
import grammar.levels.SinkLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date 31/10/2014
 */
public class LayeredGraph {
    private DirectedSparseGraph<Form, FormMapping> graph;
    private LevelwiseFormMap layeredContents;
    private final LevelSpace levelSpace;
    private final List<Level> graphLevels;
    private String name;
    public final Form SINK_VERTEX = GraphForm.getSinkInstance();
    public final Form SOURCE_VERTEX = GraphForm.getSourceInstance();

    public static LayeredGraph createInstance(Grammar g) {
        return new LayeredGraph(g.getLevelSpace());
    }

    public static LayeredGraph createInstance(LevelSpace space) {
        return new LayeredGraph(space);
    }

    protected LayeredGraph(LevelSpace ls) {
        this.levelSpace = ls;
        name = "Anonymous graph";
        layeredContents = LevelwiseFormMap.create();
        graph = new DirectedSparseGraph<Form, FormMapping>();

        // Add sink and source vertices
        addToGraph(SOURCE_VERTEX);
        addToGraph(SINK_VERTEX);

        graphLevels = new ArrayList<Level>();

        graphLevels.add(SOURCE_VERTEX.getLevel());
        for (Level l : ls) {
            graphLevels.add(l);
        }
        graphLevels.add(SINK_VERTEX.getLevel());

        validateIntegrity();
    }

    private LayeredGraph(List<Level> levels) {
        this(LevelSpace.createFromLevelList(levels));
    }

    public void addForm(Form f) {
        Level l = f.getLevel();
        if (!levelSpace.containsLevel(l)) {
            System.err
                    .printf("Trying to add a form %s at level %s -- not found in graph!\n",
                            f, l);
            return;
        }

        if (!graph.containsVertex(f)) {
            addToGraph(f);
            connectToPossibleSources(f);
        }
    }

    public void addFormMapping(FormMapping fm) {
        graph.addEdge(fm, fm.left(), fm.right());

    }

    public LayeredGraph createSubGraph(Form from, Form to) {
        if (!pathExists(from, to))
            return null; // TODO return 'null graph'
        PathFinder pf = new PathFinder(this);
        List<FormChain> tuples = pf.getSubPaths(from, to);
        for (FormChain ft : tuples) {
            System.out.println(ft.contentsToBracketedString());
        }
        // Add level list
        List<Level> newLevels = getLevelsFromTuple(tuples.get(0));
        LayeredGraph result = new LayeredGraph(newLevels);
        for (FormChain ft : tuples) {
            for (Form f : ft.getContents()) {
                if (!(f instanceof GraphForm)) {
                    result.addForm(f);

                }
            }
        }
        for (FormMapping formMapping : graph.getEdges()) {
            if (result.hasPair(formMapping.getFormPair()))
                result.addFormMapping(formMapping);
        }
        return result;

    }

    /**
     * @param ips A list of inputs
     * @return All candidates for this list of inputs
     */
    public List<Candidate> formInputsToCandidates(FormInput ip) {
        // TODO think of more general solution.
        PathFinder pf = new PathFinder(this);
        return pf.allPathsAsCandidates(ip, Direction.RIGHT);
    }

    public DirectedSparseGraph<Form, FormMapping> getGraph() {
        return graph;
    }

    public int getHeightAt(Level l) {
        return layeredContents.getSizeAtLevel(l);
    }

    public Collection<Form> getLayer(Level l) {
        return layeredContents.getFormsAtLevel(l);
    }

    public LevelwiseFormMap getLayeredContents() {
        return layeredContents;
    }

    public int getMaxNumPaths() {
        int result = 1;
        for (Level l : levelSpace) {
            result *= getHeightAt(l);
        }
        return result;
    }

    public List<Level> getOrderedLevels() {
        return levelSpace.getContentsCopy();
    }

    // Methods for adding, removing, connecting forms!

    public int getWidth() {
        return levelSpace.getSize();
    }


    public boolean pathExists(Form from, Form to) {
        // Check if both are in graph, and connected;
        if (!graph.containsVertex(from) || !graph.containsVertex(to)) {
            System.out.printf("No path from %s to %s exists.%n",
                    from.toString(), to.toString());
            System.out.println("From exists? " + graph.containsVertex(from));
            System.out.println("To exists?" + graph.containsVertex(to));
            return false; // todo replace with Null graph
        }
        UnweightedShortestPath<Form, FormMapping> testPath = new UnweightedShortestPath<Form, FormMapping>(
                graph);
        return testPath.getDistance(from, to) != null;
    }

    public void printStats() {
        System.out.printf("Number of levels: %d\n", getWidth());
        System.out.printf("Width: %d\n", getWidth());
        for (Level l : levelSpace) {
            System.out.printf("Level %s has %d vertices\n", l.toString(),
                    getHeightAt(l));
        }
    }

    public void removeForm(Form f) {
        removeFromGraph(f);
        layeredContents.removeForm(f);
    }

    private void connectToPossibleSources(Form f) {
        if (f.getLevel().equals(levelSpace.minValue())) {
            addFormMapping(PairMapping.createInstance(SOURCE_VERTEX, f));
        }

        if (f.getLevel().equals(levelSpace.maxValue())) {
            addFormMapping(PairMapping.createInstance(f, SINK_VERTEX));
        }
    }

    private List<Level> getLevelsFromTuple(FormChain ft) {
        List<Level> result = new ArrayList<Level>(ft.size());
        for (Form f : ft.getContents()) {
            if (!(f instanceof GraphForm))
                result.add(f.getLevel());
        }
        return result;
    }

    private Level getNext(Level l) {
        if (l == levelSpace.maxValue())
            return SinkLevel.getInstance();
        Level toReturn = levelSpace.next(l);
        if (toReturn == Level.NULL_LEVEL) {
            System.out.printf(
                    "Next level for %sis null level... Shouldn't happen!\n", l);
        }
        return levelSpace.next(l);
    }

    /**
     * @param fp
     * @return true if both forms exist in the grammar
     */
    private boolean hasPair(FormPair fp) {
        Form l = fp.left();
        Form r = fp.right();
        return (graph.containsVertex(l) && graph.containsVertex(r));
    }

    // TODO Move to (unit) test class.
    private void validateIntegrity() {
        if (layeredContents.hasLevel(Level.NULL_LEVEL))
            System.err
                    .println("Warning: a form with null level has been added to the graph!");

    }

    /**
     * @param f Form to add
     */
    private void addToGraph(Form f) {
        graph.addVertex(f);
        layeredContents.add(f);

    }

    /**
     * Verify if two Forms are connected.
     *
     * @param f
     * @param g
     * @return
     */
    boolean areConnected(Form f, Form g) {
        return (graph.findEdge(f, g) != null);
    }

    /**
     * Removes a Form from the graph.
     *
     * @param f Form to remove
     */
    void removeFromGraph(Form f) {
        graph.removeVertex(f);
        layeredContents.removeForm(f);
    }

    public List<Level> getGraphLevels() {
        return graphLevels;
    }

    /**
     * @param formToExpand
     * @return
     */
    public boolean isConnectedToSink(Form formToExpand) {
        return graph.getSuccessors(formToExpand).contains(SINK_VERTEX);
    }

}
