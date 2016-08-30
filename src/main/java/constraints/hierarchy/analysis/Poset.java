package constraints.hierarchy.analysis;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by janwillem on 20/08/16.
 */
public class Poset<O extends Object> {
    private List<Set<O>> contents;

    public Poset(List<Set<O>> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Poset{\n");
        for (int i=0; i < contents.size(); i++) {
            sb.append(" ").append(i).append("\t").append(contents.get(i));
            sb.append("\n");
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poset<?> poset = (Poset<?>) o;
        return Objects.equals(contents, poset.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}
