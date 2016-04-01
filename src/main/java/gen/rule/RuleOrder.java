/**
 *
 */
package gen.rule;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date 08/11/2014
 */
public class RuleOrder implements Iterable<RewriteRule> {
    private final ImmutableList<RewriteRule> contents;

    public static RuleOrder createInstance(List<RewriteRule> contents) {
        RuleOrder result = new RuleOrder(contents);
        return result;
    }

    public static Set<RuleOrder> getAllOrders(Set<RewriteRule> ruleset) {
        Set<RuleOrder> result = new HashSet<RuleOrder>();
        Set<Set<RewriteRule>> powerSet = Sets.powerSet(ruleset);
        for (Set<RewriteRule> subset : powerSet) {
            for (List<RewriteRule> perm : Collections2.permutations(subset)) {
                result.add(RuleOrder.createInstance(perm));
            }
        }
        return result;
    }

    private RuleOrder(List<RewriteRule> contents) {
        super();
        this.contents = ImmutableList.copyOf(contents);
    }

    @Override
    public Iterator<RewriteRule> iterator() {
        return contents.iterator();
    }

    @Override
    public String toString() {
        return makeString();
    }

    public String makeString() {
        StringBuffer result = new StringBuffer("( ");
        Iterator<RewriteRule> iterator = iterator();
        while (iterator.hasNext()) {
            result.append(iterator.next());
            if (iterator.hasNext()) {
                result.append(" >> ");
            } else {
                result.append(" )");
            }
        }
        return result.toString();
    }

    /**
     * @return
     */
    public List<RewriteRule> getAsList() {
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
        if (!(obj instanceof RuleOrder))
            return false;
        RuleOrder other = (RuleOrder) obj;
        if (contents == null) {
            if (other.contents != null)
                return false;
        } else if (!contents.equals(other.contents))
            return false;
        return true;
    }


}
