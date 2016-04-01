/**
 *
 */
package forms;

import forms.primitives.Subform;

/**
 * @author jwvl
 * @date 18/10/2014
 */
public abstract class SimplexForm<S extends Subform> implements Form {
    protected S content;

    protected SimplexForm(S s) {
        super();
        content = s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SimplexForm))
            return false;
        @SuppressWarnings("rawtypes")
        SimplexForm other = (SimplexForm) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        return true;
    }

    @Override
    public int getNumSubForms() {
        return 1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see gen.forms.Form#toString()
     */
    public String specificString() {
        return content.toString();
    }

    @Override
    public int countSubform(Subform sf) {
        return content.equals(sf) ? 1 : 0;
    }


}
