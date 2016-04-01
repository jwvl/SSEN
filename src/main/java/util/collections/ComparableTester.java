package util.collections;

import java.util.List;

public class ComparableTester {

    public static void printMaxFinder(List<Comparable> items) {
        @SuppressWarnings("rawtypes")
        Comparable currMax = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            @SuppressWarnings("rawtypes")
            Comparable currItem = items.get(i);
            @SuppressWarnings("unchecked")
            int compare = currItem.compareTo(currMax);
            System.out.println(comparisonPrint(currItem, currMax, compare));
            if (compare > 0)
                currMax = currItem;
        }
    }

    public static String comparisonPrint(Object o, Object p, int compare) {
        String sep;
        if (compare < 0)
            sep = " < ";
        else if (compare > 0)
            sep = " > ";
        else
            sep = " = ";
        return String.format("%s %s %s\n", o, sep, p);
    }

}
