package gen.subgen;

import forms.Form;

import java.util.Collection;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public interface Reversible<F extends Form, G extends Form> {
    Collection<F> generateLeft(G form);

}
