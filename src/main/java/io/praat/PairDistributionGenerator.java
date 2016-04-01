/**
 *
 */
package io.praat;

import grammar.levels.LevelInfo;
import util.collections.Couple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date Nov 19, 2014
 */
public class PairDistributionGenerator {

    public static boolean fromPairListWithBrackets(String outputPath, List<Couple<String>> couples, int freqToPut, LevelInfo l, LevelInfo r) {
        List<Couple<String>> bracketed = new ArrayList<Couple<String>>(couples.size());
        for (Couple<String> c : couples) {
            String origLeft = c.getLeft();
            String origRight = c.getRight();
            String bracketedLeft = l.bracket(origLeft);
            String bracketedRight = r.bracket(origRight);
            Couple<String> toAdd = Couple.of(bracketedLeft, bracketedRight);
            bracketed.add(toAdd);
        }
        return simpleFromPairList(outputPath, bracketed, freqToPut);
    }

    public static String getPreamble() {
        return ("\"ooTextFile\"\n\"PairDistribution\"\n");
    }

    /**
     * Creates a very simple PairDistribution with a pre-specified frequency for all pairs.
     *
     * @param outputPath
     * @param couples
     * @param freqToPut
     * @return true if succeeded, false otherwise
     */
    public static boolean simpleFromPairList(String outputPath, List<Couple<String>> couples, int freqToPut) {
        File outputFile = new File(outputPath);
        FileWriter fw;

        try {
            fw = new FileWriter(outputFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(getPreamble());
            bw.write(String.format("%d pairs\n", couples.size()));

            for (Couple<String> couple : couples) {
                String formatted = String.format("\"%s\"\t\"%s\"\t%d\n", couple.getLeft(), couple.getRight(), freqToPut);
                bw.write(formatted);
            }

            bw.close();


        } catch (IOException e) {
            System.err.println("Error writing to" + outputPath);
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
