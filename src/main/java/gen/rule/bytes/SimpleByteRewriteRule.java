/**
 *
 */
package gen.rule.bytes;

import forms.phon.PhoneTransform;
import forms.primitives.segment.Phone;
import gen.rule.RewriteRule;
import gen.rule.string.Side;
import util.arrays.ByteArrayUtils;
import util.arrays.ByteBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public class SimpleByteRewriteRule implements RewriteRule {
    private final byte[] focus;
    private final byte[] target;
    private List<PhoneTransform> transforms;

    /**
     * @param focus
     * @param target
     */
    private SimpleByteRewriteRule(byte[] focus, byte[] target) {
        this.focus = focus;
        this.target = target;
    }

    public static SimpleByteRewriteRule createFromStrings(String beforeString, String afterString) {
        byte[] focus = ByteArrayUtils.fromString(beforeString);
        byte[] target = ByteArrayUtils.fromString(afterString);
        return createFromBytes(focus, target);
    }

    public static SimpleByteRewriteRule createDefaultRule(byte concatenator) {
        byte[] before = new byte[]{concatenator};
        byte[] after = new byte[0];
        return new SimpleByteRewriteRule(before, after);
    }

    public static SimpleByteRewriteRule createFromBytes(byte[] beforeBytes, byte[] afterBytes) {
        byte[] before = ByteBuilder.copyExcept(beforeBytes, Phone.NULL.getId());
        byte[] after = ByteBuilder.copyExcept(afterBytes, Phone.NULL.getId());
        return new SimpleByteRewriteRule(before, after);
    }

    public static SimpleByteRewriteRule createSimpleEdgeRule(List<PhoneTransform> transforms, Phone edgePhone,
                                                             Side side) {
        ByteBuilder targetBuilder = new ByteBuilder(transforms.size() + 1);
        ByteBuilder focusBuilder = new ByteBuilder(transforms.size() + 1);
        if (side == Side.LEFT || side == Side.EITHER) {
            focusBuilder.append(edgePhone.getId());
        }
        for (PhoneTransform transform : transforms) {
            focusBuilder.appendExcept(transform.left().getId(), Phone.NULL.getId());
            targetBuilder.appendExcept(transform.right().getId(), Phone.NULL.getId());
        }
        if (side == Side.RIGHT || side == Side.EITHER) {
            focusBuilder.append(edgePhone.getId());
        }
        return SimpleByteRewriteRule.createFromBytes(focusBuilder.build(), targetBuilder.build());
    }

    public int getNextFocusIndex(byte[] toSearch, int startAt) {
        int searchResult = ByteArrayUtils.indexOf(toSearch, startAt, focus);
        return searchResult < 0 ? toSearch.length : searchResult;
    }

    public byte[] getFocus() {
        return focus;
    }

    public byte[] getTarget() {
        return target;
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.rule.RewriteRule#getTransforms()
     */
    @Override
    public List<PhoneTransform> getTransforms() {
        if (transforms == null)
            buildTransforms();
        return transforms;
    }

    public void setTransforms(List<PhoneTransform> transforms) {
        this.transforms = transforms;
    }

    public void buildTransforms() {
        int maxLength = Math.max(focus.length, target.length);
        transforms = new ArrayList<PhoneTransform>(maxLength);
        for (int i = 0; i < maxLength; i++) {
            byte before = byteOrNull(focus, i);
            byte after = byteOrNull(target, i);
            PhoneTransform toAdd = PhoneTransform.getInstance(new byte[]{before, after});
            transforms.add(toAdd);
        }
    }

    private byte byteOrNull(byte[] bytes, int i) {
        return i < bytes.length ? bytes[i] : Phone.NULL.getId();
    }

    public String toString() {
        return String.format("%s→%s", Phone.decode(focus), Phone.decode(target));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(focus);
        result = prime * result + Arrays.hashCode(target);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SimpleByteRewriteRule))
            return false;
        SimpleByteRewriteRule other = (SimpleByteRewriteRule) obj;
        if (!Arrays.equals(focus, other.focus))
            return false;
        return Arrays.equals(target, other.target);
    }

}
