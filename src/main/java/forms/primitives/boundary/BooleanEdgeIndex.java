package forms.primitives.boundary;

import util.arrays.ByteArrayUtils;

import java.util.Arrays;

/**
 * Created by janwillem on 28/03/16.
 */
public class BooleanEdgeIndex extends EdgeIndex {
    private final boolean[][] edgeIndices;
    private int hashcode;

    public BooleanEdgeIndex(int size) {
        edgeIndices = new boolean[Edge.values().length][size];
        hashcode = 0;
    }

    @Override
    public int[] getIndices(Edge edgeType) {
        int[] tempResult = new int[sequenceLength() + 1];
        int index = 0;
        for (int i = 0; i <= sequenceLength(); i++) {
            if (hasBoundaryAt(edgeType, i)) {
                tempResult[index++] = i;
            }
        }
        return Arrays.copyOfRange(tempResult, 0, index);
    }

    @Override
    public boolean hasBoundaryAt(Edge edgeType, int index) {
        return edgeIndices[edgeType.ordinal()][index];
    }

    @Override
    public int sequenceLength() {
        return edgeIndices[0].length - 1;
    }

    @Override
    public void set(Edge edgeType, int index) {
        edgeIndices[edgeType.ordinal()][index] = true;
        hashcode = 0;
    }

    @Override
    public byte[][] getSubSequences(byte[] byteArray, Edge edgeType) {
        int numSubSequences = getNumSubsequences(edgeType);
        byte[][] result = new byte[numSubSequences][];
        int from = 1;
        int nextSetBit = nextSetBit(edgeType, 1);
        result[0] = ByteArrayUtils.getSubArray(byteArray, 0, nextSetBit);
        for (int i = 1; i < numSubSequences; i++) {
            from = nextSetBit;
            nextSetBit = nextSetBit(edgeType, from);
            result[i] = ByteArrayUtils.getSubArray(byteArray, from, nextSetBit);
        }
        return result;
    }

    @Override
    public int nextSetBit(Edge edgeType, int startFrom) {
        boolean[] toSearch = edgeIndices[edgeType.ordinal()];
        for (int i = startFrom; i < toSearch.length; i++) {
            if (toSearch[i])
                return i;
        }
        return -1;
    }

    @Override
    public int getNumSubsequences(Edge edgeType) {
        int count = 1;
        for (int i = 1; i < sequenceLength() - 1; i++) {
            if (hasBoundaryAt(edgeType, i)) {
                count++;
            }
        }
        return count;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanEdgeIndex that = (BooleanEdgeIndex) o;
        for (int i=0; i < edgeIndices.length; i++) {
            if (!Arrays.equals(edgeIndices[i],that.edgeIndices[i])) {
                return false;
            }
        }
        return true;
        //return Arrays.deepEquals(edgeIndices, that.edgeIndices);
    }

    @Override
    public int hashCode() {
        if (hashcode == 0) {
            hashcode = getHashCode();
        }
        return hashcode;
    }

    public int getHashCode() {
        return Arrays.deepHashCode(edgeIndices);
    }


}
