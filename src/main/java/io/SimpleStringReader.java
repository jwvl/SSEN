package io;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

public class SimpleStringReader {

    /**
     * @param args
     */
    public static List<String> StringsFromFile(String path) {
        List<String> result = Lists.newArrayList();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                result.add(nextLine);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

}
