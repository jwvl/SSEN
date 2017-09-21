package simulate.phonetic.vowelsystems.levels;

import forms.Form;
import forms.primitives.feature.RangeFeature;

import java.util.Arrays;
import java.util.List;

public abstract class RangeListForm<R extends RangeFeature> implements Form {
    private final int[] contents;
    private final List<R> features;

    protected RangeListForm(List<R> features)
    {
        this.features = features;
        this.contents = contentsFromFeatures(features);
    }

    private int[] contentsFromFeatures(List<R> features) {
        int[] result = new int[features.size()];
        for (int i=0; i < result.length; i++) {
            result[i] = features.get(i).intValue();
        }
        return result;
    }

    @Override
    public int getNumSubForms() {
        return contents.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RangeListForm that = (RangeListForm) o;

        if (getLevelIndex() != that.getLevelIndex()) return false;
        return Arrays.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        int result = getLevelIndex();
        result = 31 * result + Arrays.hashCode(contents);
        return result;
    }

    public int[] getContents() {
        return contents;
    }

    public List<R> getFeatures() {
        return features;
    }
}
