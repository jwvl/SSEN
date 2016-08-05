/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Sets;
import forms.morphosyntax.MForm;
import gen.mapping.FormMapping;
import constraints.Constraint;
import constraints.factories.FormConstraintFactory;
import constraints.helper.ConstraintArrayList;
import simulate.french.sixlevel.constraints.AnalyzeConstraint;

import java.util.*;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class AnalyzeConstraintFactory extends FormConstraintFactory<MForm> {
    AnalyzeConstraint analyzeConstraint;
    Set<Constraint> constraintSingletonSet;
    Collection<Constraint> emptyCollection;

    public AnalyzeConstraintFactory() {
        analyzeConstraint = new AnalyzeConstraint();
        constraintSingletonSet = Sets.newHashSetWithExpectedSize(1);
        constraintSingletonSet.add(analyzeConstraint);
        emptyCollection = Collections.emptySet();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * constraints.factories.ConstraintFactory#addTransgressors(java
     * .util.Collection)
     */
    @Override
    public void addForm(MForm mForm) {
        // Does no thing!
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * constraints.factories.ConstraintFactory#computeSpecific(graph
     * .Transgressor)
     */
    @Override
    public List<Constraint> getConstraintsForForm(MForm transgressor) {
        int numViolations = analyzeConstraint.getNumViolations(transgressor);
        List<Constraint> result = new ArrayList<Constraint>(numViolations);
        for (int i = 0; i < numViolations; i++) {
            result.add(analyzeConstraint);
        }
        return result;
    }

    @Override
    protected void addViolatorsForForm(MForm form, ConstraintArrayList list) {
        int numViolations = analyzeConstraint.getNumViolations(form);
        list.addMultiple(analyzeConstraint, numViolations);
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.factories.ConstraintFactory#createAll()
     */
    @Override
    public Collection<Constraint> getAll() {
        return constraintSingletonSet;
    }

    /* (non-Javadoc)
     * @see constraints.factories.ConstraintFactory#addFormMapping(gen.mapping.FormMapping)
     */
    @Override
    public void addFormMapping(FormMapping fm) {
        // Does nothing
    }
}
