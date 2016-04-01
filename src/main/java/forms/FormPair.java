/**
 *
 */
package forms;

import grammar.levels.Level;
import graph.Direction;

import java.util.Objects;

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
        return Objects.equals(l, formPair.l) &&
                Objects.equals(r, formPair.r);
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, r);
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
