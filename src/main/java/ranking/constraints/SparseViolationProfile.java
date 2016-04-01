package ranking.constraints;

import com.google.common.collect.HashMultiset;
import gen.mapping.FormMapping;

import java.util.Collection;

/**
 * Created by janwillem on 29/03/16.
 */
public class SparseViolationProfile {
    private final FormMapping formMapping;
    private final HashMultiset<Constraint> contents;

    public SparseViolationProfile(FormMapping formMapping, HashMultiset<Constraint> contents) {
        this.formMapping = formMapping;
        this.contents = contents;
    }

    public SparseViolationProfile(FormMapping formMapping, Collection<Constraint> elements) {
        this(formMapping, HashMultiset.create(elements));
    }

    public FormMapping getFormMapping() {
        return formMapping;
    }

    public HashMultiset<Constraint> getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SparseViolationProfile that = (SparseViolationProfile) o;

        if (formMapping != null ? !formMapping.equals(that.formMapping) : that.formMapping != null) return false;
        return contents != null ? contents.equals(that.contents) : that.contents == null;

    }

    @Override
    public int hashCode() {
        int result = formMapping != null ? formMapping.hashCode() : 0;
        result = 31 * result + (contents != null ? contents.hashCode() : 0);
        return result;
    }
}
