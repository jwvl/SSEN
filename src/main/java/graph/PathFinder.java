/**
 *
 */
package graph;

import candidates.Candidate;
import candidates.FormInput;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import forms.Form;
import forms.FormChain;
import gen.mapping.FormMapping;
import util.string.CollectionPrinter;
import util.string.StringPrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds paths through a layered graph.
 *
 * @author jwvl
 * @date 01/11/2014
 */
public class PathFinder {
    protected List<FormChain> allPaths;
    protected final DirectedSparseGraph<Form, FormMapping> graph;
    protected final LayeredGraph lg;
    protected Form source;
    protected Form target;
    private static int debugCount = 0;
    private static boolean printAfterFinding = true;

    public PathFinder(LayeredGraph parent) {
        super();
        this.lg = parent;
        this.graph = lg.getGraph();

    }

    public List<Candidate> allPathsAsCandidates(FormInput ip, Direction d) {
        getAllPaths(ip.getForm(), d);
        List<Candidate> result = new ArrayList<Candidate>();
        for (FormChain tuple : allPaths) {
            result.add(Candidate.fromInputAndChain(ip, tuple));
        }
        return result;
    }

    public List<FormChain> getSubPaths(Form from, Form to) {
        this.source = from;
        this.target = to;
        allPaths = new ArrayList<FormChain>();
        runSearch();
        return allPaths;
    }

    private void addPath(List<Form> toAdd) {
        allPaths.add(FormChain.fromFormstoSimpleMappings(toAdd));
    }

    /**
     * TODO Check if iteration is faster
     *
     * @param current
     * @param next
     */
    private void findAllPathsRecursive(List<Form> current, Form next) {
        debugCount++;
        if (debugCount % 10000 == 0) {
            System.out.printf("Currently trying to find path from %s to %s\n",
                    StringPrinter.linearizeList(current, "->"), next);
        }
        current.add(next);
        if (next.getLevel().equals(target.getLevel())) {
            if (next.equals(target)) {
                addPath(current);
            }
        } else {
            for (Form suc : graph.getSuccessors(next)) {
                if (next == lg.SINK_VERTEX) {
                    System.err.printf("Huh? %s is a successor to %s...\n", suc,
                            next);
                }
                findAllPathsRecursive(new ArrayList<Form>(current), suc);
            }
        }
    }

    private List<FormChain> getAllPaths(Form start, Direction d) {
        setSourceAndTarget(start, d);
        allPaths = new ArrayList<FormChain>();
        runSearch();
        return allPaths;
    }

    private void runSearch() {
        ArrayList<Form> startList = new ArrayList<Form>(lg.getWidth());
        findAllPathsRecursive(startList, source);
        if (printAfterFinding) {
            CollectionPrinter.print(allPaths);
        }
    }

    private void setSourceAndTarget(Form start, Direction d) {
        if (d == Direction.RIGHT) {
            this.source = start;
            this.target = lg.SINK_VERTEX;
        }
        if (d == Direction.LEFT) {
            this.source = lg.SOURCE_VERTEX;
            this.target = start;
        }

    }

}
