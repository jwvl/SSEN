/**
 *
 */
package gen.mapping;

import forms.Form;
import forms.FormPair;

/**
 * @author jwvl
 * @date Jul 23, 2015
 */
public class PairMapping extends FormMapping {


    public PairMapping(FormPair fp) {
        super(fp.left(), fp.right());
    }

    private PairMapping(Form left, Form right) {
        super(left, right);
    }

    public static PairMapping createInstance(Form left, Form right) {
        return new PairMapping(left, right);
    }

}
