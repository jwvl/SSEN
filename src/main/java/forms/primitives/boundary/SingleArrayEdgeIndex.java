package forms.primitives.boundary;

import cern.colt.bitvector.BitVector;
import util.arrays.ByteArrayUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by janwillem on 01/04/16.
 */
public class SingleArrayEdgeIndex extends EdgeIndex {
    private final BitVector vector;
    private final static int numEdgeTypes = Edge.values().length;

    public SingleArrayEdgeIndex(int size) {
        this.vector = new BitVector(numEdgeTypes*size);
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
        int offset = edgeType.ordinal()*size();
        return vector.get(offset+index);
    }

    @Override
    public int sequenceLength() {
        return size()-1;
    }

    @Override
    public void set(Edge edgeType, int index) {
        int offset = edgeType.ordinal()*size();
        vector.set(offset+index);
    }

    private int size() {
        return vector.size()/numEdgeTypes;
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

        for (int i = startFrom; i < size(); i++) {
            if (hasBoundaryAt(edgeType,i))
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
        SingleArrayEdgeIndex that = (SingleArrayEdgeIndex) o;
        return Objects.equals(vector, that.vector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vector);
    }
}
