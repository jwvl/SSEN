/**
 *
 */
package forms;

import grammar.levels.Level;
import graph.Direction;

/**
 * Really just a special case of a Form-Tuple with two members; but FormPairs
 * represent a local (interlevel) mapping and as such are an important entity in
 * the framework.
 *
 * @author jwvl
 * @date 29/10/2014
 */
public class FormPair {

    final Form l, r;

    protected FormPair(Form a, Form b) {
        this.l = a;
        this.r = b;
    }

    public static FormPair of(Form a, Form b) {
        FormPair result = new FormPair(a, b);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormPair formPair = (FormPair) o;

        if (l != null ? !l.equals(formPair.l) : formPair.l != null) return false;
        return r != null ? r.equals(formPair.r) : formPair.r == null;
    }

    @Override
    public int hashCode() {
        int result = l != null ? l.hashCode() : 0;
        result = 31 * result + (r != null ? r.hashCode() : 0);
        return result;
    }

    public Form left() {
        return l;
    }

    public Form right() {
        return r;
    }

    @Override
    public String toString() {
        return getString();
    }

    public String getString() {
        return new StringBuilder().append("(").append(l).append(", ").append(r)
                .append(")").toString();

    }

    public FormPair getUnlabeled(Direction direction) {
        if (direction != Direction.LEFT) {
            return FormPair.of(left(), GraphForm.getSinkInstance());
        } else {
            return FormPair.of(GraphForm.getSourceInstance(), right());
        }
    }

    public static FormPair createUnlabeled(Form input, Direction direction) {
        if (direction != Direction.LEFT) {
            return FormPair.of(input,GraphForm.getSinkInstance());
        } else {
            return FormPair.of(GraphForm.getSourceInstance(), input);
        }
    }

    /**
     * @return
     */
    public Level getLevel() {
        return l.getLevel();
    }

    public boolean isUnlabeled() {
        return left() != GraphForm.getSinkInstance() && right() != GraphForm.getSourceInstance();
    }
}
