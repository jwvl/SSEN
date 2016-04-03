package forms.phon.syllable;

import forms.phon.Sonority;
import forms.primitives.boundary.Edge;
import forms.primitives.boundary.EdgeIndex;
import forms.primitives.boundary.EdgeIndexBuilder;
import forms.primitives.segment.PhoneSubForm;
import gen.rule.string.Side;
import util.arrays.ByteArrayUtils;

import java.util.*;

/**
 * Created by janwillem on 01/04/16.
 */
public class MaxOnsetSyllabifier implements ISyllabifier{
    private final static Set<Sonority> defaultLegalNuclei = createDefaultLegalNuclei();
    private final Set<Sonority> legalNuclei;
    private final OnsetSet onsets;

    private MaxOnsetSyllabifier(Set<Sonority> legalNuclei, OnsetSet onsets) {
        this.legalNuclei = legalNuclei;
        this.onsets = onsets;
    }

    public MaxOnsetSyllabifier(OnsetSet onsets) {
        this(defaultLegalNuclei,onsets);
    }

    /**
     * @param asByteArray
     * @return
     */
    public int[] findNuclei(byte[] asByteArray) {
        List<Integer> result = new ArrayList<Integer>(asByteArray.length);
        for (int i = 0; i < asByteArray.length; i++) {
            Sonority iSonority = Sonority.of(asByteArray[i]);
            if (legalNuclei.contains(iSonority)) {
                result.add(i);
            }
        }
        int[] array = new int[result.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = result.get(i);
        }
        return array;
    }

    /**
     * @return
     */
    private static Set<Sonority> createDefaultLegalNuclei() {
        Set<Sonority> singleton = new HashSet<Sonority>();
        singleton.add(Sonority.V);
        return singleton;
    }

    @Override
    public List<EdgeIndex> getSyllabifications(byte[] asByteArray) {
        EdgeIndex edgeIndex = EdgeIndexBuilder.getEmpty(asByteArray.length+1);
        PhoneSubForm psf = PhoneSubForm.createFromByteArray(asByteArray);
        int[] nucleusIndices = findNuclei(asByteArray);
        int numNuclei = nucleusIndices.length;
        if (numNuclei < 1) {
            System.out.println("Huh?");
        }
        for (int i=0; i < numNuclei-1; i++) {
            int startSearch = nucleusIndices[i]+1;
            int endSearch = nucleusIndices[i+1];
            int maxOnsetAt = findMaxOnset(startSearch, endSearch, asByteArray);
            edgeIndex.set(Edge.SYLLABLE, maxOnsetAt);
        }
        edgeIndex.setBoundaryAtLimit(Edge.SYLLABLE, Side.EITHER);
        return Collections.singletonList(edgeIndex);
    }

    @Override
    public SyllabifierType getType() {
        return SyllabifierType.MAX_ONSET;
    }

    private int findMaxOnset(int startSearch, int endSearch, byte[] asByteArray) {
        for (int i=startSearch; i < endSearch; i++) {
            byte[] possibleOnset = ByteArrayUtils.getSubArray(asByteArray,i,endSearch);
            if (onsets.contains(possibleOnset)) {
                return i;
            }
        }
        return endSearch;
    }
}
