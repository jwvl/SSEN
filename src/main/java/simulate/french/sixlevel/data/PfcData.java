package simulate.french.sixlevel.data;

import forms.FormPair;
import learn.data.PairDistribution;

import java.util.Set;

/**
 * Created by janwillem on 17/04/2017.
 * Helper class to contain both PairDistribution and liaison information
 */
public class PfcData {
    public final PairDistribution pairDistribution;
    private Set<FormPair> liaisonItems;

    public PfcData(PairDistribution pairDistribution, Set<FormPair> liaisonItems) {
        this.pairDistribution = pairDistribution;
        this.liaisonItems = liaisonItems;
    }
}
