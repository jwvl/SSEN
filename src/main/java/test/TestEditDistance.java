package test;

import util.string.edit.Aligner;
import util.string.edit.WeightedEditDistance;

public class TestEditDistance {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String[] test = Aligner.alignment("omfe", "tome");
        for (String s : test) {
            System.out.println(s);
        }
        new WeightedEditDistance("omfe", "tome");

    }

}
