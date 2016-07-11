/**
 *
 */
package test;

import eval.harmony.SortedListFactory;
import ranking.SortingValues;
import util.collections.AutoSortedList;
import util.debug.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author jwvl
 * @date Dec 4, 2014
 */
public class TestSortingValues {
    private static int listsToCompare = 5;
    private static int listsToMerge = 100;
    private static int maxRepeats = 10;
    private static Random rnd = new Random();
    private static int valuesToCreate = 10;

    public static void main(String[] args) {
        List<SortingValues> svList = randomList(valuesToCreate);
        Stopwatch.start();
        AutoSortedList<SortingValues> list = SortedListFactory
                .createList(svList);
        Stopwatch.reportElapsedTime("Creation of list:", true);
        AutoSortedList<SortingValues> array = SortedListFactory
                .createArray(svList);
        Stopwatch.reportElapsedTime("Creation of array:", true);
        AutoSortedList<SortingValues> set = SortedListFactory
                .createMultiset(svList);
        Stopwatch.reportElapsedTime("Creation of multiset:", true);
        list.lazySort();
        Stopwatch.reportElapsedTime("Sorting of list:", true);
        array.lazySort();
        Stopwatch.reportElapsedTime("Sorting of array:", true);
        set.lazySort();
        Stopwatch.reportElapsedTime("Sorting of set:", true);

        // Test 2: merging
        List<List<SortingValues>> allLists = new ArrayList<List<SortingValues>>();
        for (int i = 0; i < listsToMerge; i++) {
            allLists.add(randomList(valuesToCreate));
        }

        Stopwatch.start();
        for (int i = 0; i < listsToMerge; i++) {
            AutoSortedList<SortingValues> temp = SortedListFactory
                    .createArray(allLists.get(i));
            array = array.abstractMergeWith(temp);

        }
        Stopwatch.reportElapsedTime("Merging of arrays:", true);

        for (int i = 0; i < listsToMerge; i++) {
            AutoSortedList<SortingValues> temp = SortedListFactory
                    .createList(allLists.get(i));
            list = list.abstractMergeWith(temp);

        }
        Stopwatch.reportElapsedTime("Merging of lists:", true);


        for (int i = 0; i < listsToMerge; i++) {
            AutoSortedList<SortingValues> temp = SortedListFactory
                    .createMultiset(allLists.get(i));
            set = set.abstractMergeWith(temp);
        }

        Stopwatch.reportElapsedTime("Merging of sets:", true);
        list.lazySort();
        Stopwatch.reportElapsedTime("Re-sorting of list:", true);
        array.lazySort();
        Stopwatch.reportElapsedTime("Re-sorting of array:", true);
        set.lazySort();
        Stopwatch.reportElapsedTime("Re-sorting of set:", true);
        System.out.println(String.format("Size of list: %d, getNumSteps of array: %d, getNumSteps of set: %d", list.size(), array.size(), set.size()));

        list.printFirstN("List: ", 10);
        array.printFirstN("Array: ", 10);
        set.printFirstN("Set: ", 10);

        // Test 3: comparing
        AutoSortedList<SortingValues> currMax = SortedListFactory
                .createArray(allLists.get(0));
        for (int i = 1; i < listsToCompare; i++) {
            AutoSortedList<SortingValues> test = SortedListFactory
                    .createArray(allLists.get(i));
            Stopwatch.start();
            currMax = currMax.getOrdering().max(currMax, test);
            Stopwatch.reportElapsedTime(String.format("Array, compare %d: ", i), true);
        }


        currMax = SortedListFactory
                .createList(allLists.get(0));
        for (int i = 1; i < listsToCompare; i++) {
            AutoSortedList<SortingValues> test = SortedListFactory
                    .createList(allLists.get(i));
            Stopwatch.start();
            currMax = currMax.getOrdering().max(currMax, test);
            Stopwatch.reportElapsedTime(String.format("List, compare %d: ", i), true);
        }


        currMax = SortedListFactory
                .createMultiset(allLists.get(0));
        for (int i = 1; i < listsToCompare; i++) {
            AutoSortedList<SortingValues> test = SortedListFactory
                    .createMultiset(allLists.get(i));
            Stopwatch.start();
            currMax = currMax.getOrdering().max(currMax, test);
            Stopwatch.reportElapsedTime(String.format("Set, compare %d: ", i), true);
        }

    }

    private static List<SortingValues> randomList(int size) {
        List<SortingValues> result = new ArrayList<SortingValues>();
        for (int i = 0; i < valuesToCreate; i++) {
            int randomStratum = rnd.nextInt(3);
            SortingValues random = SortingValues.createInstance(randomStratum,
                    rnd.nextDouble() * 100d);
            int nRepeats = rnd.nextInt(maxRepeats) + 1;
            for (int j = 0; j < nRepeats; j++) {
                result.add(random);
            }
        }
        return result;
    }

}
