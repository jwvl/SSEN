package candidates;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import forms.Form;
import forms.FormChain;
import forms.GraphForm;
import gen.mapping.FormMapping;
import gen.rule.string.Side;

import java.util.List;
import java.util.Objects;

/**
 * A Candidate in the MLCG framework is a tuple of Forms resulting from some
 * Input. The object saves both, and can be used to iterate over the forms and
 * connections it contains.
 *
 * @author jwvl
 * @date Jan 10, 2015
 */
public class Candidate {
    private AbstractInput input;
    private FormChain output;

    /**
     * Initiates a Candidate with the provided Input and FormTuple (in OT terms,
     * its output)
     *
     * @param i Input
     * @param o FormTuple (
     * @return
     */
    public static Candidate fromInputAndChain(AbstractInput i, FormChain o) {
        return new Candidate(i, o);
    }

    public static Candidate fromInputAndForms(AbstractInput i, Form... forms) {
        List<Form> formList = Lists.newArrayList(forms);
        FormChain chain = FormChain.fromFormstoSimpleMappings(formList);
        return fromInputAndChain(i,chain);
    }

    /**
     * Private constructor containing all fields
     *
     * @param input  Input object
     * @param output Output FormTuple
     */
    private Candidate(AbstractInput input, FormChain output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Returns the output Forms as an array of Forms.
     *
     * @return An Array of Forms representing the output for this candidate.
     */
    public Form[] getForms() {
        return output.getContents();
    }

    /**
     * @return The list of FormPairs contained in this candidate.
     */
    public FormMapping[] getMappings() {
        return output.getMappings();
    }


    /**
     * @return Length of FormTuple in output.
     */
    public int length() {
        return output.size();
    }

    public String outputToBracketedString() {
        return output.contentsToBracketedString("↦");
    }

    public String toString() {
        StringBuilder result = new StringBuilder(input.toString());
        result.append(" ⇒ ");
        result.append(output.contentsToBracketedString(" "));
        return result.toString();
    }

    public boolean containsForm(Form toCheck) {
        for (Form f : output.getContents()) {
            if (f.equals(toCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     */
    public AbstractInput getInput() {
        return input;
    }

    public static Ordering<Candidate> AlphabeticOrdering = new Ordering<Candidate>(
    ) {
        @Override
        public int compare(Candidate arg0, Candidate arg1) {
            return arg0.toString().compareTo(arg1.toString());
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(input, candidate.input) &&
                Objects.equals(output, candidate.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, output);
    }

    public Form getEndForm(Side side, boolean keepGraphForms) {
        Form[] contents = output.getContents();
        if (side == Side.RIGHT) {
            Form rightMost = contents[contents.length-1];
            if (rightMost instanceof GraphForm) {
                return contents[contents.length-2];
            }
            return rightMost;
        } else if (side == Side.LEFT) {
            Form leftMost = contents[0];
            if (leftMost instanceof GraphForm) {
                return contents[1];
            }
            return leftMost;
        } else return null;
    }
}
