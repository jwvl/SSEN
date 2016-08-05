/**
 *
 */
package graph.traversal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import forms.Form;
import gen.mapping.FormMapping;
import graph.LayeredGraph;
import constraints.hierarchy.OldHierarchy;
import constraints.hierarchy.OldRankedConstraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date Dec 4, 2014
 */
public class GraphViolationMap {
    private ListMultimap<Form, OldRankedConstraint> intralevelRCs;
    private ListMultimap<FormMapping, OldRankedConstraint> interlevelRCs;
    private OldHierarchy violatedInGraph;

    protected GraphViolationMap(Collection<Form> vertices,
                                Collection<FormMapping> edges, Collection<OldRankedConstraint> constraints) {
        intralevelRCs = ArrayListMultimap.create();
        interlevelRCs = ArrayListMultimap.create();
        ArrayList<OldRankedConstraint> building = Lists.newArrayList();
        for (OldRankedConstraint rc : constraints) {
            System.out.printf("Now at constraint %s\n", rc);
            for (FormMapping fp : edges) {

                int viol = rc.getNumViolations(fp);
                if (viol > 0) {
                    for (int i = 0; i < viol; i++) {
                        interlevelRCs.put(fp, rc);
                    }
                    building.add(rc);
                }

            }
            violatedInGraph = OldHierarchy.fromRankedList(building);
        }
        System.out.println("Created violation map");
        this.printContents();
    }

    protected static GraphViolationMap createInstance(
            Collection<Form> vertices, Collection<FormMapping> edges,
            Collection<OldRankedConstraint> constraints) {
        System.out
                .printf("Creating violation map (%d vertices,  %d edges, ? constraints)\n",
                        vertices.size(), edges.size());

        return new GraphViolationMap(vertices, edges, constraints);
    }

    /**
     * @param lg
     * @param constraints
     * @return
     */
    public static GraphViolationMap createInstance(LayeredGraph lg,
                                                   Collection<OldRankedConstraint> constraints) {
        return createInstance(lg.getGraph().getVertices(), lg.getGraph()
                .getEdges(), constraints);
    }

    /**
     * @param f Form for which to retrieve constraints
     */
    protected List<OldRankedConstraint> getIntralevel(Form f) {
        return intralevelRCs.get(f);
    }

    /**
     * @param fp FormPair for which to retrieve constraints
     */
    protected List<OldRankedConstraint> getInterlevel(FormMapping fp) {
        return interlevelRCs.get(fp);

    }

    public Set<Form> getFormSet() {
        System.out.printf("Returning %d forms\n", intralevelRCs.keySet().size());
        return intralevelRCs.keySet();
    }

    public Set<FormMapping> getFormMappingSet() {
        System.out.printf("Returning %d forms\n", interlevelRCs.keySet().size());
        return interlevelRCs.keySet();
    }

    protected List<OldRankedConstraint> getConstraints(FormMapping t) {
        return getInterlevel(t);

    }

    public OldHierarchy getViolatedInGraph() {
        return violatedInGraph;
    }


    public void printContents() {
        System.out.println("Intralevel violations:");
        for (Form f : getFormSet()) {
            for (OldRankedConstraint rc : getIntralevel(f))
                System.out.printf(" %s is violated by %s\n", f, rc);
        }
        System.out.println("Interlevel violations:");

        for (FormMapping fp : getFormMappingSet()) {
            for (OldRankedConstraint rc : getInterlevel(fp))
                System.out.printf(" %s is violated by %s\n", fp, rc);
        }
    }

}
