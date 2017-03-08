package learn.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import forms.FormPair;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by janwillem on 15/10/2016.
 */
public class DistributionErrorTable {
    private final Table<FormPair,UUID,Integer> squaredErrors;

    public DistributionErrorTable() {
        squaredErrors = HashBasedTable.create();
    }

    public void addLearner(UUID uuid, PairDistribution original, PairDistribution learnersDistribution) {
        for (FormPair formPair: original.getKeys()) {
            int learnerNum = learnersDistribution.getFrequency(formPair);
            squaredErrors.put(formPair,uuid,learnerNum);
        }
    }

    public String toTableString() {
        StringBuilder builder = new StringBuilder();
        Set<FormPair> allPairs = squaredErrors.rowKeySet();
        Set<UUID> allUuids = squaredErrors.columnKeySet();
        List<FormPair> pairList = Lists.newArrayList(allPairs);
        List<UUID> uuidList = Lists.newArrayList(allUuids);
        for (FormPair formPair: pairList) {
            builder.append("\t"+formPair);
        }
        for (UUID uuid: uuidList) {
            builder.append("\n").append(uuid.toString());
        for (FormPair formPair: pairList) {
            builder.append("\t");
            builder.append(squaredErrors.get(formPair,uuid));
            }
        }
        return builder.toString();
    }
}
