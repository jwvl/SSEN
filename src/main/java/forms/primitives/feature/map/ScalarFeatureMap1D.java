package forms.primitives.feature.map;

import forms.primitives.feature.IntegerFeature;

import java.lang.reflect.Array;

public class ScalarFeatureMap1D<O> implements ScalarFeatureMap<O> {
    private O[] entries;

    public ScalarFeatureMap1D(Class<O> cls, int size) {
        this.entries = (O[]) Array.newInstance(cls,size);
    }

    @Override
    public O getObject(IntegerFeature... features) {
        return entries[features[0].intValue()];
    }

    @Override
    public O getObject(int... indices) {
        return entries[indices[0]];
    }

    @Override
    public void put(O o, int... indices) {
        entries[indices[0]] = o;
    }
}
