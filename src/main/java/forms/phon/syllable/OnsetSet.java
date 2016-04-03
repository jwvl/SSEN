package forms.phon.syllable;

import forms.primitives.segment.Phone;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by janwillem on 01/04/16.
 */
public class OnsetSet {
    private final Set<byte[]> contents;

    public OnsetSet(Set<byte[]> contents) {
        this.contents = contents;
    }

    public static OnsetSet fromFile(String filename) {
        String file = OnsetSet.class.getClassLoader().getResource(filename).getFile();
        Set<byte[]> mySet = new TreeSet<byte[]>((left, right) -> {
            for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                int a = (left[i] & 0xff);
                int b = (right[j] & 0xff);
                if (a != b) {
                    return a - b;
                }
            }
            return left.length - right.length;
        });
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                byte[] asBytes = Phone.encode(line);
                mySet.add(asBytes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OnsetSet result = new OnsetSet(mySet);
        return result;
    }

    public boolean contains(byte[] toTest) {
        return contents.contains(toTest);
    }


}
