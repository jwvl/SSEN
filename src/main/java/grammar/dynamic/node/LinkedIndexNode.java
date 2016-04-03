package grammar.dynamic.node;

import gen.mapping.FormMapping;
import ranking.violations.vectors.BitViolationVector;

/**
 * Created by janwillem on 02/04/16.
 */
public class LinkedIndexNode extends AbstractCostNode<LinkedIndexNode> implements Comparable<LinkedIndexNode>{
    private final BitViolationVector[] vectors;

    public LinkedIndexNode(LinkedIndexNode parent, FormMapping formMapping, BitViolationVector newVector) {
        super(parent, formMapping);
        int depth = (parent == null ? 0 : parent.vectors.length);
        this.vectors = new BitViolationVector[depth+1];
        for (int i=0; i < depth; i++) {
            vectors[i] = parent.vectors[i];
        }
        vectors[depth] = newVector;
    }


    private boolean hasBitAtIndex(int index) {
        for (BitViolationVector vector: vectors) {
            if (vector.bitSetAt(index)) {
                return true;
            }

        } return false;
    }


    @Override
    public int compareTo(LinkedIndexNode o) {
        for (int i=0; i < vectors[0].size(); i++) {
            int compare = bitSetCompare(hasBitAtIndex(i),o.hasBitAtIndex(i));
            if (compare != 0)
                return compare;
        }
        return 0;
    }

    private int bitSetCompare(boolean a, boolean b) {
        if (a) {
            if (!b) {
                return 1;
            }
        } else if (b) {
            return -1;
        }
        return 0;
    }
}
