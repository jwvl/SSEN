package forms.primitives.feature.map;

import forms.primitives.feature.IntegerFeature;

import java.lang.reflect.Array;

public class ScalarFeatureMap2D<O> implements ScalarFeatureMap<O> {
    private O[][] entries;

    public ScalarFeatureMap2D(Class<O> cls, int... sizes) {
        this.entries = (O[][]) Array.newInstance(cls,sizes);
    }

    @Override
    public O getObject(IntegerFeature... features) {
        return entries[features[0].getValue()][features[1].getValue()];
    }

    @Override
    public O getObject(int... indices) {
        return entries[indices[0]][indices[1]];
    }

    @Override
    public void put(O o, int... indices) {
        entries[indices[0]][indices[1]] = o;
    }
}
