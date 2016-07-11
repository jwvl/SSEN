package forms.phon.numerical;

import forms.primitives.segment.Phone;
import org.apache.commons.collections.map.IdentityMap;

import java.util.Map;
import java.util.Objects;

/**
 * Created by janwillem on 06/04/16.
 */
public class DiscretePhoneticElement implements PhoneticElement{
    private final Phone phone;
    private static Map<Phone,DiscretePhoneticElement> instances = new IdentityMap();

    private DiscretePhoneticElement(Phone phone) {
        this.phone = phone;
    }

    public static DiscretePhoneticElement getInstance(Phone phone) {
        boolean has = instances.containsKey(phone);
        if (!has) {
            DiscretePhoneticElement created = new DiscretePhoneticElement(phone);
            instances.put(phone,created);
        }
        return instances.get(phone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscretePhoneticElement that = (DiscretePhoneticElement) o;
        return Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }

    @Override
    public boolean isNull() {
        return phone.isNull();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public String toString() {
        return phone.toString();
    }
}
