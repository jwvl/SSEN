/**
 *
 */
package io.tableau;

import candidates.Candidate;
import constraints.Constraint;
import constraints.RankedConstraint;
import util.collections.FrequencyTable;

import java.util.List;

/**
 * @author jwvl
 * @date Sep 2, 2015
 */
public class SimpleTableau implements Tableau {
    final Candidate[] candidates;
    final List<RankedConstraint> constraints;
    final FrequencyTable<Candidate,Constraint> violations;
    final Candidate winner;

    /**
     * @param candidates
     * @param constraints
     * @param winner
     */
    private SimpleTableau(Candidate[] candidates, List<RankedConstraint> constraints, Candidate winner, FrequencyTable<Candidate,Constraint> violations) {
        this.candidates = candidates;
        this.constraints = constraints;
        this.winner = winner;
        this.violations = violations;
    }

    public static SimpleTableau create(Candidate[] candidates, List<RankedConstraint> constraints, Candidate winner, FrequencyTable<Candidate,Constraint> frequencyTable) {
        return new SimpleTableau(candidates, constraints, winner, frequencyTable);
    }



    /*
     * (non-Javadoc)
     *
     * @see io.tableau.Tableau#toSeparatedString(java.lang.String)
     */
    @Override
    public String toSeparatedString(String separator) {
        StringBuilder result = new StringBuilder(candidates[0].getInput().toString());
        for (RankedConstraint rankedConstraint : constraints) {
            result.append(separator).append(rankedConstraint.getConstraint().toString());
        }
        result.append("\n");
        for (RankedConstraint rankedConstraint : constraints) {
            result.append(separator).append(rankedConstraint.getRanking());
        }
        result.append("\n");
        for (int iCan = 0; iCan < candidates.length; iCan++) {
            Candidate can = candidates[iCan];
            result.append(can.outputToBracketedString());
            for (int iCon = 0; iCon < constraints.size(); iCon++) {
                Constraint cons = constraints.get(iCon).getConstraint();
                result.append(separator);
                int numViolations = violations.getCount(can, cons);
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


    public Candidate getWinner() {
        return winner;
    }



}
