package test;

import util.string.ngraph.NGraph;

import java.util.Collection;

/**
 * @author jwvl
 * @date 08/11/2014
 */
public class Tester {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String test = "Krakaka";
        Collection<NGraph> digraphs = NGraph.getAllNgraphs(3, test);
        for (NGraph dg : digraphs) {
            System.out.println(dg);
        }
    }

}
