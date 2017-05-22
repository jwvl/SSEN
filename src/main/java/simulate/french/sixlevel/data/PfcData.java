package simulate.french.sixlevel.data;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import forms.FormPair;
import forms.morphosyntax.SemSynForm;
import forms.phon.flat.PhoneticForm;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import learn.data.PairDistribution;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by janwillem on 17/04/2017.
 * Helper class to contain both PairDistribution and liaison information
 */
public class PfcData {
    public final PairDistribution pairDistribution;
    private Set<FormPair> liaisonItems;
    private final static Level LEFT_LEVEL= BiPhonSix.getSemSynFormLevel();
    private final static Level RIGHT_LEVEL = BiPhonSix.getPhoneticLevel();

    public PfcData(PairDistribution pairDistribution, Set<FormPair> liaisonItems) {
        this.pairDistribution = pairDistribution;
        this.liaisonItems = liaisonItems;
    }

    public static PfcData readFromFile(String filename) {
        URL url = Resources.getResource(filename);
        List<String> lines = new ArrayList<>();
        PairDistribution pairDistribution = new PairDistribution(filename);
        Set<FormPair> liaisonItems = Sets.newHashSet();
        try {
            lines = Resources.readLines(url, Charsets.UTF_8);
            for (String line: lines) {
                String[] parts = line.split("\t");
                if (parts.length == 4) {
                    String ssf = parts[0];
                    String pf = parts[1];
                    int freq = Integer.parseInt(parts[2]);
                    int liaisonInt = Integer.parseInt(parts[3]);
                    boolean liaisonItem = (liaisonInt == 1);
                    SemSynForm leftForm = SemSynForm.createFromString(ssf, LEFT_LEVEL);
                    PhoneticForm rightForm = PhoneticForm.createFromString(pf);
                    FormPair formPair = FormPair.of(leftForm,rightForm);
                    pairDistribution.add(leftForm, rightForm, freq);
                    if (liaisonItem) {
                        liaisonItems.add(formPair);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PfcData(pairDistribution, liaisonItems);
    }
}
