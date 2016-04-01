/**
 *
 */
package forms.primitives.segment;

import com.google.common.primitives.Bytes;
import forms.ElementCollection;
import forms.primitives.Subform;
import gen.rule.string.Side;
import util.arrays.ByteArrayToPhoneUtils;
import util.arrays.ByteArrayUtils;
import util.arrays.ByteBuilder;

import java.util.*;

/**
 * @author jwvl
 * @date Dec 16, 2014
 */
public class PhoneSubForm extends Subform implements ElementCollection<Phone> {
    private final byte[] contents;
    public final static PhoneSubForm NULL_FORM = createNullForm();

    protected PhoneSubForm(Collection<Phone> segments) {
        this.contents = ByteArrayToPhoneUtils.arrayFromPhoneCollection(segments);
    }

    protected PhoneSubForm(Phone[] segments) {
        this.contents = ByteArrayToPhoneUtils.arrayFromPhones(segments);
    }

    protected PhoneSubForm(byte[] contents) {
        this.contents = contents;
    }

    /**
     * @return
     */
    public String contentsAsString() {
        StringBuilder forStringRepresentation = new StringBuilder();
        for (byte b : contents) {
            forStringRepresentation.append(Phone.getByCode(b).getCharValue());
        }
        return forStringRepresentation.toString();
    }

    /**
     * @return The Null PhoneSubForm
     */
    private static PhoneSubForm createNullForm() {
        return new PhoneSubForm(new byte[0]);
    }

    public static PhoneSubForm createFromString(String s) {
        if (s.length() < 1)
            return NULL_FORM;
        return new PhoneSubForm(ByteArrayUtils.fromString(s));
    }

    public static PhoneSubForm createFromByteArray(byte[] input) {
        return new PhoneSubForm(input);
    }

    /**
     * @param phoneString
     * @return
     */
    public static PhoneSubForm createFromCollection(Collection<Phone> segments) {
        if (segments.size() < 1)
            return NULL_FORM;
        return new PhoneSubForm(segments);
    }

    /**
     * @param p
     * @param eDGE
     * @return
     */
    public static PhoneSubForm createFromPhones(Phone... phones) {
        return new PhoneSubForm(phones);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(contents);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PhoneSubForm))
            return false;
        PhoneSubForm other = (PhoneSubForm) obj;
        return Arrays.equals(contents, other.contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.SubForm#isNullElement()
     */
    @Override
    public boolean isNull() {
        return this == NULL_FORM;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.SubForm#size()
     */
    @Override
    public int size() {
        return contents.length;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.SubForm#toString()
     */
    @Override
    public String toString() {
        return this.equals(NULL_FORM) ? "∅" : contentsAsString();
    }

    public String toStringWithoutNulls() {
        return this.equals(NULL_FORM) ? "∅" : contentsAsString().replaceAll("∅", "");
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Phone> iterator() {
        Iterator<Phone> it = new Iterator<Phone>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size();
            }

            @Override
            public Phone next() {
                return Phone.getByCode(contents[currentIndex++]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsList()
     */
    @Override
    public List<Phone> elementsAsList() {
        return ByteArrayToPhoneUtils.arrayToPhonesList(contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsSet()
     */
    @Override
    public Set<Phone> elementsAsSet() {
        return ByteArrayToPhoneUtils.arrayToPhonesSet(contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#recursiveSize()
     */
    @Override
    public int recursiveSize() {
        return size();
    }

    /**
     * @param i
     * @return
     */
    public Phone getElementAt(int i) {
        return Phone.getByCode(contents[i]);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#concatElementsToString(java.lang.String)
     */
    @Override
    public String concatElementsToString(String separator) {
        StringBuilder result = new StringBuilder();
        for (byte b : contents) {
            Phone toAppend = Phone.getByCode(b);
            result.append(toAppend.toString()).append(separator);
        }
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform)
     */
    @Override
    public int getIndexOf(Phone element) {
        return elementsAsList().indexOf(element);
    }

    public Phone getEdgemost(Side side) {
        if (!isNull()) {
            if (side == Side.LEFT) {
                return Phone.getByCode(contents[0]);
            } else if (side == Side.RIGHT) {
                return Phone.getByCode(contents[size() - 1]);
            }
        }
        return Phone.NULL;
    }

    public boolean isAtEdge(PhoneSubForm psf, Side edgeToSearch) {
        if (psf.size() < size())
            return false;
        int thisOffset;
        if (edgeToSearch == Side.LEFT) {
            thisOffset = 0;
        } else {
            thisOffset = psf.size() - (this.size());
        }
        for (int i = 0; i < size(); i++) {
            if (contents[i] != psf.contents[i + thisOffset])
                return false;
        }
        return true;
    }

    public String getPatternString() {
        StringBuilder result = new StringBuilder();
        if (size() == 0)
            return "";
        else if (size() == 1) {
            return Phone.getByCode(contents[0]).getPatternString();
        } else {
            for (Phone p : this) {
                result.append(p.getPatternString());
            }
        }
        return result.toString();
    }

    /**
     * @param eDGE
     * @return
     */
    public PhoneSubForm edgeAppend(Phone toAppend, Side side) {
        byte[] appendByte = {toAppend.getId()};
        byte[] result;
        if (side == Side.LEFT) {
            result = Bytes.concat(appendByte, contents);
        } else {
            result = Bytes.concat(contents, appendByte);
        }
        return new PhoneSubForm(result);
    }

    public byte[] getContents() {
        return contents;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform, int)
     */
    @Override
    public int getIndexOf(Phone subform, int startAt) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsArray()
     */
    @Override
    public Phone[] elementsAsArray() {
        Phone[] phones = new Phone[size()];
        for (int i = 0; i < size(); i++) {
            phones[i] = Phone.getByCode(contents[i]);
        }
        return phones;
    }

    public byte[] getSubArray(int startAt, int endAt) {
        byte[] original = getContents();
        return ByteArrayUtils.getSubArray(original, startAt, endAt);
    }

    /**
     * @param elementsAsArray
     * @return
     */
    public static byte[] concatenate(PhoneSubForm[] elementsAsArray, byte concatByte) {
        ByteBuilder result = new ByteBuilder();
        result.append(concatByte);
        for (PhoneSubForm subForm : elementsAsArray) {
            result.append(subForm.getContents());
            result.append(concatByte);
        }
        return result.build();
    }


}
