package forms.primitives.boundary;

import util.arrays.ByteArrayUtils;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;

/**
 * Created by janwillem on 28/03/16.
 * TODO check if this works at all!
 */
public class BitsetEdgeIndex extends EdgeIndex {
    private final BitSet indices;

    public BitsetEdgeIndex(int length) {
        this.indices = new BitSet((1+length)*2);
    }

    @Override
    public void set(Edge type, int index) {
        if (type == Edge.SYLLABLE || type == Edge.WORD)
            indices.set(index*2);
        else if (type == Edge.MORPHEME) {
            indices.set(1+index*2);
        }
    }

    @Override
    public byte[][] getSubSequences(byte[] byteArray, Edge edgeType) {
        int numSubSequences = countNonEdgeBoundaries(edgeType) + 1;
        int startAt = 0;
        int endAt = nextSetBit(edgeType, 1);

        byte[][] result = new byte[numSubSequences][];
        for (int i = 0; i < numSubSequences; i++) {
            result[i] = ByteArrayUtils.getSubArray(byteArray, startAt, endAt);
            startAt = endAt;
            endAt = nextSetBit(edgeType, endAt + 1);
            if (endAt < 0) {
                endAt = indices.length()/2;
            }
        }
        return result;
    }

    @Override
    public int nextSetBit(Edge edgeType, int startFrom) {
        for (int i = startFrom; i < sequenceLength(); i++) {
            if (hasBoundaryAt(edgeType,i)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getNumSubsequences(Edge edgeType) {
        return getIndices(edgeType).length;
    }

    @Override
    public int[] getIndices(Edge edgeType) {
        int[] result = new int[sequenceLength()];
        int count =0;
        for (int i=0; i < result.length; i++) {
                if (hasBoundaryAt(edgeType,i)) {
                    result[count++] = i;
                }
            }
        return Arrays.copyOfRange(result,0,count );
    }

    @Override
    public boolean hasBoundaryAt(Edge type, int index) {
        int lengthStart = index*2;
        switch (type) {
            case WORD:
                return indices.get(lengthStart) && indices.get(lengthStart+1);
            case MORPHEME:
                return !indices.get(lengthStart) && indices.get(lengthStart+1);
            case SYLLABLE:
                return indices.get(lengthStart) && !indices.get(lengthStart+1);
        }
        return false;
    }

    @Override
    public int sequenceLength() {
        return (indices.length()*2)-1;
    }

    private int countNonEdgeBoundaries(Edge edgeType) {
        int count =0;
        for (int i=0; i < sequenceLength(); i++) {
            if (!hasBoundaryAt(edgeType,i)) {
                count++;
            }
        }
        return count;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitsetEdgeIndex that = (BitsetEdgeIndex) o;
        return Objects.equals(indices, that.indices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indices);
    }

    private boolean isEmpty(int index) {
        int lengthStart = 2*index;
        return !indices.get(lengthStart) && !indices.get(lengthStart+1);
    }
}
