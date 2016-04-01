/**
 *
 */
package forms.phon.syllable;

import forms.primitives.Subform;


/**
 * @author jwvl
 * @date May 19, 2015
 * This class merely functions as a temporary SubForm to make syllabifications.
 */
public class SyllabicNode extends Subform {

    private static SyllabicNode INSTANCE = new SyllabicNode();

    public static SyllabicNode getInstance() {
        return INSTANCE;
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof SyllabicNode);
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#hashCode()
     */
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#isNull()
     */
    @Override
    public boolean isNull() {
        return false;
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#size()
     */
    @Override
    public int size() {
        return 0;
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#toString()
     */
    @Override
    public String toString() {
        return "σ";
    }

}
