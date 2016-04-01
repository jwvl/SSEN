/**
 *
 */
package gen;

import candidates.AbstractInput;
import forms.Form;
import grammar.Grammar;
import graph.LayeredGraph;

/**
 * @author jwvl
 * @date 18/10/2014 GEN is a functional object that maps an input tuple to an
 * output set. Because there is no consensus on what GEN can and cannot do
 * (P&S 93 do not formalize it) this is an abstract class, which is to be
 * extended depending on the application/framework.
 */
public abstract class Gen {
    protected Grammar containingGrammar;

    private Gen() {
    }

    protected Gen(Grammar grammar) {
        // Nog iets mee doen?
    }

    public abstract LayeredGraph getCandidateSpace(AbstractInput ip);

    public abstract LayeredGraph getCandidateSpace(Form start, Form end);

    public abstract LayeredGraph getFullCandidateSpace();


}
