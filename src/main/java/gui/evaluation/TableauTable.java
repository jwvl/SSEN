/**
 *
 */
package gui.evaluation;

import candidates.Candidate;
import io.tableau.SimpleTableau;
import ranking.violations.ConstraintViolation;

import javax.swing.*;

/**
 * @author jwvl
 * @date Sep 9, 2015
 */
public class TableauTable extends JTable {


    /**
     * @param arg0
     * @param arg1
     */
    private TableauTable(Object[][] arg0, Object[] arg1) {
        super(arg0, arg1);
    }

    /**
     *
     */
    private static final long serialVersionUID = 4170160947859549903L;

    public static TableauTable createFromSimpleTableau(SimpleTableau input) {
        ConstraintViolation[] constraints = input.getConstraints();
        int[][] violations = input.getViolationProfiles();
        Candidate[] candidates = input.getCandidates();
        String[] columns = new String[constraints.length + 1];
        columns[0] = candidates[0].getInput().toString();
        for (int i = 0; i < constraints.length; i++) {
            columns[i + 1] = constraints[i].getConstraint().toString();
        }
        String[][] contents = new String[candidates.length + 1][columns.length];

        for (int i = 0; i < constraints.length; i++) {
            contents[0][i + 1] = constraints[i].getDisharmony().toString();
        }
        for (int i = 0; i < candidates.length; i++) {
            contents[i + 1][0] = candidates[i].outputToBracketedString();
            for (int j = 0; j < constraints.length; j++) {
                contents[i + 1][j + 1] = intToAsterisks(violations[i][j]);
            }
        }
        return new TableauTable(contents, columns);

    }

    private static String intToAsterisks(int input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input; i++) {
            result.append("*");
        }
        return result.toString();
    }

}
