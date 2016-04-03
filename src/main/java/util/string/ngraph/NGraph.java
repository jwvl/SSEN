/**
 *
 */
package util.string.ngraph;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Jun 6, 2015
 */
public class NGraph {
    private final String contents;

    /**
     * @param contents
     */
    private NGraph(CharSequence contents, int intendedLength) {
        if (contents.length() != intendedLength)
            throw new IllegalArgumentException(contents.toString());
        this.contents = contents.toString();
    }

    public static NGraph create(CharSequence contents, int intendedLength) {
        return new NGraph(contents, intendedLength);
    }

    public int size() {
        return contents.length();
    }

    public static NGraph getNgraph(int startAt, int length, CharSequence original) {
        CharSequence cs = original.subSequence(startAt, startAt + length);
        return create(cs, length);
    }

    public static Collection<NGraph> getAllNgraphs(int length, CharSequence original) {
        int originalLength = original.length();
        List<NGraph> result = new ArrayList<NGraph>(originalLength - length + 1);
        for (int i = 0; i <= originalLength - length; i++) {
            result.add(getNgraph(i, length, original));
        }
        return result;
    }

    public static Collection<NGraph> getUniqueNgraphs(int length, CharSequence original) {
        int originalLength = original.length();
        List<NGraph> result = new ArrayList<NGraph>(originalLength - length + 1);
        for (int i = 0; i <= originalLength - length; i++) {
            result.add(getNgraph(i, length, original));
        }
        return Sets.newHashSet(result);
    }

    @Override
    public String toString() {
        return contents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((contents == null) ? 0 : contents.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof NGraph))
            return false;
        NGraph other = (NGraph) obj;
        if (contents == null) {
            if (other.contents != null)
                return false;
        } else if (!contents.equals(other.contents))
            return false;
        return true;
    }


}
