package grammar.dynamic;

import constraints.SparseViolationProfile;
import constraints.helper.ConstraintArrayList;
import constraints.hierarchy.reimpl.Hierarchy;
import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import grammar.dynamic.node.LinkedNodeComparator;
import grammar.dynamic.persistent.LinkedNode;
import grammar.levels.Level;
import util.collections.FrequencyMap;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 05/08/16.
 */
public class LinkedNodeSearcher extends AbstractNodeSearcher<LinkedNode> {
    private final Hierarchy hierarchy;
    private final static short[] EMPTY_ARRAY = new short[0];
    private static final boolean KEEP_STATISTICS = true;
    private FrequencyMap<Level> formFrequencies;

    public LinkedNodeSearcher(Hierarchy hierarchy) {
        super(new PriorityQueue<>(new LinkedNodeComparator()));
        this.hierarchy = hierarchy;
        formFrequencies = new FrequencyMap<>();
    }

    @Override
    protected LinkedNode getInitial(Form initialForm) {
        PairMapping pairMapping = PairMapping.createInstance(null, initialForm);
        return new LinkedNode(null, pairMapping, EMPTY_ARRAY, Short.MAX_VALUE);
    }

    @Override
    public LinkedNode getSuccessor(LinkedNode parent, FormMapping formMapping, ConstraintArrayList constraintArrayList) {
//        formFrequencies.addOne(formMapping.getLevel());
        return new LinkedNode(parent, formMapping, SparseViolationProfile.createArrayFromConstraintArrayList(constraintArrayList,hierarchy), parent.getWorstViolator());
    }

    public int getSize() {
        int sum = 0;
        for (Level level: formFrequencies) {
            sum += formFrequencies.getCount(level);
        }
        return sum;
    }

}
