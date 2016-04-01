/**
 *
 */
package gen.rule.edit;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import forms.phon.PhoneTransform;
import forms.primitives.Submapping;
import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;
import util.collections.Couple;

import java.util.Collections;
import java.util.List;

/**
 * @author jwvl
 * @date May 22, 2015
 */
public class SimplePhoneStringDistance {
    private double insertCost;
    private double deleteCost;
    private double changeCost;
    private final double DEFAULT_INSERT_COST = 1d;
    private final double DEFAULT_DELETE_COST = 1d;
    private final double DEFAULT_CHANGE_COST = 1d;
    private byte[] from;
    private byte[] to;
    double[][] matrix;
    private List<PhoneTransform> editScript;

    private SimplePhoneStringDistance(PhoneSubForm from, PhoneSubForm to) {
        this.insertCost = DEFAULT_INSERT_COST;
        this.deleteCost = DEFAULT_DELETE_COST;
        this.changeCost = DEFAULT_CHANGE_COST;
        this.from = from.getContents();
        this.to = to.getContents();
        editScript = Lists.newArrayList();
        initializeMatrix();
        fillMatrix();
        // printMatrix();
        backTrack();
    }

    public static SimplePhoneStringDistance createInstance(PhoneSubForm a, PhoneSubForm b) {
        return new SimplePhoneStringDistance(a, b);
    }

    public static SimplePhoneStringDistance createInstance(String a, String b) {
        PhoneSubForm aPS = PhoneSubForm.createFromString(a);
        PhoneSubForm bPS = PhoneSubForm.createFromString(b);
        return new SimplePhoneStringDistance(aPS, bPS);
    }

    private static double minimum(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    private static int argMin(double a, double b, double c) {
        if (a < b) {
            if (a < c)
                return 0;
            else
                return 2;
        } else {
            if (b < c)
                return 1;
            else
                return 2;
        }
    }

    private void initializeMatrix() {
        int width = from.length + 1;
        int height = to.length + 1;
        matrix = new double[height][width];
        for (int i = 0; i < width; i++) {
            matrix[0][i] = (double) i;
        }
        for (int i = 0; i < height; i++) {
            matrix[i][0] = (double) i;
        }
    }

    private void fillMatrix() {
        for (int i = 1; i <= to.length; i++)
            for (int j = 1; j <= from.length; j++) {
                double match = (to[i - 1] == from[j - 1]) ? 0.0 : changeCost;
                double ins = matrix[i - 1][j] + insertCost;
                double del = matrix[i][j - 1] + deleteCost;
                double cha = matrix[i - 1][j - 1] + match;
                matrix[i][j] = minimum(ins, del, cha);

            }
    }

    public double getDistance() {
        return matrix[to.length][from.length];
    }

    private void backTrack() {
        double ins, del, cha;
        byte target, focus;
        int i = to.length;
        int j = from.length;
        // Phone currentRight = Phone.R_EDGE;
        // Phone currentLeft =
        while (i > 0 || j > 0) {
            ins = i == 0 ? Double.MAX_VALUE : matrix[i - 1][j];
            del = j == 0 ? Double.MAX_VALUE : matrix[i][j - 1];
            cha = (j == 0 || i == 0) ? Double.MAX_VALUE : matrix[i - 1][j - 1];
            int choice = argMin(ins, del, cha);
            if (choice == 0) {
                i -= 1;
                focus = Phone.NULL.getId();
                target = to[i];

            } else if (choice == 1) {
                j -= 1;
                focus = from[j];
                target = Phone.NULL.getId();
            } else {
                i--;
                j--;
                byte currFrom = from[j];
                byte currTo = to[i];
                focus = currFrom;
                target = currTo;

            }
            if (focus != target) {
                addToScript(focus, target);
            }
        }
        Collections.reverse(editScript);
    }

    public void printMatrix() {
        System.out.print("        ");
        // First print top row
        for (int i = 0; i < from.length; i++) {
            Phone current = Phone.getByCode(from[i]);
            System.out.print(Strings.padEnd(current.toString(), 4, ' '));
        }
        System.out.println();

        for (int i = 0; i <= to.length; i++) {
            if (i == 0) {
                System.out.print("    ");
            } else {
                Phone toPrint = Phone.getByCode(to[i - 1]);
                System.out.print(Strings.padEnd(toPrint.toString(), 4, ' '));
            }
            for (int j = 0; j <= from.length; j++) {
                System.out.print(String.format("%.1f ", matrix[i][j]));
            }
            System.out.println();
        }
    }

    // Returns
    public boolean isEdge(int i, int j) {
        return (i == 0 || i == to.length || j == 0 || j == from.length);
    }

    public List<PhoneTransform> getEditScript() {
        return editScript;
    }

    public String editScriptToString() {
        StringBuilder before = new StringBuilder();
        StringBuilder after = new StringBuilder();
        for (PhoneTransform transform : editScript) {
            before.append(transform.left());
            after.append(transform.right());
        }
        String beforeString = before.toString();
        String afterString = after.toString();
        Couple<String> result = Couple.of(beforeString, afterString);
        return String.format("Edit script from %s to %s: %s\n", from, to, result);
    }

    public double getInsertCost() {
        return insertCost;
    }

    public double getDeleteCost() {
        return deleteCost;
    }

    public double getChangeCost() {
        return changeCost;
    }

    public void setInsertCost(double insertCost) {
        this.insertCost = insertCost;
    }

    public void setDeleteCost(double deleteCost) {
        this.deleteCost = deleteCost;
    }

    public void setChangeCost(double changeCost) {
        this.changeCost = changeCost;
    }

    /**
     * @param mapping
     * @return
     */
    public static SimplePhoneStringDistance createInstance(Submapping<PhoneSubForm, PhoneSubForm> mapping) {
        return createInstance(mapping.left(), mapping.right());
    }

    public void addToScript(byte from, byte to) {
        editScript.add(PhoneTransform.getInstance(new byte[]{from, to}));
    }

}
