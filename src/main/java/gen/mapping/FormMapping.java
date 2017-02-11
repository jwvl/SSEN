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
    private final FormPair pair;
    private final static String arrow = " > ";
    private final int hashCode;

    public FormMapping(Form f, Form g) {
        pair = FormPair.of(f,g);
        this.hashCode = computeHashCode();
    }

    public FormPair getFormPair() {
        return pair;
    }

    /**
     * @return
     */
    public Form left() {
        return pair.left();
    }

    public Form right() {
        return pair.right();
    }

    @Override
    public String toString() {
        return left() + arrow + right();
    }

    public Level getLevel() {
        return right().getLevel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormMapping that = (FormMapping) o;

        return (that.left().equals(left()) && that.right().equals(right()));

    }

    @Override
    public int hashCode() {
        return hashCode;
    }


    public int computeHashCode() {
        int result = left() != null ? left().hashCode() : 0;
        result = 31 * result + (right() != null ? right().hashCode() : 0);
        return result;
    }
}
