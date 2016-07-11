/**
 *
 */
package forms.phon.flat;

import forms.LinearForm;
import forms.primitives.boundary.Edge;
import forms.primitives.boundary.EdgeIndex;
import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * An attempt to make a general 'phoneme sequence' class.
 *
 * @author jwvl
 * @date 16/02/2016
 */
public abstract class PhoneSequence implements LinearForm<Phone> {

    private final PhoneSubForm contents;
    private final EdgeIndex boundaries;

    /**
     * @param contents
     */
    public PhoneSequence(PhoneSubForm contents, EdgeIndex boundaries) {
        this.contents = contents;
        this.boundaries = boundaries;
    }

    public PhoneSequence(byte[] contentsAsBytes, EdgeIndex boundaries) {
        this(PhoneSubForm.createFromByteArray(contentsAsBytes), boundaries);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            appendPossibleBoundary(i, builder);
            builder.append(contents.getElementAt(i).toPrettyString());
        }
        appendPossibleBoundary(contents.size(), builder);
        return builder.toString();
    }

    private void appendPossibleBoundary(int index, StringBuilder builder) {
        for (Edge edgeType : Edge.values()) {
            if (index > boundaries.sequenceLength()) {
                System.out.println("Should not occur! Sequence is " + getContents().toString());
            }
            if (boundaries.hasBoundaryAt(edgeType, index)) {
                builder.append(edgeType.getSymbol());
            }
        }
    }

    /**
     * @return
     */
    public byte[] getByteArray() {
        return contents.getContents();
    }

    public PhoneSubForm getContents() {
        return contents;
    }

    public EdgeIndex getBoundaries() {
        return boundaries;
    }

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public int getIndexOf(Phone subform, int startAt) {
        return contents.getIndexOf(subform, startAt);
    }

    @Override
    public Phone[] elementsAsArray() {
        return contents.elementsAsArray();
    }

    @Override
    public List<Phone> elementsAsList() {
        return contents.elementsAsList();
    }

    @Override
    public Set<Phone> elementsAsSet() {
        return contents.elementsAsSet();
    }

    @Override
    public int recursiveSize() {
        return contents.recursiveSize();
    }

    @Override
    public int getIndexOf(Phone subform) {
        return contents.getIndexOf(subform);
    }

    @Override
    public String concatElementsToString(String separator) {
        return contents.concatElementsToString(separator);
    }

    @Override
    public Iterator<Phone> iterator() {
        return contents.iterator();
    }

    public byte[] getSubSequence(Edge edgeType, int atIndex) {
        return boundaries.getSubSequence(contents.getContents(), atIndex, edgeType);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contents == null) ? 0 : contents.hashCode());
        result = prime * result + ((boundaries == null) ? 0 : boundaries.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PhoneSequence))
            return false;
        PhoneSequence other = (PhoneSequence) obj;
        if (contents == null) {
            if (other.contents != null)
                return false;
        } else if (!contents.equals(other.contents))
            return false;
        if (boundaries == null) {
            if (other.boundaries != null)
                return false;
        } else if (!boundaries.equals(other.boundaries))
            return false;

        return true;
    }

    @Override
    public int getLevelIndex() {
        return getLevel().myIndex();
    }


}
