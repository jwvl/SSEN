package util.string;

import java.util.Collection;

public class CollectionSearcher {

    public static <O extends Object> int countMatches(Collection<O> c, String toMatch) {
        int result = 0;
        for (O o : c) {
            if (o.toString().equals(toMatch)) {
                result++;
            }
        }
        return result;
    }

    public static <O extends Object> O stringContains(Collection<O> c, String toMatch) {
        for (O o : c) {
            if (o.toString().contains(toMatch)) {
                return o;
            }
        }
        return null;
    }

    public static <O extends Object> O stringMatches(Iterable<O> c, String toMatch) {
        for (O o : c) {
            if (o.toString().equals(toMatch)) {
                return o;
            }
        }
        return null;
    }

}
