/**
 *
 */
package forms.primitives.boundary;

import gen.rule.string.Side;
import util.arrays.ByteArrayUtils;

import java.util.Arrays;

/**
 * @author jwvl
 * @date 19/02/2016
 */
public abstract class EdgeIndex {

    public void setBoundaryAtLimit(Edge edgeType, Side side) {
        if (side == Side.LEFT || side == Side.EITHER) {
            set(edgeType, 0);
        }
        if (side == Side.RIGHT || side == Side.EITHER) {
            set(edgeType, sequenceLength());
        }
    }

    public abstract int[] getIndices(Edge edgeType);

    public abstract boolean hasBoundaryAt(Edge edgeType, int index);

    public abstract int sequenceLength();

    public abstract void set(Edge edgeType, int index);


    public String toString(Edge edgeType) {
        return edgeType.toString() + Arrays.toString(getIndices(edgeType));
    }


    public byte[] getSubSequence(byte[] byteArray, int index, Edge edgeType) {
        int[] asArray = getIndices(edgeType);
        int startAt = (index == 0 ? 0 : asArray[index - 1]);
        int endAt = index < sequenceLength() + 1 ? asArray[index] : byteArray.length;
        return ByteArrayUtils.getSubArray(byteArray, startAt, endAt);
    }

    public abstract byte[][] getSubSequences(byte[] byteArray, Edge edgeType);

    public abstract int nextSetBit(Edge edgeType, int startFrom);


    /**
     * @return
     */
    public abstract int getNumSubsequences(Edge edgeType);


}
