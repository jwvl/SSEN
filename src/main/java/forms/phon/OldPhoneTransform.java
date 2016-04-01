/**
 *
 */
package forms.phon;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import forms.primitives.Submapping;
import forms.primitives.segment.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date May 22, 2015
 */
public class OldPhoneTransform extends Submapping<Phone, Phone> {
    private static Table<Phone, Phone, OldPhoneTransform> cache = HashBasedTable.create();

    /**
     * @param l
     * @param r
     */
    private OldPhoneTransform(Phone l, Phone r) {
        super(l, r);
    }

    public static OldPhoneTransform getSubstitution(Phone from, Phone to) {
        return getInstance(from, to);
    }

    public static OldPhoneTransform getDeletion(Phone deleted) {
        return getInstance(deleted, Phone.NULL);
    }

    public static OldPhoneTransform getInsertion(Phone inserted) {
        return getInstance(Phone.NULL, inserted);
    }

    public static OldPhoneTransform getIdentity(Phone unchanged) {
        return getInstance(unchanged, unchanged);
    }

    /**
     * @param unchanged
     * @param unchanged2
     * @return
     */
    public static OldPhoneTransform getInstance(Phone a, Phone b) {
        OldPhoneTransform result = cache.get(a, b);
        if (result == null) {
            result = new OldPhoneTransform(a, b);
            cache.put(a, b, result);
        }
        return result;
    }

    public String toString() {
        return String.format("%s -> %s", left(), right());
    }

    public OldPhoneTransform getReverse() {
        return getInstance(right(), left());
    }

    public static List<OldPhoneTransform> reverseAll(List<OldPhoneTransform> toReverse) {
        List<OldPhoneTransform> result = new ArrayList<OldPhoneTransform>(toReverse.size());
        for (OldPhoneTransform transform : toReverse) {
            result.add(transform.getReverse());
        }
        return result;
    }

}
