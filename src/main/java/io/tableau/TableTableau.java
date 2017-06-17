package io.tableau;

import candidates.Candidate;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multiset;
import constraints.Constraint;
import constraints.RankedConstraint;
import learn.ViolatedCandidate;

import java.util.List;

/**
 * Created by janwillem on 15/06/2017.
 */
public class TableTableau {
    private HashBasedTable<Candidate,Constraint,Integer> tableau;
    private final List<RankedConstraint> constraintList;

    public TableTableau(List<RankedConstraint> constraintList) {
        this.constraintList = constraintList;
        this.tableau = HashBasedTable.create();
    }

    public void addViolatedCandidate(ViolatedCandidate vCandidate) {
        Candidate candidate = vCandidate.getCandidate();
        Multiset<Constraint> allViolated = vCandidate.getViolated();
        for (Constraint con: allViolated) {
            tableau.put(candidate,con,allViolated.count(con));
        }
    }

    public void addViolatedCandidates(List<ViolatedCandidate> vCandidates) {
        for (ViolatedCandidate violatedCandidate: vCandidates) {
            addViolatedCandidate(violatedCandidate);
        }
    }


}
