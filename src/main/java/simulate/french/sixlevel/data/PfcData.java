package simulate.french.sixlevel.data;

import candidates.Candidate;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import forms.Form;
import forms.FormPair;
import forms.morphosyntax.SemSynForm;
import forms.phon.flat.PhoneticForm;
import gen.rule.string.Side;
import grammar.Grammar;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import graph.Direction;
import learn.ViolatedCandidate;
import learn.data.PairDistribution;
import learn.data.SinglesFilter;
import util.collections.ConfusionMatrix;

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
    private Set<FormPair> liaisonPairs;
    private final Set<SemSynForm> liaisonSsfs;
    private final ConfusionMatrix liaisonConfusions;

    private final static Level LEFT_LEVEL= BiPhonSix.getSemSynFormLevel();
    private final static Level RIGHT_LEVEL = BiPhonSix.getPhoneticLevel();

    public PfcData(PairDistribution pairDistribution, Set<FormPair> liaisonPairs) {
        this.pairDistribution = pairDistribution;
        this.liaisonPairs = liaisonPairs;
        this.liaisonSsfs = Sets.newHashSet();
        for (FormPair formPair: liaisonPairs) {
            liaisonSsfs.add((SemSynForm)formPair.left());
        }
        liaisonConfusions = createLiaisonConfusions();
    }

    private ConfusionMatrix createLiaisonConfusions() {
        ConfusionMatrix result = new ConfusionMatrix();
        for (FormPair fp: pairDistribution.getKeys()) {
            if (liaisonSsfs.contains(fp.left())) {
                int count = pairDistribution.getFrequency(fp);
                result.addFormPair(fp,count);
            }
        }
        return result;
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

    public PairDistribution getPairDistribution() {
        return pairDistribution;
    }

    public Set<FormPair> getLiaisonPairs() {
        return liaisonPairs;
    }


    public Set<SemSynForm> getLiaisonSsfs() {
        return liaisonSsfs;
    }

    public PairDistribution getLiaisonDistribution() {
        PairDistribution result = new PairDistribution("LiaisonPairs");
        for (FormPair formPair: pairDistribution.getKeySet()) {
            SemSynForm ssf = (SemSynForm) formPair.left();
            if (liaisonSsfs.contains(ssf)) {
                int count = pairDistribution.getFrequency(formPair);
                result.add(formPair, count);
            }
        }
        return result;
    }

    public boolean pairIsInData(Form from, Form to) {
        return liaisonConfusions.getCount(from, to) > 0;
    }

    public ConfusionMatrix getLiaisonConfusions() {
        return liaisonConfusions;
    }

    public double[] testGrammar(Grammar grammar, int numItemsToDraw, double evaluationNoise) {
        double[] result = new double[3];
        int liaising = 0;
        int inData = 0;
        PairDistribution liaisonDistribution = getLiaisonDistribution().filter(new SinglesFilter());
        for (int i=0; i < numItemsToDraw; i++) {
            FormPair drawn = liaisonDistribution.drawFormPair();
            FormPair input = drawn.getUnlabeled(Direction.RIGHT);
            ViolatedCandidate vCandidate = grammar.getWinner(input,true, evaluationNoise);
            Candidate candidate = vCandidate.getCandidate();
            Form left = input.left();
            Form right = candidate.getEndForm(Side.RIGHT, false);
            FormPair asPair = FormPair.of(left,right);
            if (pairDistribution.getKeySet().contains(asPair)) {
                inData++;
                if (liaisonPairs.contains(FormPair.of(left,right))) {
                    liaising++;
                }
            }
            else {
                System.out.println(asPair +" not in data...");
            }

        }
        int notInData = numItemsToDraw - inData;
        result[0] = notInData / (double) numItemsToDraw;
        result[1] = inData / (double) numItemsToDraw;
        result[2] = liaising / (double) numItemsToDraw;

        System.out.println("Finished testing on liaising items");
        System.out.printf("Out of data: %2f, in data: %2f, liaising: %2f\n",result[0],result[1],result[2]);
        return result;
    }



}
