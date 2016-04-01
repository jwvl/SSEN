/**
 *
 */
package graph;

import forms.Form;
import grammar.levels.Level;

/**
 * A wrapper class that allows distinguishing between Forms in a graph
 * and outside of them.
 *
 * @author jwvl
 * @date Dec 18, 2014
 */
public class EmbeddedForm {
    private Form f;
    private Level l;

    /**
     * @param f
     * @param l
     */
    private EmbeddedForm(Form f, Level l) {
        this.f = f;
        this.l = l;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((f == null) ? 0 : f.hashCode());
        result = prime * result + ((l == null) ? 0 : l.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EmbeddedForm))
            return false;
        EmbeddedForm other = (EmbeddedForm) obj;
        if (f == null) {
            if (other.f != null)
                return false;
        } else if (!f.equals(other.f))
            return false;
        if (l == null) {
            if (other.l != null)
                return false;
        } else if (!l.equals(other.l))
            return false;
        return true;
    }


}
