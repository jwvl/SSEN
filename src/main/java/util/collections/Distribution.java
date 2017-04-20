package util.collections;

import java.util.*;
import java.util.Map.Entry;


public class Distribution<O extends Object> {

    protected String name;
    protected Map<O, Integer> frequencies;
    private List<O> keys;
    private int keySetSize;
    private int[] cumulativeProbabilities;
    private boolean probabilitiesOutdated;
    private final Random r;

    public Distribution(String name) {
        this.name = name;
        frequencies = new HashMap<O, Integer>();
        probabilitiesOutdated = true;
        r = new Random();
    }

    public Distribution(List<O> toAdd, String name) {
//		this.name = name;
//		frequencies = new HashMap<O, Integer>();
//		probabilitiesOutdated = true;
//		r = new Random();
        this(name);
        for (O o : toAdd) {
            this.addOne(o);
        }
    }

    public void setFrequency(O o, int freq) {
        Integer value = freq;
        frequencies.put(o, value);

        probabilitiesOutdated = true;
    }

    public int getFrequency(O o) {
        Integer toReturn = frequencies.get(o);
        return toReturn == null ? 0 : toReturn;

    }

    public void addOne(O o) {
        add(o, 1);
    }

    public Distribution<O> copy() {
        Distribution<O> result = new Distribution<O>(this.name + "-copy");
        for (O o : keys) {
            int freq = getFrequency(o);
            result.setFrequency(o, freq);
        }
        return result;
    }

    public void add(O o, int toAdd) {
        int total = toAdd;
        if (frequencies.containsKey(o))
            total += frequencies.get(o);
        setFrequency(o, total);
    }

    public int getNumPairs() {
        return frequencies.size();
    }

    public void setDistribution() {
        keySetSize = frequencies.size();
        Set<Entry<O, Integer>> entrySet = frequencies.entrySet();
        List<Entry<O, Integer>> entries = new ArrayList<Entry<O, Integer>>();

        for (Entry<O, Integer> entry : entrySet) {
            entries.add(entry);
        }
        // Sort by integer values
        Collections.sort(entries, new Comparator<Entry<O, Integer>>() {

            @Override
            public int compare(Entry<O, Integer> arg0, Entry<O, Integer> arg1) {
                return arg0.getValue().compareTo(arg1.getValue());
            }
        });
        Collections.reverse(entries);
        keys = new ArrayList<O>(keySetSize);
        cumulativeProbabilities = new int[keySetSize];
        Iterator<Entry<O, Integer>> itr = entries.iterator();
        int i = 0;
        int cumProb = 0;
        while (itr.hasNext()) {
            O currObject = itr.next().getKey();
            keys.add(currObject);
            cumProb += frequencies.get(currObject);
            cumulativeProbabilities[i] = cumProb;
            i++;
        }
        probabilitiesOutdated = false;
    }

    public O drawItem() {
        if (probabilitiesOutdated)
            setDistribution();
        int random = r.nextInt(cumulativeProbabilities[keySetSize - 1]);
        for (int i = 0; i < keys.size(); i++) {
            if (random < cumulativeProbabilities[i]) {
                return keys.get(i);
            }
        }
        return null;
    }

    public void printAsList() {
        if (probabilitiesOutdated)
            setDistribution();
        for (O v : keys) {
            String toPrint = v.toString();
            toPrint += "\t:\t";
            toPrint += frequencies.get(v);
            System.out.println(toPrint);
        }

    }

    public int getSize() {
        int result = 0;
        for (Integer i : frequencies.values())
            result += i;
        return result;
    }

    public Set<O> getKeySet() {
        return frequencies.keySet();
    }

    public void addContentsOfDistribution(Distribution<O> d) {
        for (O o : d.getKeySet()) {
            int freq = d.getFrequency(o);
            add(o, freq);
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public void sortKeys(Comparator<O> c) {
        if (probabilitiesOutdated)
            setDistribution();
        Collections.sort(keys, c);
    }

    public int getMaxFrequency() {
        int max = 0;
        setDistribution();
        for (O key : getKeySet()) {
            int test = frequencies.get(key);
            if (test > max)
                max = test;
        }
        return max;
    }

    public List<O> flatten() {
        List<O> result = new ArrayList<O>();
        for (O item : this.keys) {
            for (int i = 0; i < getFrequency(item); i++) {
                result.add(item);
            }
        }
        return result;
    }

    public List<Distribution<O>> split(double fraction) {
        if (probabilitiesOutdated)
            setDistribution();
        int totalCount = cumulativeProbabilities[keySetSize - 1];
        int partOneSize = (int) (totalCount * fraction);
        int partTwoSize = totalCount - partOneSize;
        // Flatten this distribution to pass it to sublisting routine
        List<O> bag = this.flatten();
        List<List<O>> splitAsLists = ListUtils.splitList(bag, new int[]{partOneSize, partTwoSize});

        List<Distribution<O>> result = new ArrayList<Distribution<O>>(2);
        int count = 1;
        for (List<O> subList : splitAsLists) {
            String newName = this.name + "-split" + (count++);
            Distribution<O> subDistribution = new Distribution<O>(subList, newName);
            result.add(subDistribution);
        }
        return result;
    }


}
