/**
 *
 */
package forms.phon.numerical;

import forms.primitives.Subform;
import forms.primitives.feature.ScaleFeature;

/**
 * @author jwvl
 * @date 03/12/2015
 */
public class PhoneticElement extends Subform {
    private ScaleFeature feature;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((feature == null) ? 0 : feature.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PhoneticElement))
            return false;
        PhoneticElement other = (PhoneticElement) obj;
        if (feature == null) {
            if (other.feature != null)
                return false;
        } else if (!feature.equals(other.feature))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#isNull()
     */
    @Override
    public boolean isNull() {
        return feature.isNull();
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#size()
     */
    @Override
    public int size() {
        return 1;
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#toString()
     */
    @Override
    public String toString() {
        return feature.toString();
    }

}
