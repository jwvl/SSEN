package grammar.dynamic.node;

import eval.harmony.CostFactory;
import gen.mapping.FormMapping;
import constraints.Constraint;

import java.util.List;

/**
 * Created by janwillem on 29/03/16.
 */
public class ListingCostNode implements Comparable<ListingCostNode> {
    private final ListingCostNode parent;
    private final FormMapping mapping;
    private final List<Constraint> violated;
    private final CostFactory factory;

    public ListingCostNode(ListingCostNode parent, FormMapping mapping, List<Constraint> violated, CostFactory factory) {
        this.parent = parent;
        this.mapping = mapping;
        this.violated = violated;
        this.factory = factory;
    }

    public List<Constraint> buildList(List<Constraint> building) {
        building.addAll(violated);
        if (parent != null) {
            return parent.buildList(building);
        } else {
            return building;
        }
    }

    @Override
    public int compareTo(ListingCostNode o) {
        return 0;
    }
}
