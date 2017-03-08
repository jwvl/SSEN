package forms.phon;

import forms.LinearArrayForm;
import forms.phon.numerical.PhoneticSignal;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

import java.util.Arrays;

/**
 * Created by janwillem on 12/10/2016.
 */
public class AuditoryForm extends LinearArrayForm<PhoneticSignal> {

    private AuditoryForm(PhoneticSignal[] contents) {
        super(contents);
    }

    public static AuditoryForm createFromPhoneticSignals(PhoneticSignal... signals) {
        Arrays.sort(signals);
        return new AuditoryForm(signals);
    }

    @Override
    public Level getLevel() {
        return BiPhonSix.getPhoneticLevel();
    }

    @Override
    public String toBracketedString() {
        return null;
    }
}
