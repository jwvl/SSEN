package grammar.dynamic;

import forms.Form;
import gen.mapping.FormMapping;
import grammar.dynamic.node.AbstractCostNode;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.helper.ConstraintArrayList;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by janwillem on 29/03/16.
 */
public abstract class AbstractNodeSearcher<N extends AbstractCostNode> {
    private int counter = 0;
    private final PriorityQueue<N> queue;
    private final Queue<N> stack;
    private final int usePriorityLevel = BiPhonSix.getSemSynFormLevel().myIndex();
    private boolean stackIsFinished = false;

    public AbstractNodeSearcher(PriorityQueue<N> queue) {
        this.queue = queue;
        stack = new ArrayDeque<>();
    }

    protected abstract N getInitial(Form initialForm);

    public void init(Form initialForm) {
        stack.add(getInitial(initialForm));
    }

    public boolean canExpand() {
        return !(stackIsFinished && queue.isEmpty());
    }

    public AbstractCostNode nextNode() {
        if (!stackIsFinished) {
            if (stack.isEmpty()) {
                stackIsFinished = stack.isEmpty();
            } else {
                return stack.poll();
            }
        }
        return queue.poll();
    }

    public abstract N getSuccessor(N parent, FormMapping formMapping, ConstraintArrayList constraintArrayList);

    public void addSuccessor(N parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        N successor = getSuccessor(parent, formMapping, constraintList);
        Queue<N> myQueue;
        if (successor.getMappedForm().getLevelIndex() > usePriorityLevel) {
            myQueue = queue;
        } else {
            myQueue = stack;
        }
        myQueue.add(successor);
    }

    public void generateSuccessor(N parent, FormMapping formMapping, ConstraintArrayList constraintList) {
        counter++;
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
}
