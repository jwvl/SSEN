/**
 *
 */
package gen.mapping;

import forms.Form;
import forms.FormPair;
import grammar.levels.Level;

/**
 * @author jwvl
 * @date Jul 8, 2015
 */
public abstract class FormMapping {
    private final Form left;
    private final Form right;
    private final static String arrow = " > ";

    public FormMapping(Form f, Form g) {
        this.left = f;
        this.right = g;
    }

    public FormPair getFormPair() {
        return FormPair.of(left, right);
    }

    /**
     * @return
     */
    public Form left() {
        return left;
    }

    public Form right() {
        return right;
    }

    @Override
    public String toString() {
        return left + arrow + right;
    }

    public Level getLevel() {
        return right.getLevel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormMapping that = (FormMapping) o;

        if (left != null ? !left.equals(that.left) : that.left != null) return false;
        return right != null ? right.equals(that.right) : that.right == null;

    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
