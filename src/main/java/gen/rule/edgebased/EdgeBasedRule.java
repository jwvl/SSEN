/**
 *
 */
package gen.rule.edgebased;

import forms.phon.PhoneTransform;
import forms.primitives.boundary.Edge;
import forms.primitives.segment.Phone;
import gen.rule.RewriteRule;
import gen.rule.string.Side;
import util.arrays.ByteArrayUtils;
import util.arrays.ByteBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * @author jwvl
 * @date 19/02/2016
 */
public class EdgeBasedRule implements RewriteRule {
    private final String name;
    private final Edge edgeType;
    private final byte[] leftContext;
    private final byte[] rightContext;
    private final EdgeBasedOperation operation;
    private final byte[] matchSequence;
    private final byte[] replaceSequence;

    /**
     * @param edgeType
     * @param leftContext
     * @param rightContext
     * @param operation
     */
    public EdgeBasedRule(Edge edgeType, byte[] leftContext, byte[] rightContext, EdgeBasedOperation operation) {
        this.edgeType = edgeType;
        this.leftContext = leftContext;
        this.rightContext = rightContext;
        this.operation = operation;
        this.name = generateName();
        this.matchSequence = ByteBuilder.flattenArrays(leftContext, operation.getBefore(), rightContext);
        this.replaceSequence = ByteBuilder.flattenArrays(leftContext, operation.getAfter(), rightContext);
    }

    public EdgeBasedRule getEdgeless() {
        EdgeBasedOperation rightOperation = operation.changeSide(Side.RIGHT);
        return new AnyEdgeRule(leftContext, rightContext, rightOperation);
    }

    /**
     * @return
     */
    private String generateName() {
        String edgeTypeString = edgeType == null ? "" : edgeType.toString();
        StringBuilder builder = new StringBuilder();
        builder.append(operation.toString()).append(" / ");
        builder.append(Phone.decode(leftContext));
        if (operation.getSide() == Side.LEFT) {
            builder.append(edgeTypeString).append("__");
        } else if (operation.getSide() == Side.RIGHT) {
            builder.append("__").append(edgeTypeString);
        } else {
            builder.append("__");
        }
        builder.append(Phone.decode(rightContext));
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((edgeType == null) ? 0 : edgeType.hashCode());
        result = prime * result + Arrays.hashCode(leftContext);
        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + Arrays.hashCode(rightContext);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EdgeBasedRule))
            return false;
        EdgeBasedRule other = (EdgeBasedRule) obj;
        if (edgeType != other.edgeType)
            return false;
        if (!Arrays.equals(leftContext, other.leftContext))
            return false;
        if (operation == null) {
            if (other.operation != null)
                return false;
        } else if (!operation.equals(other.operation))
            return false;
        return Arrays.equals(rightContext, other.rightContext);
    }

    @Override
    public String toString() {
        return "EdgeBasedRule $" + name + "$";
    }


    public byte[] apply(byte[] original, int edgePosition) {
        return ByteBuilder.replaceAtIndex(edgePosition + getSearchOffset(), original, matchSequence.length,
                replaceSequence);
    }


    /**
     * @param asBytes
     * @param currentIndex
     * @return
     */
    public boolean ruleMatches(byte[] asBytes, int currentIndex) {
        int startSearch = currentIndex + getSearchOffset();
        boolean result = ByteArrayUtils.matches(asBytes, startSearch, matchSequence);
        return result;
    }

    private int getSearchOffset() {
        int result = 0;
        if (operation.getSide() == Side.RIGHT) {
            result -= operation.getBefore().length;
        }
        result -= leftContext.length;
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.rule.RewriteRule#getTransforms()
     */
    @Override
    public List<PhoneTransform> getTransforms() {
        return operation.getTransforms();
    }

    /**
     * @return
     */
    public int getOffset() {
        return replaceSequence.length - matchSequence.length;
    }

    public EdgeBasedRule getReverse() {
        return new EdgeBasedRule(edgeType, leftContext, rightContext, operation.getReverse());
    }

    public byte[] getLeftContext() {
        return leftContext;
    }

    public byte[] getRightContext() {
        return rightContext;
    }

    public EdgeBasedOperation getOperation() {
        return operation;
    }


}
