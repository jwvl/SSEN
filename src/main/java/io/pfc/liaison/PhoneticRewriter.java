package io.pfc.liaison;

import io.pfc.wordlist.TranscriptionList;

/**
 * Created by janwillem on 26/09/16.
 */
public class PhoneticRewriter {
    private final TranscriptionList transcriptionList;
    private static String[] nasals = {"ã","ɛ̃","ɔ̃"};
    private static String[] orals = {"ɑ","ɛ","ɔ"};

    public PhoneticRewriter(TranscriptionList transcriptionList) {
        this.transcriptionList = transcriptionList;
    }


    public String rewrite(LiaisonOpportunity liaisonOpportunity, String firstPhon, String secondPhon) {
        String linkingConsonant = liaisonOpportunity.getLiaisonConsonant();
        if (linkingConsonant.contains("n")) {
            if (!liaisonOpportunity.nasalizesVowel()) {
                firstPhon = oralizeNasal(firstPhon);
                linkingConsonant = linkingConsonant.replace("VO","");
            } else {
                linkingConsonant = linkingConsonant.replace("VN","");
            }
        }
        linkingConsonant = linkingConsonant.replace("-", "");
        linkingConsonant = linkingConsonant.replace("C[a-z]+", "");
        linkingConsonant = linkingConsonant.replace("r", "ʁ");
        return firstPhon+linkingConsonant+secondPhon;
    }
    public String rewrite(LiaisonOpportunity liaisonOpportunity) {
        String firstOrth = liaisonOpportunity.getFirstWordOrth();
        String secondOrth = liaisonOpportunity.getSecondWordOrth();
        String firstPhon = transcriptionList.getTranscription(firstOrth);
        String secondPhon = transcriptionList.getTranscription(secondOrth);
        return rewrite(liaisonOpportunity, firstPhon, secondPhon);
    }

    public String rewriteSingle(String orthWord) {
        return transcriptionList.getTranscription(orthWord);
    }

    private String oralizeNasal(String firstPhon) {
        for (int i=0; i < nasals.length; i++) {
            if (firstPhon.lastIndexOf(nasals[i]) > 0) {
                return firstPhon.replaceAll(nasals[i],orals[i]);
            }
            // TODO What if word contains multiple instances of same nasal?
            // But highly unlikely
        }

        return firstPhon;
    }
}
