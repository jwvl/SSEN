package constraints.hierarchy.reimpl;

import com.typesafe.config.ConfigFactory;
import constraints.Constraint;
import constraints.RankedConstraint;
import constraints.hierarchy.Con;
import constraints.hierarchy.violations.ConstraintViolation;
import eval.harmony.autosort.StratifiedDouble;
import eval.sample.AbstractSampler;

import java.util.*;

/**
 * Created by janwillem on 29/03/16.
 */
public class Hierarchy implements Iterable<Constraint> {
    private final Con parentCon;
    protected final double[] map;
    private final static double DEFAULT_RANKING_VALUE = 100.0;

    protected Hierarchy(double[] contents, Con parentCon) {
        map = contents;
        this.parentCon = parentCon;
    }

    public static Hierarchy createHierarchy(int expectedSize, Con parentCon) {
        double[] myMap = new double[expectedSize];
        Arrays.fill(myMap, Double.NEGATIVE_INFINITY);
        return new Hierarchy(myMap, parentCon);
    }

    public static Hierarchy createNew() {
        return createHierarchy(ConfigFactory.load().getInt("implementation.expectedNumConstraints"), new SetCon());
    }

    public double getRanking(Constraint c) {
        if (!contains(c)) {
            addConstraint(c);
        }

        return map[c.getId()];
    }

    public void addConstraint(Constraint c, double value) {
        parentCon.add(c);
        putValue(c, value);
        System.out.println("Size of hierarchy is now" +size());
    }

    public void addConstraint(Constraint c) {
        addConstraint(c, DEFAULT_RANKING_VALUE+c.getInitialBias());
    }

    public boolean contains(Constraint constraint) {
        return map[constraint.getId()] != Double.NEGATIVE_INFINITY;
    }

    public void putValue(Constraint constraint, double value) {
        map[constraint.getId()] = value;
    }

    public void updateConstraintRanking(Constraint constraint, double delta) {
        putValue(constraint, getRanking(constraint)+delta);
    }

    public int size() {
        return parentCon.size();
    }

    /**
     *
     */
    public void printContents() {
        for (RankedConstraint rc : toRankedConstraintList()) {
            System.out.println(rc);
        }
    }

    public List<RankedConstraint> toRankedConstraintList() {
        List<RankedConstraint> rankedList = new ArrayList<>(size());
        for (Constraint constraint : parentCon) {
            rankedList.add(RankedConstraint.of(constraint, getRankingValue(constraint)));
        }
        Collections.sort(rankedList);
        Collections.reverse(rankedList);
        return Collections.synchronizedList(rankedList);
    }


    protected double getRankingValue(Constraint c) {
        return map[c.getId()];
    }

    public SampledHierarchy sample(AbstractSampler sampler) {
        double[] sampledMap = new double[map.length];
        for (int i=0; i < size(); i++) {
            sampledMap[i] = sampler.sampleDouble(map[i]);
        }
        for (int i= size(); i < map.length; i++) {
            sampledMap[i] = Double.NEGATIVE_INFINITY;
        }

        SampledHierarchy result = new SampledHierarchy(this, sampledMap, sampler);
        return result;
    }

    public IndexedRanking getIndexedRanking() {
        return new IndexedRanking(toRankedConstraintList(), this);
    }

    @Override
    public Iterator<Constraint> iterator() {
        return parentCon.iterator();
    }

    public ConstraintViolation toConstraintViolation(Constraint c) {
        return ConstraintViolation.of(c, StratifiedDouble.of(c.getStratum(),getRanking(c)));
    }

    public Con getParentCon() {
        return parentCon;
    }
}
