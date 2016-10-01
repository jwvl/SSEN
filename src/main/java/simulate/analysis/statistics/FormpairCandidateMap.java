package simulate.analysis.statistics;

import candidates.Candidate;
import forms.FormPair;

import java.util.Map;
import java.util.Objects;

/**
 * Created by janwillem on 29/09/16.
 */
public class FormpairCandidateMap {
    private final Map<FormPair,Candidate> map;

    public FormpairCandidateMap(Map<FormPair,Candidate> set) {
        this.map = set;
    }


    public Map<FormPair,Candidate> getMap() {
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormpairCandidateMap that = (FormpairCandidateMap) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
