package gen.constrain;

import forms.phon.flat.UnderlyingForm;
import util.string.ngraph.NGraphMap;

/**
 * Created by janwillem on 01/04/16.
 */
public class UfConstrainer implements GenConstrainer<UnderlyingForm> {

    private final NGraphMap nGraphMap;
    private final int maxUnknownSequence;

    public UfConstrainer(NGraphMap nGraphMap, int maxUnknownSequence) {
        this.nGraphMap = nGraphMap;
        this.maxUnknownSequence = maxUnknownSequence;
    }

    @Override
    public boolean canGenerate(UnderlyingForm form) {
        String asString = form.getContents().contentsAsString();
        boolean legal = nGraphMap.isLegal(asString,maxUnknownSequence);
        if (!legal)
            System.out.println("Removing form "+form);
        return nGraphMap.isLegal(asString,maxUnknownSequence);
    }
}
