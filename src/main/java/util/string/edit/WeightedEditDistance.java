/**
 *
 */
package util.string.edit;

import util.string.CollectionPrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date Dec 9, 2014
 */
public class WeightedEditDistance {
    private int insCost;
    private int delCost;
    private int subCost;
    private int keepCost;
    private static int def_insCost = 1;
    private static int def_delCost = 1;
    private static int def_subCost = 1;
    private static int def_keepCost = 0;
    private char[] a;
    private char[] b;
    private MatrixCell lastBranchedAt;
    private List<String> results;
    private MatrixCell[][] table;

    private enum TransformType {INS, DEL, SUB, KEEP}


    /**
     * @param a
     * @param b
     */
    public WeightedEditDistance(String aString, String bString) {
        this.a = aString.toCharArray();
        this.b = bString.toCharArray();
        table = new MatrixCell[a.length + 1][b.length + 1];
        fillFirstRow();
        printTable();
    }

    private void fillFirstRow() {
        for (int i = 0; i <= b.length; i++) {
            table[0][i] = new MatrixCell(i);
        }
        for (int j = 1; j <= a.length; j++) {
            table[j][0] = new MatrixCell(j);
        }
    }

    private void fillAllCells() {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                int row = i + 1;
                int col = j + 1;
                // To the left:
            }
        }
    }

    private class MatrixCell {
        int minCost;
        List<Transform> transforms;

        private MatrixCell(int cost, List<Transform> transforms) {
            this.minCost = cost;
            this.transforms = transforms;
        }

        private MatrixCell(int cost) {
            this.minCost = cost;
            this.transforms = new ArrayList<Transform>();
        }

        public String toString() {
            return minCost + " " + CollectionPrinter.collectionToString(transforms, ";");
        }

    }

    private class Transform {
        char before;
        char after;
        TransformType type;

        Transform(int row, int col) {
            this(a[row - 1], b[col - 1]);
        }

        Transform(char before, char after) {
            this.before = before;
            this.after = after;
            if (before == after)
                type = TransformType.KEEP;
            else if (before == ' ')
                type = TransformType.INS;
            else if (after == ' ')
                type = TransformType.DEL;
            else
                type = TransformType.SUB;
        }

        int cost() {
            if (type == TransformType.DEL)
                return delCost;
            else if (type == TransformType.INS)
                return insCost;
            else if (type == TransformType.SUB)
                return subCost;
            return keepCost;
        }

        public String toString() {
            return before + "→" + after;
        }

    }

    public void printTable() {
        for (int i = 0; i <= a.length; i++) {
            for (int j = 0; j <= b.length; j++) {
                System.out.print(table[i][j] + "\t");
            }
            System.out.println("");
        }
    }


}
