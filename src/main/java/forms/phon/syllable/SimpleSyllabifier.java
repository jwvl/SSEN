/**
 *
 */
package forms.phon.syllable;

import forms.phon.Sonority;
import forms.primitives.boundary.Edge;
import forms.primitives.boundary.EdgeIndex;
import forms.primitives.boundary.EdgeIndexBuilder;
import gen.rule.string.Side;
import util.arrays.Range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public class SimpleSyllabifier implements ISyllabifier {
    private final Set<Sonority> legalNuclei;
    private final static Set<Sonority> defaultLegalNuclei = createDefaultLegalNuclei();

    /**
     * @param legalNuclei
     */
    private SimpleSyllabifier(Set<Sonority> legalNuclei) {
        this.legalNuclei = legalNuclei;
    }

    /**
     *
     */
    public SimpleSyllabifier() {
        this(defaultLegalNuclei);
    }


    /**
     * @return
     */
    private static Set<Sonority> createDefaultLegalNuclei() {
        Set<Sonority> singleton = new HashSet<Sonority>();
        singleton.add(Sonority.V);
        return singleton;
    }


    public List<EdgeIndex> getSyllabifications(byte[] asByteArray) {
        // Step 1: find nuclei
        int[] nucleusIndices = findNuclei(asByteArray);
        int numNuclei = nucleusIndices.length;
        if (numNuclei < 1) {
            System.out.println("Huh?");
        }
        Range[] ranges = new Range[numNuclei - 1];
        int size = 1;
        for (int i = 0; i < numNuclei - 1; i++) {
            int earliest = nucleusIndices[i] + 1;
            int latest = nucleusIndices[i + 1];
            size *= ((latest - earliest) + 1);
            Range currentRange = Range.of(earliest, latest);
            ranges[i] = currentRange;
        }
        List<EdgeIndex> result = new ArrayList<EdgeIndex>(size);
        int[][] cartesianRanges = Range.cartesianRanges(ranges);
        for (int[] positions : cartesianRanges) {
            EdgeIndex edgeIndex = EdgeIndexBuilder.fromIntArray(Edge.SYLLABLE, positions, asByteArray.length + 1);
            edgeIndex.setBoundaryAtLimit(Edge.SYLLABLE, Side.EITHER);
            result.add(edgeIndex);
        }
        return result;

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

}
