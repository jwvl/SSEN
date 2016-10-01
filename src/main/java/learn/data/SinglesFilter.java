package learn.data;

import com.google.common.base.Predicate;
import forms.FormPair;
import forms.morphosyntax.SemSynForm;

/**
 * Created by janwillem on 30/09/16.
 */
public class SinglesFilter implements Predicate<FormPair> {
    @Override
    public boolean apply(FormPair formPair) {
        SemSynForm left = (SemSynForm) formPair.left();
        return left.size() > 1;
    }
}
