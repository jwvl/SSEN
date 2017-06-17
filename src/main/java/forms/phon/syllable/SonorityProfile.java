/**
 *
 */
package forms.phon.syllable;

import forms.phon.Sonority;
import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;

import java.util.Arrays;

/**
 * @author jwvl
 * @date Jun 21, 2015
 */
public class SonorityProfile {
    private final Sonority[] contents;

    /**
     * @param contents
     */
    protected SonorityProfile(Sonority[] contents) {
        this.contents = contents;
    }

    public static SonorityProfile getInstance(Sonority[] contents) {
        return new SonorityProfile(contents);
    }

    public static SonorityProfile fromString(String input) {
        Sonority[] contents = new Sonority[input.length()];
        for (int i = 0; i < input.length(); i++) {
            contents[i] = Sonority.valueOf(String.valueOf(input.charAt(i)));
        }
        return getInstance(contents);
    }

    public static SonorityProfile fromSonorities(Sonority[] contents) {
        return new SonorityProfile(contents);
    }

    public static SonorityProfile fromBytes(byte[] bytes) {
        int i = 0;
        Sonority[] contents = new Sonority[bytes.length];
        for (byte b : bytes) {
            contents[i++] = Phone.getByCode(b).getSonority();
        }
        return getInstance(contents);
    }

    public static SonorityProfile fromPhones(PhoneSubForm phoneSubForm) {
        int i = 0;
        Sonority[] contents = new Sonority[phoneSubForm.size()];
        for (Phone p : phoneSubForm) {
            contents[i++] = p.getSonority();
        }
        return getInstance(contents);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(contents);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SonorityProfile))
            return false;
        SonorityProfile other = (SonorityProfile) obj;
        return Arrays.deepEquals(contents, other.contents);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Sonority sonority : contents) {
            result.append(sonority.toString());
        }
        return result.toString();
    }

    /**
     * @return
     */
    public Sonority[] getSonorities() {
        return contents;
    }

    /**
     * @return
     */
    public static SonorityProfile[] getAllowedProfiles() {
        String[] allowed = {"V", "VC", "VCC", "CV", "CVC", "CVCC", "CCV", "CCVC", "CCVCC","CCCV","CCCVC","CCCVCC"};
        SonorityProfile[] result = new SonorityProfile[allowed.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = SonorityProfile.fromString(allowed[i]);
        }
        return result;
    }

    public int size() {
        return contents.length;
    }

    public int peakLocation() {
        int maxSonority = -1;
        int argMax = -1;
        for (int i = 0; i < contents.length; i++) {
            int sonorityValue = contents[i].getSonorityValue();
            if (sonorityValue > maxSonority) {
                maxSonority = sonorityValue;
                argMax = i;
            }
        }
        return argMax;
    }


}
