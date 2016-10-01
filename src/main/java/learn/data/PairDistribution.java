/**
 *
 */
package learn.data;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import forms.Form;
import forms.FormPair;
import graph.Direction;
import util.collections.Distribution;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jwvl
 * @date Feb 13, 2015
 */
public class PairDistribution extends
        Distribution<FormPair> implements LearningData {


    /**
     * @param name
     */
    public PairDistribution(String name) {
        super(name);
    }

    public void add(Form f, Form g, int frequency) {
        FormPair toAdd = FormPair.of(f, g);
        add(toAdd, frequency);
    }

    public void addOne(Form f, Form g) {
        FormPair toAdd = FormPair.of(f, g);
        addOne(toAdd);
    }

    public Set<Form> getLeftForms() {
        Set<Form> result = new HashSet<Form>();
        for (FormPair learningDatum : getKeySet()) {
            result.add(learningDatum.left());
        }
        return result;
    }

    public Set<Form> getRightForms() {
        Set<Form> result = new HashSet<Form>();
        for (FormPair learningDatum : getKeySet()) {
            result.add(learningDatum.right());
        }
        return result;
    }

    public FormPair drawFormPair() {
        return drawItem();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public FormPair next() {
        return drawFormPair();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        // Does nothing!
    }

    @Override
    public Set<FormPair> getForLearningDirection(Direction direction) {
        Set<FormPair> result = Sets.newHashSet();
        for (FormPair learningDatum : getKeySet()) {
            result.add(learningDatum.getUnlabeled(direction));
        }
        return result;
    }

    public Collection<FormPair> getKeys() {
        Set<FormPair> result = new HashSet<FormPair>();
        for (FormPair learning : getKeySet()) {
            result.add(learning);
        }
        return result;
    }

    public PairDistribution filter(Predicate<FormPair> predicate) {
        PairDistribution result = new PairDistribution(this.getName()+"-filtered");
        for (FormPair formPair: getKeys()) {
            if (predicate.apply(formPair)) {
                int frequency = getFrequency(formPair);
                result.add(formPair, frequency);
            }
            else {
                System.out.println("Removing formpair " +formPair);
            }
        }
        return result;
    }
}
