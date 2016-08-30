package grammar.dynamic;

import constraints.helper.ConstraintArrayList;
import forms.Form;
import gen.mapping.FormMapping;
import grammar.dynamic.node.AbstractCostNode;

import java.util.PriorityQueue;

/**
 * Created by janwillem on 29/03/16.
 * Different implementations of this node searcher exist. They all use
 * a priority queue to keep the current lowest-cost path at the front of the queue.
 */
public abstract class AbstractNodeSearcher<N extends AbstractCostNode> {
    private final PriorityQueue<N> queue;

    public AbstractNodeSearcher(PriorityQueue<N> queue) {
        this.queue = queue;
    }

    protected abstract N getInitial(Form initialForm);

    public void init(Form initialForm) {
        queue.add(getInitial(initialForm));
    }

    public boolean canExpand() {
        return !(queue.isEmpty());
    }

    public AbstractCostNode nextNode() {
        return queue.poll();
    }

    public abstract N getSuccessor(N parent, FormMapping formMapping, ConstraintArrayList constraintArrayList);

    public void addSuccessor(N parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        N successor = getSuccessor(parent, formMapping, constraintList);
        queue.add(successor);
    }

    public void generateSuccessor(N parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        addSuccessor(parent, formMapping, constraintList);
    }

    private String printCompletely(AbstractCostNode toPrint, StringBuilder builder) {
        builder.append(toPrint.getMappedForm());
        if (toPrint.getParent() == null) {
            return builder.toString();
        } else {
            builder.append("\t<- ");
            return printCompletely(toPrint.getParent(), builder);
        }
    }

    public void clearQueue() {
        queue.clear();
    }

    public int getQueueSize() {
        return queue.size();
    }
}
