/**
 *
 */
package forms.phon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import forms.primitives.segment.Phone;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author jwvl
 * @date May 19, 2015
 */
public enum Sonority {
    X("Unknown"),
    C("Consonant"),
    P("Stop"),
    F("Fricative"),
    N("Nasal"),
    L("Liquid"),
    J("Semivowel"),
    V("Vowel"); // Ordered by sonority!


    private final String fullName;

    /**
     * @param fullName
     */
    Sonority(String fullName) {
        this.fullName = fullName;
    }

    private static Sonority[] map;
    private static Multimap<Sonority, Phone> reverseMap;

    static {
        map = new Sonority[256];
        reverseMap = HashMultimap.create();
        Arrays.fill(map, X);
    }


    public static void addToMap(byte c, Sonority sonority) {
        map[c + 127] = sonority;
        reverseMap.put(sonority, Phone.getByCode(c));
    }

    public static Sonority of(byte c) {
        Sonority value = map[c+127];
        if (value.equals(X)) {
            System.err.println("Sonority not found for phone " +Phone.decode(c));
            System.err.println("!");
        }
        return map[c + 127];
    }

    public Collection<Phone> getPhones() {
        return reverseMap.get(this);
    }

    /**
     * @return
     */
    public static Sonority valueOf(char c) {
        return valueOf(String.valueOf(c));
    }

    /**
     * @return
     */
    public int getSonorityValue() {
        return ordinal();
    }

    public String toString() {
        return name();
    }

    public Sonority toTwoClass() {
        if (this == V)
            return V;
        else return (C);
    }

    public String getAllPhones() {
        StringBuilder builder = new StringBuilder();
        for (Phone p: getPhones()) {
            builder.append(p.getCharValue());
        }
        return builder.toString();
    }

    public Sonority toFiveClass() {
        switch (this) {
            case X:
                return X;
            case C:
                return P;
            case P:
                return P;
            case F:
                return P;
            case N:
                return N;
            case L:
                return L;
            case J:
                return J;
            case V:
                return V;
        }
        return X;
    }

}
