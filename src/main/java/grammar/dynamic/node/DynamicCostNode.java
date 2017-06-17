package grammar.dynamic.node;

import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by janwillem on 29/03/16.
 */
public class DynamicCostNode extends AbstractCostNode<DynamicCostNode> {
    private static long counter = 0;
    private final long count = counter++;
    private final int hashCode;


    public DynamicCostNode(DynamicCostNode parent, FormMapping formMapping) {
        super(parent, formMapping);
        hashCode = calculateHashCode();
    }

    public DynamicCostNode createSuccessor(FormMapping formMapping) {
        return new DynamicCostNode(this, formMapping);
    }

    public static DynamicCostNode createNew(Form initialForm) {
        PairMapping pairMapping = PairMapping.createInstance(null, initialForm);
        return new DynamicCostNode(null, pairMapping);
    }

    public boolean isInitialNode() {
        return getParent() == null;
    }

    public List<FormMapping> getAllPredecessors() {
        List<FormMapping> result = new ArrayList<>(getFormMapping().getLevel().myIndex());
        return appendPredecessor(result);
    }

    private List<FormMapping> appendPredecessor(List<FormMapping> result) {
        result.add(getFormMapping());
        if (isInitialNode()) {
            return result;
        } else {
            return getParent().appendPredecessor(result);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DynamicCostNode that = (DynamicCostNode) o;
        return count == that.count;
    }
    public int calculateHashCode() {
        return Objects.hash(super.hashCode(), count);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
