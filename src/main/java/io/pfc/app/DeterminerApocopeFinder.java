package io.pfc.app;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by janwillem on 26/09/16.

 */
public class DeterminerApocopeFinder {
    private static boolean printWords = false;

    public static void main(String[] args) {

        String liaisonsPath = "data/pfc/liaisons.txt";
        URL url = Resources.getResource(liaisonsPath);
        List<String> allLines;
        try {
            allLines = Resources.readLines(url, Charset.defaultCharset());
            for (String line: allLines) {
                String[] parts = line.split(" ");
                String speakerInfo = parts[0].split(",")[0].trim();
                for (int i= 1; i < parts.length; i++) {
                    String toCheck = parts[i];
                    if (toCheck.startsWith("l'") || toCheck.startsWith("L'")) {
                        System.out.println(speakerInfo+"\t"+toCheck);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
