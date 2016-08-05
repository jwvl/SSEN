package constraints;

import constraints.hierarchy.reimpl.IndexedRanking;
import constraints.helper.ConstraintArrayList;

import java.util.Arrays;

/**
 * Created by janwillem on 29/03/16.
 */
public class SparseViolationProfile {
    private final short[] indices;
    private final static short[] noDifference = new short[]{0,0};
    private final static SparseViolationProfile EMPTY = createFromShortArray(new short[0]);

    private SparseViolationProfile(short[] violationIndices) {
        this.indices = violationIndices;
    }

    public static SparseViolationProfile createFromConstraintArrayList(ConstraintArrayList constraintArrayList, IndexedRanking indexedRanking) {
        short[] rankedIndices = new short[constraintArrayList.size()];
        for (int i=0; i < constraintArrayList.size(); i++) {
            rankedIndices[i] = indexedRanking.getRankingIndex(constraintArrayList.get(i));
        }
        Arrays.sort(rankedIndices);
        return new SparseViolationProfile(rankedIndices);
    }

    public static SparseViolationProfile createFromShortArray(short[] array) {
        Arrays.sort(array);
        return new SparseViolationProfile(array);
    }

    // Uses a dirty trick: sign of the number is used to indicate in whose favor the difference is
    public short[] getLeftmostDifference(SparseViolationProfile other) {
        int i = 0;
        while (i < indices.length && i < other.indices.length) {
            if (indices[i] != other.indices[i]) {
                if (indices[i] < other.indices[i]) {
                    return new short[]{indices[i],1};
                } else {
                    return new short[]{other.indices[i],-1};
                }
            }
            i++;
        }
        if (indices.length > other.indices.length) {
            return new short[]{indices[i],1};
        } else if (indices.length < other.indices.length) {
            return new short[]{other.indices[i],-1};
        } else {
            return noDifference; // In this case the items would actually be equal!
        }
    }


    public static SparseViolationProfile empty() {
        return EMPTY;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SparseViolationProfile{");
        sb.append("indices=").append(Arrays.toString(indices));
        sb.append('}');
        return sb.toString();
    }

    public SparseViolationProfile mergeWith(SparseViolationProfile s) {
        short[] tempArray = new short[size() + s.size()];
        int i = 0, j = 0, k = 0;
        while (i < size() && j < s.size()) {
            if (indices[i] <  s.indices[j]) {
                tempArray[k++] = indices[i++];

            } else {
                tempArray[k++] = s.indices[j++];
            }

        }

        while (i < size()) {
            tempArray[k++] = indices[i++];
        }

        while (j < s.size()) {
            tempArray[k++] = s.indices[j++];

        }
        return SparseViolationProfile.createFromShortArray(tempArray);

    }

    public int size() {
        return indices.length;
    }
}
