/**
 *
 */
package forms.phon;

import forms.primitives.ISubmapping;
import forms.primitives.segment.Phone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jwvl
 * @date May 22, 2015
 */
public class PhoneTransform implements ISubmapping<Phone, Phone> {
    byte[] contents;

    /**
     * @param l
     * @param r
     */
    private PhoneTransform(byte[] contents) {
        this.contents = contents;
    }

    public static PhoneTransform getSubstitution(Phone from, Phone to) {
        return getInstance(from, to);
    }

    public static PhoneTransform getDeletion(Phone deleted) {
        return getInstance(deleted, Phone.NULL);
    }

    public static PhoneTransform getInsertion(Phone inserted) {
        return getInstance(Phone.NULL, inserted);
    }

    public static PhoneTransform getIdentity(Phone unchanged) {
        return getInstance(unchanged, unchanged);
    }

    public static PhoneTransform getInstance(Phone a, Phone b) {
        return new PhoneTransform(new byte[]{a.getId(), b.getId()});
    }

    public static PhoneTransform getInstance(byte[] bytes) {
        return new PhoneTransform(bytes);
    }

    public String toString() {
        return String.format("%s -> %s", left(), right());
    }

    public PhoneTransform getReverse() {
        return new PhoneTransform(new byte[]{contents[1], contents[0]});
    }

    public static List<PhoneTransform> reverseAll(List<PhoneTransform> toReverse) {
        List<PhoneTransform> result = new ArrayList<PhoneTransform>(toReverse.size());
        for (PhoneTransform transform : toReverse) {
            result.add(transform.getReverse());
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.ISubmapping#left()
     */
    @Override
    public Phone left() {
        return Phone.getByCode(contents[0]);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.ISubmapping#right()
     */
    @Override
    public Phone right() {
        return Phone.getByCode(contents[1]);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(contents);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PhoneTransform))
            return false;
        PhoneTransform other = (PhoneTransform) obj;
        return Arrays.equals(contents, other.contents);
    }

}
