/**
 *
 */
package gen.rule;

import com.google.common.collect.Lists;
import forms.primitives.segment.Phone;

import java.util.List;

/**
 * @author jwvl
 * @date May 19, 2015
 */
public class Rewriter {

    public static List<Phone> delete(List<Phone> in, int index) {
        List<Phone> out = Lists.newArrayList(in);
        out.remove(index);
        return out;
    }

    public static List<Phone> insert(List<Phone> in, Phone p, int index) {
        List<Phone> out = Lists.newArrayList(in);
        out.add(index, p);
        return out;
    }

    public static List<Phone> replace(List<Phone> in, Phone p, int index) {
        List<Phone> out = Lists.newArrayList(in);
        out.set(index, p);
        return out;
    }

}
