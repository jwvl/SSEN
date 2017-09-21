package simulate.french.sixlevel.data;

import forms.morphosyntax.SemSynForm;
import forms.morphosyntax.SyntacticWord;
import util.collections.FrequencyMap;
import util.collections.FrequencyTable;

public class PfcCooccurrences {
    private final FrequencyMap<SyntacticWord> leftFrequencies;
    private final FrequencyMap<SyntacticWord> rightFrequencies;
    private final FrequencyTable<SyntacticWord,SyntacticWord> pairFrequencies;


    public PfcCooccurrences() {
        leftFrequencies = new FrequencyMap<>();
        rightFrequencies = new FrequencyMap<>();
        pairFrequencies = new FrequencyTable<>();
    }

    public void addPair(SyntacticWord left, SyntacticWord right) {
        leftFrequencies.addOne(left);
        rightFrequencies.addOne(right);
        pairFrequencies.addOne(left,right);
    }

    public SemSynForm getUnattestedSsf() {
        int frequency = 1;
        SyntacticWord leftRandom = null;
        SyntacticWord rightRandom = null;
        while(frequency > 0 ) {
            leftRandom = leftFrequencies.drawRandom();
            rightRandom = rightFrequencies.drawRandom();
            frequency = pairFrequencies.getCount(leftRandom, rightRandom);
        }
        return SemSynForm.fromPair(leftRandom,rightRandom);
    }

}
