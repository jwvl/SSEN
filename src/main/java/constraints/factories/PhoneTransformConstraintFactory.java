/**
 *
 */
package constraints.factories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import forms.phon.PhoneTransform;
import gen.mapping.PhoneRewriteMapping;
import constraints.Constraint;
import constraints.helper.ConstraintArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public abstract class PhoneTransformConstraintFactory extends MappingConstraintFactory<PhoneRewriteMapping> {

    private Map<PhoneTransform, Constraint> constraintCache;


    public PhoneTransformConstraintFactory() {
        constraintCache = Maps.newConcurrentMap();
    }

    /* (non-Javadoc)
     * @see constraints.factories.MappingConstraintFactory#addTransgressor(gen.mapping.FormMapping)
     */
    @Override
    public void addTransgressor(PhoneRewriteMapping f) {
        Collection<PhoneTransform> offenders = f.getRewrites();
        for (PhoneTransform phoneTransform : offenders) {
            getConstraint(phoneTransform);
        }
    }

    /**
     * @param phoneTransform
     */
    public Constraint getConstraint(PhoneTransform phoneTransform) {
        Constraint result = constraintCache.get(phoneTransform);
        if (result == null) {
            result = createConstraint(phoneTransform);
            System.out.println("Created constraint for " + phoneTransform + "--> #" + result.getId());
            constraintCache.put(phoneTransform, result);
        }
        return result;

    }

    /**
     * @param offender
     * @return
     */
    public abstract Constraint createConstraint(PhoneTransform offender);

    /* (non-Javadoc)
     * @see constraints.factories.MappingConstraintFactory#getConstraintsForMapping(gen.mapping.FormMapping)
     */
    @Override
    public List<Constraint> getConstraintsForMapping(PhoneRewriteMapping transgressor) {
        List<Constraint> result = Lists.newArrayList();
        for (PhoneTransform offender : transgressor.getRewrites()) {
            result.add(getConstraint(offender));
        }
        return result;
    }

    @Override
    protected void addViolatedForMapping(PhoneRewriteMapping formMapping, ConstraintArrayList list) {
        for (PhoneTransform offender : formMapping.getRewrites()) {
            list.add(getConstraint(offender));
        }
    }

    /* (non-Javadoc)
             * @see constraints.factories.ConstraintFactory#getAll()
             */
    @Override
    public Collection<Constraint> getAll() {
        return constraintCache.values();
    }


}
