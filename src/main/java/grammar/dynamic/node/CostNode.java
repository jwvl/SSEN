/**
 *
 */
package grammar.dynamic.node;

import eval.harmony.SimpleOTHarmony;
import eval.harmony.autosort.StratifiedDouble;
import forms.Form;
import ranking.OldRankedConstraint;
import ranking.constraints.Constraint;

import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Aug 9, 2015
 */
public class CostNode implements Comparable<CostNode> {
    private final Form form;
    private final SimpleOTHarmony cost;
    private final Constraint[] constraints;

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(CostNode arg0) {
        return cost.compareTo(arg0.cost);
    }

    private CostNode(Form form, SimpleOTHarmony cost, Constraint[] constraints) {
        this.form = form;
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
    public CostNode createSuccessor(Form successorForm,
                                    List<OldRankedConstraint> violated) {
        // Calculate new cost
        SimpleOTHarmony expansionCost = cost.merge(violated);
        Constraint[] asConstraints = new Constraint[violated.size()];
        int i = 0;
        for (OldRankedConstraint rc : violated) {
            asConstraints[i++] = rc.getConstraint();
        }
        return new CostNode(successorForm, expansionCost, asConstraints);
    }

    /**
     * @param successorForm
     * @param violated
     * @return
     */
    public CostNode createSuccessor(Form successorForm,
                                    List<Constraint> violated, Collection<StratifiedDouble> disharmonies) {
        // Calculate new cost
        Constraint[] asConstraints = new Constraint[violated.size()];
        violated.toArray(asConstraints);
        SimpleOTHarmony expansionCost = cost.mergeWithDisharmonies(disharmonies);
        return new CostNode(successorForm, expansionCost, asConstraints);
    }

    /**
     * @param startForm
     * @return
     */
    public static CostNode createNew(Form startForm) {
        SimpleOTHarmony cost = SimpleOTHarmony.initializeEmpty();
        return new CostNode(startForm, cost, new Constraint[0]);
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
        result = prime * result + ((form == null) ? 0 : form.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CostNode))
            return false;
        CostNode other = (CostNode) obj;
        if (form == null) {
            if (other.form != null)
                return false;
        } else if (!form.equals(other.form))
            return false;
        return true;
    }


}
