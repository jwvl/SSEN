package grammar.dynamic.node;

import forms.Form;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janwillem on 29/03/16.
 */
public class DynamicCostNode extends AbstractCostNode<DynamicCostNode> {

    public DynamicCostNode(DynamicCostNode parent, FormMapping formMapping) {
        super(parent, formMapping);
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


}
