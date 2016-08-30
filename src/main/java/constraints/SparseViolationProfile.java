package constraints;

import constraints.helper.ConstraintArrayList;
import constraints.hierarchy.reimpl.Hierarchy;

import java.util.Arrays;

/**
 * Created by janwillem on 29/03/16.
 */
public class SparseViolationProfile implements Comparable<SparseViolationProfile> {
    private final short[] indices;
    private final static short[] noDifference = new short[]{0,0};
    private final static SparseViolationProfile EMPTY = createFromShortArray(new short[0]);

    private SparseViolationProfile(short[] violationIndices) {
        this.indices = violationIndices;
    }

    public static SparseViolationProfile createFromConstraintArrayList(ConstraintArrayList constraintArrayList, Hierarchy hierarchy) {
        short[] rankedIndices = new short[constraintArrayList.size()];
        for (int i=0; i < constraintArrayList.size(); i++) {
            rankedIndices[i] = hierarchy.getRankingIndex(constraintArrayList.get(i));
        }
        Arrays.sort(rankedIndices);
        return new SparseViolationProfile(rankedIndices);
    }

    public static short[] createArrayFromConstraintArrayList(ConstraintArrayList constraintArrayList, Hierarchy hierarchy) {
        short[] rankedIndices = new short[constraintArrayList.size()];
        for (int i=0; i < constraintArrayList.size(); i++) {
            rankedIndices[i] = hierarchy.getRankingIndex(constraintArrayList.get(i));
        }
        Arrays.sort(rankedIndices);
        return rankedIndices;
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

    public static String arrayToString(short[] indices) {
        final StringBuilder sb = new StringBuilder("SparseViolationProfile{");
        sb.append("indices=").append(Arrays.toString(indices));
        sb.append('}');
        return sb.toString();
    }

    public static short[] mergeSortedOld(short[] indices, short[] other) {
        if (indices.length < 1) return other;
        if (other.length < 1) return indices;
        short[] tempArray = new short[indices.length + other.length];
        int i = 0, j = 0, k = 0;
        while (i < indices.length && j < other.length) {
            if (indices[i] <  other[j]) {
                tempArray[k++] = indices[i++];

            } else {
                tempArray[k++] = other[j++];
            }

        }

        while (i < indices.length) {
            tempArray[k++] = indices[i++];
        }

        while (j < other.length) {
            tempArray[k++] = other[j++];

        }
        return tempArray;
    }

    public static short[] mergeSorted(short[] a, short[] b) {
            short[] answer = new short[a.length + b.length];
            int i = a.length - 1, j = b.length - 1, k = answer.length;

            while (k > 0)
                answer[--k] =
                        (j < 0 || (i >= 0 && a[i] >= b[j])) ? a[i--] : b[j--];
        return answer;

    }

    public SparseViolationProfile mergeWith(SparseViolationProfile s) {
        if (indices.length < 1) return s;
        if (s.indices.length < 1) return this;
        short[] tempArray = new short[size() + s.size()];
        int i = 0, j = 0, k = 0;
        while (i < indices.length && j < s.indices.length) {
            if (indices[i] <  s.indices[j]) {
                tempArray[k++] = indices[i++];

            } else {
                tempArray[k++] = s.indices[j++];
            }

        }

        while (i < indices.length) {
            tempArray[k++] = indices[i++];
        }

        while (j < s.indices.length) {
            tempArray[k++] = s.indices[j++];

        }
        return SparseViolationProfile.createFromShortArray(tempArray);

    }

    public int size() {
        return indices.length;
    }

    @Override
    public int compareTo(SparseViolationProfile other) {
        int i = 0;
        while (i < indices.length && i < other.indices.length) {
            if (indices[i] != other.indices[i]) {
                if (indices[i] < other.indices[i]) {
                    return 1;
                } else {
                    return -1;
                }
            }
            i++;
        }
        if (indices.length > other.indices.length) {
            return 1;
        } else if (indices.length < other.indices.length) {
            return -1 ;
        } else {
            return 0; // In this case the items would actually be equal!
        }
    }

    public static int compareProfiles(short[] s1, short[] s2) {
        int i = 0;
        while (i < s1.length && i < s2.length) {
            if (s1[i] != s2[i]) {
                if (s1[i] < s2[i]) {
                    return 1;
                } else {
                    return -1;
                }
            }
            i++;
        }
        if (s1.length > s2.length) {
            return 1;
        } else if (s1.length < s2.length) {
            return -1 ;
        } else {
            return 0; // In this case the items would actually be equal!
        }
    }

    public SparseViolationProfile copy() {
        return createFromShortArray(Arrays.copyOf(indices,indices.length));
    }

    public short gethighestRankedViolator() {
        return size() > 0 ? indices[0] : Short.MAX_VALUE;
    }
}
