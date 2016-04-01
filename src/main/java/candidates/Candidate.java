package candidates;

import com.google.common.collect.Ordering;
import forms.Form;
import forms.FormChain;
import gen.mapping.FormMapping;

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

}
