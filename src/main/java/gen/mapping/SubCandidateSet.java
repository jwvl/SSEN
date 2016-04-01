/**
 *
 */
package gen.mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class SubCandidateSet implements Iterable<FormMapping> {
    public final static SubCandidateSet EMPTY = new SubCandidateSet(Collections.EMPTY_SET);
    private final Collection<FormMapping> contents;

    /**
     * @param contents
     */
    private SubCandidateSet(Collection<FormMapping> contents) {
        this.contents = contents;
    }

    public static SubCandidateSet of(Collection<FormMapping> contents) {
        return new SubCandidateSet(contents);
    }

    public Collection<FormMapping> getContents() {
        return contents;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<FormMapping> iterator() {
        return contents.iterator();
    }


    public int size() {
        return contents.size();
    }
}
