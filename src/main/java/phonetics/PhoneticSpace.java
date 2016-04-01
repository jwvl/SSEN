/**
 *
 */
package phonetics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date 03/12/2015
 * Basically a wrapper for a List of FeatureScales, but has some additional features (e.g. retrieving parts of
 * the space, can be used to generate constraints).
 */
public class PhoneticSpace {
    private List<FeatureScale> scales;

    /**
     *
     */
    private PhoneticSpace() {
        this.scales = new ArrayList<FeatureScale>();
    }

    public void addScale(FeatureScale scale) {
        scales.add(scale);
    }

    public int dimensionSize() {
        return scales.size();
    }


}
