package forms.primitives.feature.map;

import forms.primitives.feature.IntegerFeature;

public interface ScalarFeatureMap<O> {

    O getObject(IntegerFeature... features);
    O getObject(int... indices);
    void put(O o, int... indices);
}
