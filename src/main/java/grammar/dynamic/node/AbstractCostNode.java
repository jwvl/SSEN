package grammar.dynamic.node;

import forms.Form;
import gen.mapping.FormMapping;

/**
 * Created by janwillem on 29/03/16.
 */
public abstract class AbstractCostNode<C extends AbstractCostNode> {
    private final C parent;
    private final FormMapping formMapping;

    public AbstractCostNode(C parent, FormMapping formMapping) {
        this.parent = parent;
        this.formMapping = formMapping;
    }

    public C getParent() {
        return parent;
    }

    public FormMapping getFormMapping() {
        return formMapping;
    }

    public Form getMappedForm() {
        return formMapping.right();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractCostNode<?> that = (AbstractCostNode<?>) o;

        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        return formMapping != null ? formMapping.equals(that.formMapping) : that.formMapping == null;

    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (formMapping != null ? formMapping.hashCode() : 0);
        return result;
    }
}
