/**
 *
 */
package candidates;

import forms.Form;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormInput formInput = (FormInput) o;
        return Objects.equals(myForm, formInput.myForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myForm);
    }
}
