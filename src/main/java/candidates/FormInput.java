/**
 *
 */
package candidates;

import forms.Form;

/**
 * An Input containing a Form. Traditional two-level OT candidates
 * would be an example (the Form being an |underlying form|).
 *
 * @author jwvl
 * @date Nov 17, 2014
 */
public class FormInput extends AbstractInput {
    final Form myForm;

    /**
     * Static constructor from Form.
     *
     * @param f Form that serves as Input
     * @return a new FormInput object
     */
    public static FormInput createInstance(Form f) {
        return new FormInput(f);
    }

    /**
     * Private constructor.
     *
     * @param f Form that serves as Input.
     */
    private FormInput(Form f) {
        this.myForm = f;
    }

    public Form getForm() {
        return myForm;
    }

    @Override
    public String toString() {
        return myForm.toBracketedString();
    }


}
