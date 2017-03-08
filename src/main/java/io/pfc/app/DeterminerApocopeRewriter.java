package io.pfc.app;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by janwillem on 26/09/16.

 */
public class DeterminerApocopeRewriter {
    private static boolean printWords = false;

    public static void main(String[] args) {

        String liaisonsPath = "data/pfc/newFrenchItems-withTranscriptions.txt";
        URL url = Resources.getResource(liaisonsPath);
        List<String> allLines;
        try {
            allLines = Resources.readLines(url, Charset.defaultCharset());
            for (String line: allLines) {
                String[] parts = line.split("\t");
                String concept = parts[1];
                concept = concept.substring(0, 1).toUpperCase() + concept.substring(1);
                String gender = parts[2];
                String formattedWithArticle = String.format("DET{The}, N{%s.g=%s num=SG}*",concept,gender);
                String toPrint = formattedWithArticle;

                for (int i=3; i < parts.length; i++) {
                    toPrint +="\t";
                    if (i == 2) {
                        toPrint += formattedWithArticle;
                    } else {
                        toPrint+=parts[i];
                    }
                }
                toPrint+="\t0";
                System.out.println(toPrint);
            }

            for (String line: allLines) {
                String[] parts = line.split("\t");
                String concept = parts[1];
                concept = concept.substring(0, 1).toUpperCase() + concept.substring(1);
                String gender = parts[2];
                String formattedWithoutArticle = String.format("N{%s.g=%s num=SG}*",concept,gender);
                String toPrint = formattedWithoutArticle+"\t"+parts[3].replaceFirst("l","")+"\t0\t0";
                System.out.println(toPrint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
