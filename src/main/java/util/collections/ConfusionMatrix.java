package util.collections;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import forms.Form;
import forms.FormPair;
import learn.data.PairDistribution;

import java.util.Map;
import java.util.Set;

/**
 * Created by janwillem on 24/06/2017.
 */
public class ConfusionMatrix {
    private final Table<Form,Form,Integer> table;
    private final FrequencyMap<Form> columnCounts;

    public ConfusionMatrix() {
        table = HashBasedTable.create();
        columnCounts = new FrequencyMap<>();
    }

    public void addMapping(Form from, Form to, int count) {
        int oldCount = getCount(from,to);
        int newCount = oldCount+count;
        table.put(from,to,newCount);
        columnCounts.add(from,count);
    }

    public void addMapping(Form from, Form to) {
        addMapping(from, to, 1);
    }

    public Set<Form> getInputs() {
        return table.rowKeySet();
    }

    public int getCount(Form from, Form to) {
        Integer result = table.get(from,to);
        if (result == null) {
            return 0;
        }
        return result;
    }

    public int getCount(FormPair pair) {
        return getCount(pair.left(), pair.right());
    }

    public void addFormPair(FormPair mapping, int count) {
        addMapping(mapping.left(), mapping.right(), count);
    }

    public void addFormPair(FormPair mapping) {
        addFormPair(mapping, 1);
    }

    public Map<Form,Integer> getCounts(Form input) {
        return table.row(input);
    }

    public Map<Form,Double> getProportions(Form input) {
        Map<Form,Integer> counts = getCounts(input);
        Map<Form,Double> proportions = Maps.newHashMapWithExpectedSize(counts.size());
        for (Form f: counts.keySet()) {
            double proportion = counts.get(f) / ((double) columnCounts.getCount(input));
            proportions.put(f, proportion);
        }
        return proportions;
    }

    public double getProportion(Form input, Form output) {
        return getCount(input,output) / ((double) columnCounts.getCount(input));
    }

    public static double rmse(ConfusionMatrix expected, ConfusionMatrix actual) {
        double summedSquaredErrors = 0;
        int counts = 0;
        Set<Form> inputs = expected.getInputs();
        for (Form input: inputs) {
            Map<Form,Double> proportions = expected.getProportions(input);
            for (Form output: proportions.keySet()) {
                double expValue = proportions.get(output);
                double actualValue = actual.getProportion(input,output);
                double squaredError = squaredError(expValue,actualValue);
                summedSquaredErrors += squaredError;
                counts++;
            }
        }
        double meanSquaredError = summedSquaredErrors / counts;
        double rmse = Math.sqrt(meanSquaredError);
        return rmse;
    }

    private static double squaredError(double expectedValue, double actualValue) {
        double diff = expectedValue-actualValue;
        return diff*diff;
    }

    public void printToString() {
        for (Form input: getInputs()) {
            System.out.println(input + "\t"+getCounts(input));
            Map<Form,Integer> counts = getCounts(input);
            for (Form output: counts.keySet()) {
                int count = counts.get(output);
                double prop = getProportion(input,output);
                System.out.println("\t"+output+"\t"+count+"\t"+String.format("%.3f",prop));
            }
        }
    }

    public static ConfusionMatrix fromPairDistribution(PairDistribution pairDistribution) {
        ConfusionMatrix result = new ConfusionMatrix();
        for (FormPair fp: pairDistribution.getKeys()) {
            int count = pairDistribution.getFrequency(fp);
            result.addFormPair(fp,count);
        }
        return result;
    }
}
