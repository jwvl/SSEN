/**
 *
 */
package forms.phon.syllable;

import forms.primitives.boundary.EdgeIndex;

import java.util.List;

/**
 * @author jwvl
 * @date 24/02/2016
 */
public interface ISyllabifier {
    int[] findNuclei(byte[] asByteArray);

    List<EdgeIndex> getSyllabifications(byte[] asByteArray);
}
