package util.string.encode;

import com.google.common.base.Charsets;
import com.google.common.collect.*;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by janwillem on 23/12/2016.
 */
public class LemmaMap {
    private final Multimap<String,String> lemmas;
    private final Multimap<String,String> lemmasFull;

    public LemmaMap(String filePath) {
        lemmas = ArrayListMultimap.create();
        lemmasFull = ArrayListMultimap.create();
        String file = Resources.getResource(filePath).getFile();
        File asFile = new File(file);
        List<String> lines = Lists.newArrayList();
        try {
            lines = Files.readLines(asFile, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Multiset<String> counts = HashMultiset.create();
        for (String s: lines) {
            String[] split = s.split("\\\\");
            String encoded = StringTransformationCoder.getEncoding(split[0],split[2]);
            lemmas.put(split[0],encoded);
            lemmasFull.put(split[0],split[2]);
            System.out.println(split[0] +" -> "+ split[2] +" :: " +encoded);
            counts.add(encoded);
        }

        for (String key: counts.elementSet()) {
            int count = counts.count(key);
            if (count > 50) {
                System.out.println(key + " : " +count);
            }
        }
        Set<String> valuesSet = Sets.newHashSet(lemmas.values());
        Set<String> fullValuesSet = Sets.newHashSet(lemmasFull.values());
        System.out.println(valuesSet.size());
        System.out.println(fullValuesSet.size());
    }

    public static void main(String[] args) {
        LemmaMap lemmaMap = new LemmaMap("lemmas.txt");
    }
}
