/**
 *
 */
package util.string;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author jwvl
 * @date Nov 26, 2014
 */
public class CollectionPrinter {

    public static <O> String collectionToStringlines(Collection<O> l) {
        return collectionToStringlines(l, l.size());
    }

    public static <O> String collectionToStringlines(Collection<O> l, int stopAt) {
        StringBuffer result = new StringBuffer();
        int nItems = l.size();
        int numberLength = String.valueOf(nItems).length();
        int counter = 1;
        for (O o : l) {
            result.append(Strings.padStart(String.valueOf(counter++), numberLength, '0'));
            result.append(": ");
            result.append(o.toString());
            result.append("\n");
            if (counter == stopAt) {
                return result.toString();
            }
        }
        return result.toString();
    }

    public static <O> void print(Collection<O> c, int stopAt) {
        System.out.println(collectionToStringlines(c, stopAt));
    }

    public static <O> void print(Collection<O> c) {
        System.out.println(collectionToStringlines(c));
    }

    public static <O> String collectionToString(Collection<O> l, String sep) {
        if (l.isEmpty())
            return ("<empty collection>");
        StringBuffer result = new StringBuffer();
        Iterator<O> it = l.iterator();
        result.append(it.next());
        while (it.hasNext()) {
            O o = it.next();
            result.append(sep);
            result.append(o.toString());
        }
        return result.toString();
    }

    /**
     * @param Map to print
     */
    public static <O, P> void printMap(Map<O, P> map) {
        for (O o : map.keySet()) {
            System.out.println(o + " :: " + map.get(o));
        }

    }

}
