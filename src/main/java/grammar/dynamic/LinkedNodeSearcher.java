package grammar.dynamic;

import constraints.hierarchy.reimpl.Hierarchy;
import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import grammar.dynamic.node.LinkedNodeComparator;
import grammar.dynamic.persistent.LinkedNode;
import constraints.SparseViolationProfile;
import constraints.helper.ConstraintArrayList;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 05/08/16.
 */
public class LinkedNodeSearcher extends AbstractNodeSearcher<LinkedNode> {
    private final Hierarchy hierarchy;

    public LinkedNodeSearcher(Hierarchy hierarchy) {
        super(new PriorityQueue<LinkedNode>(new LinkedNodeComparator()));
        this.hierarchy = hierarchy;
    }

    @Override
    protected LinkedNode getInitial(Form initialForm) {
        PairMapping pairMapping = PairMapping.createInstance(null, initialForm);
        return new LinkedNode(null, pairMapping, SparseViolationProfile.empty());
    }

    @Override
    public LinkedNode getSuccessor(LinkedNode parent, FormMapping formMapping, ConstraintArrayList constraintArrayList) {
        return new LinkedNode(parent, formMapping, SparseViolationProfile.createFromConstraintArrayList(constraintArrayList,hierarchy.getIndexedRanking()));
    }
}
