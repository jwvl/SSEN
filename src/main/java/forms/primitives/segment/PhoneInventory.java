/**
 *
 */
package forms.primitives.segment;

import forms.phon.Sonority;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jwvl
 * @date 26/09/2015
 */
public class PhoneInventory {
    public Set<Phone> allPhones = new HashSet<Phone>();

    public void addPhone(Phone p) {
        allPhones.add(p);
    }

    public Set<Phone> getBySonority(Sonority s) {
        Set<Phone> result = new HashSet<Phone>(allPhones.size());
        for (Phone p : allPhones) {
            if (p.getSonority() == s) {
                result.add(p);
            }
        }
        return result;
    }

    public int size() {
        return allPhones.size();
    }

}
