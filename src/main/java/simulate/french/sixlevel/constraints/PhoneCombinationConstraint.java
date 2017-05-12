package simulate.french.sixlevel.constraints;

import constraints.FormConstraint;
import forms.phon.flat.PhoneSequence;
import forms.primitives.segment.Phone;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

/**
 * Created by janwillem on 09/05/2017.
 */
public class PhoneCombinationConstraint extends FormConstraint<PhoneSequence> {

    private final byte[] offenders;

    protected PhoneCombinationConstraint(Level rightLevel, byte[] offenders) {
        super(rightLevel);
        this.offenders = offenders;
        System.out.println("Created combination constraint *"+Phone.decode(offenders));
    }

    public PhoneCombinationConstraint(byte[] offenders) {
        this(BiPhonSix.getPhoneticLevel(), offenders);
    }

    @Override
    public int getNumViolations(PhoneSequence phones) {
        int count = 0;
        byte[] bytes = phones.getByteArray();
        int stopat = (1+bytes.length) - offenders.length;
        for (int i = -1; i <= stopat; i++) {
            boolean found = true;
            for (byte b: offenders) {
                if (getByteAt(i, bytes) != b) {
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

    private byte getByteAt(int index, byte[] sequence) {
        if (index < 0 || index >= sequence.length) {
            return Phone.NULL.getId();
        } else {
            return sequence[index];
        }
    }

}
