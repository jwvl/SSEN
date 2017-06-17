package io.tableau;

import candidates.Candidate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import constraints.Constraint;
import constraints.RankedConstraint;
import constraints.hierarchy.reimpl.Hierarchy;
import learn.ViolatedCandidate;
import util.collections.FrequencyTable;

import java.util.List;
import java.util.Set;

/**
 * Created by janwillem on 15/10/2016.
 */
public class SimpleTableauBuilder {
    private final Hierarchy hierarchy;
    private Set<Constraint> constraints;
    private List<Candidate> candidates;
    private FrequencyTable<Candidate,Constraint> violations;
    private Candidate winner;


    public SimpleTableauBuilder(Hierarchy h) {
        this.hierarchy = h;
        this.constraints = Sets.newHashSet();
        this.candidates = Lists.newArrayList();
        this.violations = new FrequencyTable<>();
        this.winner = null;
    }

    public void addViolatedCandidate(ViolatedCandidate violatedCandidate) {
        Candidate candidate = violatedCandidate.getCandidate();
        candidates.add(candidate);
        for (Constraint c: violatedCandidate.getViolated()) {
            constraints.add(c);
            violations.addOne(candidate, c);
        }
    }

    public void setWinner(Candidate winner) {
        this.winner = winner;
    }

    public SimpleTableau build() {
        List<RankedConstraint> rankedConstraintList = Lists.newArrayList();
        for (Constraint c: constraints) {
            RankedConstraint rankedConstraint = RankedConstraint.of(c, hierarchy.getRanking(c));
            rankedConstraintList.add(rankedConstraint);
        }
        Candidate[] candidatesArray = new Candidate[candidates.size()];
        for (int i=0; i < candidatesArray.length; i++) {
            candidatesArray[i] = candidates.get(i);
        }
        return SimpleTableau.create(candidatesArray,rankedConstraintList,winner,violations);
    }



}
