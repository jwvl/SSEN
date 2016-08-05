/**
 *
 */
package io.tableau;

import candidates.Candidate;
import com.google.common.collect.Lists;
import constraints.hierarchy.reimpl.Hierarchy;
import learn.ViolatedCandidate;
import constraints.Constraint;
import constraints.hierarchy.violations.ConstraintViolation;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author jwvl
 * @date Sep 2, 2015
 */
public class SimpleTableau implements Tableau {
    final Candidate[] candidates;
    final ConstraintViolation[] constraints;
    final Candidate winner;
    int[][] violationProfiles;

    /**
     * @param candidates
     * @param constraints
     * @param winner
     */
    private SimpleTableau(Candidate[] candidates, ConstraintViolation[] constraints, Candidate winner,
                          int[][] violationProfiles) {
        this.candidates = candidates;
        this.constraints = constraints;
        this.winner = winner;
        this.violationProfiles = violationProfiles;
    }


    /**
     *
     */
    private static int[][] fillViolationProfiles(List<ViolatedCandidate> winnerLoserPair,
                                                 ConstraintViolation[] constraints) {
        int[][] result = new int[winnerLoserPair.size()][constraints.length];
        for (int iCan = 0; iCan < winnerLoserPair.size(); iCan++) {
            ViolatedCandidate iCandidate = winnerLoserPair.get(iCan);
            for (int iCon = 0; iCon < constraints.length; iCon++) {
                Constraint iConstraint = constraints[iCon].getConstraint();
                result[iCan][iCon] = iCandidate.getConstraints().count(iConstraint);
            }
        }
        return result;
    }

    public static SimpleTableau createPairTableau(ViolatedCandidate winner, ViolatedCandidate loser, Hierarchy conmap) {
        Set<ConstraintViolation> allConstraints = new TreeSet<ConstraintViolation>();
        for (Constraint c : winner.getConstraints().elementSet()) {
            ConstraintViolation toAdd = conmap.toConstraintViolation(c);
            allConstraints.add(toAdd);
        }
        for (Constraint c : loser.getConstraints().elementSet()) {
            ConstraintViolation toAdd = conmap.toConstraintViolation(c);
            allConstraints.add(toAdd);
        }
        Candidate[] candidates = new Candidate[]{winner.getCandidate(), loser.getCandidate()};
        ConstraintViolation[] constraints = allConstraints.toArray(new ConstraintViolation[allConstraints.size()]);
        int[][] violations = fillViolationProfiles(Lists.newArrayList(winner, loser), constraints);
        return new SimpleTableau(candidates, constraints, winner.getCandidate(), violations);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.tableau.Tableau#toSeparatedString(java.lang.String)
     */
    @Override
    public String toSeparatedString(String separator) {
        StringBuilder result = new StringBuilder(candidates[0].getInput().toString());
        for (ConstraintViolation con : constraints) {
            result.append(separator).append(con.getConstraint().toString());
        }
        result.append("\n");
        for (ConstraintViolation con : constraints) {
            result.append(separator).append(con.getDisharmony().toString());
        }
        result.append("\n");
        for (int iCan = 0; iCan < candidates.length; iCan++) {
            Candidate can = candidates[iCan];
            result.append(can.outputToBracketedString());
            for (int iCon = 0; iCon < constraints.length; iCon++) {
                result.append(separator);
                int numViolations = violationProfiles[iCan][iCon];
                result.append(intToAsterisks(numViolations));
            }
            result.append("\n");
        }
        return result.toString();
    }

    private String intToAsterisks(int input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input; i++) {
            result.append("*");
        }
        return result.toString();
    }


    public Candidate[] getCandidates() {
        return candidates;
    }


    public ConstraintViolation[] getConstraints() {
        return constraints;
    }


    public Candidate getWinner() {
        return winner;
    }


    public int[][] getViolationProfiles() {
        return violationProfiles;
    }


    public void setViolationProfiles(int[][] violationProfiles) {
        this.violationProfiles = violationProfiles;
    }


}
