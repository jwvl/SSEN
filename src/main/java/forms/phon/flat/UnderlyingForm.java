/**
 *
 */
package forms.phon.flat;

import forms.primitives.Subform;
import forms.primitives.boundary.Edge;
import forms.primitives.boundary.EdgeIndex;
import forms.primitives.boundary.EdgeIndexBuilder;
import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import util.arrays.ByteBuilder;

import java.util.List;
import java.util.Objects;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public class UnderlyingForm extends PhoneSequence {

    /**
     * @param boundaries
     */
    private UnderlyingForm(PhoneSubForm concatenatedContents, EdgeIndex boundaries) {
        super(concatenatedContents, boundaries);
    }


    public static UnderlyingForm createInstance(List<PhoneSubForm> subForms, int[] wordBoundariesAtMorphemes) {
        int numPhonemes = 0;
        int wordBoundaryCounter = 0;
        for (PhoneSubForm phoneSubForm : subForms) {
            numPhonemes += phoneSubForm.size();
        }
        int numSubForms = subForms.size();

        EdgeIndex edgeIndex = EdgeIndexBuilder.getEmpty(numPhonemes + 1);
        ByteBuilder builder = new ByteBuilder();
        int cumulativeLength = 0;
        for (int i = 0; i < numSubForms; i++) {
            PhoneSubForm phoneSubForm = subForms.get(i);

            if (wordBoundaryCounter < wordBoundariesAtMorphemes.length
                    && wordBoundariesAtMorphemes[wordBoundaryCounter] == i) {
                edgeIndex.set(Edge.WORD, cumulativeLength);
                wordBoundaryCounter++;
            }
            if (!phoneSubForm.isNull()) {

                cumulativeLength += phoneSubForm.size();
                builder.append(phoneSubForm.getContents());
                if (i < numSubForms - 1) {
                    edgeIndex.set(Edge.MORPHEME, cumulativeLength);
                }
            }
        }

        byte[] result = builder.build();
        PhoneSubForm concatenatedPhones = PhoneSubForm.createFromByteArray(result);
        UnderlyingForm underlyingForm = new UnderlyingForm(concatenatedPhones, edgeIndex);
        return underlyingForm;
    }


    /*
     * (non-Javadoc)
     *
     * @see forms.Form#getLevel()
     */
    @Override
    public Level getLevel() {
        return BiPhonSix.getUnderlyingFormLevel();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.Form#getNumSubForms()
     */
    @Override
    public int getNumSubForms() {
        return size();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.Form#toBracketedString()
     */
    @Override
    public String toBracketedString() {
        // TODO Auto-generated method stub
        return getLevel().getInfo().bracket(toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.Form#countSubform(forms.primitives.Subform)
     */
    @Override
    public int countSubform(Subform sf) {
        int result = 0;
        for (Phone p : getContents()) {
            if (sf.equals(p))
                result++;
        }
        return result;
    }

    /**
     * @return
     */
    public PhoneSubForm[] getPhoneSubforms() {
        byte[][] asByteArrays = getBoundaries().getSubSequences(getByteArray(), Edge.MORPHEME);
        PhoneSubForm[] result = new PhoneSubForm[asByteArrays.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = PhoneSubForm.createFromByteArray(asByteArrays[i]);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UnderlyingForm that = (UnderlyingForm) o;
        return getLevel() == that.getLevel();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLevel());
    }
}
