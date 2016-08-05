/**
 *
 */
package grammar.dynamic.node;

import eval.harmony.SimpleOTHarmony;
import eval.harmony.autosort.StratifiedDouble;
import forms.Form;
import constraints.hierarchy.OldRankedConstraint;
import constraints.Constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Aug 9, 2015
 */
public class CostNodeWithBackpointer implements Comparable<CostNodeWithBackpointer> {
    private final Form form;
    private final CostNodeWithBackpointer parent;
    private final SimpleOTHarmony cost;
    private final Constraint[] constraints;


    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(CostNodeWithBackpointer arg0) {
        return cost.compareTo(arg0.cost);
    }

    private CostNodeWithBackpointer(Form form, CostNodeWithBackpointer parent, SimpleOTHarmony cost, Constraint[] constraints) {
        this.form = form;
        this.parent = parent;
        this.cost = cost;
        this.constraints = constraints;
    }


    public Form getForm() {
        return form;
    }

    /**
     * @param successorForm
     * @param violated
     * @return
     */
    public CostNodeWithBackpointer createSuccessor(Form successorForm,
                                                   List<OldRankedConstraint> violated) {
        // Calculate new cost
        SimpleOTHarmony expansionCost = cost.merge(violated);
        Constraint[] asConstraints = new Constraint[violated.size()];
        int i = 0;
        for (OldRankedConstraint rc : violated) {
            asConstraints[i++] = rc.getConstraint();
        }
        return new CostNodeWithBackpointer(successorForm, this, expansionCost, asConstraints);
    }

    /**
     * @param successorForm
     * @param violated
     * @return
     */
    public CostNodeWithBackpointer createSuccessor(Form successorForm,
                                                   List<Constraint> violated, Collection<StratifiedDouble> disharmonies) {
        // Calculate new cost
        Constraint[] asConstraints = new Constraint[violated.size()];
        violated.toArray(asConstraints);
        SimpleOTHarmony expansionCost = cost.mergeWithDisharmonies(disharmonies);
        return new CostNodeWithBackpointer(successorForm, this, expansionCost, asConstraints);
    }

    /**
     * @param startForm
     * @return
     */
    public static CostNodeWithBackpointer createNew(Form startForm, CostNodeWithBackpointer parent) {
        SimpleOTHarmony cost = SimpleOTHarmony.initializeEmpty();
        return new CostNodeWithBackpointer(startForm, parent, cost, new Constraint[0]);
    }


    @Override
    public String toString() {
        return String.format("CostNode %s %s%n", form, cost.toString());
    }

    /**
     * @return the constraints
     */
    public Constraint[] getConstraints() {
        return constraints;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(constraints);
        result = prime * result + ((cost == null) ? 0 : cost.hashCode());
        result = prime * result + ((form == null) ? 0 : form.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CostNodeWithBackpointer))
            return false;
        CostNodeWithBackpointer other = (CostNodeWithBackpointer) obj;
        if (!Arrays.equals(constraints, other.constraints))
            return false;
        if (cost == null) {
            if (other.cost != null)
                return false;
        } else if (!cost.equals(other.cost))
            return false;
        if (form == null) {
            if (other.form != null)
                return false;
        } else if (!form.equals(other.form))
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        return true;
    }

    /**
     * @return the parent
     */
    public CostNodeWithBackpointer getParent() {
        return parent;
    }

}
