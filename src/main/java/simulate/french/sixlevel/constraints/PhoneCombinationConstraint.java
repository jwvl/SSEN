package simulate.french.sixlevel.constraints;

import constraints.FormConstraint;
import forms.phon.Sonority;
import forms.phon.flat.PhoneSequence;
import forms.primitives.segment.Phone;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by janwillem on 09/05/2017.
 */
public class PhoneCombinationConstraint extends FormConstraint<PhoneSequence> {

    private final char[] offenders;

    private final Pattern toSearch;

    protected PhoneCombinationConstraint(Level rightLevel, byte[] offenders) {
        super(rightLevel);
        this.offenders = Phone.decode(offenders).toCharArray();
        System.out.println("Created combination constraint *"+Phone.decode(offenders));
        toSearch = buildPattern();
    }

    private Pattern buildPattern() {
        String classes ="XCPFNLJV";
        StringBuilder builder= new StringBuilder();
        for (char c: offenders) {
            int index = classes.indexOf(c) ;
            if (index >=0) {
                Sonority s = Sonority.valueOf(classes.charAt(index));
                    builder.append(("["));
                    builder.append(s.getAllPhones());
                    builder.append("]");
                } else {
                    builder.append(c);
                }
            }
        return Pattern.compile(builder.toString());
    }

    public PhoneCombinationConstraint(byte[] offenders) {
        this(BiPhonSix.getPhoneticLevel(), offenders);
    }

    public PhoneCombinationConstraint(String offenders) {
        this(BiPhonSix.getPhoneticLevel(), Phone.encode(offenders));
    }

    @Override
    public int getNumViolations(PhoneSequence phones) {
        String asString = phones.toString();
        int count = 0;
        Matcher matcher = toSearch.matcher(asString);
        while (matcher.find()) {
            count++;
        }
        return count;
    }


    @Override
    public String toString() {
        return "*CUE"+offenders.toString();
    }

    @Override
    public boolean caches() {
        return false;
    }


    public String getOffenders() {
        return offenders.toString();
    }


}
