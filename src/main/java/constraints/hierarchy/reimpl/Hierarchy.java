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
    private List<RankedConstraint> rankedConstraints;
    private IndexedRanking indexedRanking;
    private int size;
    private boolean rankingChanged;

    protected Hierarchy(double[] rankings, int size, Hierarchy parentHierarchy) {
        this.rankings = rankings;
        this.size = size;
        this.parentHierarchy = parentHierarchy;
        updateRankingInfo();
    }

    protected Hierarchy(double[] rankings, List<RankedConstraint> rankedConstraints, int size, Hierarchy parentHierarchy) {
        this.rankings = rankings;
        this.size = size;
        this.parentHierarchy = parentHierarchy;
        this.rankedConstraints = rankedConstraints;
        this.indexedRanking = createIndexedRanking();
        rankingChanged = false;
    }

    // Updates both the list of RankedConstraints and the
    private void updateRankingInfo() {
        rankedConstraints = createRankedConstraintList();
        indexedRanking = createIndexedRanking();
        rankingChanged = false;
    }

    private static Hierarchy createHierarchy(int expectedSize) {
        double[] rankings = new double[expectedSize];
        Arrays.fill(rankings, Double.NEGATIVE_INFINITY);
        Hierarchy result = new Hierarchy(rankings, 0, null);
        for (int i=0; i < Constraint.getNumberCreated(); i++) {
            Constraint constraint = Constraint.withIndex(i);
            result.add(constraint);
        }
        return result;
    }

    public static Hierarchy createNew() {
        return createHierarchy(ConfigFactory.load().getInt("implementation.expectedNumConstraints"));
    }

    public double getRanking(Constraint c) {
        if (!contains(c)) {
            add(c);
        }
        return rankings[c.getId()];
    }

    public void addConstraint(Constraint c, double value) {
        if (isSampled()) {
            parentHierarchy.addConstraint(c,value);
        }
        if (!contains(c)) {
            putValue(c, value);
           // System.out.println("Added constraint " +c +" to map");
            size++;
           // System.out.println("Size of hierarchy is now" + size());
            rankingChanged = true;
        }
    }


    public boolean contains(Constraint constraint) {
        boolean result = rankings[constraint.getId()] != Double.NEGATIVE_INFINITY;
        if (!result) {
        //    System.out.println("Constraint "+constraint+" not found!");
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
        return rankedConstraints;
    }

    private List<RankedConstraint> createRankedConstraintList() {
        int size = Constraint.getNumberCreated();
        List<RankedConstraint> rankedList = new ArrayList<>(size);
        for (int i=0; i < size; i++) {
            Constraint instance = Constraint.withIndex(i);
            double value = getRanking(instance);
            rankedList.add(RankedConstraint.of(instance, value));

        }
        Collections.sort(rankedList);
        return rankedList;
    }


    public Hierarchy sample(AbstractSampler sampler) {
        double[] sampledRankings = new double[rankings.length];
        List<RankedConstraint> rankedList = new ArrayList<>(size());
        Arrays.fill(sampledRankings,Double.NEGATIVE_INFINITY);
        for (int i=0; i < size(); i++) {
            Constraint instance = Constraint.withIndex(i);
            double value = getRanking(instance);
            sampledRankings[i] = sampler.sampleDouble(value);
            rankedList.add(RankedConstraint.of(instance, sampledRankings[i]));
        }
        Collections.sort(rankedList);
        Hierarchy result = new Hierarchy(sampledRankings, rankedList, this.size(),this);
        return result;
    }

    private IndexedRanking createIndexedRanking() {
        return new IndexedRanking(rankedConstraints);
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
        Iterator<RankedConstraint> rankedConstraintIterator = rankedConstraints.iterator();
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
        for (RankedConstraint rankedConstraint: rankedConstraints) {
            stringBuilder.append(rankedConstraint).append("\n");
        }
        return stringBuilder.toString();
    }

    public Hierarchy copy() {
        double[] copiedRankings = Arrays.copyOf(rankings, rankings.length);
        return new Hierarchy(copiedRankings,copiedRankings.length,parentHierarchy);
    }

    public void normalize(double meanAt) {
        double sum = 0;
        int numConstraints = 0;
        for (double ranking: rankings) {
            if (ranking > Double.NEGATIVE_INFINITY) {
                numConstraints++;
                sum+=ranking;
            }
        }
        double mean = sum / numConstraints;
        for (int i=0; i < rankings.length; i++) {
            if (rankings[i] != Double.NEGATIVE_INFINITY) {
                rankings[i] = rankings[i] - mean + meanAt;
            }
        }
    }
}

