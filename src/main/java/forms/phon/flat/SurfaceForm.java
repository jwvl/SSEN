/**
 *
 */
package forms.phon.flat;

import forms.phon.Sonority;
import forms.phon.syllable.SonorityProfile;
import forms.primitives.Subform;
import forms.primitives.boundary.BitsetEdgeIndex;
import forms.primitives.boundary.Edge;
import forms.primitives.boundary.EdgeIndex;
import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public class SurfaceForm extends PhoneSequence {


    /**
     * @param contents
     * @param boundaries
     */
    private SurfaceForm(PhoneSubForm contents, EdgeIndex boundaries) {
        super(contents, boundaries);

    }

    /**
     * @param contentsAsBytes
     * @param boundaries
     */
    private SurfaceForm(byte[] contentsAsBytes, EdgeIndex boundaries) {
        this(PhoneSubForm.createFromByteArray(contentsAsBytes), boundaries);
    }

    public static SurfaceForm fromString(String value) {
        StringBuilder strippedString = new StringBuilder();
        EdgeIndex edgeIndices = new BitsetEdgeIndex(value.length());
        int cursor = 0;
        for (char c : value.toCharArray()) {
            if (Edge.isEdgeSymbol(c)) {
                Edge edge = Edge.fromChar(c);
                if (edge == Edge.SYLLABLE) {
                    edgeIndices.set(edge, cursor);
                }
            } else {
                strippedString.append(c);
                cursor++;
            }
        }
        PhoneSubForm phoneSubForm = PhoneSubForm.createFromString(strippedString.toString());
        return new SurfaceForm(phoneSubForm, edgeIndices);
    }


    public static SurfaceForm createInstance(PhoneSubForm contents, EdgeIndex boundaries) {
        return new SurfaceForm(contents, boundaries);
    }


    /* (non-Javadoc)
     * @see forms.Form#getLevel()
     */
    @Override
    public Level getLevel() {
        return BiPhonSix.getSurfaceFormLevel();
    }

    /* (non-Javadoc)
     * @see forms.Form#getNumSubForms()
     */
    @Override
    public int getNumSubForms() {
        return getContents().size();
    }

    /* (non-Javadoc)
     * @see forms.Form#toBracketedString()
     */
    @Override
    public String toBracketedString() {
        return getLevel().getInfo().bracket(this.toString());
    }

    /* (non-Javadoc)
     * @see forms.Form#countSubform(forms.primitives.Subform)
     */
    @Override
    public int countSubform(Subform sf) {
        int result = 0;
        for (Phone p : getContents()) {
            if (sf.equals(p)) {
                result += 1;
            }
        }
        return result;
    }


    /**
     * @return
     */
    public SonorityProfile[] getSonorityProfiles() {
        return createSonorityProfiles();
    }

    /**
     * @param transformed
     * @param edgeIndex
     * @return
     */
    public static SurfaceForm createInstance(byte[] transformed, EdgeIndex edgeIndex) {
        return new SurfaceForm(transformed, edgeIndex);
    }


    private static Sonority[] getSubArray(Sonority[] original, int from, int to) {
        int newLength = to - from;
        Sonority[] result = new Sonority[newLength];
        System.arraycopy(original, from, result, 0, newLength);
        return result;
    }

    private SonorityProfile[] createSonorityProfiles() {
        Edge edgeType = Edge.SYLLABLE;
        EdgeIndex edgeIndex = getBoundaries();
        int numSubSequences = edgeIndex. getNumSubsequences(edgeType);
        int startAt = 0;
        int endAt = edgeIndex.nextSetBit(edgeType, 1);

        SonorityProfile[] result = new SonorityProfile[numSubSequences];
        for (int i = 0; i < numSubSequences; i++) {
            result[i] = SonorityProfile.getInstance(getSonorities(startAt, endAt));
            startAt = endAt;
            endAt = edgeIndex.nextSetBit(edgeType, endAt + 1);
            if (endAt < 0) {
                endAt = size();
            }

        }
        return result;
    }

    public Sonority[] getSonorities(int from, int to) {
        byte[] contents = getByteArray();
        Sonority[] result = new Sonority[to-from];
        for (int i = 0; i < result.length; i++) {
            result[i] = Sonority.of(contents[from+i]);
        }
        return result;
    }


}
