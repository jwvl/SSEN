package grammar.dynamic.node.hg;

import java.util.Comparator;

/**
 * Created by janwillem on 28/09/16.
 */
public class HarmonicNodeComparator implements Comparator<HarmonicCostNode> {

    @Override
    public int compare(HarmonicCostNode o1, HarmonicCostNode o2) {
        return Double.compare(o1.totalDisharmony,o2.totalDisharmony);
    }
}
