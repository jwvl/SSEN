package io.pfc.lemma;

import java.util.Objects;

/**
 * Created by janwillem on 19/04/2017.
 */
public class LexiqueEntry {
    private final String phonString;
    private final String lemma;
    private final String pos;
    private final char gender;
    private final char number;
    private static String[] notNouns = {"l","la","non","nécessaire","avoir","juste","long","longues","avant","premier","première","nouveau","nouvelle","originale","principal","y","je","si","quatre","propre","propres","ça","a","tout","pour","on","tu","par","sur","une","ben","moi","est","petit","petite","plus","première","dire","grand","gros","bon","bonne","bien"};

    public LexiqueEntry(String phonString, String lemma, String pos, String gender, String number) {
        this.phonString = rewritePhonString(phonString);
        this.lemma = lemma;
        this.pos = pos;
        this.gender = gender.isEmpty() ? 'x' : gender.charAt(0);
        this.number = number.isEmpty() ? 'x' : number.charAt(0);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LexiqueEntry that = (LexiqueEntry) o;
        return gender == that.gender &&
                number == that.number &&
                Objects.equals(phonString, that.phonString) &&
                Objects.equals(lemma, that.lemma) &&
                Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phonString, lemma, pos, gender, number);
    }

    public boolean isAdjective() {
        return pos.startsWith("ADJ");
    }

    public boolean isArticle() {
        return pos.startsWith("ART");
    }

    public boolean isNoun() {
        if (pos.startsWith("NOM")) {
            for (String nonNouns: notNouns) {
                if (lemma.equals(nonNouns)) {
                    return false;
                }
            }
            return gender == 'f' || gender == 'm';
        }
        return false;
    }

    private String rewritePhonString(String orig) {
        orig = orig.replaceAll("E","ɛ");
        orig = orig.replaceAll("O","ɔ");
        orig = orig.replaceAll("°","ə");
        orig = orig.replaceAll("2","ø");
        orig = orig.replaceAll("9","œ");
        orig = orig.replaceAll("§","Q");
        orig = orig.replaceAll("A","a");
        orig = orig.replaceAll("@","&");
        orig = orig.replaceAll("5","3");
        orig = orig.replaceAll("1","3");
        orig = orig.replaceAll("S","ʃ");
        orig = orig.replaceAll("Z","ʒ");
        orig = orig.replaceAll("R","ʁ");
        orig = orig.replaceAll("N","ɲ");
        orig = orig.replaceAll("G","ŋ");
        orig = orig.replaceAll("8","ɥ");
        return orig;
    }

    public String getPos() {
        return pos;
    }

    public String getPhonString() {
        return phonString;
    }

    public String getBiphonSsf() {
        String printPos ="";
        String content = "";
        String features = "";
        if (isNoun()) {
            printPos = "N";
            content = lemma;
            features +=(".g="+String.valueOf(gender).toUpperCase());
            features +=" num=";
            if (number == 's') {
                features +="SG";
            } else {
                features +="PL";
            }
        } else if (pos.equals("ART:ind")) {
            printPos = "DET";
            content = "ArtInd";
        } else if (pos.equals("ART:def")) {
            printPos = "DET";
            content = "ArtDef";
        } else if (pos.startsWith("ADJ:num")) {
            printPos ="NUM";
            content = lemma;
        }else if (pos.startsWith("ADJ:dem")) {
            printPos ="DET";
            content = "Dem";
        }
        else if (pos.startsWith("ADJ:pos")) {
            printPos ="DET";
            content = "Poss"+String.valueOf(lemma.charAt(0)).toUpperCase();
        }else if (pos.startsWith("ADJ")) {
            printPos ="ADJ";
            content = lemma;
        }
        String result = printPos+"{"+content+features+"}";
        if (printPos.equals("N")) {
            result+="*";
        }

        return result;
    }

    public String getLemma() {
        return lemma;
    }
}


