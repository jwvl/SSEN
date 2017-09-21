package simulate.phonetic.vowelsystems.subgens;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import forms.primitives.feature.ScaleFeature;
import forms.primitives.feature.SurfaceRangeFeature;
import phonetics.DiscretizedScale;
import simulate.phonetic.vowelsystems.levels.FeatureValueForm;
import simulate.phonetic.vowelsystems.levels.PhoneticValueForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RangeFeatureFactory {
    private static List<FeatureValueForm> surfaceFormsCached;

    public static List<PhoneticValueForm> getAllPfs(List<DiscretizedScale> scales) {
        List<PhoneticValueForm> result = Lists.newArrayList();
        List<Set<ScaleFeature>> subLists = new ArrayList<>(scales.size());
        for (int i=0; i < scales.size(); i++) {
            Set<ScaleFeature> toAdd = Sets.newHashSet(scales.get(i).getValues());
            subLists.add(toAdd);
        }
        Set<List<ScaleFeature>> cartesian = Sets.cartesianProduct(subLists);
        for (List<ScaleFeature> product: cartesian) {
            PhoneticValueForm toAdd = new PhoneticValueForm(product);
            result.add(toAdd);
        }
        return result;
    }

    public static List<FeatureValueForm> getAllSfs(List<List<SurfaceRangeFeature>> scales) {
        surfaceFormsCached = Lists.newArrayList();
        List<Set<SurfaceRangeFeature>> subLists = new ArrayList<>(scales.size());
        for (int i=0; i < scales.size(); i++) {
            Set<SurfaceRangeFeature> toAdd = Sets.newHashSet(scales.get(i));
            subLists.add(toAdd);
        }
        Set<List<SurfaceRangeFeature>> cartesian = Sets.cartesianProduct(subLists);
        for (List<SurfaceRangeFeature> product: cartesian) {
            FeatureValueForm toAdd = new FeatureValueForm(product);
            surfaceFormsCached.add(toAdd);
        }
        return getAllSfs();
    }

    public static List<FeatureValueForm> getAllSfs() {
        return surfaceFormsCached;
    }
}
