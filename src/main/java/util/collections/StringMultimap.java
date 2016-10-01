package util.collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.MyStringTable;

import java.io.*;
import java.util.Collection;

/**
 * Created by janwillem on 01/10/16.
 */
public class StringMultimap {
    private final Multimap<String, String> contents;

    private StringMultimap(Multimap<String, String> contents) {
        this.contents = contents;
    }

    public static StringMultimap createNew() {
        return new StringMultimap(HashMultimap.create());
    }

    public void put(String key, String value) {
        contents.put(key, value);
    }

    public Collection<String> get(String key) {
        return contents.get(key);
    }

    public Collection<String> keySet() {
        return contents.keySet();
    }

    public void writeToFile(String path) {
        try {
            File outputFile = new File(path);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Key\tValue");
            for (String key: keySet()) {
                for (String value: get(key)) {
                    bw.write("\n"+key+"\t"+value);
                }
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringMultimap readFromFile(String path) {
        String prefix = MyStringTable.class.getClassLoader().getResource("").getFile();
        String fileName = prefix + path;

        File file = new File(fileName);
        StringMultimap result = StringMultimap.createNew();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String firstLine = reader.readLine();
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                String[] next = nextLine.split("\t");
                result.put(next[0],next[1]);
            }
        }
        catch (IOException e) {
            System.err.println("Couldn't find file " + fileName);
            return null;
        }
        return result;
    }


}
