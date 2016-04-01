/**
 *
 */
package graph.traversal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import edu.uci.ics.jung.graph.Graph;
import eval.harmony.GenericHarmonyInitializer;
import eval.harmony.HGHarmony;
import eval.harmony.Harmony;
import eval.harmony.OTHarmony;
import forms.Form;
import forms.FormChain;
import gen.mapping.FormMapping;
import grammar.levels.Level;
import graph.LayeredGraph;
import ranking.OldHierarchy;
import ranking.OldRankedConstraint;
import util.collections.ComparableTester;

import java.util.*;

/**
 * @author jwvl
 * @date Dec 5, 2014
 */
public class ViterbiGraph<H extends Harmony<H>> {
    private LayeredGraph lg;
    private GraphViolationMap violationMap;
    private OldHierarchy violatedConstraints;
    private Map<Form, H> stateCosts;
    private Map<FormMapping, H> transitionCosts;
    private Map<Form, H> summedCosts;
    private ListMultimap<Form, Form> backPointers;
    private H INFINITE;

    public ViterbiGraph(Class<H> h, LayeredGraph lg,
                        Collection<OldRankedConstraint> constraints) {
        this.lg = lg;
        this.violationMap = GraphViolationMap.createInstance(lg, constraints);
        violatedConstraints = violationMap.getViolatedInGraph();
        violatedConstraints.sort();
        initializeCosts(new GenericHarmonyInitializer<H>(h, constraints.size()));

    }

    public static ViterbiGraph<OTHarmony> createOT(LayeredGraph lg,
                                                   Collection<OldRankedConstraint> constraints) {
        return new ViterbiGraph<OTHarmony>(OTHarmony.class, lg, constraints);
    }

    public static ViterbiGraph<HGHarmony> createHG(LayeredGraph lg,
                                                   Collection<OldRankedConstraint> constraints) {
        return new ViterbiGraph<HGHarmony>(HGHarmony.class, lg, constraints);
    }

    public FormChain evaluate(Form end) {
        // TODO Not done yet!
        calculateCosts();
        List<Form> result = fromBackPointers(end);
        Collections.reverse(result);
        return FormChain.fromFormstoSimpleMappings(result);
    }

    /**
     * Initializes probabilities in the graph.
     */
    private void initializeCosts(GenericHarmonyInitializer<H> hInit) {
        stateCosts = new HashMap<Form, H>();
        transitionCosts = new HashMap<FormMapping, H>();
        INFINITE = hInit.getInfinite();
        for (Form f : violationMap.getFormSet()) {
            List<OldRankedConstraint> violators = violationMap.getIntralevel(f);
            stateCosts.put(f, hInit.apply(violators));
        }
        stateCosts.put(lg.SOURCE_VERTEX, INFINITE.getNull());
        for (FormMapping fp : violationMap.getFormMappingSet()) {
            List<OldRankedConstraint> violators = violationMap.getInterlevel(fp);
            transitionCosts.put(fp, hInit.apply(violators));
        }

    }

    private Map<Form, H> copyStateCosts() {
        Map<Form, H> result = new HashMap<Form, H>();
        for (Form f : stateCosts.keySet()) {
            result.put(f, stateCosts.get(f));
        }
        return result;
    }

    private void calculateCosts() {
        summedCosts = copyStateCosts();
        backPointers = ArrayListMultimap.create();
        List<Level> orderedLevels = lg.getGraphLevels();
        for (Level l : orderedLevels) {
            for (Form f : lg.getLayer(l)) {
                setMinIncomingCost(f);
            }
        }
    }

    private List<Form> fromBackPointers(Form f) {
        List<Form> result = new ArrayList<Form>();
        Form next = f;
        boolean endReached = false;
        while (!endReached) {
            result.add(next);
            if (next == lg.SOURCE_VERTEX) {
                endReached = true;
            } else {
                List<Form> nextForms = backPointers.get(next);
                next = nextForms.get(0);
            }

        }
        return result;
    }

    private void setMinIncomingCost(Form f) {
        Graph<Form, FormMapping> g = lg.getGraph();
        if (g.inDegree(f) < 1)
            return;
        List<Form> minForms = new ArrayList<Form>();
        H minH = INFINITE;
        for (FormMapping fp : g.getInEdges(f)) {
            H tc = transitionCosts.get(fp);
            H fromCost = summedCosts.get(fp.left());
            H summed = fromCost.createMerger(tc);
            int compare = summed.getOrdering().compare(summed, minH);
            System.out.println(ComparableTester.comparisonPrint(summed, minH,
                    compare));
            if (compare <= 0) {
                if (compare < 0)
                    minForms.clear();
                minForms.add(fp.left());
                minH = summed;
            }
            System.out.printf(
                    "After checking connection %s, minimum harmony is %s\n",
                    fp, minH);
        }
        summedCosts.put(f, minH);
        System.out.println("Backpointers for " + f);
        for (Form mf : minForms) {
            System.out.printf(" %s (cost: %s)\n", mf, minH);
        }
        backPointers.putAll(f, minForms);
    }
}
