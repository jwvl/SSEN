package gen.constrain;

import forms.phon.flat.UnderlyingForm;
import util.string.ngraph.ByteNGraphMap;

/**
 * Created by janwillem on 01/04/16.
 */
public class UfConstrainer implements GenConstrainer<UnderlyingForm> {

    private final ByteNGraphMap nGraphMap;
    private final int maxUnknownSequence;

    public UfConstrainer(ByteNGraphMap nGraphMap, int maxUnknownSequence) {
        this.nGraphMap = nGraphMap;
        this.maxUnknownSequence = maxUnknownSequence;
    }

    @Override
    public boolean canGenerate(UnderlyingForm form) {
        return nGraphMap.isLegal(form.getByteArray(),maxUnknownSequence);
    }
}
