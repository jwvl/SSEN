package constraints.hierarchy.reimpl;

import com.typesafe.config.ConfigFactory;
import constraints.Constraint;
import constraints.RankedConstraint;
import constraints.hierarchy.Con;
import eval.sample.AbstractSampler;

import java.util.*;

/**
 * Created by janwillem on 29/03/16.
 */
public class Hierarchy implements Con {
    protected final double[] rankings;
    private final Hierarchy parentHierarchy;
    private final static double DEFAULT_RANKING_VALUE = 100.0;
    private List<RankedConstraint> constraintList;
    private IndexedRanking indexedRanking;
    private int size;
    private boolean rankingChanged;

    protected Hierarchy(double[] rankings, int size, Hierarchy parentHierarchy) {
        this.rankings = rankings;
        this.size = size;
        this.parentHierarchy = parentHierarchy;
        updateRankingInfo();
        rankingChanged = false;
    }

    // Updates both the list of RankedConstraints and the
    private void updateRankingInfo() {
        constraintList = createRankedConstraintList();
        indexedRanking = createIndexedRanking();
        rankingChanged = false;
    }

    private static Hierarchy createHierarchy(int expectedSize) {
        double[] rankings = new double[expectedSize];
        Arrays.fill(rankings, Double.NEGATIVE_INFINITY);
        return new Hierarchy(rankings, 0,null);
    }

    public static Hierarchy createNew() {
        return createHierarchy(ConfigFactory.load().getInt("implementation.expectedNumConstraints"));
    }

    public double getRanking(Constraint c) {
        if (!contains(c)) {
            add(c);
        }
        if (rankingChanged) {
            updateRankingInfo();
        }
        return rankings[c.getId()];
    }

    public void addConstraint(Constraint c, double value) {
        if (isSampled()) {
            parentHierarchy.addConstraint(c,value);
        }
        if (!contains(c)) {
            putValue(c, value);
            System.out.println("Added constraint " +c +" to map");
            size++;
            System.out.println("Size of hierarchy is now" + size());
            rankingChanged = true;
        }
    }


    public boolean contains(Constraint constraint) {
        boolean result = rankings[constraint.getId()] != Double.NEGATIVE_INFINITY;
        if (!result) {
            System.out.println("Constraint "+constraint+" not found!");
        }
        return result;
    }

    @Override
    public void add(Constraint constraint) {
        addConstraint(constraint, DEFAULT_RANKING_VALUE + constraint.getInitialBias());
    }

    public void putValue(Constraint constraint, double value) {
        rankings[constraint.getId()] = value;
        rankingChanged = true;
    }

    public void changeConstraintRanking(Constraint constraint, double delta) {
        putValue(constraint, getRanking(constraint)+delta);
    }

    public int size() {
        return size;
    }

    /**
     *
     */
    public void printContents() {
        for (RankedConstraint rc : getRankedConstraintList()) {
            System.out.println(rc);
        }
    }

    public List<RankedConstraint> getRankedConstraintList() {
        if (rankingChanged) {
            updateRankingInfo();
        }
        return constraintList;
    }

    private List<RankedConstraint> createRankedConstraintList() {
        List<RankedConstraint> rankedList = new ArrayList<>(size());
        for (int i=0; i < Constraint.getNumberCreated(); i++) {
            double value = rankings[i];
            if (value != Double.NEGATIVE_INFINITY) {
                Constraint instance = Constraint.withIndex(i);
                rankedList.add(RankedConstraint.of(instance, value));
            }
        }
        Collections.sort(rankedList);
        Collections.reverse(rankedList);
        return Collections.synchronizedList(rankedList);
    }


    public Hierarchy sample(AbstractSampler sampler) {
        double[] sampledRankings = new double[rankings.length];
        Arrays.fill(sampledRankings,Double.NEGATIVE_INFINITY);
        for (int i=0; i < Constraint.getNumberCreated(); i++) {
            double value = rankings[i];
            if (value != Double.NEGATIVE_INFINITY) {
                sampledRankings[i] = sampler.sampleDouble(value);
            }
        }
        return new Hierarchy(sampledRankings,this.size(),this);
    }

    private IndexedRanking createIndexedRanking() {
        return new IndexedRanking(constraintList);
    }

    public IndexedRanking getIndexedRanking() {
        if (rankingChanged) {
            updateRankingInfo();
        }
        return indexedRanking;
    }

    public boolean isSampled() {
        return parentHierarchy != null;
    }

    @Override
    public Iterator<Constraint> iterator() {
        if (rankingChanged) {
            updateRankingInfo();
        }
        Iterator<RankedConstraint> rankedConstraintIterator = constraintList.iterator();
        return new Iterator<Constraint>() {


            @Override
            public boolean hasNext() {
                return rankedConstraintIterator.hasNext();
            }

            @Override
            public Constraint next() {
                return rankedConstraintIterator.next().getConstraint();
            }
        };
    }

    public short getRankingIndex(Constraint constraint) {
        if (!contains(constraint)) {
            add(constraint);
        }
        return getIndexedRanking().getRankingIndex(constraint);
    }

    public String printRankedConstraints() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RankedConstraint rankedConstraint: constraintList) {
            stringBuilder.append(rankedConstraint).append("\n");
        }
        return stringBuilder.toString();
    }
}

