/**
 *
 */
package forms.phon.flat;

import forms.primitives.Subform;
import forms.primitives.boundary.EdgeIndex;
import forms.primitives.boundary.EdgeIndexBuilder;
import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public class PhoneticForm extends PhoneSequence {
    private int hashCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PhoneticForm phones = (PhoneticForm) o;
        return Arrays.equals(getByteArray(), phones.getByteArray());
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public int computeHashCode() {
        return Objects.hash(getLevel(), Arrays.hashCode(getByteArray()));
    }

    /**
     * @param contents
     * @param boundaries
     */
    private PhoneticForm(PhoneSubForm contents, EdgeIndex boundaries) {
        super(contents, boundaries);
        hashCode = computeHashCode();
    }

    public PhoneticForm(PhoneSubForm contents) {
        this(contents, EdgeIndexBuilder.getEmpty(contents.size() + 1));
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.Form#getLevel()
     */
    @Override
    public Level getLevel() {
        return BiPhonSix.getPhoneticLevel();
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
                result += 1;
        }
        return result;
    }

    /**
     * @param psf
     * @return
     */
    public static PhoneticForm fromPhoneSubForm(PhoneSubForm psf) {
        return new PhoneticForm(psf);
    }

    /**
     * @param rightString
     * @return
     */
    public static PhoneticForm createFromString(String rightString) {
        rightString = rightString.replaceAll("\\[","").replaceAll("\\]","");
        PhoneSubForm contents = PhoneSubForm.createFromString(rightString);
        return new PhoneticForm(contents);
    }



}
