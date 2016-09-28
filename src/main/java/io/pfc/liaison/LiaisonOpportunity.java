package io.pfc.liaison;

import io.pfc.preprocess.WordCleaner;

import java.util.Objects;

/**
 * Created by janwillem on 26/09/16.
 */
public class LiaisonOpportunity {
    //private static final Pattern UNDESIRABLES = Pattern.compile("[][(){},.;!?<>%]");
    private final String firstWordOrth;
    private final String secondWordOrth;
    private final String firstPosTag;
    private final String secondPosTag;
    private final String liaisonConsonant;


    public LiaisonOpportunity(String firstWordOrth, String secondWordOrth, String firstPosTag, String secondPosTag, String liaisonConsonant) {
        this.firstWordOrth = firstWordOrth;
        this.secondWordOrth = secondWordOrth;
        this.firstPosTag = firstPosTag;
        this.secondPosTag = secondPosTag;
        this.liaisonConsonant = liaisonConsonant;
    }

    public boolean hasLiaison() {
        return !liaisonConsonant.isEmpty();
    }

    public boolean nasalizesVowel() {
        return liaisonConsonant.contains("VN");
    }

    public static LiaisonOpportunity parseFromStrings(String first, String second, String firstPosTag, String secondPosTag) {
        String[] firstWordSplit = LiaisonNotationParser.getWordAndConsonant(first);
        String secondWord = LiaisonNotationParser.getWordAndConsonant(second)[0];
        String firstWordStripped = WordCleaner.cleanWord(firstWordSplit[0]);
        String secondWordStripped = WordCleaner.cleanWord(secondWord);
        return new LiaisonOpportunity(firstWordStripped.toLowerCase(), secondWordStripped.toLowerCase(), firstPosTag, secondPosTag, firstWordSplit[1]);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(firstWordOrth);
        sb.append("\t").append(secondWordOrth);
        sb.append("\t").append(firstPosTag);
        sb.append("\t").append(secondPosTag);
        sb.append("\t").append(hasLiaison());
        sb.append("\t").append(nasalizesVowel());
        sb.append("\t").append(liaisonConsonant);
        return sb.toString();
    }

    public String getFirstWordOrth() {
        return firstWordOrth;
    }

    public String getSecondWordOrth() {
        return secondWordOrth;
    }

    public String getLiaisonConsonant() {
        return liaisonConsonant;
    }

    public static String stripPunctuation(String orig) {
        return orig.replace("?","").replace(".","").replace("<","").replace(">","").replace("/","");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiaisonOpportunity that = (LiaisonOpportunity) o;
        return Objects.equals(firstWordOrth, that.firstWordOrth) &&
                Objects.equals(secondWordOrth, that.secondWordOrth) &&
                Objects.equals(firstPosTag, that.firstPosTag) &&
                Objects.equals(secondPosTag, that.secondPosTag) &&
                Objects.equals(liaisonConsonant, that.liaisonConsonant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstWordOrth, secondWordOrth, firstPosTag, secondPosTag, liaisonConsonant);
    }
}
