/**
 *
 */
package test;

/**
 * @author jwvl
 * @date Nov 16, 2014
 */
public class ParadigmGenerator {
    static String[] adjAffixMorphs = {"adjSG", "adjPL"};
    static String[] adjectives = {"good", "bad", "small", "great"};
    static String[] adjMorphs = {"GOOD", "BAD", "SMALL", "GREAT"};
    static String[] affixes = {"", "s"};
    static String[] nMorphs = {"HUSBAND.M", "CAR.F", "ACTOR.M", "WAVE.F"};
    static String[] nounAffixMorphs = {"nSG", "nPL"};
    static String[] nouns = {"husband", "car", "actor", "wave",};

    static boolean printMorph = true;

    public static void main(String[] args) {

        for (int i = 0; i < adjectives.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                for (int k = 0; k < affixes.length; k++) {
                    StringBuffer str = new StringBuffer("");
                    StringBuffer mrf = new StringBuffer("");
                    str.append(adjectives[i]);
                    mrf.append(adjMorphs[i]);
                    if (nMorphs[j].contains(".F")) {
                        mrf.append(" F");
                    } else {
                        mrf.append(" M");
                    }
                    mrf.append(" " + adjAffixMorphs[k]);

                    str.append(" " + nouns[j]);
                    mrf.append(" " + nMorphs[j]);

                    str.append("" + affixes[k]);
                    mrf.append(" " + nounAffixMorphs[k]);
                    if (printMorph)
                        str.append("\t" + mrf.toString());
                    System.out.println(str.toString());

                }
            }
        }

    }

}
