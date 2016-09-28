package io.pfc.ngrams;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import util.collections.FrequencyMap;

/**
 * Created by janwillem on 26/09/16.
 */
public class TwoWayBigramMap {
    private final Multimap<String,Bigram> leftToRight;
    private final Multimap<String,Bigram> rightToLeft;
    private final FrequencyMap<Bigram> pairFrequencies;

    private TwoWayBigramMap(Multimap<String, Bigram> leftToRight, Multimap<String, Bigram> rightToLeft, FrequencyMap<Bigram> pairFrequencies) {
        this.leftToRight = leftToRight;
        this.rightToLeft = rightToLeft;
        this.pairFrequencies = pairFrequencies;
    }

    public static TwoWayBigramMap createNew() {
        Multimap<String,Bigram> leftToRight = HashMultimap.create();
        Multimap<String,Bigram> rightToLeft = HashMultimap.create();
        FrequencyMap<Bigram> frequencies = new FrequencyMap<>();
        return new TwoWayBigramMap(leftToRight,rightToLeft,frequencies);
    }

    public void addBigram(Bigram bigram) {
        leftToRight.put(bigram.left, bigram);
        rightToLeft.put(bigram.right,bigram);
        pairFrequencies.addOne(bigram);

    }
}
