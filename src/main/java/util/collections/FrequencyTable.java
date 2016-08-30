package util.collections;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Set;

/**
 * Created by janwillem on 20/08/16.
 */
public class FrequencyTable<O extends Object, P extends Object> {
    private final Table<O,P,Integer> counts;
    private final FrequencyMap<O> columnCounts;

    private FrequencyTable(Table<O, P, Integer> counts, FrequencyMap<O> columnCounts) {
        this.counts = counts;
        this.columnCounts = columnCounts;
    }

    public FrequencyTable() {
        this(HashBasedTable.create(),new FrequencyMap<O>());
    }

    public void add(O o, P p, int count) {
        int oldValue= getCount(o,p);
        counts.put(o,p,oldValue+count);
        columnCounts.add(o,count);
    }

    public void addOne(O o, P p) {
        add(o,p,1);
    }

    public int getCount(O o, P p) {
        int result = 0;
        if (counts.contains(o,p)) {
            result = counts.get(o,p);
        }
        return result;
    }

    public double getFraction(O o, P p) {
        int opCount = getCount(o,p);
        if (opCount == 0)
            return 0.0;
        return opCount / (double) columnCounts.getCount(o);
    }

    public Set<O> getColumnSet() {
        return columnCounts.contents.elementSet();
    }

    public Set<P> getRowsForColumn(O o) {
        return counts.row(o).keySet();
    }
}
