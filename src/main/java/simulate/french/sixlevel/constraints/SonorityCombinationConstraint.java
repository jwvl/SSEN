package simulate.french.sixlevel.constraints;

import constraints.FormConstraint;
import forms.phon.Sonority;
import forms.phon.flat.PhoneSequence;
import forms.phon.syllable.SonorityProfile;
import forms.primitives.segment.Phone;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

/**
 * Created by janwillem on 09/05/2017.
 * TODO finish
 */
public class SonorityCombinationConstraint extends FormConstraint<PhoneSequence> {

    private final SonorityProfile offendingProfile;

    protected SonorityCombinationConstraint(Level rightLevel, SonorityProfile offendingProfile) {
        super(rightLevel);
        this.offendingProfile = offendingProfile;
        System.out.println("Created combination constraint *"+offendingProfile.toString());
    }

    public SonorityCombinationConstraint(SonorityProfile offenders) {
        this(BiPhonSix.getPhoneticLevel(), offenders);
    }

    @Override
    public int getNumViolations(PhoneSequence phones) {
        int count = 0;
        byte[] bytes = phones.getByteArray();
        int stopat = (1+bytes.length) - offendingProfile.size();
        for (int i = -1; i <= stopat; i++) {
            boolean found = true;
            for (Sonority s: offendingProfile.getSonorities()) {
                if (getByteAt(i, bytes) != s) {
                    found = false;
                    break;
                }
            }
            if (found) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean caches() {
        return false;
    }

    private Sonority getByteAt(int index, byte[] sequence) {
        if (index < 0 || index >= sequence.length) {
            return Sonority.X;
        } else {
            return Phone.getByCode(sequence[index]).getSonority();
        }
    }

}
