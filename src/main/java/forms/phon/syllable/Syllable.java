/**
 *
 */
package forms.phon.syllable;

import com.google.common.collect.Lists;
import forms.phon.Sonority;
import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;

import java.util.Collection;

/**
 * @author jwvl
 * @date May 19, 2015
 */
public class Syllable extends PhoneSubForm {
    private SonorityProfile profile;

    /**
     * @param segments
     */
    protected Syllable(Collection<Phone> segments) {
        super(segments);
        this.profile = SonorityProfile.fromPhones(this);
    }

    protected Syllable(byte[] segments) {
        super(segments);
        this.profile = SonorityProfile.fromBytes(segments);
    }

    public static Syllable createFromString(String string) {
        return new Syllable(Lists.newArrayList(Phone.toPhoneArray(string)));
    }

    public static Syllable createFromPhones(Collection<Phone> segments) {
        return new Syllable(segments);
    }

    public static Syllable createFromBytes(byte[] segments) {
        return new Syllable(segments);
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder("(");
        for (Phone p : elementsAsList()) {
            temp.append(p.getCharValue());
        }
        return temp.append(")σ").toString();
    }

    /**
     * @return the sonorityProfile
     */
    public boolean hasSonorityProfile(SonorityProfile toCheck) {
        Sonority[] sonorities = toCheck.getSonorities();
        if (size() != sonorities.length) {
            return false;
        }
        int i = 0;
        for (byte c : getContents()) {
            if (sonorities[i++] != Sonority.of(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return
     */
    public SonorityProfile getSonorityProfile() {
        return profile;
    }


}
