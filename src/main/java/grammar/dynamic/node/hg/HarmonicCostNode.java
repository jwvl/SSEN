package grammar.dynamic.node.hg;

import gen.mapping.FormMapping;
import grammar.dynamic.node.AbstractCostNode;

/**
 * Created by janwillem on 28/09/16.
 */
public class HarmonicCostNode extends AbstractCostNode<HarmonicCostNode> {
    public final double totalDisharmony;

    public HarmonicCostNode(HarmonicCostNode parent, FormMapping formMapping, double newDisharmony) {
        super(parent, formMapping);
        totalDisharmony = parent == null ? 0.0 : parent.totalDisharmony + newDisharmony;
    }
}
