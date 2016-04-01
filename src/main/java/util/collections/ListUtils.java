package util.collections;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListUtils {

    public static <O> List<O> union(List<O> a, List<O> b) {
        ArrayList<O> result = new ArrayList<O>(a.size());
        for (O c : a) {
            if (b.contains(c))
                result.add(c);
        }
        result.trimToSize();
        return result;
    }

    public static <O extends Object> List<O> difference(List<O> a, List<O> b) {
        ArrayList<O> result = new ArrayList<O>(a.size());
        for (O c : a) {
            if (!b.contains(c))
                result.add(c);
        }
        result.trimToSize();
        return result;
    }

    public static <T extends Comparable<? super T>> List<T> asSortedList(
            Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        Collections.sort(list);
        return list;
    }

    public static <O> List<List<O>> splitList(List<O> input, int[] sizes) {
        int numberOfListsToReturn = sizes.length;
        List<List<O>> result = new ArrayList<List<O>>(numberOfListsToReturn);
        List<O> inputCopy = new ArrayList<O>(input);
        Collections.shuffle(inputCopy);
        int totalCount = 0;
        for (int subSize : sizes) {
            List<O> currentSubList = new ArrayList<O>(subSize);
            for (int i = 0; i < subSize; i++) {
                currentSubList.add(inputCopy.get(totalCount));
                totalCount++;
            }
            result.add(currentSubList);
        }
        return result;
    }

}
