package forms.primitives.boundary;

import util.arrays.ByteArrayUtils;

import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by janwillem on 28/03/16.
 */
public class BitsetEdgeIndex extends EdgeIndex {
    private final BitSet[] indices;
    private final int length;

    public BitsetEdgeIndex(int length) {
        this.indices = createIndices(length);
        this.length = length;
    }

    @Override
    public void set(Edge edgeType, int value) {
        indices[edgeType.ordinal()].set(value);
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
                endAt = length;
            }
        }
        return result;
    }

    @Override
    public int nextSetBit(Edge edgeType, int startFrom) {
        return indices[edgeType.ordinal()].nextSetBit(startFrom);
    }

    @Override
    public int getNumSubsequences(Edge edgeType) {
        BitSet asBitset = indices[edgeType.ordinal()];
        int result = asBitset.cardinality();
        if (asBitset.get(0)) {
            result--;
        }
        if (asBitset.get(sequenceLength())) {
            result--;
        }
        return result;
    }

    @Override
    public int[] getIndices(Edge edgeType) {
        BitSet asBitSet = indices[edgeType.ordinal()];
        int[] result = new int[asBitSet.cardinality()];
        int nowAt = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = asBitSet.nextSetBit(nowAt);
            nowAt = result[i] + 1;
        }
        return result;
    }

    @Override
    public boolean hasBoundaryAt(Edge edgeType, int index) {
        return indices[edgeType.ordinal()].get(index);
    }

    @Override
    public int sequenceLength() {
        return length - 1;
    }

    private int countNonEdgeBoundaries(Edge edgeType) {
        BitSet bitSet = indices[edgeType.ordinal()];
        int result = bitSet.cardinality();
        if (bitSet.get(0))
            result -= 1;
        if (bitSet.get(sequenceLength()))
            result -= 1;
        return result;
    }

    /**
     * @return
     */
    private static BitSet[] createIndices(int length) {
        int numEdgeTypes = Edge.values().length;
        BitSet[] result = new BitSet[numEdgeTypes];
        for (int i = 0; i < numEdgeTypes; i++) {
            result[i] = new BitSet(length);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitsetEdgeIndex that = (BitsetEdgeIndex) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.deepEquals(indices, that.indices);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(indices);
    }
}
