package util.combinatorics;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import util.collections.Superset;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Generates all _partitions_ of a list/set of Strings
 * Gemaakt voor Marlou's typologische onderzoek
 *
 * @author jwvl
 * @date 27/10/2014
 */
public class PartitionGenerator<O extends Object> {

    private List<O> inputsAsList;
    private List<Superset<O>> partitions;


    public PartitionGenerator(List<O> inputsAsList) {
        this.inputsAsList = inputsAsList;
        partitions = Lists.newArrayList();
        Superset<O> starter = new Superset<O>();
        divide(starter, this.inputsAsList);
    }

    public PartitionGenerator(List<O> inputsAsList, Set<O> prePartitioned) {
        this.inputsAsList = inputsAsList;
        partitions = Lists.newArrayList();
        Superset<O> starter = new Superset<O>();
        starter.addSet(prePartitioned);
        divide(starter, this.inputsAsList);
    }

    public List<Superset<O>> getPartitions() {
        return partitions;
    }

    private void divide(Superset<O> partitioned, List<O> remaining) {

        if (remaining.size() == 0) {
            partitions.add(partitioned);
            return;
        }

        // Otherwise loop through remainder
        O head = remaining.get(0);
        List<O> tail = tail(remaining);

        // Option one: add head to its own set and add this to superset
        Set<O> singleton = Sets.newHashSet();
        singleton.add(head);

        divide(partitioned.createJoin(singleton), tail);
        // Option two: add head to one of the existing sets

        for (Set<O> s : partitioned) {
            Superset<O> grown = partitioned.createAdd(head, s);
            divide(grown, tail);
        }

    }

    private List<O> tail(List<O> original) {
        List<O> result = new ArrayList<O>(original);
        result.remove(0);
        return result;
    }

}
