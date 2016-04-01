package grammar.dynamic;

import com.google.common.collect.ImmutableSet;
import forms.Form;
import ranking.constraints.SparseViolationProfile;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by janwillem on 29/03/16.
 */
public class SubEvaluation {
    private final static SubEvaluation EMPTY = new SubEvaluation(null, Collections.EMPTY_LIST);
    private final Form input;
    private ImmutableSet<SparseViolationProfile> profiles;

    public Form getInput() {
        return input;
    }

    public ImmutableSet<SparseViolationProfile> getProfiles() {
        return profiles;
    }

    public SubEvaluation(Form input, Collection<SparseViolationProfile> profiles) {
        this.input = input;
        this.profiles = ImmutableSet.copyOf(profiles);
    }

    public static SubEvaluation getEmpty() {
        return EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubEvaluation that = (SubEvaluation) o;

        if (input != null ? !input.equals(that.input) : that.input != null) return false;
        return profiles != null ? profiles.equals(that.profiles) : that.profiles == null;

    }

    @Override
    public int hashCode() {
        int result = input != null ? input.hashCode() : 0;
        result = 31 * result + (profiles != null ? profiles.hashCode() : 0);
        return result;
    }
}
