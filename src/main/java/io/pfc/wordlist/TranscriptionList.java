package io.pfc.wordlist;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

/**
 * Created by janwillem on 26/09/16.
 */
public class TranscriptionList {
    private final Map<String,String> contents;

    public TranscriptionList(Map<String, String> contents) {
        this.contents = contents;
    }

    public static TranscriptionList createFromFile(String path) {
        File file = new File(path);
        Map<String,String> map = Maps.newHashMap();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                String[] split = nextLine.split("\t");
                if (split.length > 1) {
                    map.put(split[0],split[1]);
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return new TranscriptionList(map);
    }

    public String getTranscription(String orthographic) {
        return contents.get(orthographic);
    }
}
