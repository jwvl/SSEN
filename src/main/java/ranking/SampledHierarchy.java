package ranking;

import ranking.constraints.Constraint;

/**
 * Created by janwillem on 02/04/16.
 */
public abstract class SampledHierarchy extends Hierarchy {
    private final GrammarHierarchy template;

    public SampledHierarchy(GrammarHierarchy template) {
        this.template = template;
    }

    protected void addToOriginalIfNecessary(Constraint c) {
        if (!template.contains(c)) {
            template.addNewConstraint(c);
        }
    }
}
