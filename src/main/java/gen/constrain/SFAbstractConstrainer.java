package gen.constrain;

import forms.phon.flat.SurfaceForm;

/**
 * Created by janwillem on 28/06/2017.
 */
public class SFAbstractConstrainer implements GenConstrainer<SurfaceForm> {

    private byte[] abstractPhones;

    public SFAbstractConstrainer(byte[] abstractPhones) {
        this.abstractPhones = abstractPhones;
    }

    @Override
    public boolean canGenerate(SurfaceForm form) {
        byte[] asByteArray = form.getByteArray();
        for (byte phone: asByteArray) {
            for (byte b : abstractPhones) {
                if (phone == b) {
                    return false;
                }
            }
        }
        return true;
    }
}
