/**
 *
 */
package gen.subgen;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import forms.Form;
import gen.constrain.GenConstrainer;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import grammar.levels.Level;
import ranking.constraints.Constraint;
import ranking.constraints.factories.ConstraintFactory;
import ranking.constraints.helper.ConstraintArrayList;
import util.collections.Couple;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author jwvl
 * @date Dec 18, 2014
 */
public abstract class SubGen<F extends Form, G extends Form> {

    private List<GenConstrainer<G>> constrainers;
    private List<ConstraintFactory> constraintFactories;
    protected final Level leftLevel, rightLevel;
    protected LoadingCache<F, SubCandidateSet> mappingCache;
    protected LoadingCache<FormMapping, ConstraintArrayList> constraintCache;
    // Test method
    private static boolean PRINT_CONSTRAINTS = false;

    protected SubGen(Level leftLevel, Level rightLevel) {
        this.leftLevel = leftLevel;
        this.rightLevel = rightLevel;
        this.constrainers = Lists.newArrayList();
        mappingCache = null;
        constraintFactories = Lists.newArrayList();
    }

    protected SubGen(Level leftLevel, Level rightLevel, int cacheSize) {
        this(leftLevel, rightLevel);
        addLoadingCache(cacheSize);
    }

    public void addConstrainer(GenConstrainer<G> constrainer) {
        constrainers.add(constrainer);
    }

    public void addConstraintFactory(ConstraintFactory factory) {
        constraintFactories.add(factory);
    }

    public SubCandidateSet generateRight(Form f) {
        F form = (F) f;
        if (cachesPairs()) {
            try {
                return mappingCache.get(form);
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        SubCandidateSet result = getRightFunction().apply(form);
        removeConstrainedMappings(result);
        for (FormMapping g : result) {

            if (PRINT_CONSTRAINTS) {

                for (Constraint constraint : getAssociatedConstraints(g)) {
                    if (constraint == null) {
                        System.out.println("Associated constraint is null?");
                    }
                    int numViolations = constraint.getNumViolations(g);
                    if (numViolations > 0) {
                        System.out.printf("%s  %s (%d)\n", g, constraint.toString(), numViolations);
                    }
                }

            }
        }

        return result;
    }

    public ConstraintArrayList computeConstraints(FormMapping mapping) {
        ConstraintArrayList result = ConstraintArrayList.create();
        for (ConstraintFactory factory : constraintFactories) {
            factory.addViolatedToList(mapping, result);
        } //TODO improve more?
        return result;
    }


    public ConstraintArrayList getAssociatedConstraints(FormMapping mapping) {
        if (cachesPairs()) {
            try {
                return constraintCache.get(mapping);
            } catch (ExecutionException e) {
                e.printStackTrace();
                return computeConstraints(mapping);
            }
        } else {
            return computeConstraints(mapping);
        }
    }

    public Couple<Level> getLevels() {
        return Couple.of(leftLevel, rightLevel);
    }

    /**
     * @param result
     */
    private void removeConstrainedMappings(SubCandidateSet result) {
        Collection<FormMapping> retain = Sets.newHashSet();
        for (FormMapping formMapping : result) {
            boolean isLegal = true;
            Form rightForm = formMapping.right();
            @SuppressWarnings("unchecked")
            G g = (G) rightForm;
            for (GenConstrainer<G> constrainer : constrainers) {
                if (!constrainer.canGenerate(g)) {
                    isLegal = false;
                }
            }
            if (isLegal) {
                retain.add(formMapping);
            }
        }
        result = SubCandidateSet.of(retain);
    }

    protected boolean cachesPairs() {
        return mappingCache != null;
    }

    protected abstract Function<F, SubCandidateSet> getRightFunction();

    public void addLoadingCache(int maxSize) {
        if (maxSize <= 0)
            return;
        CacheLoader<F, SubCandidateSet> candidateLoader = new CacheLoader<F, SubCandidateSet>() {
            @Override
            public SubCandidateSet load(F f) throws Exception {
                return getRightFunction().apply(f);
            }
        };
        mappingCache = CacheBuilder.newBuilder().maximumSize(maxSize).build(candidateLoader);
        CacheLoader<FormMapping, ConstraintArrayList> constraintLoader = new CacheLoader<FormMapping, ConstraintArrayList>() {
            @Override
            public ConstraintArrayList load(FormMapping formMapping) throws Exception {
                return computeConstraints(formMapping);
            }
        };
        constraintCache = CacheBuilder.newBuilder().maximumSize(maxSize).weakKeys().build(constraintLoader);
    }

    /**
     *
     */
    public void printStats() {
        if (cachesPairs()) {
            CacheStats stats = mappingCache.stats();
            System.out.printf("Cache for %s %n Hit rate: %.2f%n", this, stats.hitRate());
            System.out.printf("Size: %d%n", stats.loadCount());
            System.out.printf("Average load penalty: %.2f%n", stats.averageLoadPenalty());
        }

    }

}
