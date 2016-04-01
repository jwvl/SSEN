/**
 *
 */
package gen;

import candidates.AbstractInput;
import forms.Form;
import gen.mapping.FormMapping;
import grammar.Grammar;
import graph.LayeredGraph;

/**
 * @author jwvl
 * @date Dec 5, 2014
 */
public class NetworkGen extends Gen {
    private LayeredGraph myGraph;

    /**
     * @param grammar
     */
    private NetworkGen(Grammar grammar) {
        super(grammar);
    }

    /**
     * @param liaisonGrammar
     * @return
     */
    public static NetworkGen addNewToGrammar(Grammar liaisonGrammar) {
        NetworkGen result = new NetworkGen(liaisonGrammar);
        result.myGraph = LayeredGraph.createInstance(liaisonGrammar);
        return result;
    }

    public void addLink(FormMapping fp) {
        myGraph.addFormMapping(fp);
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.Gen#getCandidateSpace(candidates.AbstractInput)
     */
    @Override
    public LayeredGraph getCandidateSpace(AbstractInput ip) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.Gen#getCandidateSpace(gen.forms.Form, gen.forms.Form)
     */
    @Override
    public LayeredGraph getCandidateSpace(Form start, Form end) {
        return myGraph.createSubGraph(start, end);
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.Gen#getFullCandidateSpace()
     */
    @Override
    public LayeredGraph getFullCandidateSpace() {
        return myGraph;
    }

}
