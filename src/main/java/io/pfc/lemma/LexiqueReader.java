package io.pfc.lemma;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by janwillem on 19/04/2017.
 */
public class LexiqueReader {
    public final String pathToData;

    public LexiqueReader(String pathToData) {
        this.pathToData = pathToData;
    }

    public Multimap<String,LexiqueEntry> readMap() {
        Multimap<String,LexiqueEntry> result = HashMultimap.create();
        URL url = Resources.getResource(pathToData);
        List<String> lines = Lists.newArrayList();
        try {
            lines = Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i=1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split("\t");
            String form = parts[0];
            String lemma = parts[2];
            String phon = parts[1];
            String pos = parts[3];
            String gender = parts[4];
            String number = parts[5];
            LexiqueEntry entry = new LexiqueEntry(phon, lemma, pos, gender, number);
            result.put(form,entry);
        }
        return result;
    }
}
