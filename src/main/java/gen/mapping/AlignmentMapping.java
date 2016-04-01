/**
 *
 */
package gen.mapping;

import forms.Form;
import forms.LinearListForm;
import forms.primitives.Subform;
import gen.alignment.Alignment;

/**
 * @author jwvl
 * @date Jul 22, 2015
 */
public class AlignmentMapping<S extends Subform, T extends Subform> extends FormMapping {

    private Alignment<S, T> alignment;

    /**
     * @param f
     * @param g
     */
    public AlignmentMapping(LinearListForm<S> f, LinearListForm<T> g) {
        super(f, g);
        this.alignment = null;
    }

    public AlignmentMapping(Form left, Form right, Alignment<S, T> alignment) {
        super(left, right);
        this.alignment = alignment;
    }


    public Alignment<S, T> getAlignment() {
        return alignment;
    }


    public void setAlignment(Alignment<S, T> alignment) {
        this.alignment = alignment;
    }

}
