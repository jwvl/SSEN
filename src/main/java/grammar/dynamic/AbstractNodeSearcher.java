package grammar.dynamic;

import forms.Form;
import gen.mapping.FormMapping;
import grammar.dynamic.node.AbstractCostNode;
import ranking.constraints.helper.ConstraintArrayList;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 29/03/16.
 */
public abstract class AbstractNodeSearcher<N extends AbstractCostNode> {
    private final PriorityQueue<N> queue;

    public AbstractNodeSearcher(PriorityQueue<N> queue) {
        this.queue = queue;
    }

    public abstract void init(Form initialForm);

    public boolean canExpand() {
        return !queue.isEmpty();
    }

    public AbstractCostNode nextNode() {
        return queue.poll();
    }

    public PriorityQueue<N> getQueue() {
        return queue;
    }

    public abstract void addSuccessor(N parent, FormMapping formMapping, ConstraintArrayList constraintList);
}
