/**
 *
 */
package io.praat;

import candidates.Candidate;
import candidates.FormInput;
import gen.Gen;
import grammar.Grammar;
import graph.LayeredGraph;
import ranking.OldHierarchy;
import ranking.OldRankedConstraint;
import util.string.VectorString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date Nov 19, 2014
 */
public class OTMultiExporter {
    private String outputPath;
    private static DecimalFormat df = new DecimalFormat("#.####");

    public static OTMultiExporter init(String outputPath) {
        return new OTMultiExporter(outputPath);
    }

    private static void writeConstraint(OldRankedConstraint rc, BufferedWriter bw) throws IOException {
        String name = "\"" + PraatString.friendly(rc.toString()) + "\" ";
        bw.write(name);
        bw.write(df.format(rc.getRankingValue()) + " ");
        bw.write(df.format(rc.getDisharmony()) + " 1.0\n");//TODO CHANGE TO NON-MAGIC NUMBER
    }

    private static void writeEvaluatedCandidateStrings(List<String> strings, BufferedWriter bw) throws IOException {
        for (String s : strings) {
            bw.write(s);
        }
    }

    /**
     * @param outputPath
     */
    private OTMultiExporter(String outputPath) {
        this.outputPath = outputPath;
    }

    public boolean writeInputsToOTMulti(Grammar g, OldHierarchy h, List<FormInput> inputs) {

        File outputFile = new File(outputPath);
        FileWriter fw;

        try {
            fw = new FileWriter(outputFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            // Write preamble
            bw.write("\"ooTextFile\"\n\"OTMulti 2\"\n");
            bw.write("<OptimalityTheory>\n");
            bw.write("0.0 ! leak\n");
            // n. of Constraints
            bw.write(h.getSize() + " constraints\n");
            // Start listing constraints!
            for (OldRankedConstraint rc : h) {
                writeConstraint(rc, bw);
            }


            // n. of Candidates
            List<String> stringsToPrint = new ArrayList<String>();
            Gen gen = null; // TODO this will crash it!


            List<Candidate> allCandidates = new ArrayList<Candidate>();
            List<String> stringsToWrite = new ArrayList<String>();
            int candidateCount = 0;
            for (FormInput ip : inputs) {

                // Get candidate space for this input
                LayeredGraph lg = gen.getCandidateSpace(ip);
                for (Candidate c : lg.formInputsToCandidates(ip)) {
                    candidateCount++;
                    String candidateString = "\" " + PraatString.friendly(c.outputToBracketedString()) + "\" ";
                    int[] vv = h.getViolationVector(c);
                    String vectorString = VectorString.toSeparatedString(vv, " ");
                    stringsToWrite.add(candidateString + vectorString);

                }

            }
            int nCandidates = candidateCount;
            bw.write(nCandidates + " candidates\n");
            for (String s : stringsToWrite) {
                bw.write(s + "\n");
            }
            bw.close();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }


        return true;

    }

}
