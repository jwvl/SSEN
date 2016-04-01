/**
 *
 */
package gen.rule.edgebased;

import forms.phon.PhoneTransform;
import forms.primitives.boundary.Edge;
import forms.primitives.segment.Phone;
import gen.rule.string.Side;
import util.arrays.ByteArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jwvl
 * @date 19/02/2016
 */
public class EdgeBasedOperation {
    private final OperationType type;
    // Which edge/side does the rule operate on?
    private final Side side;
    private final byte[] before;
    private final byte[] after;
    private final List<PhoneTransform> transforms;

    /**
     * @param type
     * @param side
     * @param before
     * @param after
     */
    private EdgeBasedOperation(OperationType type, Side side, byte[] before, byte[] after) {
        this.type = type;
        this.side = side;
        this.before = before;
        this.after = after;
        this.transforms = createTransforms();
    }

    public static EdgeBasedOperation createInsertion(byte[] toInsert) {
        return new EdgeBasedOperation(OperationType.INSERT, Side.NEITHER, ByteArrayUtils.EMPTY, toInsert);
    }

    public static EdgeBasedOperation createDeletion(byte[] toDelete, Side side) {
        return new EdgeBasedOperation(OperationType.DELETE, side, toDelete, ByteArrayUtils.EMPTY);
    }

    public static EdgeBasedOperation createChange(byte[] before, byte[] after, Side side) {
        return new EdgeBasedOperation(OperationType.CHANGE, side, before, after);
    }

    public EdgeBasedOperation changeSide(Side newSide) {
        return new EdgeBasedOperation(getType(), newSide, getBefore(), getAfter());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(after);
        result = prime * result + Arrays.hashCode(before);
        result = prime * result + ((side == null) ? 0 : side.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EdgeBasedOperation))
            return false;
        EdgeBasedOperation other = (EdgeBasedOperation) obj;
        if (!Arrays.equals(after, other.after))
            return false;
        if (!Arrays.equals(before, other.before))
            return false;
        if (side != other.side)
            return false;
        return type == other.type;
    }

    public OperationType getType() {
        return type;
    }

    public Side getSide() {
        return side;
    }

    public byte[] getBefore() {
        return before;
    }

    public byte[] getAfter() {
        return after;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (before.length == 0) {
            builder.append("∅");
        } else {
            builder.append(Phone.decode(before));
        }
        builder.append(" → ");
        if (after.length == 0) {
            builder.append("∅");
        } else {
            builder.append(Phone.decode(after));
        }
        return builder.toString();
    }

    public static EdgeBasedOperation fromString(String inputString) {
        String[] splitByArrow = inputString.split("→");
        String beforeArrow = splitByArrow[0].trim();
        String afterArrow = splitByArrow[1].trim();

        Side side = Side.NEITHER;
        char firstChar = beforeArrow.charAt(0);
        char lastChar = beforeArrow.charAt(beforeArrow.length() - 1);

        String beforeArrowCleansed = Edge.cleanFromString(beforeArrow);
        if (Edge.isEdgeSymbol(firstChar)) {
            side = Side.LEFT;
        } else if (Edge.isEdgeSymbol(lastChar)) {
            side = Side.RIGHT;
        }

        if (beforeArrowCleansed.length() < 1 || beforeArrowCleansed.equals("∅")) {
            return createInsertion(ByteArrayUtils.fromString(afterArrow));
        } else if (afterArrow.length() < 1 || afterArrow.equals("∅")) {
            return createDeletion(ByteArrayUtils.fromString(beforeArrowCleansed), side);
        } else {
            return createChange(ByteArrayUtils.fromString(beforeArrowCleansed), ByteArrayUtils.fromString(afterArrow), side);
        }
    }

    /**
     * @return
     */
    public List<PhoneTransform> createTransforms() {
        byte[][] both = {before, after};
        padLongest(both);
        List<PhoneTransform> transforms = new ArrayList<PhoneTransform>(both[0].length);
        for (int i = 0; i < both[0].length; i++) {
            Phone in = Phone.getByCode(both[0][i]);
            Phone out = Phone.getByCode(both[1][i]);
            transforms.add(PhoneTransform.getInstance(in, out));
        }
        return transforms;
    }

    /**
     * Not so nice -- changes the input
     *
     * @param both
     */
    private void padLongest(byte[][] both) {
        if (both[0].length > both[1].length) {
            both[1] = ByteArrayUtils.padToLength(both[1], both[0].length, side, Phone.NULL.getId());
        } else if (both[0].length < both[1].length) {
            both[0] = ByteArrayUtils.padToLength(both[0], both[1].length, side, Phone.NULL.getId());
        }
    }

    /**
     * @return
     */
    public List<PhoneTransform> getTransforms() {
        return transforms;
    }

    public EdgeBasedOperation getReverse() {
        return new EdgeBasedOperation(getType().getInverse(), getSide(), after, before);
    }


}
