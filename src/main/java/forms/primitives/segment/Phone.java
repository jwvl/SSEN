/**
 *
 */
package forms.primitives.segment;

import com.google.common.collect.Lists;
import forms.phon.Sonority;
import forms.primitives.Subform;
import io.MyStringTable;
import util.arrays.ByteArrayUtils;
import util.string.Regex;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basically a wrapper for a 'char', but serves as an Element within subforms
 * (e.g. syllables, lexical items)...
 *
 * @author jwvl
 * @date 15/11/2014
 */
public class Phone extends Subform implements Comparable<Phone> {

    private Sonority sonority;
    private final byte id;
    private final char contentAsChar;
    private String stringAlias;

    private static final byte idOffset = 127;
    private static byte idCounter = 0 - idOffset;
    private static Phone[] byteMap = new Phone[256];
    private static Map<Character, Phone> charPhoneMap = new HashMap<Character, Phone>();
    private static PhoneInventory inventory = new PhoneInventory();
    public static Phone NULL = new Phone('∅');


    private Phone(char c) {
        this.contentAsChar = c;
        this.stringAlias = String.valueOf(c);
        this.id = idCounter;
        byteMap[((int) id) + idOffset] = this;
        idCounter++;
        charPhoneMap.put(c, this);
        inventory.addPhone(this);
        System.out.println("Created phone " + c + ", id: #" + ((int) id + 127));
    }

    public static Phone getInstance(char c) {
        Phone result = charPhoneMap.get(c);
        if (result == null) {
            result = new Phone(c);
        }

        return result;

    }

    /**
     * @param b
     * @return
     */
    public static Phone getByCode(byte b) {
        return byteMap[((int) b) + idOffset];
    }


    /*
     * (non-Javadoc)
     *
     * @see gen.forms.primitives.SubForm#toString()
     */
    @Override
    public String toString() {
        return this == NULL ? "∅" : String.valueOf(contentAsChar);
    }

    public char getCharValue() {
        return contentAsChar;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + contentAsChar;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Phone))
            return false;
        Phone other = (Phone) obj;
        return contentAsChar == other.contentAsChar;
    }

    /**
     * @param s
     * @return
     */
    public static Phone[] toPhoneArray(String s) {
        int length = s.length();
        Phone[] result = new Phone[length];
        char[] asChars = s.toCharArray();
        for (int i = 0; i < length; i++) {
            char iChar = asChars[i];
            Phone toAdd = Phone.getInstance(iChar);
            result[i] = toAdd;
        }
        return result;
    }

    public boolean isNull() {
        return this == NULL;
    }

    public int size() {
        return 1;
    }

    public int compareTo(Phone arg0) {
        if (contentAsChar < arg0.contentAsChar) {
            return -1;
        } else if (contentAsChar > arg0.contentAsChar) {
            return 1;
        } else {
            return 0;
        }
    }

    public byte getId() {
        return id;
    }

    /**
     * @return the stringAlias
     */
    public String getStringAlias() {
        return stringAlias;
    }

    /**
     * @param stringAlias the stringAlias to set
     */
    public void setStringAlias(String stringAlias) {
        this.stringAlias = stringAlias;
    }

    public String getPatternString() {
        if (this == NULL)
            return "";
        return Regex.charToEscapeString(contentAsChar);
    }

    public static String decode(byte... bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(Phone.getByCode(b).toString());
        }
        return builder.toString();
    }

    public static byte[] encode(String string) {
        return ByteArrayUtils.fromString(string);
    }

    public static Collection<Phone> getFromStringTable(MyStringTable mst,
                                                       String charColName, String stringColName, String sonorityColName) {
        List<Phone> result = Lists.newArrayList();
        for (int i = 0; i < mst.getNumRows(); i++) {
            char asChar = mst.getString(i, charColName).charAt(0);
            String asString = mst.getString(i, stringColName);
            String sonorityString = mst.getString(i, sonorityColName);
            Phone phone = getInstance(asChar);
            phone.setStringAlias(asString);
            phone.setSonority(Sonority.valueOf(sonorityString));
            result.add(phone);
        }
        return result;
    }

    public Sonority getSonority() {
        return sonority;
    }

    public void setSonority(Sonority sonority) {
        Sonority.addToMap(id, sonority);
        this.sonority = sonority;
    }

    /**
     * @return
     */
    public static PhoneInventory getInventory() {
        return inventory;
    }

}
