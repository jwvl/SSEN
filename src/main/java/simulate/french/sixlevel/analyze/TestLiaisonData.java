package simulate.french.sixlevel.analyze;

import forms.Form;
import forms.FormPair;
import forms.morphosyntax.SemSynForm;
import learn.data.PairDistribution;
import simulate.french.sixlevel.data.PfcData;
import util.collections.FrequencyTable;

import java.util.Set;

/**
 * Created by janwillem on 21/06/2017.
 */
public class TestLiaisonData {
    public TestLiaisonData(PfcData pfcData) {
        this.pfcData = pfcData;
    }
    private int countTo = 10;
    private PairDistribution liaisonsOnly;
    private final PfcData pfcData;
    public FrequencyTable<Form, Form> realDistribution;

    public void init() {
        Set<SemSynForm> ssfs = pfcData.getLiaisonSsfs();
        PairDistribution pairDistribution = pfcData.getPairDistribution();
        PairDistribution randomDistribution = new PairDistribution("randomlyDrawn");
        Set<FormPair> liaisingAnswers;
        for (SemSynForm ssf: ssfs) {
            for (int i=0; i < countTo; i++) {
                FormPair drawn = pairDistribution.drawFormPair();
                randomDistribution.add(drawn,1);
            }
        }
        realDistribution = randomDistribution.toFrequencyTable();
    }

    public void testFrequencyTable(FrequencyTable<Form, Form> freqTable) {
        for (FormPair formPair: pfcData.getLiaisonPairs()) {
            double aCount = freqTable.getFraction(formPair.left(), formPair.right());
            double bCount = realDistribution.getFraction(formPair.left(), formPair.right());
            System.out.println("Liaison pair " +formPair);
            System.out.println("Real data: " + bCount);
            System.out.println("Learners:" + aCount);
        }

    }
}
